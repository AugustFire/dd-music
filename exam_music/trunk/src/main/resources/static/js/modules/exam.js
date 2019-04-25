$(function() {
	/*搜索年度和考试条件联动*/
	$("body").on("click",".layui-input-inline:eq(0)",function(){
		var list = $(this).find(".layui-form-select").find("dd");
		list.click(function(){
			var year = $(this).attr("lay-value");
			if("0"!=year){
				var herf = "/exams?year="+year;
				$.getJSON(herf, function(json) {
					$("#exam_id").html("");
					$("#exam_id").siblings().find(".layui-anim").html("");
					$("#exam_id").siblings().find(".layui-select-title").find("input").val("");
					$("#exam_id").siblings().find(".layui-select-title").find("input").attr("placeholder","");
					var n = json.length;
					for(var i=0;i<n;i++){
						var html = "<dd lay-value='"+ json[i].id +"'>"+json[i].title+"</dd>";
						$("#exam_id").siblings().find(".layui-anim").append(html);
						
						$("#exam_id").siblings().find(".layui-anim").find("dd").click(function(){
							$(this).addClass("layui-this").siblings().removeClass("layui-this");
							var text = $(this).text();
							var b = $(this).parent().siblings(".layui-select-title").find("input").val(text);
						});
						var html = "<option value='"+ json[i].id +"'>"+json[i].title+"</option>";
						$("#exam_id").append(html);
					
					}
				});
			}
		});
});
	
	function hidden(id){
		var div = $("#hidden").find("div[data-id="+ id +"]");
		div.find("input").each(function(){
			$(this).val("");
		});
		var Title = $("#Title").val();
		var TotalScore = $("#points").val();
		var num = $("#num").val();
		var time =$("#testtime").val();
		var score =$("#point").val();
		div.find("input:eq(2)").val(TotalScore);
		div.find("input:eq(4)").val(score);
		div.find("input[name*=Num]").val(num);
		div.find("input[name*=ResolvedTime]").val(time);
		div.find("input[name*=Title]").val(Title);
	}
	/*单选题下一步设置题型分数*/
	function typeHidden(id){
		var div = $("#hidden").find("div[data-id="+ id +"]");
		div.find("input[name=yueliSingleTotalScore]").val("");
		div.find("input[name=yueliSingleNum]").val("");
		div.find("input[name=yueliSingleScore]").val("");
		
		div.find("input[name=yueliMultiTotalScore]").val("");
		div.find("input[name=yueliMultiNum]").val("");
		div.find("input[name=yueliMultiScore]").val("");
		
		div.find("input[name=yueliShortTotalScore]").val("");
		div.find("input[name=yueliShortNum]").val("");
		div.find("input[name=yueliShortScore]").val("");
		var yueliSingleTotalScore = $("#yueliSingleTotalScore").val();
		var yueliSingleNum = $("#yueliSingleNum").val();
		var yueliSingleScore = $("#yueliSingleScore").val();
		
		var yueliMultiTotalScore =$("#yueliMultiTotalScore").val();
		var yueliMultiNum =$("#yueliMultiNum").val();
		var yueliMultiScore = $("#yueliMultiScore").val();
		
		var yueliShortTotalScore = $("#yueliShortTotalScore").val();
		var yueliShortNum = $("#yueliShortNum").val();
		var yueliShortScore =$("#yueliShortScore").val();

		div.find("input[name=yueliSingleTotalScore]").val(yueliSingleTotalScore);
		div.find("input[name=yueliSingleNum]").val(yueliSingleNum);
		div.find("input[name=yueliSingleScore]").val(yueliSingleScore);
		
		div.find("input[name=yueliMultiTotalScore]").val(yueliMultiTotalScore);
		div.find("input[name=yueliMultiNum]").val(yueliMultiNum);
		div.find("input[name=yueliMultiScore]").val(yueliMultiScore);
		
		div.find("input[name=yueliShortTotalScore]").val(yueliShortTotalScore);
		div.find("input[name=yueliShortNum]").val(yueliShortNum);
		div.find("input[name=yueliShortScore]").val(yueliShortScore);

	}
	/*回显*/
	function echo(id){
		var div = $("#hidden").find("div[data-id="+ id +"]");
		var Title =div.find("input[name*=Title]").val();
		var time =div.find("input[name*=ResolvedTime]").val();
		var TotalScore =div.find("input:eq(2)").val();
		var score = div.find("input:eq(4)").val();
		var num = div.find("input[name*=Num]").val();
		
		var yueliSingleTotalScore = div.find("input[name=yueliSingleTotalScore]").val();
		var yueliSingleNum = div.find("input[name=yueliSingleNum]").val();
		var yueliSingleScore = div.find("input[name=yueliSingleScore]").val();
		
		var yueliMultiTotalScore =div.find("input[name=yueliMultiTotalScore]").val();
		var yueliMultiNum =div.find("input[name=yueliMultiNum]").val();
		var yueliMultiScore = div.find("input[name=yueliMultiScore]").val();
		
		var yueliShortTotalScore = div.find("input[name=yueliShortTotalScore]").val();
		var yueliShortNum = div.find("input[name=yueliShortNum]").val();
		var yueliShortScore =div.find("input[name=yueliShortScore]").val();
		
		
		$("#Title").val(Title);
		$("#points").val(TotalScore);
		$("#testtime").val(time);
		$("#point-hidden").val(score);
		$("#num").val(num);
		$("#point").val(score);
		
		$("#yueliSingleTotalScore").val(yueliSingleTotalScore);
		$("#yueliSingleNum").val(yueliSingleNum);
		$("#yueliSingleScore").val(yueliSingleScore);
		$("#yueliMultiTotalScore").val(yueliMultiTotalScore);
		$("#yueliMultiNum").val(yueliMultiNum);
		$("#yueliMultiScore").val(yueliMultiScore);
		$("#yueliShortTotalScore").val(yueliShortTotalScore);
		$("#yueliShortNum").val(yueliShortNum);
		$("#yueliShortScore").val(yueliShortScore);
		
	}
	/*添加考点弹出框*/
	$("body").on("click","#test-items",function(){
		var title = $(this).text();
		var id = $(this).attr("data-id");
		$("#myModalLabel").text(title);
		$("#myModalLabel").attr("data-id",id);
		var step = $(".modal-body").find(".list-step").first();
		step.show();
		step.siblings(".list-step").hide();
		echo(id);
		var url = '/exam_points/json';
		$.ajax({
			url: url,
			type: 'GET',
			success: function(data) {
				if(data){
					/*var strdata =  JSON.stringify(data)*/
					 var html = template('lists',{list:data});
		 			 $("#kaodianlist").html(html);
		 			 var pids = [];
		 			 var ids = $("#check-kaodian").val();
		 			 $("body").find(".firstcheck").each(function(){
		 				 var $this = $(this);
		 				 var id=$this.attr("data-id");	
		 				 if(ids.indexOf(id)>=0){
		 					 $this.prop("checked",true);
		 				 }
		 			 })
		 			 
		 			 
		 			 $("body").on("click",".firstcheck",function(){
		 				 var $this=$(this);
		 				 var id=$this.attr("data-id");
		 				 if($this.is(":checked")){
		 					 var list = $this.parent().next(".kaodian-lists");
		 					 var len = list.find("p").length;
		 					 pids.push(id);
		 					 $("#kaodianlist").find("label").css("color","#333");
		 					 $this.siblings("label").css("color","#1FB5AD");
		 					 $("#check-kaodian").val(pids);
		 					$(".kaodian-lists").hide();
		 					 if(len>1){
			 					 list.show();	
		 					 }
		 				 }else{
		 					var val = $("#check-kaodian").val();
		 					$this.siblings("label").css("color","#333");
		 					 $this.css("marginBottom",'');
		 					 if(val.indexOf(id)>=0){
		 					   var str=val.replace(id,"");
		 					  $("#check-kaodian").val(str);
		 					  pids=str.split(",");
		 					 }
		 					 $(".kaodian-lists").hide();
		 					 $this.parent().next(".kaodian-lists").hide(); 
		 				 }
		 			 });
		 			$("body").on("click","#test-Modal .btn-default",function(){
		 				pisd=[];
		 				 $("#check-kaodian").val("");
		 			});
				}else{
					layer.msg("获取数据失败!")
				}
			}
		});	 	
		

	});
	/*选择考点*/
	$("body").on("click","#kaodianbtn",function(){
		var n = $("body").find(".firstcheck:checked").length;
		$("#checked-kaodian").html("");
		if(n>0){
			$("#test-items").addClass("get-btn");	
		}
		$("body").find(".firstcheck:checked").each(function(){
			var name = $(this).siblings("label").text();
			var span = '<span>'+name+'</span>';
			$("#checked-kaodian").append(span);
			$("#checked-kaodian").parent().show();
		});
		$("#test-Modal").modal("hide");
	});

	$("body").on("click",".submitbtn",function(){
		$("#examAdd_form").submit();
	});
	/*$("body").on("blur","#yueliSingleTotalScore",function(){*/
	$("#yueliSingleTotalScore").blur(function(){
		var $this = $(this);
		var total = $this.parents(".modal-body").find("#points").val();
		var num = parseInt(total);
		var yueliSingleTotalScore = $(this).val();
		var yueliMultiTotalScore = $(this).parents(".modal-body").find("#yueliMultiTotalScore").val();
		var yueliShortTotalScore = $(this).parents(".modal-body").find("#yueliShortTotalScore").val();	
		var score = 0;
		if(yueliSingleTotalScore!==""){
			var single = parseInt(yueliSingleTotalScore);
			score=score+single;
		}
		if(yueliMultiTotalScore!==""){
			var Multi = parseInt(yueliMultiTotalScore);
			score=score+Multi;
		}
		if(yueliShortTotalScore!==""){
			var Short = parseInt(yueliShortTotalScore);
			score=score+Short;
		}	
		if(score>total){
			$this.val("");
			$this.attr("placeholder","大于简答题总分，请重新输入");
		}
	}); 
	$("#yueliMultiTotalScore").blur(function(){
		var $this = $(this);
		var total = $this.parents(".modal-body").find("#points").val();
		var num = parseInt(total);
		var yueliMultiTotalScore = $(this).val();
		var yueliSingleTotalScore = $(this).parents(".modal-body").find("#yueliSingleTotalScore").val();
		var yueliShortTotalScore = $(this).parents(".modal-body").find("#yueliShortTotalScore").val();	
		var score = 0;

		if(yueliSingleTotalScore!==""){
			var single = parseInt(yueliSingleTotalScore);
			score=score+single;
		}
		if(yueliMultiTotalScore!==""){
			var multi = parseInt(yueliMultiTotalScore);
			score=score+multi;
		}
		if(yueliShortTotalScore!==""){
			var short = parseInt(yueliShortTotalScore);
			score=score+short;
		}
		if(score>total){
			$this.val("");
			$this.attr("placeholder","大于简答题总分，请重新输入");
		}
	}); 
	$("#yueliShortTotalScore").blur(function(){
		var $this = $(this);
		var total = $this.parents(".modal-body").find("#points").val();
		var num = parseInt(total);
		var yueliShortTotalScore = $(this).val();
		var yueliMultiTotalScore = $(this).parents(".modal-body").find("#yueliMultiTotalScore").val();
		var yueliSingleTotalScore = $(this).parents(".modal-body").find("#yueliSingleTotalScore").val();	
		var score = 0;		
		if(yueliSingleTotalScore!==""){
			var single = parseInt(yueliSingleTotalScore);
			score=score+single;
		}
		if(yueliMultiTotalScore!==""){
			var Multi = parseInt(yueliMultiTotalScore);
			score=score+Multi;
		}
		if(yueliShortTotalScore!==""){
			var Short = parseInt(yueliShortTotalScore);
			score=score+Short;
		}	
		if(score>total){
			$this.val("");
			$this.attr("placeholder","大于简答题总分，请重新输入");
		}else if(score<total){
			$this.val("");
			$this.attr("placeholder","小于简答题总分，请重新输入");	
		}

	}); 

	/*选择考试科目弹出框*/
	$("body").on("click","#set-pager .gray-btn",function(){
		var title = $(this).text();
		var id = $(this).attr("data-id");
		$("#myModalLabel").text(title);
		$("#myModalLabel").attr("data-id",id);
		var step = $(".modal-body").find(".list-step").first();
		step.show();
		step.siblings(".list-step").hide();
		echo(id);
		if(id==1){
			/*下一步*/
			$("body").on("click","#checknext",function(){
				var id = $(this).parents(".modal-content").find("#myModalLabel").attr("data-id");	
				var list = $(this).parents(".list-step");
				list.hide();
				$("#othertype").hide();
				$("#yueliitems").show();
				hidden(id);
			});
			/*上一步*/
			$("body").on("click","#checkprev",function(){
				var id = $(this).parents(".modal-content").find("#myModalLabel").attr("data-id");
				var list = $(this).parents(".list-step");
				list.hide();
				$("#othertype").hide();
				$("#othertype").prev(".list-step").show();
				hidden(id);
			});
			
		}else{
			/*下一步*/
			$("body").on("click","#checknext",function(){
				var id = $(this).parents(".modal-content").find("#myModalLabel").attr("data-id");	
				var list = $(this).parents(".list-step");
				list.hide();
				$("#othertype").show();
				$("#yueliitems").hide();
				hidden(id);
			});
		}

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
		}else{
		/*	layer.alert("小题分数不为整数，请重新输入")*/
			$(this).val("");
			$(this).attr("placeholder","小题分数不为整数，请重新输入");
			$(this).parents(".layui-form-item").siblings().find("input").val("");
		}
		
	});
	$("body").on("blur",".question-num",function(){
		var num = $(this).val();
		var points = $(this).parents(".layui-form-item").prev().find(".question-totalscore").val();
		var point = points/num;
		var score = $(this).parents(".layui-form-item").next().find(".question-score")		
		if(point%1==0){
			score.val(point);
		}else{
		/*	layer.alert("小题分数不为整数，请重新输入")*/
			$(this).val("");
			$(this).attr("placeholder","小题分数不为整数，请重新输入");
		}
		
	});
	
	$()
	
	/*试题提交*/
	$("body").on("click",".submit-btn",function(){
		var id = $(this).parents(".modal-content").find("#myModalLabel").attr("data-id");
		if(id==1){
			typeHidden(id)
		}else{
			hidden(id);
		}
		var i = 1;
		var div = $("#hidden").find("div[data-id="+ id +"]");
		var n = div.find("input").length;
		div.find("input").each(function(){
			var content = $(this).val();
			if(content!==""){
				i++;
			}else{
				
			}
		});
		if(i>=n-1){		
			$(".w70").find("button[data-id="+ id +"]").addClass("get-btn");
		}
		$("#Modal").modal("hide");
		
	});
	
})