$(function () {
    $('#adicon').customFileInput({
        button_position : 'right'
        ,button_text:'浏&nbsp;览',
        feedback_text:'选择应用ICON',
        button_change_text:'重 选'
    });
    
    $('#adinfo').customFileInput({
        button_position : 'right'
        ,button_text:'浏&nbsp;览',
        feedback_text:'选择应用截图',
        button_change_text:'重 选'
    });
    
    $('#adApk').customFileInput({
        button_position : 'right'
        ,button_text:'浏&nbsp;览',
        feedback_text:'选择应用',
        button_change_text:'重 选'
    });

    $('#appIcon').customFileInput({
        button_position : 'right'
        ,button_text:'浏&nbsp;览',
        feedback_text:'选择应用ICON',
        button_change_text:'重 选'
    });

    $('#img1').customFileInput({
		button_position : 'right'
		,button_text:'浏&nbsp;览',
		feedback_text:'选择应用截图',
		button_change_text:'重 选'
    });
	$('#img2').customFileInput({
		button_position : 'right',
		button_text:'浏&nbsp;览',
		feedback_text:'选择应用截图',
		button_change_text:'重 选'
    });
	$('#img3').customFileInput({
		button_position : 'right',
		button_text:'浏&nbsp;览',
		feedback_text:'选择应用截图',
		button_change_text:'重 选'
	});
	$('#img3, #img2, #img1').change(function(){
//		$('#tb_' + this.id).html('<img width="160" src="' + getPath(this) + '" />');
	});

	$('#file').customFileInput({
		button_position : 'right',
		button_text:'浏&nbsp;览',
		feedback_text:'选择应用APK',
		button_change_text:'重 选'
	}); 
      
});