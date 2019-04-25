$(function() {

	jQuery.validator.messages.required = "";
	jQuery.validator.messages.digits = "<span class='error' generated='true' style='color: red; font-size: 14px;'>请输入密码 </span>";
	jQuery.validator.messages.digits = "<span class='error' generated='true' style='color: red; font-size: 14px;'>验证码不正确</span>";
	jQuery.validator.messages.email = "<span class='error' generated='true' style='color: red; font-size: 14px;'>请填写正确的email </span>";
	
	$("#session_form").validate({
		rules : {
			login : {required : true},
			password : {required : true},
			code : {required : true}
		}
	});	
	
	$("#register_form").validate({
		rules : {
			login : {required : true},
			password : {required : true},
			repassword : {equalTo:"#password"},
			name : {required : true},
			idcard : {required : true},
			phone : {required : true},
			email : {email : true}	
		}
	});	
	
	$("#examinee_form").validate({
		rules : {
			login : {required : true},
			name : {required : true},
			idcard : {required : true},
			phone : {required : true},
			email : {email : true}	
		}
	});	
	
	$("#examAdd_form").validate({
		rules : {
			title : {required : true},
			intro : {required : true},
			startAt : {required : true},
			endAt : {required : true}
		}
	});	
	
	$("#examEdit_form").validate({
		rules : {
			title : {required : true},
			intro : {required : true},
			startAt : {required : true},
			endAt : {required : true}
		}
	});	
	
	$("#examPaper_form").validate({
		rules : {
			title : {required : true},
			intro : {required : true},
			startAt : {required : true},
			endAt : {required : true}
		}
	});
	
	$("#examPaperAdd_form").validate({
		rules : {
			title : {required : true},
			year : {required : true},
			questionNum : {required : true},
			score : {required : true}
		}
	});
	
	$("#examPaperEdit_form").validate({
		rules : {
			title : {required : true},
			year : {required : true},
			questionNum : {required : true},
			score : {required : true}
		}
	});
	
	$("#passwordBackEmail_form").validate({
		rules : {
			email : {email : true}
		}
	});
	
	$("#passwordBackInput_form").validate({
		rules : {
			password : {required : true},
			repassword : {equalTo:"#password"}
		}
	});
	
	jQuery.extend(jQuery.validator.messages, {
		  equalTo: "<span class='error' generated='true' style='color: red; font-size: 12px;'>两次密码输入不一致 </span>",
		});
});