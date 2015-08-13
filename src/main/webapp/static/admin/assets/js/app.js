//用户退出
function logout() {
	$.getJSON(getCtxPath() + "/admin/ajax/logout", function(data) {
		setTimeout(function() {
			location.reload(true);
		}, 500);
	});
}

//表单校验
function checkForm($form) {
	var temp = true;
	$.each($form.find("input[pattern]"), function(i, tag) {
		var value = $(tag).val();
		var pat   = $(tag).attr("pattern");
		if (!new RegExp(pat).test(value)) {
			temp = false;
			return false;
		}
	});
	return temp;
}

//jquery form表单校验
function validate(formData, jqForm, options) { 
	return checkForm(jqForm);
}

//图片预览，兼容各个浏览器
function previewImage(file) {
	var porImg  = $(file),
		viewImg = $('#viewImg');
	var image = porImg.val();
	if (!/^\S*\.(?:png|jpe?g|bmp|gif)$/i.test(image)) {
		$alertMessage.html("请选择图片");
		$alertModal.removeClass("am-modal-no-btn").modal();
		porImg.val("");
		return false;
	}
	if (file["files"] && file["files"][0]) {
		var reader = new FileReader();
		reader.onload = function(evt){
			viewImg.attr({src : evt.target.result});
		}
		reader.readAsDataURL(file.files[0]);
	} else {
		var ieImageDom = document.createElement("div");
		var proIeImageDom = document.createElement("div");
		$(ieImageDom).css({
			float: 'left',
			position: 'relative',
			overflow: 'hidden',
			width: '100px',
			height: '100px'
		}).attr({"id":"view"});
		$(proIeImageDom).attr({"id": porImg.attr("id")});
		porImg.parent().prepend(proIeImageDom);
		porImg.remove();
		file.select();
		path = document.selection.createRange().text;
		$(ieImageDom).css({"filter": "progid:DXImageTransform.Microsoft.AlphaImageLoader(enabled='true',sizingMethod='scale',src=\"" + path + "\")"});
	}
}

/**
 * post 通用方法
 * @param selector 按钮的id、class...
 * @param cb 回调函数
 * @returns {Boolean}
 */
function $post(selector, cb) {
	var _btn = $(selector);
	_btn.attr("disabled", true);
	var $form = _btn.parents("form");
	// 参数表单参数
	if (!checkForm($form)) {
		_btn.attr("disabled", false);
		return false;
	}
	$.post($form.attr("action"), $form.serialize(), function(data){
		_btn.attr("disabled", false);
		if (data.code === 1) {
			layer.msg(data.message, {shift: 6});
			return false;
		}
		if(data.code === 2){
			layer.msg(data.message, {shift: 6});
			setTimeout(function(){window.location.reload(true);}, 1200);
			return false;
		}
		cb(data);
	});
}

//删除用户
function deleteUser(userId, type){
	if(type == 1){
		$("#deleteUserIds").val(userId);
	}else{
		var deleteUserIds = "";
		$("[name='userCheckbox']").each(function(){
			if($(this).is(":checked")){
				if(deleteUserIds == ""){
					deleteUserIds = $(this).val();
				}else{
					deleteUserIds += "," + $(this).val();
				}
			}
		});
		$("#deleteUserIds").val(deleteUserIds);
	}
	if($("#deleteUserIds").val() == "" || $("#deleteUserIds").val() == undefined){
		alert("请先勾选要删除的用户！");
		return false;
	}
	//询问框
	layer.confirm('确定要删除用户？', {
		btn: ['确认','取消'], //按钮
		shade: true //不显示遮罩
	}, function(){
		//删除用户
		var userIds = $("#deleteUserIds").val();
		var url = getCtxPath() + "/admin/ajax/del_user"
		$.post(url, {userIds:userIds} ,function(data){
			if(data.code == 1){
				alert(data.message);
				return false;
			}else{
				layer.msg('删除成功!', {shift: 6});
				$("#userForm").submit();
			}
		});
	}, function(){
	    layer.msg('取消成功', {shift: 6});
	});
}

$(function(){
	//图片上传预览
	$('#user-pic').change(function(e){
		previewImage(this);
	});

	//全选--用户列表
	$('th.table-check input[type="checkbox"]').on("click", function() {
		$(this).closest("table").find('td input[type="checkbox"]').prop("checked", $(this).prop("checked"));
	});

	//用户列表跳转至新增或者修改用户
	$(".add-update-user").click(function(){
		var id = $(this).attr("rel");
		var url = getCtxPath() + "/admin/add_update_user";
		if(id != "" && id != undefined){
			url += "?id=" + id;
		}
		window.location.href= url;
	});

	// 用户列表表单提交
	$('#js_form_user').ajaxForm({
		dataType: "json",
		beforeSubmit: validate,
		success:  function(data) {
			if (data.code === 0) {
				window.location.href = getCtxPath() + "/admin/user_list";
			} else {
				alert(data.message);
				return false;
			}
		}
	});
	
	// 用户列表表单提交
	$('#js_form_mv').ajaxForm({
		dataType: "json",
		beforeSubmit: validate,
		success:  function(data) {
			if (data.code === 0) {
				window.location.href = getCtxPath() + "/admin/mv_list";
			} else {
				alert(data.message);
				return false;
			}
		}
	});
	
	//通用删除
	$(".js_del").on("click", function() {
		var action = $(this).data("action");
		if (null == action) {
			layer.msg("请先设置 data-action", {shift: 6});
			return false;
		}
		var _this = this;
		$.getJSON(action, function(data) {
			if (data.code === 1) {
				layer.msg(data.message, {shift: 6});
			} else if(data.code === 2){
				layer.msg(data.message, {shift: 6});
				setTimeout(function(){window.location.reload(true);}, 1200);
			} else {
				layer.msg("删除成功~", {shift: 1});
				$(_this).closest("tr").remove();
			}
		});
		return false;
	});

	// 镜像构建提交，带有文件上传，用ajax from
	$("#js_images_form").ajaxForm({
		dataType: "json",
		beforeSubmit: validate,
		success:  function(data) {
			if (data.code === 0) {
				layer.msg("构建中，请稍后~", {shift: 1});
				setTimeout(function(){window.location.href = getCtxPath() + "/admin/images";}, 2000);
			} else {
				layer.msg(data.message, {shift: 6});
			}
		}
	});

	// 系统添加、更新
	$("#js_app_add_update_form").ajaxForm({
		dataType: "json",
		beforeSubmit: validate,
		success:  function(data) {
			if (data.code === 0) {
				layer.msg("操作成功~", {shift: 1});
				setTimeout(function(){window.location.href = getCtxPath() + "/admin/app_list";}, 2000);
			} else {
				layer.msg(data.message, {shift: 6});
			}
		}
	});

	// 登陆提交
	$("#js_login_submit").on("click", function(){
		$post(this, function(data){
			layer.msg("登陆成功~", {shift: 1});
			setTimeout(function(){window.location.href = getCtxPath() + "/admin";}, 2000);
		});
		return false;
	});
	
	//数据库管理列表时间控件
	var startDate = "";
    var endDate = new Date();
    //开始时间
    $('#my-start').datepicker().on('changeDate.datepicker.amui', function(event) {
        if (event.date.valueOf() > endDate.valueOf()) {
        	$("#my-start").val('');
        	alert('开始日期应小于结束日期！');
        	return false;
        } 
    	startDate = new Date(event.date);
    	$('#my-startDate').text($('#my-start').data('date'));
        $(this).datepicker('close');
    });
    //结束时间
    $('#my-end').datepicker().on('changeDate.datepicker.amui', function(event) {
        if (event.date.valueOf() < startDate.valueOf()) {
			alert('结束日期应大于开始日期！');
			$("#my-end").val('');
			return false;
        } 
		$alert.hide();
		endDate = new Date(event.date);
		$('#my-endDate').text($('#my-end').data('date'));
        $(this).datepicker('close');
    });
	
    //用户管理--应用管理
    //停止或者启动实例
    $(".update-user-app-status").click(function(){
    	var id = $(this).attr("rel");
    	//询问框
    	layer.confirm('确定要更改该应用运行状态？', {
    		btn: ['确认','取消'], //按钮
    		shade: true //不显示遮罩
    	}, function(){
    		//用户APP的ID
    		var url = getCtxPath() + "/admin/ajax/update_user_app_status"
    		$.post(url, {id:id} ,function(data){
    			if(data.code == 1){
    				alert(data.message);
    				return false;
    			}else{
    				layer.msg('停止运行成功!', {shift: 6});
    				$("#userAPPForm").submit();
    			}
    		});
    	}, function(){
    	    layer.msg('取消成功', {shift: 6});
    	});
    });
    
    //删除实例
    $(".delete-user-app").click(function(){
    	var id = $(this).attr("rel");
    	//询问框
    	layer.confirm('确定要删除该应用？', {
    		btn: ['确认','取消'], //按钮
    		shade: true //不显示遮罩
    	}, function(){
    		//用户APP的ID
    		var url = getCtxPath() + "/admin/ajax/delete_user_app"
    		$.post(url, {id:id} ,function(data){
    			if(data.code == 1){
    				alert(data.message);
    				return false;
    			}else{
    				layer.msg('删除成功!', {shift: 6});
    				$("#userAPPForm").submit();
    			}
    		});
    	}, function(){
    	    layer.msg('取消成功', {shift: 6});
    	});
    });
    
    //更新实例
    $(".update-user-app").click(function(){
    	alert("功能暂未开放，敬请期待~");
    	return false;
    });
    
    
    $(".add-or-update").click(function(){
    	var url = getCtxPath() + $(this).attr("action-url");
    	var rel = $(this).attr("rel");
    	if(rel != undefined && rel != null){
    		url += "?id="+rel;
    	}
    	window.location.href = url;
    });
    
    
    
    

	$("#recUrlUp").click(function(){
		upImage("#recUrl");
	});

	$("#imgUp").click(function(){
		upImage("#img");
	});
	
	$("#smallUrlUp").click(function(){
		upImage("#smallUrl");
	});
	
	$("#addressUp").click(function(){
		upfile("#addressUrl");
	});
});

