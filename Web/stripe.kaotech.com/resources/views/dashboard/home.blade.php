@extends('layouts.main')

@section('sub-title')
<div class="" style="padding-bottom:10px;    text-align: center;">
  <a href="#"><img alt="logo" src="assets/img/logo.png" style="height:50px;"></a>
  <!-- <span style="font-size: 15px;font-weight: normal;padding-left:10px">Stripe : gudansk@gmail.com</span> -->
</div>
@endsection


@section('content')
  <div class="row">
    <div class="col-md-12">
      <div class="panel widget">
        <div class="panel-heading vd_bg-grey">
          <h3 class="panel-title"> <span class="menu-icon"> <i class="fa fa-dot-circle-o"></i> </span> PAYMENT LOGS </h3>
        </div>
        <style media="screen">
          @media only screen and (max-device-width: 500px) {
            td {
              padding: 2px!important;
              word-break: break-word;
              font-size: 10px;
            }
            th{
                  padding: 2px!important;
                  font-size: 11px!important;
              }
          }
          th{
                font-size: 14px;
                text-align: center!important;
          }
          td{
            text-align: center!important;
          }
        </style>
        <div class="panel-body table-responsive" style="padding:0px;">
          <table id="datatable" class="tablesaw table-striped table-bordered table-hover" data-tablesaw-mode="swipe"
              data-tablesaw-sortable data-tablesaw-sortable-switch data-tablesaw-minimap
              data-tablesaw-mode-switch>
                <thead>
                  <tr>
                    <th data-tablesaw-sortable-col data-tablesaw-sortable-default-col data-tablesaw-priority="persist">
                      TIME
                    </th>
                    <th data-tablesaw-sortable-col data-tablesaw-priority="1">
                      FROM
                    </th>
                    <th data-tablesaw-sortable-col data-tablesaw-priority="2">
                      AMOUNT
                    </th>
                    <th data-tablesaw-sortable-col data-tablesaw-priority="3">
                      STATUS
                    </th>
                  </tr>
                </thead>
                <tbody>
                  @foreach($data as $one)
                  <tr>
                    <td>{{$one['time']}}</td>
                    <td>{{$one['from']}}</td>
                    <td>{{$one['amount']}}</td>
                    <td>
                      @if($one['status']==0)
                      <span class="label label-warning">Unpaid</span>
                      @else
                      <span class="label label-success">Paid</span>
                      @endif
                    </td>
                  </tr>
                  @endforeach
                </tbody>
              </table>
        </div>
      </div>
      <!-- Panel Widget -->
    </div>
    <!-- col-md-12 -->
    <div class="" style="text-align:center">
      <button id="showMore" class="btn vd_btn vd_bg-blue font-semibold" style="margin-bottom:15px;" onclick='onVeiwMore();'>View more</button>
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
<script type="text/javascript">
  var log_count = {{count($data)}};
  if (log_count < 10) {
    $('#showMore').hide();
  }

  function onVeiwMore(){
    showProgress();
    $.ajax({
        url: "{{ url('/getlogs') }}?count="+log_count,
        type: 'GET',
        success: function(data) {
            if (data.length < 10) {
              $('#showMore').hide();
            }
            log_count += data.length;
            for (var i = 0; i < data.length; i++) {
              tr_html = '<tr>' +
                        '<td>' + data[i].time +'</td>' +
                        '<td>' + data[i].from +'</td>' +
                        '<td>' + data[i].amount +'</td>';
              if (data[i].status==0) {
                tr_html += '<td><span class="label label-warning">Unpaid</span></td></tr>';
              } else {
                tr_html += '<td><span class="label label-success">Paid</span></tr>';
              }
              $('#datatable tr:last').after(tr_html);
            }

            hideProgress();

        },
        fail: function() {
          notification("topright","error","fa fa-exclamation-circle vd_red","Error", "Server Connection Error");
          hideProgress();
        },
      });
  }

  function showProgress(){
    $('#progressDlg').addClass('show');
  }

  function hideProgress(){
    $('#progressDlg').removeClass('show');
  }

</script>
@endsection
