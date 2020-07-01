<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| This file is where you may define all of the routes that are handled
| by your application. Just tell Laravel the URIs it should respond
| to using a Closure or controller method. Build something great!
|
*/

Route::get('/test', function () {
    return view('dashboard/home');
});



Route::group(['middleware' => 'guest'], function() {
  // Route::get('/home', 'HomeController@index');
  // Route::post('/addRisk', 'HomeController@addRisk');
  // Route::get('/removeRisk', 'HomeController@removeRisk');
  Route::get('/stripe-log', 'HomeController@index');
  Route::get('/getlogs', 'HomeController@getLogs');
});


// Auth::routes();

// Route::get('/', function () {
//   return redirect('/login');
// });

Route::get('/', 'HomeController@login');
Route::post('/login', 'HomeController@postLogin');

Route::get('donation', 'HomeController@donation');
Route::post('donation', 'HomeController@postDonation');

// Route::get('/register', 'HomeController@register');
// Route::post('/register', 'HomeController@postRegister');

// Route::group(['middleware' => ['auth'], 'namespace' => 'Dashboard', 'prefix'=>'home'], function () {
//     Route::get('/', 'HomeController@index');
// });
