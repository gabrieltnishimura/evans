<html class=" -webkit-">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
		
		function nodeExecution(name, reset) {
			$.ajax({
				type: "POST",  
				url: "BehaviorExecution",  
				data: {
					name: name,
					reset: reset
				},
				success: function(e) {
					
				}
			});
		}
		</script>
	</head>

	<body style="">
	        <div style="float: left;">
				<nav class="GUI">
					<ul class="GUI-list">
						<c:forEach items="${childs}" var="parents" varStatus="status" begin="0">
					    	<li class="GUI-list-item node_each node${status.index}">
					    		<a style="cursor: pointer;" class="parent_node">
						        	<span class="fontawesome-angle-right icon"></span>
					        		<span class="label">${parents.name}</span>  
					      		</a>
					   			<ul>
					   				<c:forEach items="${parents.childs}" var="childs">
						    	    	<li><a href="javascript:void(0)" onclick="nodeExecution('${childs.name}', '0');" 
						    	    	style="cursor: pointer;" >${childs.name}</a></li>
					    	    	</c:forEach>
					    			<li><a href="javascript:void(0)" onclick="addNode();" >+ add node</a></li>
					     		 </ul>
					   		 </li>
						</c:forEach>
					</ul>
				</nav>
			</div>
			<div id="console" style="float: left;">
				evans console
			</div>
	</body>
</html>