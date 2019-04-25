$(function() {
	layui.use(['form', 'layedit', 'laydate'], function(){
		  var form = layui.form()
		  ,layer = layui.layer
		  ,layedit = layui.layedit
		  ,laydate = layui.laydate;
		  
		});
	$(".btn").click(function(){
		var password = $("#password").val();
		var code = $("#code").val();
		var name = $("#login").val();
		if(!$.trim(name)){
			layer.alert("用户名不能为空");
			$(".error-msg").last().html("");
		}else if(!$.trim(password)){
			layer.alert("密码不能为空");
			$(".error-msg").last().html("");
		}else{
			$("#session_form").submit();
		}
	});
	


});