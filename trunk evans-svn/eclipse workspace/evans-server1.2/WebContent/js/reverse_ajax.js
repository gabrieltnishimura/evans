var array = new Array();
var started = false;
var object = new Object();

$(document).ready(function() {
	$('.device_each :checkbox').iphoneStyle();
	
	setTimeout(function(){
			start();
			bindChagesDeviceState();
	},1000);
});
jQuery(function($) {
	function processEvents(events) {
		if (events == null) {
			$('#logs').append('<span style="color: red;">[client] no event</span><br/>');
		} else {
			populateCheckboxes(events);
		}
	}

	function long_polling() {
		$.getJSON('reverseAjax', function(events) {
			processEvents(events);
			long_polling();
		});
	}

	long_polling();
});

function populateCheckboxes(events) {
	console.log("--- reverse ajax from JAVA ---");
	if (typeof events['deviceList'] != 'undefined') {
		for (var i in events['deviceList']) {
			var state = events['deviceList'][i]['state'];
			console.log("[device] " + events['deviceList'][i]['device_name'] +  "[" + state + "]");
			array[i] = state;
			
			var checkbox = $(".device"+i).children().children().children('input');
			if (state) {
				checkbox.val('on').attr('checked', 'checked').iphoneStyle('refresh');
			} else {
				checkbox.val('off').removeAttr('checked').iphoneStyle('refresh');
			}
		}
		console.log("------------------------------");
	}
}

function bindChagesDeviceState() { 
	$('.iPhoneCheckContainer').unbind('touchend');
	$('.iPhoneCheckContainer').unbind('mousedown');
	
	$('.iPhoneCheckContainer').bind("mousedown", function() {
		var className = $(this).parents('.device_each').attr('class');
		var explode = className.split(' device_each');
		var idDevice = explode[0].substring(explode[0].indexOf('device')+6, explode[0].indexOf('device')+7);
		var deviceName = $('.device'+idDevice).children().children('label').html();
		
		var checkbox = $(".device"+idDevice).children().children().children('input');
		
		if (checkbox.val() == 'off') { // was off -> gunna be on
			if (!array[idDevice]) {  // was off is on
				array[idDevice] = true;
				checkbox.prop('checked', !checkbox.is(':checked')).iphoneStyle('refresh');
				ajaxSwitchStates(deviceName); 
			} 
		} else {
			if (array[idDevice]) { //was on is off now
				array[idDevice] = false;
				checkbox.prop('checked', !checkbox.is(':checked')).iphoneStyle('refresh');
				ajaxSwitchStates(deviceName); 
			} 
		}
	});
	$('.iPhoneCheckContainer').bind("touchend", function() {
		var className = $(this).parents('.device_each').attr('class');
		var explode = className.split(' device_each');
		var idDevice = explode[0].substring(explode[0].indexOf('device')+6, explode[0].indexOf('device')+7);
		var deviceName = $('.device'+idDevice).children().children('label').html();
		
		var checkbox = $(".device"+idDevice).children().children().children('input');
		
		if (checkbox.val() == 'off') { // was off -> gunna be on
			if (!array[idDevice]) {  // was off is on
				array[idDevice] = true;
				ajaxSwitchStates(deviceName); 
			} 
		} else {
			if (array[idDevice]) { //was on is off now
				array[idDevice] = false;
				ajaxSwitchStates(deviceName); 
			} 
		} 
	}); 
}

function ajaxSwitchStates(deviceName) {
	$.ajax({
		type: "POST",  
		url: "handlingSpeech",  
		data: {
			recognitionString: deviceName
		}           
	});
}

function start() {
	$.ajax({
		type: "POST",  
		url: "DeviceManagerInit",  
		data: {init: true},  
		success: function (events) {
			populateCheckboxes(events);
		}
	});
}