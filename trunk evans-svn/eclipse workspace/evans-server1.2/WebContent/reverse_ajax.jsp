<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="br.com.evans.jndi.states.DeviceMonitor"%>
<%@page import="br.com.evans.devices.arduino.ArduinoDevice"%>
<%@page import="javax.naming.Context"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.naming.NamingException"%>
<%@page import="java.util.TreeMap"%>
<html>
    <head>
        <title>HTTP Polling</title>
        <link rel="stylesheet" type="text/css" href="css/iphone_button.css" />
        <script type="text/javascript" src='js/jquery-1.9.0.min.js'></script>
        <script type="text/javascript" src='js/reverse_ajax.js'></script>
        <script type="text/javascript" src='js/iphone-style-checkboxes.js'></script>
    </head>
    <body>
        <%
       	Context initCtx = new InitialContext();
       	Context envCtx = (Context) initCtx.lookup("java:comp/env");
		
		//Get the device monitor so it can get device states
		DeviceMonitor deviceMonitor = (DeviceMonitor) envCtx.lookup("states/DeviceMonitorFactory");
		TreeMap<Integer, ArduinoDevice> tree = deviceMonitor.getStatesTreeMap();
		Object[] keySet = tree.navigableKeySet().toArray();
        %>
        
        <table>
        <% for(int i = 0; i < keySet.length; i++) { %>
        	 <tr class="device<%= i %> device_each">
        	 	<th><label><%= tree.get(keySet[i]).getLocation() %></label></th>
        	 	<td><input type="checkbox" <% if (tree.get(keySet[i]).getDeviceStatus()) {
        	 		%> checked="checked" <% } %> />
        	 	</td>
       	 	</tr>
       	<% } %>
        </table>
        <hr>
        <div id="logs" style="font-family: monospace;"></div>
    </body>
</html>
