$(function() {
	layui.use(['form', 'layedit', 'laydate'], function(){
		  var form = layui.form()
		  ,layer = layui.layer
		  ,layedit = layui.layedit
		  ,laydate = layui.laydate;
		  
		});
	var usernameValid = false;
	var passwordValid = false;
	var repasswordValid = false;
	var emailValid = false;
	var codeValid = false;
	var re = /^[a-z0-9]{6,20}$/;
	var resname = /^[a-zA-Z0-9\u4e00-\u9fa5]{2,20}$/;
/*	var resname =/^[a-z0-9]{6,20}\u4e00-\u9fa5$/; */
	// 验证用户名是否存在或者为空
	$("#user").blur(function() {
		usernameValid = false;
		var username = $(this).val().trim();
		// 判断用户名是否为空
		if (username == "") {		
			return;
		}
		var result=  resname.test(username);
		if(!result){
			layer.alert("用户名必须是2-20位数字,中文或者字母!");
			return;
		}
		usernameValid = true;
	});

	// 检查两次密码是否一致
	$("#password").blur(function() {
		passwordValid = false;
		var password = $(this).val().trim();
		if (password == "") {
			return;
		}
		var re_password = /^[a-z0-9]{6,12}$/;
		if (!re_password.test(password)) {
			layer.alert("密码必须是6-12位数字或者字母!");
			return;
		}
		var repassword = $.trim($("#repassword").val());
		if (repassword != "" && password != repassword){
			layer.alert("两次密码输入不一样，请重新输入!");
			return;
		}
		passwordValid = true;
	});

	$("#repassword").blur(function() {
		repasswordValid = false;
		var confirmpw = $(this).val().trim();
		if (confirmpw == "") {
			layer.alert("重复密码不能为空!");
			return;
		} else if (confirmpw != $("#password").val()) {
			layer.alert("两次密码输入不一样，请重新输入!");
			return;
		} else {
			repasswordValid = true;
		}
	});
	
/*	验证邮箱*/
	$("#email").blur(function(){
		emailValid=false;
		var text = $(this).val();
		var email=text.replace(/^\s+|\s+$/g,"").toLowerCase();
		var reg=/^[a-z0-9](\w|\.|-)*@([a-z0-9]+-?[a-z0-9]+\.){1,3}[a-z]{2,4}$/i;
		if (email == "") {
			return;
		}
		if (email.match(reg)==null) {
			layer.alert("请输入有效的邮箱地址");
		    }else{
		    	emailValid = true;
		    };
	
	})
		//发送验证码
	$("#send_email").click(function () {
				var email = $("#email").val();
				$("#email_err").text("");
				var $this = $(this);
				//判断邮箱是否已存在
				if($.trim(email)!==""){
					$.ajax({
						method: 'get',
						url: '/user/email/exist?email=' + email,
						success: function (data) {
							if (data.err != null && data.err != "") {
								layer.alert(data.err);
							
							} else if (data.code = 200 && data.existEmail) {
								layer.alert("邮箱已绑定");
							} else{
								time($this);
								sendEmail(email);
							}
						}
					});							
				}else{
					layer.alert("请输入邮箱！")
			
					
				}

			});
	//发送验证码
	$("#resend_email").click(function () {
			var email = $("#email").val();
			$("#email_err").text("");
			var $this = $(this);
			if($.trim(email)!==""){
				time($this);
				sendEmail(email);			
			}else{
				layer.alert("请输入邮箱！")
			}

		});
	function sendEmail(email) {
		$("#email_err").text("");
		$.ajax({
			url: "/user/email/send_code?email=" + email,
			type: 'POST',
			dataType: 'json',
			success: function (data) {
				if (data.code == 200) {
					
				} else {
					layer.msg("邮件发送失败")
				}
			}
		});
		return false;
	}

var wait=60;
function time(o) {
		if (wait == 0) {
			o.attr("disabled",false);
			o.removeClass("gray");
			o.text("发送验证码");
			wait = 60;
			
		} else {
			o.addClass("gray");
			o.attr("disabled",true);
			o.text("重新发送(" + wait + ")s");
			wait--;
			setTimeout(function() {
				time(o)
			},
			1000)
		}
	}
		$("#code").blur(function() {
			codeValid=false;
			var email = $("#email").val();
			var code =$.trim($("#code").val());
			var params={
					"email":email,
					"code":code,
			}		
			$.ajax({
				method: 'post',
				url: '/user/email/check',
				data:params,
				success: function (data) {
					if (data.code==200) {
						$("#codeerr").text("");
						codeValid=true;
					} else if(code!==""){
						layer.alert("验证码错误！")
						return;
					}else{
						return;
					}
				}
			});
			
		})
		
	$("#res_form").submit(function() {
		if(!usernameValid){
			layer.msg("请输入姓名！")
			return false;
		}
		if(!emailValid){
			layer.msg("请输入邮箱！")
			return false;
		}
		if(!codeValid){
			layer.msg("验证码为空或已过期！")
			return false;
		}
		if(!passwordValid){
			layer.msg("请输入密码！")
			return false;
		}
		if(!repasswordValid){
			layer.msg("请输入确认密码！")
			return false;
		}

	});
			
		
		$("#find_form").submit(function() {
			if(!emailValid){
				layer.msg("请输入邮箱！")
				return false;
			}
			if(!codeValid){
				layer.msg("验证码为空或已过期！")
				return false;
			}
			if(!passwordValid){
				layer.msg("请输入密码！")
				return false;
			}
			if(!repasswordValid){
				layer.msg("请输入确认密码！")
				return false;
			}

		});
		
		$("#set-user").submit(function() {
			// if(!usernameValid){
			// 	layer.msg("请输入姓名！")
			// 	return false;
			// }

		});
});



