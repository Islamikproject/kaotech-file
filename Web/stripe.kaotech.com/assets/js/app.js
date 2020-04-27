var DocumnetType = function() {
	var m_data = {
		imagefilename:'',
		audiofilename:'',
	};

	var settings = {
		RISK_RATING_LOW: 			0,
		RISK_RATING_MEDIUM: 	1,
		RISK_RATING_HIGH: 		2,

		STATUS_UNACCEPT:     	0,
		STATUS_ACCEPT:     		1,
		STATUS_COMPLETE:     	2,
		STATUS_CLOSED:    		3,

		selContentID:0,
		//  Modal Dialog Controls
		myModal:						$('#myModal'),
		frmModal:						$('#frmModal'),
		txtModalTitle:			$('#txtModalTitle'),
		btnContentControl:	$('#btnContentControl'),

		txtContentID:   		$('#txtContentID'),
		txtProjectName:   	$('#txtProjectName'),
		txtRiskDescription: $('#txtRiskDescription'),
		txtSourceDoc:   		$('#txtSourceDoc'),
		optRatingLow:   		$('#optRatingLow'),
		optRatingMedium:   	$('#optRatingMedium'),
		optRatingMedium:   	$('#optRatingMedium'),
		optRatingHigh:   		$('#optRatingHigh'),
		txtRiskChampion:   	$('#txtRiskChampion'),
		optStatusUnaccept: 	$('#optStatusUnaccept'),
		optStatusAccept:   	$('#optStatusAccept'),
		optStatusComplete:  $('#optStatusComplete'),
		optStatusClosed:   	$('#optStatusClosed'),
		txtWorkPlan:   			$('#txtWorkPlan'),
		txtNote:   					$('#txtNote'),

		//preveiw Modal
		myViewModal:			$('#myViewModal'),
		lblViewTitle:			$('#lblViewTitle'),
		imgView:					$('#imgView'),
		lblViewDetail:		$('#lblViewDetail'),
		audioView:				$('#audioView'),
		audioSrc:					$('#audioSrc'),

		//end preView Modal
	};

	var bindUI = function(){
		//bind modal Dialog events

		settings.myViewModal.on('hidden.bs.modal', function () {
			settings.audioView[0].pause();
		})
		//End bind modal Dialog events


	}

	var initModal = function(content_id, mode){

			settings.txtModalTitle.html('Register Risk');
			settings.btnContentControl.html('Register');
			settings.btnContentControl.show();

			settings.txtProjectName.prop('disabled', false);
			settings.txtRiskDescription.prop('disabled', false);
			settings.optRatingLow.prop('disabled', false);
			settings.txtRiskChampion.prop('disabled', false);
			settings.optRatingLow.prop('disabled', false);
			settings.optRatingMedium.prop('disabled', false);
			settings.optRatingHigh.prop('disabled', false);
			settings.txtSourceDoc.prop('disabled', false);
			settings.optStatusUnaccept.prop('disabled', false);
			settings.optStatusAccept.prop('disabled', false);
			settings.optStatusComplete.prop('disabled', false);
			settings.optStatusClosed.prop('disabled', false);
			settings.txtWorkPlan.prop('disabled', false);
			settings.txtNote.prop('disabled', false);

			settings.txtProjectName.val('');
			settings.txtRiskDescription.val('');
			settings.txtSourceDoc.val('');
			settings.optRatingLow.click();
			settings.txtRiskChampion.val('');
			settings.optStatusUnaccept.click();
			settings.txtWorkPlan.val('');
			settings.txtNote.val('');
		if (content_id != 0) {
			settings.txtModalTitle.html('Update Risk');
			settings.btnContentControl.html('Save');
			settings.btnContentControl.show();

			settings.txtProjectName.prop('disabled', true);
			settings.txtRiskDescription.prop('disabled', true);
			settings.optRatingLow.prop('disabled', true);
			settings.txtRiskChampion.prop('disabled', true);

			if (mode == 1) {
				settings.txtModalTitle.html('View Risk');
				settings.btnContentControl.hide();
			}
		}
	}


		setModalData = function(content_id, mode){
			settings.txtContentID.val(content_id);
			settings.txtProjectName.val($($(".content-" + content_id + " input")[1]).val());
			settings.txtRiskDescription.val($($(".content-" + content_id + " input")[4]).val());
			settings.txtSourceDoc.val($($(".content-" + content_id + " input")[7]).val());
			settings.txtRiskChampion.val($($(".content-" + content_id + " input")[8]).val());
			settings.txtWorkPlan.val($($(".content-" + content_id + " input")[2]).val());
			settings.txtNote.val($($(".content-" + content_id + " input")[5]).val());

			rating = $($(".content-" + content_id + " input")[3]).val();
			if (rating == settings.RISK_RATING_LOW) {
				settings.optRatingLow.click();
			} else if (rating == settings.RISK_RATING_MEDIUM) {
				settings.optRatingMedium.click();
			} else if (rating == settings.RISK_RATING_HIGH) {
				settings.optRatingHigh.click();
			}

			settings.optRatingLow.prop('disabled', true);
			settings.optRatingMedium.prop('disabled', true);
			settings.optRatingHigh.prop('disabled', true);

			status = $($(".content-" + content_id + " input")[6]).val();
			if (status == settings.STATUS_UNACCEPT) {
				settings.optStatusUnaccept.click();
			} else if (status == settings.STATUS_ACCEPT) {
				settings.optStatusAccept.click();
			} else if (status == settings.STATUS_COMPLETE) {
				settings.optStatusComplete.click();
			} else if (status == settings.STATUS_CLOSED) {
				settings.optStatusClosed.click();
			}

			if (mode == 1) {
				settings.txtSourceDoc.prop('disabled', true);
				settings.optStatusUnaccept.prop('disabled', true);
				settings.optStatusAccept.prop('disabled', true);
				settings.optStatusComplete.prop('disabled', true);
				settings.optStatusClosed.prop('disabled', true);
				settings.txtWorkPlan.prop('disabled', true);
				settings.txtNote.prop('disabled', true);
			}


	}

	var getFileName = function(str){
		return str.substring(str.lastIndexOf("/") + 1, str.length);
	}

	var validateModalForm = function(){
		var res = true;
		if (!settings.frmModal.valid()) {
			res =  false;
		}
		if (settings.txtContentID.val() == 0) {
			if (settings.txtImageFile.val() == "") {
				settings.divImageInput.addClass('error');
				settings.lblImageValidErr.show();
				res = false;
			}
			if (settings.txtAudioFile.val() == "") {
				settings.divAudioInput.addClass('error');
				settings.lblAudioValidErr.show();
				res = false;
			}
		}
		return res;

	}

	var initUI = function(){

	}

	var showNotification = function(){
		switch (resultStatus) {
			case STATUS_ADD_SUCCESS:
				notification("topright","success","fa fa-check-circle vd_green","Success","Audio added successfully!");
				break;
			case STATUS_UPDATE_SUCCESS:
				notification("topright","success","fa fa-check-circle vd_green","Success","Audio updated successfully!");
				break;
			case STATUS_REMOVE_SUCCESS:
				notification("topright","success","fa fa-check-circle vd_green","Success","Audio removed successfully!");
				break;
			default:
				break;
		}
	}

	return {
		EDIT_ITEM:  					0,
		ADD_ITEM:							1,
		init:function(){
			bindUI();
			initUI();
		},

		showModal:function(content_id, mode){
			// when from View Modal
			settings.myViewModal.modal('hide');
			if (content_id == undefined) {
				content_id = settings.selContentID;
			}
			//End when from View Modal

			initModal(content_id, mode);
			if (content_id != 0) {
				setModalData(content_id, mode);
			}
			settings.myModal.modal('show');
		},

		showViewModal:function(content_id){
			settings.selContentID = content_id;

			settings.lblViewTitle.html($($(".audio-" + content_id + " input")[0]).val());
			settings.lblViewDetail.html($($(".audio-" + content_id + " input")[4]).val());
			var d = new Date();
    	var timestamp = d.getTime();
			var img_url = $($(".audio-" + content_id + " input")[2]).val() + "?" + timestamp;
			settings.imgView.attr('src', img_url);
			// settings.imgView.attr('src', $($(".audio-" + content_id + " input")[2]).val());

			var audio_url = $($(".audio-" + content_id + " input")[1]).val() + "?" + timestamp;
			settings.audioSrc.attr('src', audio_url);
			// settings.audioSrc.attr('src', $($(".audio-" + content_id + " input")[1]).val());
			settings.audioView[0].pause();
			settings.audioView[0].load();

			settings.myViewModal.modal('show');
		},

		controlContent(){
			if(!settings.frmModal.valid()) return;
			settings.btnContentControl.enable(false);
			var formData = new FormData(settings.frmModal[0]);
		  $.ajax({
					url: APP_URL + '/addRisk',
		      type: 'POST',
		      data: formData,
		      async: false,
		      cache: false,
		      contentType: false,
		      processData: false,
		      success: function(data) {
		        if (data.success) {
							if (data.status == 0) {
								notification("topright","success","fa fa-check-circle vd_green","Success","Risk Registered successfully!");
							} else {
								notification("topright","success","fa fa-check-circle vd_green","Success","Risk Updated successfully!");
							}
		          window.location.href = APP_URL + '/home';
		        } else {
							settings.myViewModal.modal('hide');
							settings.btnContentControl.enable(true);
		          notification("topright","error","fa fa-exclamation-circle vd_red","Error", data.error);
		        }
		      },
		      fail: function() {
		        notification("topright","error","fa fa-exclamation-circle vd_red","Error", "Server Connection Error");
		      },
		    });
			// settings.frmModal.submit();
		},

		removeContent(id){
			$.confirm({
        text: "Are you sure you want to remove this Risk?",
        confirm: function(button) {
					$.ajax({
							url: APP_URL + '/removeRisk?risk_id='+id,
				      type: 'GET',
				      success: function(data) {
				        if (data.success) {
									window.location.href = APP_URL + '/home';
									notification("topright","success","fa fa-check-circle vd_green","Success","Risk Removed successfully!");
				        } else {
									settings.myViewModal.modal('hide');
									settings.btnContentControl.enable(true);
				          notification("topright","error","fa fa-exclamation-circle vd_red","Error", data.error);
				        }
				      },
				      fail: function() {
				        notification("topright","error","fa fa-exclamation-circle vd_red","Error", "Server Connection Error");
				      },
				    });
				}
			});
		},

	};
}

var dctype = new DocumnetType();
dctype.init();
