//用户退出
function logout() {
	$.getJSON(getCtxPath() + "/ajax/logout", function(data) {
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