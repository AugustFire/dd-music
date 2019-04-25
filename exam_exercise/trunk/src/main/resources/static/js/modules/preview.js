
$(function(){
	$("#preview-content").find("li:eq(0)").addClass("layui-this");
	$("#preview-content").find(".layui-tab-item:eq(0)").addClass("layui-show");
	$("#preview-content").on("click","li",function(){
		var n = $(this).index();
		$(this).addClass("layui-this").siblings().removeClass("layui-this");
		$(".layui-tab-item").eq(n).addClass("layui-show").siblings().removeClass("layui-show");
	});

	/*删除试题*/
	var typ = 1;
	var page = 1;
	var id = $(".question-type-title").find("p").attr("data-id");
	$("#preview-content").on("click",".del-btn",function(){
	 var questionId = $(this).attr("data-id");
	 var url = '../../exam_paper/' +id +'/remove_questions';
	 var btn = $(this);
	 var data ={
			 "id":id,
			 "questionId":questionId,
	 }
	 $.ajax({
		 type: 'PUT',
	 	 url: url,
	 	 data: data,
	 	success:function(data){
	 		if(data){
	 			 location.reload();
	 		}else{
	 			layer.msg("删除失败，请重新删除");
	 		}
	 	}	
	 });	 
	});
	
	/*设置分数*/
	$(".score").on("keyup","input",function(){
		var questionId = $(this).parents("p").attr("data-id");
		var url = "../../exam_paper/"+ id +"/set_score";
		var score = $(this).val();
		console.log(questionId)
		var params = {
				 "id":id,
				"questionId":questionId,
				"score": score
		};
		var t = $(this)

			 $.ajax({
				 type: 'PUT',
			 	 url: url,
			 	 data: params,			 	 
			 	 success:function(data){
			 		 if(data){
			 			t.val(score);
			 		 }else{
			 			 layer.msg("设置分数失败");
			 		 }
			 	 }
			 });	
		

	});
	
	
/*	添加试题*/
	$("#preview-content").on("click",".add-btn",function(){	
		typ = $(this).attr("data-id");
		var title = $(".layui-this").text();
		$("#myModalLabel").text("添加"+title);
		 $("#pagination").show();
		 page = 1;
		 loadList();
		/* 搜索试题*/
		 $("body").on("click","#search-btn",function(){
			 var word = $(this).siblings("input").val();
			 var params = {
					 "type": typ,
					 "word":word
			 };
		 	 var url = "/exam_questions/?type=" + typ +"&word=" + word + "&page=" + page;
		 	 $.ajax({
		 		 type: 'GET',
		 		 url: url,
		 		 success:function(data){
		 			   var html = template('list', data);
			 			$("#type-list").html(html);
				 			if(data.page.total!=0){
				 				var html = template('list', data);
				 				$("#type-list").html(html);	
					 			  $("#pagination").pagination({	
					 				  totalData: data.page.total,
					 				  showData: data.page.page_size,
					 				  current:data.page.page,	 
					 				  callback: function(p){
					 					  page = p.getCurrent();
					 					  loadList();
					 				  }
					 			  })

				 			}else{
								var html = '<tr><td colspan="3" class="center">无数据</td></tr>';
						 		$("#type-list").html(html);	
				 			}
					 }
		 	 })
		 })

	});
	
	

	 /* 获取选中的题ID*/
	  $("#type-list").on("click","input",function(){	
		  var id = $(this).parents("tr").attr("data-id");
		  var hidden = $("#hidden").find("input");
		  var val = hidden.val();
		  var text = "";
			  if($(this).is(":checked")){
				  var text = val + id + ",";
				  hidden.val(text);
			  }else{
				  var nuk = val.indexOf(id);
				  console.log(val);
				  if(nuk>=0){
					  var removetext = val.replace(id+",","");
					  hidden.val(removetext);	  
				  }
			  }
	  }); 
	 


	  
	/* 设置考卷获取数据*/
	/* 提交*/
	 $("#btn-primary").click(function(){
		 var url = "/exam_paper/"+ id +"/set_questions";
		 var questionIds = $("#hidden").find("input").val();		 
		 var data = {
				 "questionIds":questionIds	 
		 };
		 console.log(questionIds);
		 console.log(url);
		 $.ajax({
			 url: url,
			 type: 'POST',
			 data:data,
			 success: function(data){
				 location.reload();
			 }
		 });
	 });

	
	//加载数据
	 function loadList(){
	 	 var url = "../../exam_questions/json?type=" + typ + "&page=" + page;
	 	 $.ajax({
	 		 type: 'GET',
	 		 url: url,
	 		 success:function(data){
	 			  $(".layui-table").find("th").find("input").prop("checked",false);		/*取消表头选框选定状态	*/	  
	 			   var html = template('list', data);
	 			  $("#type-list").html(html);	
	 			  $("#pagination").pagination({	
	 				  totalData: data.page.total,
	 				  showData: data.page.page_size,
	 				  current:data.page.page,	 
	 				  callback: function(p){
	 					  page = p.getCurrent();
	 					  loadList(typ);
	 				  }
	 		
	 			  });
				 /* 勾选试题回显*/
	 		 		$("#type-list").find("tr").each(function(){
	 		 			var tr = $(this);
	 		 			var id = tr.attr("data-id");
	 		 			var hidden = $("#hidden").find("input");
	 		 			var content = hidden.val();
	 		 			if(content!==""){
	 		 				var num = content.indexOf(id);
	 		 				if(num>=0){
	 		 					 tr.find("input").prop("checked",true);
	 		 					}					
	 		 				}

	 		 			 });
	 			  
	 		 }	 		 
	 	 });

	 }

	
})

