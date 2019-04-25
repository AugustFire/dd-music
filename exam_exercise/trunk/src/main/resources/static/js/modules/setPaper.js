$(function() {
$("button[type=submit]").unbind();
$("#sub-btn").click(function(){
	var n = $("#set-pager").find("input[type=checkbox]:checked").length;
	var num = 0;
	var num1 = 0;
	if(n==0){
		layer.msg("请选择试卷");
		window.event.returnValue = false ;
		return false;
		
	}else if(n>0){
		var result = true;
		var message = "";
		
		$("#set-pager").find("input[type=checkbox]:checked").each(function(){	
			var $this = $(this);
			var tip = $this.siblings("label").text();
			var t = $this.parent().siblings(".setweight").siblings("span").length;
			var input = $this.parent().siblings("span").find("input");
			var score = $this.parent().siblings(".setweight").find("input").val();
			if(score==""){
				result = false;
				message = tip+"权重不能为空";
				return false;
			}else{
				var s = parseInt(score);
				num += s;						
			}
			if(t>0){
				if(input.eq(0).val()==""){
					result = false;
					message = tip+"机器评分权重不能为空";
					//layer.msg(tip+"机器评分权重不能为空");
					//window.event.returnValue = false ;
					return false;
				}else{
					var n = input.eq(0).val();
					var s = parseInt(n);
					num1+=s;
					
				}
				if(input.eq(1).val()==""){
					result = false;
					message = tip+"专家评分权重不能为空";
					//layer.msg(tip+"专家评分权重不能为空");
					//window.event.returnValue = false ;
					return false;
				}else{
					var n = input.eq(1).val();
					var s = parseInt(n);
					num1+=s;
				}
				if(num1!==100){
					result = false;
					message = tip+"机器评分权重和专家评分权重相加不等于100";
					//layer.alert("机器评分权重和专家评分权重相加不等于100");
					//window.event.returnValue = false ;
					return false;
				}
			}

		});	
		if(result){
			if(num!==100){
				layer.alert("权重相加不等于100");
				window.event.returnValue = false ;
				return false;
			}
			}else{
			layer.alert(message);
			window.event.returnValue = false ;
			return false;
		}
		
	}
		
	




});

	
})