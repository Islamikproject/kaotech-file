@extends('layouts.main')

@section('sub-title')
<div class="" style="padding-bottom:10px;    text-align: center;">
  <a href="#"><img alt="logo" src="assets/img/logo.png" style="height:50px;"></a>
  <!-- <span style="font-size: 15px;font-weight: normal;padding-left:10px">Stripe : gudansk@gmail.com</span> -->
</div>
@endsection


@section('content')


<style>
        .demo-container {
            width: 100%;
            max-width: 350px;
            margin: 50px auto;
        }

        form {
            margin: 30px;
        }
        input {
            width: 200px;
            margin: 10px auto;
            display: block;
        }

    </style>


<div class="demo-container">
        <div class="card-wrapper"></div>

        <div class="form-container active">
            <form method="post">
                <input type="hidden" name="sermon" value="{{$_GET['sermon']}}">
                <input placeholder="Card number" type="tel" name="number" required>
                <input placeholder="Full name" type="text" name="name" required>
                <input placeholder="MM/YY" type="tel" name="expiry" required>
                <input placeholder="CVC" type="number" name="cvc" required>
                <input placeholder="Amount" type="number" name="amount" value="20" required>

                <div class="text-center">
                  <button type="submit" class="btn btn-primary">Submit</button>
                </div>
            </form>
        </div>
    </div>

  <div id="progressDlg" class="modal fade in" role="basic" style="padding-right: 17px;z-index:9999999999999;" [class.show] = "appState.get('isLoading') > 0">
     <div class="modal-backdrop fade in" style="height: 100%;background-color: rgba(214, 255, 217, 0.39) !important;"></div>
     <div class="modal-dialog" style="width:45px;height:100%;margin:auto;float: none;">
       <i class="fa fa-circle-o-notch fa-spin fa-fw" aria-hidden="true" style="font-size: 44px;top: 50%;position: absolute;color: rgb(0, 255, 45);"></i>
     </div>
    </div>


@endsection

@section('bottom_js')
<script src="/assets/card/card.js"></script>
<script src="/assets/card/jquery.card.js"></script>

<script type="text/javascript">

new Card({
            form: document.querySelector('form'),
            container: '.card-wrapper'
        });

@if($errors->any())
notification("topright","error","fa fa-exclamation-circle vd_red","Error", "{{$errors->first()}}");
@endif

@if(session()->has('success'))    
notification("topright","success","fa fa-exclamation-circle vd_red","Success", "Thanks for your donation, it has been sent to the mosque account.");    
@endif
  
  function showProgress(){
    $('#progressDlg').addClass('show');
  }

  function hideProgress(){
    $('#progressDlg').removeClass('show');
  }

</script>
@endsection
