$(function(){
layui.use(['form', 'layedit', 'laydate','element'], function(){
  var form = layui.form()
  ,layer = layui.layer
  ,layedit = layui.layedit
  ,laydate = layui.laydate; 
  var $ = layui.jquery
  ,element = layui.element; //Tab的切换功能，切换事件监听等，需要依赖element模块
});

$(".tip-close").click(function(){
	$(this).parents(".tips").css("display","none");
});
$("#year").click(function(){
	WdatePicker({lang:'zh-cn',dateFmt:'yyyy',minDate:'%y',maxDate:'#{%y+3}'});	
});
	
$("#year01").click(function(){
	WdatePicker({lang:'zh-cn',dateFmt:'yyyy',minDate:'%y',maxDate:'#{%y+3}'});	
});
var wH = window.innerHeight;
var h = $(".wrap").outerHeight()+200;
if(h<wH){
	$(".main-content").height(wH-100);
	$(".footer").find("p").show();
}else{
	$(".footer").find("p").show();
}
$("#content").find("img").each(function(){
	var w = $(this).width();
	if(w<100){
	}else{
		$(this).css("width","100");
	}
})
$(".search-form").find("button").click(function(){
	var form = $(this).parents("form");
	form.find("input").each(function(){
		var txt = $.trim($(this).val());
		if(txt==""){
			$(this).val("");
		}
	});	
});	
$("button[type=submit]").click(function(){
	var form = $(this).parents("form").not(".search-form");
	form.find("input").each(function(){
		var txt = $(this).val();
		if(txt!==""){

		}else{
			$(this).focus();
			event.preventDefault();
			return false;
		}
	});

	var tn =  form.find("textarea").length;
	if(tn>0){
		form.find("textarea").each(function(){
			var txt = $.trim($(this).val());
			if(txt!==""){

			}else{
				$(this).focus();
				event.preventDefault();
				return false;
			}
		});	
	}

	var n = form.find("input[type=checkbox]").length;
		if(n>0){
			var checkedbox = form.find("input[type=checkbox]:checked").length;
			if(checkedbox<=0){
				layer.msg("勾选框不能为空");
			}
		}


	
});


//获取url中的参数

function getQueryString(key){
    var reg = new RegExp("(^|&)"+key+"=([^&]*)(&|$)");
    var result = window.location.search.substr(1).match(reg);
    return result?decodeURIComponent(result[2]):null;
  }
$(".input-radio").find("input[type=radio]").each(function(){
	var $this = $(this);
	 var name = $this.attr("name");
	 var typenum = $.trim($this.val());
	 var type = getQueryString(name); 
	 if(type == typenum){
		 $(this).prop("checked",true);
	 }
});
/*获取到url中的type=后面的值*/
$(".search-form").find("input[type=text]").each(function(){
	var name = $(this).attr("name");
	var str =getQueryString(name);
	$(this).val(str);
});
$(".search-form").find("select").find("option").each(function(){
	var $this = $(this);
	var value = $this.parent().attr("name");
	var name = $.trim($(this).val());
	var text = getQueryString(value);
	if(name==text){
		$(this).prop("selected",true);
	}
});
/*学生个人信息*/
$(".info").find("span").each(function(){
	 	var value = $(this).siblings("input").val();
 		$(this).text(value);
	 			
	 });

 $(".info").on("click",".edit",function(){
 	$(".content-item").find(".layui-input-block").each(function(){
 		var num = $(this).find("img").index();
 		if(num < 0){
	 		 $(this).find("span").hide();
	 		$(this).find("input").attr("type","text");
 		}else{
	
	 		var file = '<input type="file" />'
	 				+ '<span class="red">照片尺寸为150px*150px</span>';
 			$(this).append(file);	
 	}
 	});

 	$(this).text("保存").removeClass("edit").addClass("save");

 });

/*侧边导航栏*/
 $(".siderbar-list").on("click","li",function(){
 	var index = $(this).index();
 	$("body").find(".content-item").removeClass("show-item");
	$("body").find(".content-item").eq(index).addClass("show-item");


 });
 
/* 管理员编辑学习信息页面，学生性别选定*/


 $("#sexchecked").find("input[type=radio]").each(function(){
	 var sex = $("#sexchecked").find("input[type=hidden]").val();
	 var name = $(this).val();
	 if(name == sex){
		 $(this).prop("checked",true);
	 }

 });

 
/* 删除按钮操作*/
 $(".layui-table").on("click",".del-btn",function(){
	 event.preventDefault();
	 var url=$(this).attr("href");
	 if(!layer.confirm("确认删除？")){
		 return false;
	 }else{
		$("body").on("click",".layui-layer-btn0",function(){
			 $.ajax({
					url: url,
					type: 'GET',
					success: function(data) {
						if(data){
						window.location.reload();
						}else{
							layer.msg("删除失败!")
						}
					}
				});	 	
		});
	 }

 });
 
	/*全部选中*/
	$("body").on("click","#all-check",function(){
		var button = $(this);
		var input = $(".layui-table").find("input");
		var text = button.text();	
		if(text=="全部选中"){
			 $(".layui-table").find(".layui-unselect").addClass("layui-form-checked");
		}else{
			 $(".layui-table").find(".layui-unselect").removeClass("layui-form-checked");
		}
		if(input.is(":checked")){
			input.each(function(){
				this.checked=false;
			}).change()
			button.text("全部选中");	
		}else{
			input.each(function(){
				this.checked=true;
			}).change()
			button.text("全部取消");	
		}

	
	});

 

 
})

