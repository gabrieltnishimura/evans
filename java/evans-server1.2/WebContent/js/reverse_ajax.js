var array = new Array();
var started = false;
var object = new Object();

/**
 * Binding the document ready function so that
 * it creates all iphoneStyle checkboxes and
 * redo all binding on clicks 
 */
$(document).ready(function() {
	$('.device_each :checkbox').iphoneStyle();
	
	setTimeout(function(){
			start();
			bindChagesDeviceState();
	},500);
});

/**
 * Even features null events
 * @param events
 */
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

/**
 * Reverse ajax
 * constantly waits for a ajax from 
 * 'reverseAjax' link
 */
jQuery(function($) {
	long_polling();
});

/**
 * This function populates all checkboxes with
 * 'checked' or 'unchecked' option, given the 
 * JSON event array, provided from AJAX call.
 * @param events
 */
function populateCheckboxes(events) {
	if (typeof events['deviceList'] != 'undefined') {
		console.log("--- reverse ajax from JAVA ---");
		for (var i in events['deviceList']) {
			var state = events['deviceList'][i]['state'];
			console.log("[device] " + events['deviceList'][i]['device_name'] +  "[" + state + "]");
			array[i] = state;
			
			var checkbox = $(".device"+i).children().children().children('input');	
			if (state) {
				checkbox.prop('checked', true).change();
			} else {
				checkbox.prop('checked', false).change();
			}
		}
		console.log("------------------------------");
	}
}

/**
 * Rebinding clicks so that when the user clicks,
 * a AJAX-device-state call is made.
 */
function bindChagesDeviceState() { 
	$('.iPhoneCheckContainer').unbind('touchend');
	$('.iPhoneCheckContainer').unbind('mousedown');
	$(".iPhoneCheckContainer").bind("mousedown", function() {
		var className = $(this).parents('.device_each').attr('class');
		var explode = className.split(' device_each');
		var idDevice = explode[0].substring(explode[0].indexOf('device')+6, explode[0].indexOf('device')+7);
		var deviceName = $('.device'+idDevice).children().children('label').html();
		
		var checkbox = $(".device"+idDevice).children().children().children('input');
		if (!checkbox.prop("checked")) { // was off -> gunna be on
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
		checkbox.prop('checked', !checkbox.is(':checked')).iphoneStyle('refresh'); // this visualy changes state
	});
	$('.iPhoneCheckContainer').bind("touchend", function() {
		var className = $(this).parents('.device_each').attr('class');
		var explode = className.split(' device_each');
		var idDevice = explode[0].substring(explode[0].indexOf('device')+6, explode[0].indexOf('device')+7);
		var deviceName = $('.device'+idDevice).children().children('label').html();
		
		var checkbox = $(".device"+idDevice).children().children().children('input');
		
		if (!checkbox.prop("checked")) { // was off -> gunna be on
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

/**
 * AJAX call so that the device 'deviceName' 
 * switch states
 */
function ajaxSwitchStates(deviceName) {
	$.ajax({
		type: "POST",  
		url: "handlingSpeech",  
		data: {
			recognitionString: deviceName
		}
	});
}

/**
 * AJAX starting function: returns when succeded
 * a device state populated array
 */
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