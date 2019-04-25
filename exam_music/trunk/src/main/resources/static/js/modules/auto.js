
$(function(){
/*考试范围树形结构*/
$("body").on("click",".item-lists label",function(){
	var ul = $(this).parent().find("ul");
	var span = $(this).siblings("span");
	var list = $(this).parent("li").siblings("ul");
	ul.toggle();
	list.toggle();
	span.toggleClass("glyphicon-plus");
	span.toggleClass("glyphicon-minus");

});
/*试卷提交*/
$("#exam-btn").click(function(){
/*判断试卷名称是否为空*/
	var examTitle = $("#exam-title").val();
	if(examTitle==""){
		layer.msg("请输入试卷名称");
		return false;	
	}
/*判断试卷类型是否勾选*/
	var examType = $("#exam-type").find("input:checked").length;
	if(examType!=1){
		layer.msg("请选择试卷类型");
		return false;	
	}
	/*判断试题分数是否等于试卷总分*/
	var totalScore = $("body").find("#exam-score").val();
	var total = 0;
	$("#hidden").find("input[name*=TotalScore]").each(function(){
		var num =  $(this).val();
		if(num!==""){
			var score = parseInt(num);
			total += score;
		}
	});
	if(totalScore==""){
		layer.msg("试卷分数不能为空");
		return false;
	}
	if(totalScore>total){
		layer.msg("试题总分小于试卷总分");
		return false;
	}else if(totalScore<total){
		layer.msg("试题总分大于试卷总分");
		return false;
	};	
	$(this).parents("form").submit();
	
	
});

/*弹出框*/
$("body").on("click",".input-radio .gray-btn",function(){
	var title = $(this).text();
	var id = $(this).attr("data-id");
	$("#myModalLabel").text(title);
	$("#myModalLabel").attr("data-id",id);
	var url = '/api/yueli/knowledge_map';
	var step = $(".modal-body").find(".list-step").first();
	step.show();
	step.siblings(".list-step").hide();
	echo(id);
	$.ajax({
			url: url,
			type: 'GET',
			success: function(data) {
				if(data){
	 			 var html = template('idd_js',data.map);
	 			 $("#lists").html(html);
	 			 console.log(data)
				
				}else{
					layer.msg("获取数据失败!")
				}
			}
		});	 	
	
	
});

/*上一步*/
$("body").on("click",".pre-btn",function(){
	var id = $(this).parents(".modal-content").find("#myModalLabel").attr("data-id");
	var list = $(this).parents(".list-step");
	list.hide();
	list.prev(".list-step").show();
	hidden(id);
});
/*下一步*/
$("body").on("click",".next-btn",function(){
	var id = $(this).parents(".modal-content").find("#myModalLabel").attr("data-id");	
	var list = $(this).parents(".list-step");
	list.hide();
	list.next(".list-step").show();	
	hidden(id);
});
/*判断小题分数s是否为整数*/
$("body").on("blur","#num",function(){
	var points = $("#points").val();
	var num = $("#num").val();
	var point = points/num;
	if(point%1==0){
		$("#point").val(point);
		$("#point-hidden").val(point);
	}else{
		layer.alert("小题分数不为整数，请重新输入")
		$(this).val("");
		$(this).focus();
		$(this).attr("placeholder","小题分数不为整数，请重新输入");
	}
	
});

/*试题提交*/
$("body").on("click",".submit-btn",function(){
	var id = $(this).parents(".modal-dialog").find("#myModalLabel").attr("data-id");
	var no = [];
	$("#lists").find("input:checked").each(function(){
		var id = $(this).attr("data-id");
		no.push(id);
	});
	var div = $("#hidden").find("div[data-id="+ id +"]");	
	div.find("input[name*=No]").val(no);
	var i = 0;
	div.find("input").each(function(){
		var content = $(this).val();
		if(content!==""){
			i++;
		}else{
			
		}
	});
	console.log(i);
	if(i>=3){		
		$(".input-radio").find("button[data-id="+ id +"]").addClass("get-btn");
	}
	$("#Modal").modal("hide");
	
});

function hidden(id){
	var div = $("#hidden").find("div[data-id="+ id +"]");
	div.find("input").each(function(){
		$(this).val("");
	});
	var points = $("#points").val();
	var point = $("#point-hidden").val();
	var num = $("#num").val();
	var require = $("#require").val();
	div.find("input[name*=TotalScore]").val(points);
	div.find("input:eq(2)").val(point);
	div.find("input[name*=Num]").val(num);
	div.find("input[name*=No]").val(require);
}
/*回显*/
function echo(id){
	var div = $("#hidden").find("div[data-id="+ id +"]");
	var name =div.find("input[name*=name]").val();
	var points =div.find("input[name*=TotalScore]").val();
	var point = div.find("input:eq(2)").val();
	var num = div.find("input[name*=Num]").val();
	var require = div.find("input[name*=No]").val();
	$("#points").val(points);
	$("#point").val(point);
	$("#point-hidden").val(point);
	$("#num").val(num);
	$("#require").val(require);	

}

})

