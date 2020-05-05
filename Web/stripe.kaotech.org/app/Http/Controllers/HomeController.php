<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Parse\ParseObject;
use Parse\ParseQuery;
use Parse\ParseACL;
use Parse\ParsePush;
use Parse\ParseUser;
use Parse\ParseInstallation;
use Parse\ParseException;
use Parse\ParseAnalytics;
use Parse\ParseFile;
use Parse\ParseCloud;
use Parse\ParseClient;

use Session;

use Stripe\Stripe as Stripe;
use Stripe\Account as StripeAccount;
use Stripe\Error\Base as Stripe_Error;

class HomeController extends Controller
{
    // Test Keys
    public $STRIPE_SECRET_KEY = 'sk_test_v0xKIeyGxMWyL3dqhebbUXAh00AQfnhZpK';
    public $STRIPE_CLIENT_ID = 'ca_HAu0nFiHFKWtYrCs8x2Nn4AwiQOcqC23'; // dev

    //Live Keys
    //public $STRIPE_SECRET_KEY = 'sk_live_38jWbIQc9WLirGH3PU429uzi';
    //public STRIPE_CLIENT_ID = 'ca_Ad4xfP7ici4qlstU7omGbtBNrVxYvuLi'; // live

    public $STRIPE_CONNECT_URL    = 'https://connect.stripe.com/oauth/authorize';
    public $STRIPE_TOKEN_URL      = 'https://connect.stripe.com/oauth/token';
    public $STRIPE_RESPONSE_TYPE  = 'code';
    public $STRIPE_SCOPE          = 'read_write';
    public $STRIPE_GRANT_TYPE     = 'authorization_code';

    public $GET_DATA_COUNT = 10;

    public function __construct()
    {
      // init Parse
      $app_id = 'b0484b2d-2135-4a2d-a924-b916750cf001';
      $rest_key = 'b0bae137-c18c-4a73-aceb-51726922b001';
      $master_key = 'b054d1ea-0128-4a27-b107-59b61b4db001';
      ParseClient::initialize($app_id, $rest_key, $master_key);
      ParseClient::setServerURL('http://parse.kaotech.org:20001/','parse');

      // init Stripe
      Stripe::setApiKey($this->STRIPE_SECRET_KEY);
    }


    public function index(Request $request)
    {
        $user_id = $request->session()->get('parse_user');
        // $user_id = 'UnYKzpma8m';

        $query = ParseUser::query();
        $user = $query->get($user_id);

        $query = new ParseQuery("Payment");
        $query->includeKey("toUser");
        $query->EqualTo("toUser", $user);
        $query->descending("updatedAt");
        $query->limit($this->GET_DATA_COUNT);
        $results = $query->find();

        $data = array();
        foreach ($results as $log) {
		  if(!$log->renterUser) continue;
          $one['time'] = $log->getUpdatedAt()->format('Y-m-d H:i');
          $one['from'] = $log->renterUser->firstName." ".$log->renterUser->lastName;
          $one['amount'] = $log->amount;
          $one['status'] = 1;
          $data[] = $one;
        }

        return view('dashboard.home', ['data'=>$data]);
    }

    public function login(Request $request){
      $input = $request->input();

      if (!isset($input['error'])) {
        $input['error'] = '';
      }

      // if redirect from Stripe
      if (isset($input['scope']) && isset($input['code'])) {
        $code = $input['code'];
        $post = 'grant_type='.$this->STRIPE_GRANT_TYPE.
                '&client_secret='.$this->STRIPE_SECRET_KEY.
                '&code='.$code;
          $ch = curl_init();
          curl_setopt($ch, CURLOPT_URL,$this->STRIPE_TOKEN_URL);
          curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
          curl_setopt($ch, CURLOPT_POST, 1);
          curl_setopt($ch, CURLOPT_POSTFIELDS,$post);
          curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);
          // execute!
          $response = curl_exec($ch);
          curl_close($ch);
          if ($response === false) {
            $input['error'] = 'CURL ERROR';
          } else {
            $strip_res = json_decode($response);
            if (isset($strip_res->error)) {
              $input['error'] = 'Authorization Fail, Please reconnect to our service.';
            } else {
              $stripe_id = $strip_res->stripe_user_id;
              Session::put('stripe_user', $stripe_id);

              $parse_user_id = $request->session()->get('parse_user');

              // $query = ParseUser::query();
              // $parse_user = $query->get($parse_user_id);
              //
              // $currentUser = ParseUser::getCurrentUser();
              // var_dump($currentUser->getObjectId());
              // var_dump($parse_user_id);

              $parse_email = $request->session()->get('parse_email');
              $parse_password = $request->session()->get('parse_password');
              try {
                $parse_user = ParseUser::logIn($parse_email, $parse_password);
              } catch (ParseException $ex) {
                $res['success'] = false;
                $res['error'] = $ex->getMessage();
                return view('auth.login', $res);
              }

              $parse_user->set("accountId", $stripe_id);
              $parse_user->save();

              return redirect('/stripe-log');
            }
          }
      }

      // init session
      // $request->session()->forget('parse_user');
      $request->session()->forget('stripe_user');

      return view('auth.login', $input);


    }

    public function postLogin(Request $request){
      try {
        $user = ParseUser::logIn($request->input('email'), $request->input('password'));
      } catch (ParseException $ex) {
        $res['success'] = false;
        $res['error'] = $ex->getMessage();
        return $res;
      }

      Session::put('parse_user', $user->getObjectId());
      Session::put('parse_email', $request->input('email'));
      Session::put('parse_password', $request->input('password'));
      $res['success'] = true;
      $res['stripe_url'] = $this->STRIPE_CONNECT_URL.'?'.
      '&response_type='.$this->STRIPE_RESPONSE_TYPE.
      '&client_id='.$this->STRIPE_CLIENT_ID.
      '&scope='.$this->STRIPE_SCOPE;

      if ($user->accountId) {
        $error = '';
        try {
          $stripe_account = StripeAccount::retrieve($user->accountId);
        } catch (Stripe_Error $e) {
          $error = $e->getMessage();
        }

        if ($error) {
          $user->accountId = '';
          $user->save();
        } else {
          Session::put('stripe_user', $user->accountId);
          $res['stripe_url'] = url('/stripe-log');
        }

      }
      return $res;
    }

    public function getLogs(Request $request){
        $user_id = $request->session()->get('parse_user');
        // $user_id = 'UnYKzpma8m';

        $query = ParseUser::query();
        $user = $query->get($user_id);

        $query = new ParseQuery("Payment");
        $query->includeKey("toUser");
        $query->EqualTo("toUser", $user);
        $query->descending("updatedAt");
        $query->limit($this->GET_DATA_COUNT);
		$query->skip($request->input('count'));
        $results = $query->find();

        $data = array();
        foreach ($results as $log) {
          $one['time'] = $log->getUpdatedAt()->format('Y-m-d H:i');
          $one['from'] = $log->renterUser->firstName." ".$log->renterUser->lastName;
          $one['amount'] = $log->amount;
          $one['status'] = 1;
          $data[] = $one;
        }


      return $data;
    }

}
