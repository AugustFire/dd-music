
$(function(){
	resizeAction();
	$(window).resize(resizeAction);
})
//resize
var resizeAction= function(){
	$('#_main_content').height($(window).height()- $("#_main_fixed_header").height()-$("#_main_bar1").height()-$(".main-path").height()-30-$(".footer").height());
	//$("#_main_nav").height($(window).height()- $("#_main_fixed_header").height()+7);
}
