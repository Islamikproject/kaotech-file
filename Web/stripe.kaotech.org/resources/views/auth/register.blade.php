<!DOCTYPE html>
<!--[if IE 8]>			<html class="ie ie8"> <![endif]-->
<!--[if IE 9]>			<html class="ie ie9"> <![endif]-->
<!--[if gt IE 9]><!-->	<html><!--<![endif]-->

<!-- Specific Page Data -->

<!-- End of Data -->

<head>
    <meta charset="utf-8" />
    <title>PDot</title>

    <!-- Set the viewport width to device width for mobile -->
    <meta name="viewport" content="width=device-width, initial-scale=1.0">


    <!-- Fav and touch icons -->
    <link rel="apple-touch-icon-precomposed" sizes="144x144" href="img/ico/apple-touch-icon-144-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="114x114" href="img/ico/apple-touch-icon-114-precomposed.png">
    <link rel="apple-touch-icon-precomposed" sizes="72x72" href="img/ico/apple-touch-icon-72-precomposed.png">
    <link rel="apple-touch-icon-precomposed" href="img/ico/apple-touch-icon-57-precomposed.png">
    <link rel="shortcut icon" href="img/ico/favicon.png">


    <!-- CSS -->

    <!-- Bootstrap & FontAwesome & Entypo CSS -->
    <link href="assets/css/bootstrap.min.css" rel="stylesheet" type="text/css">
    <link href="assets/css/font-awesome.min.css" rel="stylesheet" type="text/css">
    <!--[if IE 7]><link type="text/css" rel="stylesheet" href="css/font-awesome-ie7.min.css"><![endif]-->
    <link href="assets/css/font-entypo.css" rel="stylesheet" type="text/css">

    <!-- Fonts CSS -->
    <link href="assets/css/fonts.css"  rel="stylesheet" type="text/css">

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
    <!-- Responsive CSS -->
    <link href="assets/css/theme-responsive.min.css" rel="stylesheet" type="text/css">
    <!-- Custom CSS -->
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

    <div class="vd_content-wrapper" >
      <div class="vd_container">
        <div class="vd_content clearfix">
          <div class="vd_content-section clearfix">
            <div class="vd_register-page">
              <div class="heading clearfix">
                <div class="logo">
                  <h2 class="mgbt-xs-5"><img src="{{url('assets/img/logo.png')}}" alt="logo"></h2>
                  <!-- <h2 class="mgbt-xs-5" style="font-weight:bold;padding: 10px;color: #16cc70;">i-Mobilize</h2> -->
                </div>
                <!-- <h4 class="text-center font-semibold vd_grey">USER REGISTRATION</h4> -->
              </div>
              <div class="panel widget">
                <div class="panel-body">

                  <form class="form-horizontal" role="form" id="register-form" method="POST" action="{{ url('/register') }}">
                    {{ csrf_field() }}
                  <div class="alert alert-warning vd_hidden" id="pwd-error">
                    <button type="button" class="close" data-dismiss="" aria-hidden="true" onclick='$("#pwd-error").addClass("vd_hidden")'><i class="icon-cross"></i></button>
                    <span class="vd_alert-icon"><i class="fa fa-exclamation-circle vd_red"></i></span>The password confirmation does not match.</div>

                  <div class="alert alert-success vd_hidden">
                    <button type="button" class="close" data-dismiss="alert" aria-hidden="true"><i class="icon-cross"></i></button>
                    <span class="vd_alert-icon"><i class="fa fa-check-circle vd_green"></i></span>Registration confirmation has been sent to your email. </div>
                    <div class="form-group">
                      <div class="col-md-6">
                        <div class="label-wrapper">
                          <label class="control-label">First Name<span class="vd_red">*</span></label>
                        </div>
                        <div class="vd_input-wrapper" id="first-name-input-wrapper"> <span class="menu-icon"> <i class="fa fa-user"></i> </span>
                          <input type="text" placeholder="John" class="required" required name="firstname" id="firstname" value="">
                        </div>
                      </div>
                      <div class="col-md-6">
                        <div class="label-wrapper">
                          <label class="control-label">Last Name <span class="vd_red">*</span></label>
                        </div>
                        <div class="vd_input-wrapper" id="last-name-input-wrapper"> <span class="menu-icon"> <i class="fa fa-user"></i> </span>
                          <input type="text" placeholder="Doe" class="required" required name="lastname" id="lastname">
                        </div>
                      </div>
                    </div>
                    <div class="form-group">
                      <div class="col-md-12">
                        <div class="label-wrapper">
                          <label class="control-label">Company Name <span class="vd_red">*</span></label>
                        </div>
                        <div class="vd_input-wrapper" id="company-input-wrapper"> <span class="menu-icon"> <i class="fa fa-briefcase"></i> </span>
                          <input type="text" placeholder="Your Company Co, Ltd." class="required" required  name="company" id="company">
                        </div>
                      </div>
                    </div>
                    <div class="form-group">
                      <div class="col-md-12">
                        <div class="label-wrapper">
                          <label class="control-label">Email <span class="vd_red">*</span></label>
                        </div>
                        <div class="vd_input-wrapper" id="email-input-wrapper"> <span class="menu-icon"> <i class="fa fa-envelope"></i> </span>
                          <input type="email" placeholder="Email" class="required" required  name="email" id="email" value="{{ old('email') }}">
                        </div>
                      </div>
                    </div>
                    <div class="form-group">
                      <div class="col-md-6">
                        <div class="label-wrapper">
                          <label class="control-label">Password <span class="vd_red">*</span></label>
                        </div>
                        <div class="vd_input-wrapper" id="password-input-wrapper"> <span class="menu-icon"> <i class="fa fa-lock"></i> </span>
                          <input type="password" placeholder="Password" class="required" required  name="password" id="password">
                        </div>
                      </div>
                      <div class="col-md-6">
                        <div class="label-wrapper">
                          <label class="control-label">Confirm Password <span class="vd_red">*</span></label>
                        </div>
                        <div class="vd_input-wrapper" id="confirm-password-input-wrapper"> <span class="menu-icon"> <i class="fa fa-lock"></i> </span>
                          <input type="password" placeholder="Password" class="required" required  name="password_confirmation" id="confirmpassword">
                        </div>
                      </div>
                    </div>
                    <!-- <div id="vd_login-error" class="alert alert-danger hidden"><i class="fa fa-exclamation-circle fa-fw"></i> Please fill the necessary field </div> -->
                    <div class="form-group">
                      <!-- <div class="col-md-12 mgbt-xs-10 mgtp-20">
                        <div class="vd_checkbox">
                          <input type="checkbox" id="checkbox-1" value="1">
                          <label for="checkbox-1"> Send me newsletter about the latest update</label>
                        </div>
                        <div class="vd_checkbox">
                          <input type="checkbox" id="checkbox-2" value="1" required name="checkbox-2">
                          <label for="checkbox-2"> I agree with <a href="#">terms of service</a></label>
                        </div>
                      </div> -->
                      <div class="col-md-12 text-center mgtp-20 mgbt-xs-5">
                        <button class="btn vd_bg-green vd_white width-100" type="button" id="submit-register" name="submit-register" onclick="onRegister();">Register</button>
                      </div>
                    </div>
                  </form>
                </div>
              </div>
              <!-- Panel Widget -->
              <div class="register-panel text-center font-semibold"> Already Have an Account? <br/>

                <a href="{{url('/login')}}">SIGN IN<span class="menu-icon"><i class="fa fa-angle-double-right fa-fw"></i></span></a> </div>
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

<!-- Footer Start -->
  <footer class="footer-2"  id="footer">
    <div class="vd_bottom ">
        <div class="container">
            <div class="row">
              <div class=" col-xs-12">
                <div class="copyright text-center">

                </div>
              </div>
            </div><!-- row -->
        </div><!-- container -->
    </div>
  </footer>
<!-- Footer END -->

</div>

<!-- .vd_body END  -->
<a id="back-top" href="#" data-action="backtop" class="vd_back-top visible"> <i class="fa  fa-angle-up"> </i> </a>
<!--
<a class="back-top" href="#" id="back-top"> <i class="icon-chevron-up icon-white"> </i> </a> -->

<!-- Javascript =============================================== -->
<!-- Placed at the end of the document so the pages load faster -->
<script type="text/javascript" src="assets/js/jquery.js"></script>
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

function onRegister(){
  if (!$("#register-form").valid()) {
    return;
  }
  if ($('#password').val() != $('#confirmpassword').val()) {
    $('#pwd-error').removeClass('vd_hidden');
    return;
  }
  $("#submit-register").addClass('disabled');
  var formData = new FormData($('#register-form')[0]);
  $.ajax({
      url: "{{ url('/register') }}",
      type: 'POST',
      data: formData,
      async: false,
      cache: false,
      contentType: false,
      processData: false,
      success: function(data) {
        $("#submit-register").enable(true);
        if (data.success) {
          notification("topright","success","fa fa-check-circle vd_green","Success","User registered successfully!");
          window.location.href = "{{url('/login')}}";
        } else {
          notification("topright","error","fa fa-exclamation-circle vd_red","Error", data.error);
        }
      },
      fail: function() {
        $("#submit-register").enable(true);
        notification("topright","error","fa fa-exclamation-circle vd_red","Error", "Server Connection Error");
      },
    });


}
$(document).ready(function() {

	"use strict";

	var form_register_2 = $('#register-form');
	var error_register_2 = $('.alert-danger', form_register_2);
	var success_register_2 = $('.alert-success', form_register_2);
	var warning_register_2 = $('.alert-warning', form_register_2);


	var options = {
		type: "POST",
		url:  $("#register-form").attr('action'),
		dataType: "json",
		success: function(data) {
			if (data.response == "success") {

				setTimeout(function(){
					$('#submit-register .fa-spinner').remove()	;
					$('#submit-register').addClass('disabled');
					success_register_2.fadeIn(500);
					error_register_2.fadeOut(500);
					warning_register_2.fadeOut(500);
				},1500);

			} else if (data.response == "passerror") {
				setTimeout(function(){
					error_register_2.fadeOut(500);
					warning_register_2.fadeIn(500);
					$('#submit-register .fa-spinner').remove()	;
					form_register_2.find('#submit-register').removeClass('disabled');
					scrollTo(form_register_2,-100);
				},1500);


			} else if (data.response == "empty") {

			} else if (data.response == "unexpected") {

			}



		},
		error: function() {

		}
	};


        form_register_2.validate({
            errorElement: 'div', //default input error message container
            errorClass: 'vd_red', // default input error message class
            focusInvalid: false, // do not focus the last invalid input
            ignore: "",
            rules: {
                firstname: {
                    minlength: 3,
                    required: true
                },
                lastname: {
                    minlength: 3,
                    required: true
                },
                email: {
                    required: true,
                    email: true
                },
                website: {
                    required: true,
                    url: true
                },
                company: {
                    minlength: 3,
                    required: true
                },
                country: {
                    required: true
                },
                phone: {
                    required: true
                },
                password: {
                    minlength: 6,
                    required: true
                },
                password_confirmation: {
                    minlength: 6,
                    required: true
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
				        scrollTo(form_register_2,-100);

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

        });



});
</script>
</body>
</html>
