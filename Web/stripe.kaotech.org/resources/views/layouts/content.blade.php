<div class="vd_content-wrapper1">
      <div class="vd_container1">
        <div class="vd_content clearfix">
          <div class="vd_head-section clearfix" hidden="">
            <div class="vd_panel-header">
              <ul class="breadcrumb">
                <li><a href="{{url('/')}}">Home</a> </li>
                <li>@yield('sub-title') </li>
                <!-- <li><a href="layouts-simple.html">Layouts</a> </li> -->
                <!-- <li class="active">Layout Simple Medium Profile</li> -->
              </ul>
              <div class="vd_panel-menu hidden-sm hidden-xs" data-intro="<strong>Expand Control</strong><br/>To expand content page horizontally, vertically, or Both. If you just need one button just simply remove the other button code." data-step=5  data-position="left">
                <div data-action="remove-navbar" data-original-title="Remove Navigation Bar Toggle" data-toggle="tooltip" data-placement="bottom" class="remove-navbar-button menu"> <i class="fa fa-arrows-h"></i> </div>
                <div data-action="remove-header" data-original-title="Remove Top Menu Toggle" data-toggle="tooltip" data-placement="bottom" class="remove-header-button menu"> <i class="fa fa-arrows-v"></i> </div>
                <div data-action="fullscreen" data-original-title="Remove Navigation Bar and Top Menu Toggle" data-toggle="tooltip" data-placement="bottom" class="fullscreen-button menu"> <i class="glyphicon glyphicon-fullscreen"></i> </div>

              </div>

            </div>
          </div>
          <!-- vd_head-section -->


          <div class="vd_title-section clearfix" style="    margin: 0px -15px;">
            <div class="vd_panel-header">
              @yield('sub-title')
              <!-- <small class="subtitle">@yield('sub-title-comment')</small> </div> -->
          </div>
          <!-- vd_title-section -->

          <div class="vd_content-section clearfix">

						@yield('content')




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
