$(document).ready(function() {
	$(".GUI").find("li > a").not(".parent_node").unbind("click");
	$(".GUI").find("li > a").not(".parent_node")bind("click", function(i, e) {
		console.log("clicked into '"+$(this).html()+"'");
		ajaxNavigateThroughNodes($(this).html());
	});
	resetParent();
});

function ajaxAccessNode(nodeName) {
	$.ajax({
		type: "POST",  
		url: "BehaviorNavigator",  
		data: {
			destination: nodeName
		},
		success: function(e) {
			
		}
	});
}

function ajaxNavigateThroughNodes(nodeName) {
	$.ajax({
		type: "POST",  
		url: "BehaviorNavigator",  
		data: {
			destination: nodeName
		},
		success: function(e) {
			// if successfully retrieved nodes repopulate hierarchy
			$("table>tbody").empty();
			for (var index in e["childs"]) {
				$("table>tbody").append('<tr class="node'+index+' node_each"><th>\
					<h1 style="background-color:grey; cursor:pointer;"><a class="clickable">'+e["childs"][index]["name"]+'</a></h1></th></tr>');
			}
			$(".clickable").unbind("click");
			$(".clickable").bind("click", function(i, e) {
				console.log("clicked into '"+$(this).html()+"'");
				ajaxNavigateThroughNodes($(this).html());
			});
		}
	});
}

function resetParent() {
	$.ajax({
		type: "GET",  
		url: "BehaviorNavigator?reset=1"
	});
}