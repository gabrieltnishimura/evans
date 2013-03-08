var input;
var cursor;
var hiddenInput;
var content = [];
var lastContent = "", targetContent = "";
var inputLock = false;
var autoWriteTimer;

var isMobile, isIE;

window.onload = function() {
	addLine();
};

function whenTouchStart(e) {
	clearInterval(autoWriteTimer);
	targetContent = lastContent;
}

function whenClick(e) {
	clearInterval(autoWriteTimer);
	targetContent = lastContent;
	hiddenInput.focus();
}

function whenInput(e) {
	e.preventDefault();
	if (!inputLock) {
		refresh();
	}
}

function refresh() {

	inputLock = true;
	targetContent = hiddenInput.value;

	if (targetContent.length - lastContent.length == 0) return;

	var v = targetContent.substring(0, lastContent.length + 1);

	content = [];

	var blinkPadding = false;

	for (var i = 0; i < v.length; i++) {
		var l = v.charAt(i);

		var d = document.createElement('div');
		d.setAttribute('class', 'letterContainer');

		var d2 = document.createElement('div');

		var animClass = (i % 2 == 0) ? 'letterAnimTop' : 'letterAnimBottom';

		var letterClass = (lastContent.charAt(i) == l) ? 'letterStatic' : animClass;

		if (letterClass != 'letterStatic') blinkPadding = true;

		d2.setAttribute('class', letterClass);

		d.appendChild(d2);

		d2.innerHTML = l;
		content.push(d);
	}

	input.innerHTML = '';

	for (var i = 0; i < content.length; i++) {
		input.appendChild(content[i]);
	}

	cursor.style.paddingLeft = (blinkPadding) ? '22px' : '0';

	if (!isMobile && !isIE) input.appendChild(cursor);

	if (targetContent.length - lastContent.length > 1) setTimeout(refresh, 100);
	else inputLock = false;

	lastContent = v;
}

function bindKeyupInput(counter) {
	if (counter == 1) {
		$("#hiddenInput").unbind('keyup');
		$("#hiddenInput").bind('keyup', function() {
			if (typeof $('.letterStatic').last().offset() != 'undefined') {
				var position = $('.letterStatic').last().offset();
				if (typeof position.left != "undefined" && position.left > 730) {
					addLine();
					console.log("added line");
				}
			}
		});
	} else {
		if (counter == 2) {
			$("#hiddenInput").unbind('keyup');
		} else {
			$("#hiddenInput_"+(counter-1)).unbind('keyup');
		}
		$("#hiddenInput_"+counter).unbind('keyup');
		$("#hiddenInput_"+counter).bind('keyup', function(e) {
			var position = $('.letterStatic').last().offset();
			if (typeof position.left != "undefined" && position.left > 730) {
				addLine();
				console.log("added line2");
			}
		});
	}
}

function addLine() {
	
//	document.removeEventListener('touchstart', whenTouchStart, false);
//	document.removeEventListener('click', whenClick, false);
//	hiddenInput.removeEventListener('input', whenInput, false);
	
	var counter = $("#counter").val();
	if (counter == 0) {
		$("#input").children('cursor').remove();
	} else {
		$("#input_"+counter).children('cursor').remove();
	}
	
	counter++;
	$("#counter").val(counter);

	$("#container").append('<div id="input_'+ counter +'"></div>'+
			'<input type="text" id="hiddenInput_'+ counter +'">');
	
	$("#hiddenInput_"+counter).css("font-size", "40px");
	$("#hiddenInput_"+counter).css("font-family", "Roboto Condensed, sans-serif");
	$("#hiddenInput_"+counter).css("background-color", "#808080");
	$("#hiddenInput_"+counter).css("position", "absolute");
	$("#hiddenInput_"+counter).css("margin-top", "-" + parseInt(22*counter) + "px");
	$("#hiddenInput_"+counter).css("font-size", "40px");
	$("#hiddenInput_"+counter).css("margin-left", "125px");
	$("#hiddenInput_"+counter).css("opacity", "0");
	$("#hiddenInput_"+counter).css("filter", "alpha(opacity = 0)");
	
	$("#hiddenInput_"+counter).css("position", "absolute");
	$("#hiddenInput_"+counter).css("margin-top", "-" + parseInt(20*counter) + "px");
	
	isMobile = navigator && navigator.platform && navigator.platform.match(/^(iPad|iPod|iPhone)$/);

	isIE = (navigator.appName == 'Microsoft Internet Explorer');

	input = document.getElementById('input_'+counter);

	hiddenInput = document.getElementById('hiddenInput_'+counter);
	hiddenInput.focus();

	cursor = document.createElement('cursor');
	cursor.setAttribute('class', 'blink');
	cursor.innerHTML = "|";

	if (!isMobile && !isIE) input.appendChild(cursor);

	if (document.addEventListener) {
		document.addEventListener('touchstart', whenTouchStart, false);
		document.addEventListener('click', whenClick, false);
		hiddenInput.addEventListener('input', whenInput, false);
	}

	hiddenInput.value = "";

	autoWriteTimer = setTimeout(function() {
		if (lastContent != "") return;
		targetContent = "type something...";
		refresh();
	}, 2000);
}