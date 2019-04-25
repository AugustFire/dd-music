$(function() {
	
/*	全部选中
	$("#all-check").click(function(){
		var button = $(this);
		var input = $(".layui-table").find("input");
		if(input.is(":checked")){
			input.prop("checked",false);
			button.text("全部选中");	
		}else{
			input.prop("checked",true);
			button.text("全部取消");	
		}
	
	});
*/
	/*单个通过*/
	$("body").on("click",".pass",function(){
		var id = $(this).attr("id");
		var url = "/enrollment/" + id + "/pass";
		layer.confirm('确定通过？', {
		  btn: ['确定','取消'] //按钮
		}, function(){
			$.ajax({
				url : url,
				type : 'GET',
				success : function(data) {
					if (data) {
						layer.msg("报名已通过");
						location.reload();
					} else {
					}
				}
			});
		}, function(){

		  });
	});
		
	
/*	批量通过*/	
	$("body").on("click","#pass-all",function(){
		var id = "";
		/*获取已勾选选项id*/
		$(".layui-table").find("input:checked").each(function(){
			var inputId= $(this).siblings().val();
			id += inputId+",";
		});
		if(id==""){
			layer.msg("请先勾选要通过的选项");
		}else{
			var url = "/enrollment/" + id + "/pass";
			layer.confirm('确定通过？', {
			  btn: ['确定','取消'] //按钮
			}, function(){

					$.ajax({
						url : url,
						type : 'GET',
						success : function(data) {
							if (data) {
								layer.msg("报名已通过");
								location.reload();
							} else {
							}
						}
					});	


			}, function(){

			  });
		}

	});
	
	
	
	/*单个驳回*/
	$("body").on("click",".unpass",function(){
		reasonList();
		var id = $(this).parents("tr").find("input[type=hidden]").val();
		var url = "/enrollment/" + id + "/unpass";
		$("body").on("click",".btn-primary",function(){
			var textarea =$.trim($(".modal-body").find("textarea").val());
			if(textarea==""){
				var n = $("#reason-body").find("input:checked").length;
				if(n<=0){
					layer.msg("请说明驳回理由");
					return false;
				}	
			}else{
				ajaxReject(url);	
			}
			
		
		});
	});
	
/*	批量驳回*/
	$("body").on("click","#unpass-all",function(){
		var id = "";
		/*获取已勾选选项id*/
		$(".layui-table").find("input:checked").each(function(){
			var inputId= $(this).siblings().attr("value");
			id += inputId+",";
		});
		console.log(id);
		/*	判断是否勾选选项*/		
		if(id==""){
			layer.msg("请先勾选要驳回的选项");
			return false;
		}else{
			reasonList();
			$("body").on("click",".btn-primary",function(){
				var textarea =$.trim($(".modal-body").find("textarea").val());
				if(textarea==""){
					var n = $("#reason-body").find("input:checked").length;
					if(n<=0){
						layer.msg("请说明驳回理由");
						return false;
					}	
				}else{
					var url = "/enrollment/" + id + "/unpass";
					ajaxReject(url);	
				}
			});
		}

	});

	function ajaxReject(url){
		var params = {reason:''};
		var textarea =$.trim($(".modal-body").find("textarea").val());
		if(textarea!==""){
			params.reason += textarea + ';';
		}
		$("#reason-body").find("input:checked").each(function(){
			var text = $(this).siblings("label").text();
			params.reason += text + ';';
		});
		console.log(params);
		$.ajax({
			url : url,
			type : 'GET',
			data: params,
			success : function(data) {
				if (data) {
					location.reload();
				} else {
				}
			}
		}); 
		
		$("#modal").modal("hide");
	}

	
	function reasonList(){
		$("#reason-body").html("");
		var reason = $("#reason").text();/*获取驳回理由字符串*/
		var item = [];
		item=reason.split(";") /*驳回理由字符串转化为数组*/
		$("#modal").modal("show");
		var n = item.length;
		for(var i = 0;i<n-1;i++){
			var html = "<span class='reason-item'><input type='checkbox' id='"
				+ i 
				+"' />"
				+"<label for='"
				+i
				+"'>"
				+ item[i]
				+"</label></span>" ;
			$("#reason-body").append(html);
		}
	}

	
	//获取url中的参数
	function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg); //匹配目标参数
	if (r != null) return unescape(r[2]); return null; //返回参数值
	}



	
});
