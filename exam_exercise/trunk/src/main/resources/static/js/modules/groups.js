$(function() {
	/*	学生分组*/	
	$("body").on("click",".analyze-btn",function(){
		var $this = $(this);
		var url='/examinee/group/examinees';
		getNumber($this,url);
	/*	$("#modal").modal("show");*/
	});	
	$("body").on("click","#setgroup",function(){
		var $this = $(this);
		var url = "/examinee/set_group";
		var g = "gid";
		var d = "eids";
		setNumber($this,g,d,url)
	});
/*	专家分组*/
	$("body").on("click",".expert-btn",function(){
		var $this = $(this);
		var url='/expert/group/experts';
		getNumber($this,url);
		$("#setstudent").hide();
		$("#setexpert").show();

	});
	$("body").on("click","#setexpert",function(){
		var $this = $(this);
		var url = "/expert/set_group";
		var g = "gid";
		var d = "eids";
		setNumber($this,g,d,url)
	});

	/*关联考生*/
	$("body").on("click",".allot",function(){
		$("#setstudent").show();
		$("#setexpert").hide();
		var $this = $(this);
		var url='/expert/examinee_groups';
		$("#myModalLabel").text("关联考生");
		getNumber($this,url);	
	});

	$("body").on("click","#setstudent",function(){
		var $this = $(this);
		var g = "expertGroupIds";
		var d = "examineeGroupIds";
		var url = "/expert/related_groups";
		setNumber($this,g,d,url)
	});
	
	$("body").on("change",".layui-table input",function(){
		var id = $(this).parents("tr").attr("data-id");
		var text = $.trim($("#hidden").text());
		if($(this).is(":checked")){
			if(text.indexOf(id)>=0){				
			}else{
				$("#hidden").append(id+",");				
			}

		}else{
			var str =  $.trim(text.replace(id+",",""));
			$("#hidden").html(str);		
		}
	});
	
	$("body").on("click", ".pagination a", function(){
		$.ajax({
			method: 'get',
			url: this.href,
			success:function(data){
				$(".modal-body").html(data);
			}
		})
		return false;
	});

	function getNumber($this,url){
		$("#modal").modal("show");
		$("#hidden").text("");
		var id = $this.parents("tr").attr("data-id");
		$("body").find(".modal-footer").attr("data-id",id);
		$.ajax({
			method: 'get',
			url: url,
			success:function(data){
				$(".modal-body").html(data);
			}
		})
	}
	function setNumber($this,g,d,url){
		var gid = $this.parent().attr("data-id");
		var eids = $("#hidden").text();
		var params = {};
		params[g] = gid;
		params[d] = eids;
		if(eids==""){
			layer.alert("勾选项为空，请选择");
			return false;
		}else{
			$.ajax({
				method: 'put',
				url: url,
				data: params,
				success:function(data){
					if(data){
						$("#modal").modal("hide");
					}else{
						layer.msg("操作失败")
					}
				}
			})			
		}

	}
	
})