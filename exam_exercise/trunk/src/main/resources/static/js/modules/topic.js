$(function() {
	layui.use('layer', function(){ 
		  var $ = layui.jquery, layer = layui.layer;
		  //触发事件
		  var active = {
		    offset: function(othis){
		      var text = othis.text();		      
		      layer.open({
		        type: 1
		        ,offset: 'auto'
		        ,title:'设置题目'
		        ,moveOut: true
		       	,area: ['60%', '600px']
		        ,btnAlign: 'r' //按钮居中
			    ,shade: 0 //不显示遮罩
			    ,zIndex:8000
		        ,content: $('.topic-content')
		        ,success:function(layero,index){
		    		/*获取所有题目接口*/
		        	$("#topic-table").find(".topic-btn").removeClass("blue");
		        	othis.addClass("blue");
		    		var subjectType =othis.attr("data-Type");
		    		var tid = othis.attr("data-id");
		    		var url = '/questions?subjectType='+subjectType;
		    		$("body").find(".topicList").html("");
		    		var params = {
		    				"tid":tid
		    		};	
		    		 $.ajax({
		    				url: url,
		    				type: 'GET',
		    				data:params,
		    				success: function(data) {
		    					$("body").find(".topicList").html(data);
		    					var allIds = $("body").find("input[name=Tids]").attr("value");
		    					var arrryId=allIds.split(",")
		    					$("body").find(".topicList").find(".layui-unselect").click(function(){
		    						var $this = $(this);
		    						var pId = $this.parents("tr").attr("data-id");
		    						getInputIds($this,arrryId,pId);
		    					})
		    					bindEvent();
		    				}
		    			});	
		        }
		        ,btn: ['提交','取消']
		        ,yes: function(index,layero){
		        	var tid = $("#topic-table").find(".blue").attr("data-id");
					 var url = '/topic/'+tid+'/set_questions';
					 var qids=$("body").find("input[name=Tids]").attr("value");
					 console.log(tid)
					 if (qids.substr(0,1)==','){
						 s=qids.substr(1); 
					 }
					 if(qids==""){

					 }
					 var params = {
							 tid:tid,
							 qids:qids
					 }
					 $.ajax({
						 url:url,
						 method:'post',
						 data:params,
						 success:function(data){
							location.reload();
						 }
					 })
		        }
		      	,btn2:function(index,layero){
		      		$("#topic-table").find(".topic-btn").removeClass("blue");
		      	}
		      	,cancel: function(index, layero){ 
		      		$("#topic-table").find(".topic-btn").removeClass("blue");
		      	}
		      });
		    }
		  };
		  
		  $('#topic-table .layui-btn').on('click', function(){
		    var othis = $(this), method = othis.data('method');
		    active[method] ? active[method].call(this, othis) : '';
		  });
		  
		});

	 function getInputIds($this,arrryId,pId){
		if($this.hasClass("layui-form-checked")){
			$this.removeClass("layui-form-checked");
			arrryId.removeByValue(pId);
			$("body").find("input[name=Tids]").attr("value",arrryId);
		}else{
			$this.addClass("layui-form-checked");
			arrryId.push(pId);
			var stringIds = arrryId.join(",")
			$("body").find("input[name=Tids]").attr("value",stringIds);
		}
 }
 Array.prototype.removeByValue = function(val) {
	  for(var i=0; i<this.length; i++) {
	    if(this[i] == val) {
	      this.splice(i, 1);
	      break;
	    }
	  }
	}
	
	function bindEvent(){
		$('.pagination').find('a').click(function(){
			var allIds = $("body").find("input[name=Tids]").attr("value");
			var arrryId=allIds.split(",")
			$.get(this.href, function(data){
				$("body").find(".topicList").html(data);
				$("body").find("input[name=Tids]").attr("value",arrryId);
				/*回显*/
				$("body").find(".topicList").find(".layui-unselect").each(function(){
					var $this = $(this);
					var pId = $this.parents("tr").attr("data-id");
					if(allIds.indexOf(pId)>=0){
						$this.addClass("layui-form-checked");
					}else{
						$this.removeClass("layui-form-checked");
					}
				})
				$("body").find(".topicList").find(".layui-unselect").click(function(){
					var $this = $(this);
					var pId = $this.parents("tr").attr("data-id");
					getInputIds($this,arrryId,pId);
				})
				bindEvent();
			})
			return false;
		});	
	}
	
	$("body").on("click",".del-topic",function(){
		var $this = $(this);
		var id = $this.attr("data-id");
		var url = "/topic/remove_topic?topicId="+id;
		var params={"topicId":id};
		$.ajax({
			method: 'delete',
			url: url,
			success:function(data){
				if(data.returnCode==1){
					$this.parents("tr").remove();
				}else{
					layer.alert("删除失败，请刷新重试！")
				}
				
			}
		})
	})
	$("body").on("click","#topics-ip",function(){
		$("#Modal").find(".layui-input").val("");
		/*获取所有专题接口*/
			var theadTr = '<tr><td>序号</td><td>专题</td><td>费用</td></tr>';
			$("#a-thead").html(theadTr);
			var url = '/topics/json';
			getTopics(url);
			/*检索专题*/
			$("body").find(".seach-p-btn").unbind("click").click(function(){
				var key = $(this).siblings(".layui-input").val();
				var url='/topics?key='+key;
				getTopics(url);
			});
			/*输入框实时搜索*/
			$("body").off("input",".layui-input").on("input",".layui-input",function(){
				var key = $(this).val();
				var url='/topics?key='+key;
				getTopics(url);
			});
			
		});
	$("body").on("click","#practicer-ip",function(){
		$("#Modal").find(".layui-input").val("");
		/*获取所有练习者接口*/
		var theadTr = '<tr><td>序号</td><td>姓名</td><td>邮箱</td></tr>';
		$("#a-thead").html(theadTr);
		var url = '/users';
		getUser(url);
		/*检索练习者*/
		$("body").find(".seach-p-btn").unbind("click").click(function(){
			var key = $(this).siblings(".layui-input").val();
			var url='/users?key='+key;
			getUser(url);
		});
		/*输入框实时搜索*/
		$("body").off("input",".layui-input").on("input",".layui-input",function(){
			var key = $(this).val();
			var url='/users?key='+key;
			getUser(url);
		});

		});
	/*设置主题*/
	$(".set_subject").unbind("click").click(function(){
		var input = $(this);
		var url = '/subjects/json';
		$.ajax({
			method:'get',
			url: url,
			success:function(data){			
				var n = data.subjects.length;
				if(data.code==200){
					if(n==0){
						$("#sublist").html('<tr><td colspan="2">暂无主题</td></tr>');
					}else{
						$("#sublist").html("");
						for(var i=0;i<n;i++){
							var tr = '<tr data-id="'
								+data.subjects[i].id
								+'" data-title="'
								+data.subjects[i].title
								+'"><td>'
								+data.subjects[i].title
								+'</td><td><button type="button" class="layui-btn layui-btn-small delete" data-id="'
								+data.subjects[i].id
								+'">删除</button></td></tr>';
							$("#sublist").append(tr);
						}
						
						
						
					}
					
					
				}else{
					layer.alert("获取失败");
				}
				var sids = $("body").find("input[name=sids]").attr("value");
			
				if(sids!==null){
					$("#sublist").find("tr").each(function(){
						var id = $(this).attr("data-id");
						var $this = $(this);					
						if(sids.indexOf(id)<0){
						}else{				
							$this.addClass("tr-checked");
						}
					})					
				}

			}

		})
	/*	添加主题*/
		$("body").find(".add").unbind("click").click(function(){
			var title = $("input[name=subtitle]").val();
			$.ajax({
				url:'/subject',
				method:'post',
				data:{"title":title},
				success:function(data){
					if(data.code==200){
						var tr = '<tr data-id="'
							+data.id
							+' "data-title="'
							+data.title
							+'"><td>'
							+data.title
							+'</td><td><button type="button" class="layui-btn layui-btn-small delete" data-id="'
							+data.id
							+'">删除</button></td></tr>';
						$("#sublist").append(tr);
					}else{
						layer.alert("主题重复");
					}
					
				}
			})
		})
		/*选中主题*/
		$("body").off("click","#sublist tr").on("click","#sublist tr",function(){
			var $this = $(this);
			if($this.hasClass("tr-checked")){
				$this.removeClass("tr-checked");
			}else{
				$this.addClass("tr-checked");		
			}
		})
		/*选中主题回显*/
		$("body").find(".btn-primary").unbind("click").click(function(){
			var ids=[];
			var names=[];
			$("#sublist").find(".tr-checked").each(function(){
				var title = $(this).attr("data-title");
				names.push(title);
				var id = $(this).attr("data-id");
				ids.push(id);
			})
			input.val(names.join(","));
			$("input[name=sids]").val(ids.join(","))
			$("#Modal").modal("hide");
		})
		
	})
		
	$("body").on("blur",".topic-input",function(){
		var $this = $(this);
		var num = parseFloat($this.val());
		if(num<0){
			layer.alert("费用不合理，请重新输入！")
			$this.val("");
			
		}
	})
	/*删除主题*/
	$("body").on("click",".delete",function(){
			var id = $(this).attr("data-id");
			var $this = $(this);
			$.ajax({
				url:'/subject/'+id,
				method:'delete',
				success:function(data){
					if(data.code==200){
						$this.parents("tr").remove();
					}else{
						layer.alert("删除失败，请刷新重试！")
					}
				}
			})
		})
	function getUser(url){
		 $.ajax({
				url: url,
				type: 'GET',
				success: function(data) {
					if(data.code==200){
						var n = data.users.length;
						if(n>0){
							$("#a-list").html("");
							for(var i=0;i<n;i++){
								var index = i+1
								var tr = '<tr data-id="'
									+data.users[i].id
									+'" data-name="'
									+data.users[i].name
									+'"><td>'
									+index
									+'</td><td>'
									+data.users[i].name
									+'</td><td>'
									+data.users[i].email
									+'</td></tr>';
								$("#a-list").append(tr);
								
							}
							}else{
								var tr = '<tr><td colspan="3">暂无信息</td></tr>';
								$("#a-list").html(tr);
							}
						/*	选中专题*/
						 $("#a-list").find("tr").click(function(){
							 $(this).toggleClass("tr-checked")
							 $(this).siblings().removeClass("tr-checked");
						 })
					}else{
						layer.msg("获取专题失败，请刷新后重试!")
					}
				}
			})
			/* 提交选中练习者*/
			 $("#Modal").find(".btn-primary").unbind("click").click(function(){
				 var title = $("#a-list").find(".tr-checked").attr("data-name");
				 var id = $("#a-list").find(".tr-checked").attr("data-id");
				 $("#practicer-ip").val(title);
				 $("input[name=toAuthorizerId]").val(id)
				  $("#Modal").modal("hide");
			 })
	}
	
	function getTopics(url){
		 $.ajax({
				url: url,
				type: 'GET',
				success: function(data) {
					if(data.code==200){
						var m = data.topics;
						var n = data.topics.length;
						var m = data.topics;
						if(n>0){
							$("#a-list").html("");
							for(var i=0;i<n;i++){
								var index = i+1
								var tr = '<tr data-id="'
									+data.topics[i].id
									+'" data-title="'
									+data.topics[i].title
									+'"><td>'
									+index
									+'</td><td>'
									+data.topics[i].title
									+'</td><td>'
									+data.topics[i].fee
									+'</td></tr>';
								$("#a-list").append(tr);
								
							}
							}else{
								var tr = '<tr><td colspan="3">暂无信息</td></tr>';
								$("#a-list").html(tr);
							}
						/*	选中专题*/
						 $("#a-list").find("tr").click(function(){
							 $(this).toggleClass("tr-checked")
							 $(this).siblings().removeClass("tr-checked");
						 })
					}else{
						layer.msg("获取专题失败，请刷新后重试!")
					}
				}
			})
			/* 提交选中专题*/
			 $("#Modal").find(".btn-primary").unbind("click").click(function(){
				 var title = $("#a-list").find(".tr-checked").attr("data-title");
				 var id = $("#a-list").find(".tr-checked").attr("data-id");
				 $("#topics-ip").val(title);
				 $("input[name=topicId]").val(id)
				  $("#Modal").modal("hide");
			 })
	}

})
	
