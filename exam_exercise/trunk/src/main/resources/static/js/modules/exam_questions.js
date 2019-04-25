$(function () {
	$.museum($('#content img'));
	template.defaults.imports.dateFmt = function (ns) {
		return new Date(parseInt(ns)).toLocaleString();
	};
	var page = 1;
	var typ = 1;
	/* 难度系数*/
	$("body").on("click", "#difficulty", function () {
		var num = $.trim($(this).siblings(".layui-inline").find("input[name=difficulty]").val());
		if (num == "") {

		} else {
			var re = /^[0-1]$|^0\.[00-99]|1.0+$/;
			if (!re.test(num)) {
				layer.msg("难度系数在0-1.0之间");
				return false;
			}
		}
	});
	/*	 审核记录*/
	$("body").on("click", ".record-btn", function () {
		$("#recordmodal").modal("show");
		var id = $(this).parents("tr").attr("data-id");
		var url = '/checkRecords/' + id + '?type=question';
		$.ajax({
			method: 'get',
			url: url,
			success: function (data) {
				if (data) {
					var html = template('checkrecords', {list: data});
					$("#lists").html(html);
					console.log(data);

				} else {
					var html = '<tr><td colspan="5">无数据</td></tr>';
					$("#lists").html(html);
				}
			}
		})
	});
	/*审核通过*/
	$("body").on("click", ".pass", function () {
		var id = $(this).parents("tr").attr("data-id");
		var url = "/exam_question/" + id + "/pass";
		var $this = $(this);
		$.ajax({
			method: 'get',
			url: url,
			data: id,
			success: function (data) {
				if (data) {
					location.reload();
				} else {
					layer.msg("操作失败，请稍后重试！");
				}
			}

		})
	});

	/*单个驳回*/
	$("body").on("click", ".unpass", function () {
		reasonList();
		var id = $(this).parents("tr").attr("data-id");
		var url = "/exam_question/" + id + "/unpass";
		$("body").on("click", ".btn-primary", function () {
			var textarea = $.trim($(".modal-body").find("textarea").val());
			if (textarea == "") {
				var n = $("#reason-body").find("input:checked").length;
				if (n <= 0) {
					layer.msg("请说明驳回理由");
					return false;
				}
			} else {
				ajaxReject(url);
			}


		});
	});
	/*	批量驳回*/
	$("body").on("click", "#unpass-all", function () {
		var id = "";
		/*获取已勾选选项id*/
		$(".layui-table").find("input:checked").each(function () {
			var inputId = $(this).parents("tr").attr("data-id");
			id += inputId + ",";
		});
		/*	判断是否勾选选项*/
		if (id == "") {
			layer.msg("请先勾选要驳回的选项");
			return false;
		} else {
			reasonList();
			$("body").on("click", ".btn-primary", function () {
				var textarea = $.trim($(".modal-body").find("textarea").val());
				if (textarea == "") {
					var n = $("#reason-body").find("input:checked").length;
					if (n <= 0) {
						layer.msg("请说明驳回理由");
						return false;
					}
				} else {
					var url = "/enrollment/" + id + "/unpass";
					ajaxReject(url);
				}

			});
		}

	});
	/*	批量通过*/
	$("body").on("click", "#pass-all", function () {
		var id = "";
		/*获取已勾选选项id*/
		$(".layui-table").find("input:checked").each(function () {
			var inputId = $(this).parents("tr").attr("data-id");
			id += inputId + ",";
		});
		if (id == "") {
			layer.msg("请先勾选要通过的选项");
		} else {
			var url = "/exam_question/" + id + "/pass";
			layer.confirm('确定通过？', {
				btn: ['确定', '取消'] //按钮
			}, function () {

				$.ajax({
					url: url,
					type: 'GET',
					success: function (data) {
						if (data) {
							location.reload();
						} else {
						}
					}
				});


			});
		}

	});

	function ajaxReject(url) {
		var params = {reason: ''};
		var textarea = $.trim($(".modal-body").find("textarea").val());
		if (textarea !== "") {
			params.reason += textarea + ';';
		}
		$("#reason-body").find("input:checked").each(function () {
			var text = $(this).siblings("label").text();
			params.reason += text + ';';
		});
		$.ajax({
			url: url,
			type: 'GET',
			data: params,
			success: function (data) {
				if (data) {
					location.reload();
				} else {
				}
			}
		});

		$("#modal").modal("hide");
	}


	function reasonList() {
		$("#reason-body").html("");
		var reason = $("#reason").text();
		/*获取驳回理由字符串*/
		var item = [];
		item = reason.split(";")
		/*驳回理由字符串转化为数组*/
		$("#modal").modal("show");
		var n = item.length;
		for (var i = 0; i < n - 1; i++) {
			var html = "<span class='reason-item'><input type='checkbox' id='"
				+ '0' + i
				+ "' />"
				+ "<label for='"
				+ '0' + i
				+ "'>"
				+ item[i]
				+ "</label></span>";
			$("#reason-body").append(html);
		}
	}


	/* 获取选中的题ID*/
	$("#type-list").on("click", "input", function () {
		var type = $("#topic-selection").find("button").not(".gray").attr("data-id");
		var id = $(this).parents("tr").attr("data-id");
		var hidden = $("#hidden").find("input[data-id=" + type + "]");
		var val = hidden.val();
		var text = "";
		if ($(this).is(":checked")) {
			var text = val + id + ",";
			hidden.val(text);
		} else {
			var nuk = val.indexOf(id);
			if (nuk >= 0) {
				var removetext = val.replace(id + ",", "");
				hidden.val(removetext);
			}

		}
	});


	/*	 设置考卷获取数据*/
	$("#topic-selection").on("click", "button", function () {
		$(this).removeClass("gray").siblings().addClass("gray");
		typ = $(this).attr("data-id");
		page = 1;
		loadList();
	});

	/* 提交*/
	$("#submit-btn").click(function () {
		var id = $.trim($("#examid").text());
		var url = "/exam_paper/" + id + "/set_questions";
		var questionIds = '';
		$("#hidden").find("input").each(function () {
			var id = $(this).val();
			if (id != "") {
				questionIds += id + ',';
			}
		});
		$.ajax({
			url: url,
			type: 'POST',
			data: {"questionIds": questionIds},
			success: function (data) {
				window.location = "/exam_paper/" + id + "/preview";
			}
		});

	});

	//加载数据
	function loadList() {
		var url = "../../exam_questions/json?type=" + typ + "&page=" + page;
		$.ajax({
			type: 'GET',
			url: url,
			success: function (data) {
				$(".layui-table").find("th").find("input").prop("checked", false);
				/*取消表头选框选定状态	*/
				var html = template('list', data);
				$("#type-list").html(html);
				console.log(data)
				$("#pagination").pagination({
					totalData: data.page.total,
					showData: data.page.page_size,
					current: data.page.page,
					callback: function (p) {
						page = p.getCurrent();
						loadList(typ);
					}

				});
				/*  勾选试题回显*/
				$("#type-list").find("tr").each(function () {
					var tr = $(this);
					var id = tr.attr("data-id");
					var hidden = $("#hidden").find("input[data-id=" + typ + "]");
					var content = hidden.val();
					if (content !== "") {
						var num = content.indexOf(id);
						if (num >= 0) {
							tr.find("input").prop("checked", true);
						}
					}

				});

			}
		});

	}

	/*删除*/
	$("body").on("click", ".delete", function () {
		var id = $(this).parents("tr").attr("data-id");
		var url = "/exam_question/delete/" + id;
		layer.confirm('确定删除？', {
			btn: ['确定', '取消'] //按钮
		}, function () {
			$.ajax({
				method: 'get',
				url: url,
				data: id,
				success: function (data) {
					if (data) {
						location.reload();
					} else {
						layer.msg("操作失败，请稍后重试！");
					}
				}

			})
		});
	});

	/*批量删除*/
	$("body").on("click", "#delete-all", function () {
		var ids = [];
		/*获取已勾选选项id*/
		$(".layui-table").find("input:checked").each(function () {
			var inputId = $(this).parents("tr").attr("data-id");
			// id += inputId + ",";
			ids.push(inputId)
		});
		if (ids.length==0) {
			layer.msg("请先勾选要通过的选项");
		} else {
			var url = "/exam_question/delete/" + ids.join(",");
			layer.confirm('确定删除？', {
				btn: ['确定', '取消'] //按钮
			}, function () {
				$.ajax({
					url: url,
					type: 'GET',
					success: function (data) {
						if (data) {
							location.reload();
						} else {
						}
					}
				});
			});
		}
	});
});


