<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.com.evans.behavior.nodes.manager.BehaviorNodeManager"%>
<%@page import="br.com.evans.behavior.nodes.core.BehaviorNode"%>
<html>
    <head>
        <title>HTTP Polling</title>
        <script type="text/javascript" src='js/jquery-1.9.0.min.js'></script>
        <script type="text/javascript" src='js/behavior_ajax.js'></script>
    </head>
    <body>
        <%
	       	BehaviorNodeManager nodeManager = new BehaviorNodeManager();
	        BehaviorNode parent = nodeManager.getParent();
	        int i = 0;
        %>
        
        <table>
        	<tbody>
	        <% for (BehaviorNode node : parent.getChilds()) { %>
	        	 <tr class="node<%= i %> node_each">
	        	 	<th><h1 style="background-color:grey; cursor:pointer;"><a href="javascript:void(0)" 
	        	 		onclick="$.ajax({type:'GET',url:'<%= node.getLink() %>'});" class="clickable"><%= node.getName() %></a></h1></th>
	       	 	</tr>
	       	<% i++;} %>
       		</tbody>
        </table>
    </body>
</html>
