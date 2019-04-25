$(function() {
	//点击切换验证图片
	$("#codeImg").click(function() {
		$(this).attr("src", "/code?t=" + Math.random());
	});
	$(".btn").click(function(){
		var password = $("#password").val();
		var code = $("#code").val();
		var name = $("#login").val();
		if(!$.trim(name)){
			$(".error-msg").first().html("用户名不能为空");
			$(".error-msg").last().html("");
		}else if(!$.trim(password)){
			$(".error-msg").first().html("密码不能为空");
			$(".error-msg").last().html("");
		}else{
			if(!$.trim(code)){
				$(".error-msg").first().html("验证码不能为空");
				$(".error-msg").last().html("");
				return false;
			}	
		}
	});
	


});