<html class=" -webkit-">
<%@page import="br.com.evans.behavior.nodes.manager.BehaviorNodeManager"%>
<%@page import="br.com.evans.behavior.nodes.core.BehaviorNode"%>
	<head>
		<meta charset="UTF-8">
		<title>Behavior Node Backend</title>
		<style media="" data-href="/assets/reset/reset.css">
			html,body,div,span,applet,object,iframe,h1,h2,h3,h4,h5,h6,p,blockquote,pre,a,abbr,acronym,address,big,cite,code,del,dfn,em,img,ins,kbd,q,s,samp,small,strike,strong,sub,sup,tt,var,b,u,i,center,dl,dt,dd,ol,ul,li,fieldset,form,label,legend,table,caption,tbody,tfoot,thead,tr,th,td,article,aside,canvas,details,embed,figure,figcaption,footer,header,hgroup,menu,nav,output,ruby,section,summary,time,mark,audio,video{margin:0;padding:0;border:0;font-size:100%;font:inherit;vertical-align:baseline}article,aside,details,figcaption,figure,footer,header,hgroup,menu,nav,section{display:block}body{line-height:1}ol,ul{list-style:none}blockquote,q{quotes:none}blockquote:before,blockquote:after,q:before,q:after{content:'';content:none}table{border-collapse:collapse;border-spacing:0}
		</style>
		<link rel="stylesheet" type="text/css" href="css/menu.css" />
		<script src="js/jquery-1.10.1.min.js"></script><script src="http://s.codepen.io/assets/libs/empty.js"></script>
		<script>
		jQuery(function($) {
		  $('.GUI-list-item > a').each(function(){
		    $(this).on('click', function(){
		       if ($(this).hasClass('on')) {
		        $(this).removeClass('on').addClass('off');
		      } 
		      else {
		        $('.on').removeClass('on');
		        $(this).removeClass('off').addClass('on');
		      } 
		      return false;
		    });
		  });
		});
		
		function addNodeInsideParent(nodeId) {
			$("#addExtraNode"+nodeId).fadeIn();
			/*$.ajax({
				type: "POST",  
				url: "BehaviorNavigator",  
				data: {
					destination: nodeName
				},
				success: function(e) {
					
				}
			});*/
		}
		</script>
	</head>

	<body style="">
	        <%
	        	BehaviorNodeManager manager = new BehaviorNodeManager();
		        BehaviorNode parent = manager.getParent();
	        
		        int i = 0;
	        %>
	        <div style="float: left;">
				<nav class="GUI">
					<ul class="GUI-list">
				 	<% for (BehaviorNode node : parent.getChilds()) { %>
				    	<li class="GUI-list-item node_each node<%= i %>">
				    		<a style="cursor: pointer;" class="parent_node">
					        	<span class="fontawesome-angle-right icon"></span>
				        		<span class="label"><%= node.getName() %></span>  
				      		</a>
				   			<ul>
								<% int j = 0; for (BehaviorNode inner_node : node.getChilds()) { %>
					    	    	<li>
					    	    		<a href="javascript:void(0)" onclick="$.ajax({type:'GET',url:'<%= inner_node.getLink() %>'});" 
					    	    			style="cursor: pointer;" ><%= inner_node.getName() %></a>
					    	    	</li>
				    			<% j++;} %>
				    			<li>
				    				<a href="javascript:void(0)" onclick="addNodeInsideParent(<%= node.getId() %>);" >+ add node
				    				</a>
				    					<div id="addExtraNode<%= node.getId() %>" style="display: none; float: right; position: relative; bottom: 4px;">
					    					<input type="text">
					    				</div>
				    			</li>
				     		 </ul>
				   		 </li>
					  <% i++;} %>
					  </ul>
				</nav>
			</div>
			<div id="" style="float: left;">
				evans console
			</div>
	</body>
</html>