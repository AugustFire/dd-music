$(function() {
 
	layui.use(['form', 'layedit', 'laydate'], function(){
		  var form = layui.form()
		  ,layer = layui.layer
		  ,layedit = layui.layedit
		  ,laydate = layui.laydate;
		  
		});
	var res = /^[a-z0-9]{6,20}$/;
	// 验证用户名是否存在或者为空
	$("#login").blur(function() {
		var t = $(this);
		var re = /^[a-z0-9]{6,20}$/;
		var id = "login";
		var error = "用户名必须是6-20位数字或者字母"
		var text = "用户名";
		if(avalid(id,text,error,re)){
			ajax(id,text,t);
		}
		
	});

	// 验证手机号码是否存在或者为空
	$("#phone").blur(function() {
		var t = $(this)
		var re = /(13[0-9]|17[0-9]|14[5|7]|15[0|1|2|3|5|6|7|8|9]|18[0|1|2|3|5|6|7|8|9])\d{8}$/;
		var id="phone";
		var error = "手机号码长度不正确！"
		var text = "手机号码";
		if(avalid(id,text,error,re)){
			ajax(id,text,t);
		}
	});
	
	// 验证邮箱是否存在或者为空
	$("#email").blur(function() {
		var t = $(this)
		var id="email";
		var text = "邮箱";
		var error = "请输入有效的邮箱地址！"
		var re =  /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
		if(avalid(id,text,error,re)){
			ajax(id,text,t);
		}
	});
	// 验证身份证是否存在或者为空
	$("#idcard").blur(function() {
		var t = $(this)
		var re = /^\d{15}|\d{18}$/;
		var id = "idcard";
		var error = "身份证长度不正确！"
		var text = "身份证";
		avalid(id,text,error,re);
	});
	// 检查两次密码是否一致
	$("#password").blur(function() {
		var password = $(this).val().trim();
/*		if (password == "") {
			layer.alert("密码不能为空!");
			return;
		}*/
		if (password !== "") {
			if (!res.test(password)) {
				layer.alert("密码必须是6-20位数字或者字母!");
				return;
			}
			var repassword = $("#repassword").val().trim();
			if (repassword != "" && password != repassword){
				layer.alert("两次密码输入不一样，请重新输入!");
				return;
			}
		}


	});

	$("#repassword").blur(function() {
		var confirmpw = $(this).val().trim();
	/*	if (confirmpw == "") {
			layer.alert("重复密码不能为空!");
			return;
		} else*/ 
		if (confirmpw !== "") {
			if (confirmpw != $("#password").val()) {
				layer.alert("两次密码输入不一样，请重新输入!");
				return;
			}
		}

	});

/*	$("button[type=submit]").click(function(){
		var form = $(this).parents("form");
		form.find("input[type=text]").each(function(){
			var txt = $.trim($(this).val());
			if(txt==""){
				$(this).focus();
				$(this).attr("placeholder","不能为空");
				event.preventDefault();
				return false;
			}else{
				$(this).parents("form").submit();
				
				
			}
		});
	});*/
	
	$("form").submit(function() {
		$(this).find("input[type=text]").each(function(){
			var txt = $.trim($(this).val());
			if(txt==""){
				$(this).focus();
				$(this).attr("placeholder","不能为空");
				event.preventDefault();
				return false;
			}
		});

	});
	/*错误提示信息回填*/
	var str = $(".error-text").text();
	$("#register_form").find("input").each(function(){
		var name = $(this).attr("name");
		var val = getVal(str,name);
		if($(this).attr("type") == 'radio'){
			if(val == $(this).val()){
				$(this).attr('checked', true);
			}
			
		}else{
			$(this).val(val);
		}
		
	});
	
	function getVal(str, key){
		var re = new RegExp(key + '=(.*?)[,|\}]');
		if(str.match(re)){
			return RegExp.$1;
		}
	}

	function ajax(id,text,t){
		var name = t.val();
		var url =  "examinee/"+id+"/exsit?"+id+"="+name;
		$.ajax({
			method: "GET",
			url: url,
			success:function(data){
				if(data){
					layer.alert(text+'已被注册，请重新输入', {
							
						}, function(){
						    t.val("");
						    layer.closeAll();
						});

				}else{
				}
			}
		});
	}
	function avalid(id,text,error,re){
			var name = $("#"+id).val().trim();
			// 判断是否为空
			if (name == "") {
				layer.alert(text+"不能为空！");
				return;
			}
			
			var result=  re.test(name);
			if (name !== "") {
				if(!result){
					layer.alert(error);
					return;
				}
			}

			return true;

	}


});