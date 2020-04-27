<!-- Footer Start -->


</div>

<script type="text/javascript" src="{{url('assets/js/jquery.js')}}"></script>
<script type="text/javascript" src="{{url('assets/js/jquery.confirm.js')}}"></script>
<script type="text/javascript" src="{{url('assets/js/bootstrap.min.js')}}"></script>
<script type="text/javascript" src="{{url('assets/plugins/jquery-ui/jquery-ui.custom.min.js')}}"></script>
<script type="text/javascript" src="{{url('assets/plugins/jquery-ui-touch-punch/jquery.ui.touch-punch.min.js')}}"></script>
<script type="text/javascript" src="{{url('assets/js/caroufredsel.js')}}"></script>
<script type="text/javascript" src="{{url('assets/js/plugins.js')}}"></script>
<script type="text/javascript" src="{{url('assets/plugins/breakpoints/breakpoints.js')}}"></script>
<script type="text/javascript" src="{{url('assets/plugins/dataTables/jquery.dataTables.min.js')}}"></script>
<script type="text/javascript" src="{{url('assets/plugins/prettyPhoto-plugin/js/jquery.prettyPhoto.js')}}"></script>
<script type="text/javascript" src="{{url('assets/plugins/mCustomScrollbar/jquery.mCustomScrollbar.concat.min.js')}}"></script>
<script type="text/javascript" src="{{url('assets/plugins/tagsInput/jquery.tagsinput.min.js')}}"></script>
<script type="text/javascript" src="{{url('assets/plugins/bootstrap-switch/bootstrap-switch.min.js')}}"></script>
<script type="text/javascript" src="{{url('assets/plugins/blockUI/jquery.blockUI.js')}}"></script>
<script type="text/javascript" src="{{url('assets/plugins/pnotify/js/jquery.pnotify.min.js')}}"></script>

<script type="text/javascript" src="{{url('assets/plugins/tablesaw/tablesaw.jquery.js')}}"></script>
<script type="text/javascript" src="{{url('assets/plugins/tablesaw/tablesaw0.js')}}"></script>

<script type="text/javascript" src="{{url('assets/js/theme.js')}}"></script>
<script type="text/javascript" src="{{url('assets/plugins/dataTables/dataTables.bootstrap.js')}}"></script>
<script type="text/javascript" src="{{url('assets/plugins/google-code-prettify/prettify.js')}}"></script>
<script type="text/javascript" src="{{url('assets/custom/custom.js')}}"></script>

<script type="text/javascript">
  var CSRF_TOKEN = "{{ csrf_token() }}";
  var APP_URL = "{{url('/')}}";
	jQuery(document).ready(function($){
		"use strict";

		$('#data-table').dataTable();
	});
</script>



@yield('bottom_js')




</body>
</html>
