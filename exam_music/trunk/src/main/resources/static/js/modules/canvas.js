$(function(){
	var secWidth = 125;
	/*绘制Y轴*/
	function drawY(){
		$("#pitch").find(".charts-name").html("");
		$("#pitch").find(".charts-name-text").html("");
		for(var n=0;n<NoteNames.length;n++){
			var num = 83-n;
			if(NoteNames[n].indexOf('#')>=0){
				var li = '<li class="black-key" data-key="'+num +'"><span>'+ NoteNames[n]+'</span></li>';	
			}else if(NoteNames[n].indexOf('C')>=0 && NoteNames[n].indexOf('#')<=0){
				var li = '<li class="grey-key" data-key="'+ num +'"><span>'+ NoteNames[n]+'</span></li>';	
			}else{
				var li = '<li class="white-key" data-key="'+ num+'"><span>'+ NoteNames[n]+'</span></li>';	
			}			
			var li1 = '<li></li>';
			$("#pitch").find(".charts-name").append(li1);
			$("#pitch").find(".charts-name-text").append(li);
			
		}
	}
	/*绘制X轴*/
	function drawX(secWidth,maxTime,lens){
		$("#pitch .x-time-text").html("");
		$('#pitch-charts-wrap #charts-line').html("");
		$("#pitch .x-time-text").width(lens * secWidth+250+ "px")
		$("#pitch .charts").css({
			width: lens * secWidth +250+ "px",
			height: NoteNames.length*25 + "px"
		});		
		for(var n=0;n <= lens;n++){
			var li = $('<li>'+ n +'s</li>').css("width", secWidth);
			var li2 = $('<li></li><li></li>').css({
				width: secWidth/2 +"px",
				height: NoteNames.length*25 + "px" 
			});
			$("#pitch .x-time-text").append(li);
			$('#pitch-charts-wrap #charts-line').append(li2);
			
		}
	}
	/*找到音标在Y轴的位置*/
	function getIndex(NoteName){
		for(var i=0; i<NoteNames.length; i++){
			if(NoteNames[i] == NoteName){
				return i
			}
		}
	}
	function scrollToCenter(){
		var div = $(".pitch-wrap");
		var top=1000;
		div[0].scrollTop = top;
	}

	var canvasWidth = 0;
	var dots = [];
	var currentTime = 10;
	var NoteNames = ['B6', 'A#6', 'A6', 'G#6', 'G6', 'F#6', 'F6', 'E6', 'D#6', 'D6', 'C#6', 'C6', 'B5', 'A#5', 'A5', 'G#5', 'G5', 'F#5', 'F5', 'E5', 'D#5', 'D5', 'C#5', 'C5', 'B4', 'A#4', 'A4', 'G#4', 'G4', 'F#4', 'F4', 'E4', 'D#4', 'D4', 'C#4', 'C4', 'B3', 'A#3', 'A3', 'G#3', 'G3', 'F#3', 'F3', 'E3', 'D#3', 'D3', 'C#3', 'C3'];
	 layui.use('layer', function(){ 
		  var $ = layui.jquery, layer = layui.layer; 		  
		  //触发事件
		  var active = {
				  offset: function(othis){
					  var url = othis.data("id");
					  $(".cav-btn").css("background","#1AA194");
					  othis.css("background","#085D58");
				      layer.open({
					        type: 1
					        ,area: ['65%', '750px']
				      		,title:othis.data("name")+"音高分析"
				      		,moveOut: true
					        ,content: $('#pitch')
					        ,shade: 0 //不显示遮罩
					        ,success: function(){
					        	$(".x-time-text").html("");
					        	cvs.resize(0, 0);
					        	$("#charts-line").empty();
					        	$.ajax({
					        		method: 'get',
					        		url: url,
					        		success:function(data){
					        			dots = [];
					        			currentTime = 0;
					        			cvs.draw(dots);
					        			var audioId = othis.siblings("a").attr("data-id");
					        			var audioUrl = "/looksing/" + audioId;
					        			$("audio").attr("src",audioUrl);
					        			drawY();
					        			scrollToCenter();
					        		 //pitch-wrap
					        			$.ajax({
					        				method: 'get',
					        				url: url,
					        				success:function(data){	
					        					var data = JSON.parse(data);
					        					var scale = data.Scale;
					        					if(data){						
					        						var rate=1;
					        						var timeRate = 60/secWidth;
					        						var maxTime = data.VoicePitchs.length*1;
					        						 var lens = Math.ceil(maxTime/100)/10;
					        						var lens = Math.ceil(maxTime/100)/10*scale;
					        						drawX(secWidth,maxTime,lens);
					        						canvasWidth = maxTime*10/1000*125;
					        						cvs.resize(canvasWidth, NoteNames.length*25)
					        						for(var i=0; i<data.VoicePitchs.length; i++){
					        							var item = data.VoicePitchs[i];
					        							
					        							if(item>36){
					        								var height = (84-item)*25;
					        								var list =  { label: '', value:height, y: height};
					        								dots.push(list);
					        							}else{
					        								var list =  { label: '', value: 0, y: NoteNames.length*25- 1};
					        								dots.push(list);
					        							}
					        						}
					        						for(var i=0; i<data.VoiceNotes.length; i++){
					        							var item = data.VoiceNotes[i];
					        							var numY = getIndex(item.NoteName);
					        							var square = $("<div class='square' data-at="+item.AbsoluteTime+" data-nl="+item.NoteLength+" data-name="+item.NoteName+"></div>").css({
					        								'top': numY*25 +'px',
					        								'left': item.AbsoluteTime/1000*secWidth+ 'px',
					        								'width': item.NoteLength/1000*secWidth +'px',
					        							});						
					        							$('#pitch-charts-wrap #charts-line').append(square);
					        						}

					        						scrollToCenter();
					        					}else{
					        						layer.msg("数据加载失败");
					        					}
					        				}
					        			})
									
								}
							})

			    		  }
				      ,cancel:function(){
				    	  $(".cav-btn").css("background","#1AA194");
				      }
					      });						
					    }
			  };
		  $('#layerDemo .layui-btn').on('click', function(){
		    var othis = $(this), method = othis.data('method');
		    active[method] ? active[method].call(this, othis) : '';
		  });
		  
		});
		/*音高分析*/

		function scrollToCenter(){
			var div = $(".pitch-wrap");
			var top = (div[0].scrollHeight - div.height()) / 2;
			div[0].scrollTop = top;
		}	
	
var cvs = new LineCanvas({
	width: 2000,
	height: 800
})

setInterval(function(){
	cvs.draw(dots);
	currentTime = audio[0].currentTime;
}, 50);

	
function LineCanvas(options){
	var cvs = document.getElementById("canvas-wrap").appendChild( document.createElement( 'canvas' ) );
	var ctx = cvs.getContext( '2d' );	
	cvs.width = options.width;
	cvs.height = options.height;
	ctx.font = '10px sans-serif';
	ctx.clearRect( 0, 0, cvs.width, cvs.height );
	
	function fillBg(){
		ctx.clearRect( 0, 0, cvs.width, cvs.height );
	}
	
	this.resize = function(w, h){
		cvs.width = w;
		cvs.height = h;
		fillBg();
	}
	
	this.ctx = ctx;
	
	this.draw = function(lines){
		if(!lines || lines.length < 1)return;
		fillBg();
		ctx.beginPath();
		ctx.strokeStyle = '#1baee1';
		ctx.lineWidth = 2;
		ctx.moveTo(0, lines[0].y);
		for(var i=1; i<lines.length; i++){
			var item = lines[i];
			ctx.lineTo(i*125/1000*10, item.y);
		}
		ctx.stroke();
		ctx.closePath();
		
		if(currentTime != 0){
			ctx.beginPath();
			ctx.strokeStyle = '#F6A831';
			ctx.lineWidth = 1;
			console.log(currentTime * secWidth)
			ctx.moveTo(currentTime * secWidth, 0);
			ctx.lineTo(currentTime * secWidth, cvs.height);
			ctx.stroke();
			ctx.closePath();
			
		}
		
		
	}
	
	
}

var audio = $('<audio preload="auto" controls="controls"></audio>');
$(".player-wrap").append(audio);

$("#chart-wrap").scroll(function(){
	var left= $(this).scrollLeft();
	var top = $(this).scrollTop();
	$("#pitch").find(".x-time-text").css("marginLeft",65-left);
	$("#pitch").find(".charts-name-text").css("marginTop",42-top);
})


})