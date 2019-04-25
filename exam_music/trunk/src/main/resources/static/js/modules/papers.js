$(function() {
	template.defaults.imports.dateFmt = function(ns){
		return new Date(parseInt(ns)).toLocaleString();
		};
		

	/*审核通过*/
	 $("body").on("click",".pass",function(){
		 var id = $(this).parents("tr").attr("data-id");
		 var url = "/exam_paper/"+ id +"/pass";
		 var $this = $(this);
		 $.ajax({
			 method:'get',
			 url : url,
			 success:function(data){
				 if(data){
					 location.reload();
				 }else{
					 layer.msg("操作失败，请稍后重试！");
				 }
			 }
			 
		 })
	 });
		/*审核驳回*/

	$("body").on("click",".unpass",function(){
			reasonList();
			var id = $(this).parents("tr").attr("data-id");
			var url = "/exam_paper/"+ id +"/unpass";
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
	 
	 /*	 审核记录*/
	 $("body").on("click",".record-btn",function(){
		 $("#recordmodal").modal("show");
		 var id = $(this).parents("tr").attr("data-id");
		 var url = '/checkRecords/'+id +'?type=paper';
		 $.ajax({
			 method:'get',
			 url: url,
			 success:function(data){
				 if(data){
		 			 var html = template('checkrecords',data);
		 			 $("#lists").html(html);
					
				 }
			 }
		 })
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
					+ '0'+i 
					+"' />"
					+"<label for='"
					+ '0'+i 
					+"'>"
					+ item[i]
					+"</label></span>" ;
				$("#reason-body").append(html);
			}
		}
	
		
		var examPagerDataList = {};
		$("body").on("click",".preview-a",function(){
			$("#present-modal").modal("show");
			
			 var id= $(this).parents("tr").attr("data-id");
			 var url = "exam_paper/" + id +"/preview"
			 $.ajax({
				 method: 'get',
				 url : url,
				 success: function(data){
					 examPagerDataList = data
					 if(data){
						 var html = template('present-lists',data);
			 			 $("#main-present").html(html);
			 			 for(var i=0; i<data.questions.length; i++){
			 				 var item = data.questions[i];
			 				 var list = $('.left-list').find('.type' + item.type).show().find('.glist');
			 				 var nt = $('<a href="#" class="show-question">第' + (list.find('a').length + 1) + '题</a>&nbsp;&nbsp;').data('index', i);
			 				 list.append(nt)
			 			 }
			 			$("body").find("#present-modal .items").hide();
			 			$("body").find("#present-modal .items").eq(0).show();
			 			setTimeout(setButtonState, 100)
					 }
						$.museum($('#content img'));
				 }
			 })
		});
		function showQuestionByIdx(idx){
			$("body").find("#present-modal .items").hide().eq(idx).show();
		}
		function setButtonState(){
			var nextBtn = $(".next-question");
			var prevBtn = $(".prev-question");
			var index = parseInt($("body").find("#present-modal .items:visible").data('index'));
			if(index + 1 >= examPagerDataList.questions.length){
				nextBtn.hide()
			}else{
				nextBtn.show()
			}
			if(index - 1 < 0){
				prevBtn.hide()
			}else{
				prevBtn.show()
			}
		}
		
		//跳转
		$("body").on("click",".show-question",function(){
			showQuestionByIdx(parseInt($(this).data('index')))
			setButtonState()
		})
		
		//下一题
		$("body").on("click",".next-question",function(){
			var index = parseInt($("body").find("#present-modal .items:visible").data('index'));
			$("body").find("#present-modal .items").hide().eq(index+1).show();
			setButtonState()
		})
		//上一题
		$("body").on("click",".prev-question",function(){
			var index = parseInt($("body").find("#present-modal .items:visible").data('index'));
			$("body").find("#present-modal .items").hide().eq(index-1).show();
			setButtonState()
		})
		
		
		
		
		

	
})