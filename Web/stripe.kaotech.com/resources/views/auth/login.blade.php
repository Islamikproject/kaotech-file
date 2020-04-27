<!DOCTYPE html>
<!--[if IE 8]>			<html class="ie ie8"> <![endif]-->
<!--[if IE 9]>			<html class="ie ie9"> <![endif]-->
<!--[if gt IE 9]><!-->	<html><!--<![endif]-->

<!-- Specific Page Data -->

<!-- End of Data -->

<head>
    <meta charset="utf-8" />
    <title>BoothRent</title>
    <meta name="keywords" content="HTML5 Template, CSS3, All Purpose Admin Template, V  endroid" />
    <meta name="description" content="Login Pages - Responsive Admin HTML Template">
    <meta name="author" content="Venmond">

    <!-- Set the viewport width to device width for mobile -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <!-- CSS -->

    <!-- Bootstrap & FontAwesome & Entypo CSS -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <!--[if IE 7]><link type="text/css" rel="stylesheet" href="css/font-awesome-ie7.min.css"><![endif]-->

    <!-- Fonts CSS -->

    <!-- Plugin CSS -->
    <link href="assets/plugins/jquery-ui/jquery-ui.custom.min.css" rel="stylesheet" type="text/css">
    <link href="assets/plugins/prettyPhoto-plugin/css/prettyPhoto.css" rel="stylesheet" type="text/css">
    <link href="assets/plugins/isotope/css/isotope.css" rel="stylesheet" type="text/css">
    <link href="assets/plugins/pnotify/css/jquery.pnotify.css" media="screen" rel="stylesheet" type="text/css">
	  <link href="assets/plugins/google-code-prettify/prettify.css" rel="stylesheet" type="text/css">

    <link href="assets/plugins/mCustomScrollbar/jquery.mCustomScrollbar.css" rel="stylesheet" type="text/css">
    <link href="assets/plugins/tagsInput/jquery.tagsinput.css" rel="stylesheet" type="text/css">
    <link href="assets/plugins/bootstrap-switch/bootstrap-switch.css" rel="stylesheet" type="text/css">
    <link href="assets/plugins/daterangepicker/daterangepicker-bs3.css" rel="stylesheet" type="text/css">
    <link href="assets/plugins/bootstrap-timepicker/bootstrap-timepicker.min.css" rel="stylesheet" type="text/css">
    <link href="assets/plugins/colorpicker/css/colorpicker.css" rel="stylesheet" type="text/css">
    <!-- Theme CSS -->
    <link href="assets/css/theme.min.css" rel="stylesheet" type="text/css">
    <!--[if IE]> <link href="css/ie.css" rel="stylesheet" > <![endif]-->
    <link href="assets/css/chrome.css" rel="stylesheet" type="text/chrome"> <!-- chrome only css -->
    <link href="assets/css/theme-responsive.min.css" rel="stylesheet" type="text/css">
    <link href="assets/custom/custom.css" rel="stylesheet" type="text/css">

    <script type="text/javascript" src="assets/js/modernizr.js"></script>
    <script type="text/javascript" src="assets/js/mobile-detect.min.js"></script>
    <script type="text/javascript" src="assets/js/mobile-detect-modernizr.js"></script>
</head>

<body id="pages" class="full-layout no-nav-left no-nav-right  nav-top-fixed background-login     responsive remove-navbar login-layout   clearfix" data-active="pages "  data-smooth-scrolling="1">
<div class="vd_body">
<!-- Header Start -->

<!-- Header Ends -->
<div class="content">
  <div class="container">

    <!-- Middle Content Start -->

    <div class="vd_content-wrapper">
      <div class="vd_container">
        <div class="vd_content clearfix">
          <div class="vd_content-section clearfix">
            <div class="vd_login-page" style="margin: 0 auto;">
              <div class="heading clearfix" style="margin: 30px 0 0px;">
                <div class="logo">
                  <h2 class="mgbt-xs-5"><img src="{{url('assets/img/logo.png')}}" alt="logo" width="60%"></h2>
                  <!-- <h2 class="mgbt-xs-5" style="font-weight:bold;padding: 10px;color: #16cc70;">i-Mobilize</h2> -->
                </div>
                <br/>
                <!-- <h4 class="text-center font-semibold vd_grey">LOGIN TO YOUR ACCOUNT</h4> -->
              </div>
              <div class="panel widget">
                <div class="panel-body">
                  <form class="form-horizontal" id="login-form" role="form" method="POST" action="{{ url('/login') }}">
                    {{ csrf_field() }}
                    @if ($errors->has('email'))
                        <div class="alert alert-danger">
                          <button type="button" class="close" data-dismiss="alert" aria-hidden="true"><i class="icon-cross"></i></button>
                          <span class="vd_alert-icon"><i class="fa fa-exclamation-circle vd_red"></i></span><strong>Login Error</strong><br> Invalid Email or Password </div>
                    @endif
                    <div class="form-group  mgbt-xs-20">
                      <div class="col-md-12">
                        <div class="label-wrapper sr-only">
                          <label class="control-label" for="email">Email</label>
                        </div>
                        <div class="vd_input-wrapper" id="email-input-wrapper"> <span class="menu-icon"> <i class="fa fa-envelope"></i> </span>
                          <input type="" placeholder="Phone" id="email" name="email" class="required" required>

                        </div>
                        <div class="label-wrapper">
                          <label class="control-label sr-only" for="password">Password</label>
                        </div>
                        <div class="vd_input-wrapper" id="password-input-wrapper" style="    margin-top: 5px;"> <span class="menu-icon"> <i class="fa fa-lock"></i> </span>
                          <input type="password" placeholder="Password" id="password" name="password" class="required" required>
                        </div>
                      </div>
                    </div>
                    <div id="vd_login-error" class="alert alert-danger hidden"><i class="fa fa-exclamation-circle fa-fw"></i> Please fill the necessary field </div>
                    <div class="form-group">
                      <div class="col-md-12 text-center mgbt-xs-5">
                        <button class="btn vd_bg-green vd_white width-100" type="button" id="submit-login" onclick="onLogin();">Login</button>
                      </div>
                      <div class="col-md-12">
                        <div class="row">
                          <div class="col-xs-6" hidden="">
                            <div class="vd_checkbox">
                              <input type="checkbox" id="checkbox-1"  name="remember">
                              <label for="checkbox-1"> Remember me</label>
                            </div>
                          </div>
                          <div class="col-xs-6 text-right">
                            <!-- <div class=""> <a href="pages-forget-password.html">Forget Password? </a> </div> -->
                          </div>
                        </div>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
              <!-- Panel Widget -->
              <!-- <div class="register-panel text-center font-semibold"> <a href="{{ url('/register') }}">CREATE AN ACCOUNT<span class="menu-icon"><i class="fa fa-angle-double-right fa-fw"></i></span></a> </div> -->
            </div>
            <!-- vd_login-page -->

          </div>
          <!-- .vd_content-section -->

        </div>
        <!-- .vd_content -->
      </div>
      <!-- .vd_container -->
    </div>
    <!-- .vd_content-wrapper -->

    <!-- Middle Content End -->

  </div>
  <!-- .container -->
</div>
<!-- .content -->

</div>

<div id="progressDlg" class="modal fade in" role="basic" style="padding-right: 17px;z-index:9999999999999;" [class.show] = "appState.get('isLoading') > 0">
   <div class="modal-backdrop fade in" style="height: 100%;background-color: rgba(214, 255, 217, 0.39) !important;"></div>
   <div class="modal-dialog" style="width:45px;height:100%;margin:auto;float: none;">
     <i class="fa fa-circle-o-notch fa-spin fa-fw" aria-hidden="true" style="font-size: 44px;top: 50%;position: absolute;color: rgb(0, 255, 45);"></i>
   </div>
  </div>

<!-- .vd_body END  -->
<a id="back-top" href="#" data-action="backtop" class="vd_back-top visible"> <i class="fa  fa-angle-up"> </i> </a>
<!--
<a class="back-top" href="#" id="back-top"> <i class="icon-chevron-up icon-white"> </i> </a> -->

<!-- Javascript =============================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script type="text/javascript" src="assets/js/jquery.min.js"></script>
<!--[if lt IE 9]>
  <script type="text/javascript" src="js/excanvas.js"></script>
<![endif]-->
<script type="text/javascript" src="assets/js/bootstrap.min.js"></script>
<script type="text/javascript" src='assets/plugins/jquery-ui/jquery-ui.custom.min.js'></script>
<script type="text/javascript" src="assets/plugins/jquery-ui-touch-punch/jquery.ui.touch-punch.min.js"></script>

<script type="text/javascript" src="assets/js/caroufredsel.js"></script>
<script type="text/javascript" src="assets/js/plugins.js"></script>

<script type="text/javascript" src="assets/plugins/breakpoints/breakpoints.js"></script>
<script type="text/javascript" src="assets/plugins/dataTables/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="assets/plugins/prettyPhoto-plugin/js/jquery.prettyPhoto.js"></script>

<script type="text/javascript" src="assets/plugins/mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js"></script>
<script type="text/javascript" src="assets/plugins/tagsInput/jquery.tagsinput.min.js"></script>
<script type="text/javascript" src="assets/plugins/bootstrap-switch/bootstrap-switch.min.js"></script>
<script type="text/javascript" src="assets/plugins/blockUI/jquery.blockUI.js"></script>
<script type="text/javascript" src="assets/plugins/pnotify/js/jquery.pnotify.min.js"></script>

<script type="text/javascript" src="assets/js/theme.js"></script>
<script type="text/javascript" src="assets/custom/custom.js"></script>

<!-- Specific Page Scripts Put Here -->
<script type="text/javascript">

function onLogin(){
  // if (!$("#login-form").valid()) {
  //   return;
  // }
  showProgress();
  $("#submit-login").enable(false);
  var formData = new FormData($('#login-form')[0]);
  $.ajax({
      url: "{{ url('/login') }}",
      type: 'POST',
      data: formData,
      async: true,
      cache: false,
      contentType: false,
      processData: false,
      success: function(data) {
        $("#submit-login").enable(true);
        if (data.success) {
          // notification("topright","success","fa fa-check-circle vd_green","Success","Login successfully!");
          window.location.href = data.stripe_url;
        } else {
          notification("topright","error","fa fa-exclamation-circle vd_red","Error", data.error);
          hideProgress();
        }
      },
      fail: function() {
        $("#submit-login").enable(true);
        notification("topright","error","fa fa-exclamation-circle vd_red","Error", "Server Connection Error");
        hideProgress();
      },
    });
}
$(document).ready(function() {
		"use strict";

    var email = "{{isset($email)?$email:''}}";
    var password = "{{isset($password)?$password:''}}";

    if (email != '' && password != '') {
      $('#email').val(email);
      $('#password').val(password);
      onLogin();
    }

    var error = "{{isset($error)?$error:''}}";
    if (error != '') {
      notification("topright","error","fa fa-exclamation-circle vd_red","Error", error);
    }

        var form_register_2 = $('#login-form');
        var error_register_2 = $('.alert-danger', form_register_2);
        var success_register_2 = $('.alert-success', form_register_2);

        form_register_2.validate({
            errorElement: 'div', //default input error message container
            errorClass: 'vd_red', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            ignore: "",
            rules: {
                email: {
                    required: true,
                    email: true
                },
                password: {
                    required: true,
                },

            },

			errorPlacement: function(error, element) {
				if (element.parent().hasClass("vd_checkbox") || element.parent().hasClass("vd_radio")){
					element.parent().append(error);
				} else if (element.parent().hasClass("vd_input-wrapper")){
					error.insertAfter(element.parent());
				}else {
					error.insertAfter(element);
				}
			},

            invalidHandler: function (event, validator) { //display error alert on form submit
                success_register_2.hide();
                error_register_2.show();


            },

            highlight: function (element) { // hightlight error inputs

				$(element).addClass('vd_bd-red');
				$(element).parent().siblings('.help-inline').removeClass('help-inline hidden');
				if ($(element).parent().hasClass("vd_checkbox") || $(element).parent().hasClass("vd_radio")) {
					$(element).siblings('.help-inline').removeClass('help-inline hidden');
				}

            },

            unhighlight: function (element) { // revert the change dony by hightlight
                $(element)
                    .closest('.control-group').removeClass('error'); // set error class to the control group
            },

            success: function (label, element) {
                label
                    .addClass('valid').addClass('help-inline hidden') // mark the current input as valid and display OK icon
                	.closest('.control-group').removeClass('error').addClass('success'); // set success class to the control group
				$(element).removeClass('vd_bd-red');


            },

        //     submitHandler: function (form) {
        //       $('#login-submit').prepend('<i class="fa fa-spinner fa-spin mgr-10"></i>').addClass('disabled').attr('disabled');
				//         // $(form).find('#login-submit')
        //         // success_register_2.show();
        //         // error_register_2.hide();
				// // setTimeout(function(){window.location.href = "index.html"},2000)	 ;
        //     }
        });


});
function showEffect(){
  // $('#login-submit').prepend('<i class="fa fa-spinner fa-spin mgr-10"></i>').addClass('disabled').attr('disabled');
}

function showProgress(){
  $('#progressDlg').addClass('show');
}

function hideProgress(){
  $('#progressDlg').removeClass('show');
}
</script>

</body>
</html>
