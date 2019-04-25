$(function() {
	$.museum($('#content img'));
	/*搜索年度和考试条件联动*/
	$("body").on("click","#year_select",function(){
		var list = $(this).find(".layui-form-select").find("dd");
		list.click(function(){
			var year = $(this).attr("lay-value");
			if("0"!=year){
				var herf = "/topics?year="+year;
				$.getJSON(herf, function(json) {
					$("#topic_id").html("");
					$("#topic_id").siblings().find(".layui-anim").html("");
					$("#topic_id").siblings().find(".layui-select-title").find("input").val("");
					$("#topic_id").siblings().find(".layui-select-title").find("input").attr("placeholder","");
					var n = json.length;
					for(var i=0;i<n;i++){
						var html = "<dd lay-value='"+ json[i].id +"'>"+json[i].title+"</dd>";
						$("#topic_id").siblings().find(".layui-anim").append(html);
						var html = "<option value='"+ json[i].id +"'>"+json[i].title+"</option>";
						$("#topic_id").append(html);
					}
					$("#topic_id").siblings().find(".layui-anim").find("dd").click(function(){
						$(this).addClass("layui-this").siblings().removeClass("layui-this");
						var text = $(this).text();
						var index = $(this).index();
						var b = $(this).parent().siblings(".layui-select-title").find("input").attr("placeholder",text);
						var b = $(this).parent().siblings(".layui-select-title").find("input").val(text);
						$("#topic_id").find("option").attr("selected",false);
						$("#topic_id").find("option").eq(index).attr("selected",true);
					});
				});
			}
		});
	});
	$("body").on("click","#layui-chart",function(){
		$("#chart").html();
	});

	$("body").on("mouseenter","#standard-answer tr",function(){
		var n = $(this).attr("data-key");
		var h = $(this).parent("tbody").scrollTop();
		var ids = [];
		$("body").find("#answer-table tr").each(function(){
			var $this=$(this);
			var td = $(this).find("td").eq(0);
			var key = td.attr("data-key");
			if(key==n){
				var sh = $this.height();
				var id = $this.attr("data-id");
				ids.push(id);
				$this.css("background","#AFFCF7");
			}
		})
		var sum = parseInt(45*ids[0]);
		var item = parseInt(sum/350);
		var height = item*350;
		if(item<1){
			$("#answer-table").scrollTop(0);
		}else{
			$("#answer-table").scrollTop(height);
			console.log(height)
		}
		});
	$("body").on("mouseleave","#standard-answer tr",function(){
		var n = $(this).attr("data-key");
		$("body").find("#answer-table tr").each(function(){
			var $this=$(this);
			var td = $(this).find("td").eq(0);
			var key = td.attr("data-key");
			if(key==n){
				$this.css("background","");
			}
		})
		});		
	$("body").on("mouseenter","#answer-table tr",function(){
		var n = $(this).find("td").eq(0).attr("data-key");
		var ids = [];
		$("body").find("#standard-answer tr").each(function(){
			var $this=$(this);
			var key = $this.attr("data-key");
			var id = $this.attr("data-id");
			if(key==n){
				$this.css("background","#AFFCF7");
				ids.push(id);
			}
		})
		var sum = parseInt(45*ids[0]);
		var item = parseInt(sum/350);
		var height = item*350;
		if(item<1){
			$("#standard-answer").scrollTop(0);
		}else{
			$("#standard-answer").scrollTop(height);
		}
		$("body").find("#answer-table tr").each(function(){
			var $this=$(this);
			var key = $this.attr("data-key");
			if(key==n){
				$this.css("background","#AFFCF7");
			}
		})
	});
	$("body").on("mouseleave","#answer-table tr",function(){
		var n = $(this).find("td").eq(0).attr("data-key");
		$("body").find("#answer-table tr").each(function(){
			var $this=$(this);
			var td = $(this).find("td").eq(0);
			var key = td.attr("data-key");
			if(key==n){
				$this.css("background","");
			}
		})
		$("body").find("#standard-answer tr").each(function(){
			var $this=$(this);
			var key = $this.attr("data-key");
			if(key==n){
				$this.css("background","");
			}
		})
	});
	var NoteNames = ['B6', 'A#6', 'A6', 'G#6', 'G6', 'F#6', 'F6', 'E6', 'D#6', 'D6', 'C#6', 'C6', 'B5', 'A#5', 'A5', 'G#5', 'G5', 'F#5', 'F5', 'E5', 'D#5', 'D5', 'C#5', 'C5', 'B4', 'A#4', 'A4', 'G#4', 'G4', 'F#4', 'F4', 'E4', 'D#4', 'D4', 'C#4', 'C4', 'B3', 'A#3', 'A3', 'G#3', 'G3', 'F#3', 'F3', 'E3', 'D#3', 'D3', 'C#3', 'C3'];
/*	图形*/
	 layui.use('layer', function(){ 
		  var $ = layui.jquery, layer = layui.layer; 		  
		  //触发事件
		  var active = {
				  confirmTrans: function(othis){
					  var url = othis.data("id");
					  $(".curr-btn").css("background","#1AA194");
					  othis.css("background","#085D58");
				      layer.open({
					        type: 1
					        ,area: ['65%', '750px']
				      		,title:othis.data("name")+"-答案分析"
				      		,moveOut: true
				      		,resize: false
					        ,content: $('#modal')
					        ,shade: 0 //不显示遮罩
					        ,success: function(){
					        	$("#standard-answer").html("");
			        			$("#answer-table").html("");
			        			$(".cardinal").find(".list-text").empty();
			        			$("#dot-wrap").find(".x-time-text").empty();
			        			$("#dot-wrap").find(".x-time").empty();
					        	$.ajax({
					        		method: 'get',
					        		url: url,
					        		success:function(data){
									/*表格显示*/
									var data = JSON.parse(data);
									var Vlength = data.VoiceMusic.length;
									if(Vlength==0){
										var tr = '<tr><td colspan="6">无数据</td></tr>';
										$("#answer-table").html(tr);
									}else{
										var html = template("studentanswer-lists",data);
										$("#answer-table").html(html);
									}
									var html1 = template("standard-lists",data);
									$("#standard-answer").html(html1);
									var html3 = template("cardinal-list",data);
									$(".cardinal").html(html3);
									
									/*图形显示*/
									var secWidth = 60;
									var rate = secWidth / 1000;
									var maxTime ="" ; 
									var SourceMusic = data.SourceMusic;
									var VoiceMusic = data.VoiceMusic;
									drawY();					
									/*找到音标在Y轴的位置*/
									var examBg = '#68D5F9'
									var errBg = '#ff0000'
									var srcBg = '#9D82CE'
									var coefficient = data.VoiceSpeed/data.SampleSpeed;
									var Slength= SourceMusic.length-1;
									var SmaxTime = SourceMusic[Slength].AbsoluteTime+SourceMusic[Slength].NoteLength;
									if(Vlength<1){
										maxTime=SmaxTime
										/*x轴时间*/
										drawX(secWidth,maxTime);
										
									}else{
										
										var VmaxTime = VoiceMusic[Vlength-1].AbsoluteTime+VoiceMusic[Vlength-1].NoteLength;
										if(VmaxTime>SmaxTime){
											maxTime=VmaxTime
										}else{
											maxTime=SmaxTime
										}
										/*x轴时间*/
										drawX(secWidth,maxTime);
										
										/*	学生音	*/
										NoteBar(SourceMusic,VoiceMusic,coefficient,rate,errBg,examBg);
										/*矫正音高*/
										$("#pitch-num").click(function(){
											var $this = $(this);
											var n = $(".voice-bar").length;
											var voiceBar = $(".voice-bar");	
											if($this.is(":checked")){	
												$(".voice-bar").remove();
												NoteBar(SourceMusic,VoiceMusic,coefficient,rate,errBg,examBg);							
											}else{					
												for(var i = 0; i<n;i++){
													var NoteName =$(voiceBar[i]).attr("data-NoteName");
													var AbsoluteTime=$(voiceBar[i]).attr("data-at");
													$(voiceBar[i]).css({'top':getIndex(NoteName) * 25 + 15+'px',"left":AbsoluteTime*rate+ 'px'});								
												}
											}
										})
											
									}
									/*标准音*/
									for(var i=0; i<SourceMusic.length; i++){
										var item = SourceMusic[i];
										var bar = $("<div class='bar' data-at="
											+item.AbsoluteTime
											+" data-nl="
											+item.NoteLength
											+" data-name="
											+item.NoteName
											+"></div>").css({
											'top': getIndex(item.NoteName) * 25 + 'px',
											'left': item.AbsoluteTime*rate+ 'px',
											'width': item.NoteLength*rate+ 'px',
											'background': srcBg,
										});

									$('#modal .x-time').append(bar);
								}
									
									
								}
							})

			    		  }
					      ,cancel: function(){ 
					    	  $(".curr-btn").css("background","#1AA194");
					    	}   
					      });						
					    }
			  };
		  $('#layerDemo .layui-btn').on('click', function(){
		    var othis = $(this), method = othis.data('method');
		    active[method] ? active[method].call(this, othis) : '';
		  });
		  
		});
	
/*绘制Y轴*/
	function drawY(){
		$("#modal").find(".charts-name").html("");
		$("#modal").find(".charts-name-text").html("");
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
			$("#modal").find(".charts-name").append(li1);
			$("#modal").find(".charts-name-text").append(li);
			
		}
	}
/*绘制X轴*/
	function drawX(secWidth,maxTime){
		var lens = parseInt(maxTime/1000)+6;
		$("#dot-wrap .charts").css({
			width: lens * secWidth + 200+ "px",
			height: NoteNames.length*25 + "px"
		});
		$(".x-time-text,.x-time").css("width",lens * secWidth + 200+ "px");
		for(var n=0;n <= lens;n++){
			var li = $('<li>'+ n +'s</li>').css("width", secWidth);
			var li2 = $('<li></li><li></li>').css({
				width: secWidth/2 +"px",
				height: NoteNames.length*25 + "px" 
			});
			$("#dot-wrap .x-time-text").append(li);
			$("#dot-wrap .x-time").append(li2);
			
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

/*去掉音符*/
	$("body").on("click","#note",function(){
		var $this = $(this);
		if($this.is(":checked")){
			$this.parents(".charts-view").find(".charts-name-text").find("span").show();
		}else{
			$this.parents(".charts-view").find(".charts-name-text").find("span").hide();
		}
	});

	
	$("body").on("click","#charts-li",function(){
		var width = $("#modal").find(".layui-tab-content").width()*0.78;
		var height =  $("#dot-wrap").height();
		$("#dot-wrap").find(".scroll-y").width(width);
		$("#dot-wrap").find(".scroll-x").height(height-15);
		$("#dot-wrap").find(".scroll-y").height(height-50);
		$("#dot-wrap").find(".scroll-x").width(width-85);
		var w = $("#dot-wrap").find(".charts").width();
		$("#dot-wrap").find(".x-time-text").width(w-100);	
		$("#dot-wrap").find(".x-time").width(w-100);
		$("#dot-wrap").find(".scroll-x").scroll(function(){		
			var lw = $("#dot-wrap").find('.scroll-x').scrollLeft();
			$("#dot-wrap").find(".scroll-y").find(".x-time").css("marginLeft",65-lw)
			/*var th = $('.scroll-y').scrollTop();*/
		})
		$("#dot-wrap").find(".scroll-y").scroll(function(){
		/*	var th = $("#dot-wrap").find('.scroll-y').scrollTop();
			var lw = $("#dot-wrap").find('.scroll-x').scrollLeft();	*/		
		})

	});
/*添加浮动提示信息*/
	$("body").on("mouseenter",".bar,.square",function(){
		$(this).remove(".bar-msg");
		var AbsoluteTime=$(this).attr("data-at");
		var NoteLength=$(this).attr("data-nl");
		var NoteName=$(this).attr("data-name");
		var Key=$(this).attr("data-key");
		var Id=$(this).attr("data-id");
		var RectifyName=$(this).attr("data-RectifyName");
		var div = "<div class='bar-msg'>"
				+"<p>音高名:" + NoteName+"</p>"
				+"<p>相对时间:" + AbsoluteTime+"ms</p>"
				+"<p>音长:" + NoteLength+"ms</p>"
				"</div>";
		$(this).append(div);
	});
	/*移除浮动提示信息*/	
	$("body").on("mouseleave",".bar,.square",function(){
		$(this).empty() ;
	});
	
	
	function NoteBar(SourceMusic,VoiceMusic,coefficient,rate,errBg,examBg){
		for(var i=0; i<SourceMusic.length; i++){
			var skey =  SourceMusic[i].Key;
			var voiceLeft = SourceMusic[i].AbsoluteTime*rate;
			var vleft = 0;						
			for(var m=0; m<VoiceMusic.length; m++){
				var vkey = VoiceMusic[m].Key;
				if(skey==vkey){
					var item = VoiceMusic[m];
					var bar = $('<div class="bar voice-bar" data-SequenceNo='
						+item.SequenceNo
						+" data-NoteName="
						+ item.NoteName 
						+" data-RectifyName="
						+item.RectifyName
						+"  data-at="
						+item.AbsoluteTime
						+" data-nl="
						+item.NoteLength
						+" data-name="
						+item.NoteName
						+'></div>').css({
						'top': getIndex(item.RectifyName) * 25 + 11+'px',	
						'left': voiceLeft+vleft+ 'px',
						'width': item.NoteLength*rate*coefficient+ 'px',
						'background': examBg,
					});
					vleft+=item.NoteLength*rate*coefficient;								
					if(item.NoteIsTrue==false){
						bar.css('background', errBg);
					}
					if(item.LengthIsTrue==false){
						bar.css('border', 'solid 1px #FFFF00');
					}
					$('#modal .x-time').append(bar);
				}else{
					vleft =0;
				}
				
			}
		}
	}
	



})