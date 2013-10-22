<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<script src="${pageContext.request.contextPath}/plugins/jquery-1.10.1.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui-1.10.1.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="${pageContext.request.contextPath}/plugins/jquery-ui/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>      
<script src="${pageContext.request.contextPath}/plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js" type="text/javascript" ></script>
<!--[if lt IE 9]>
<script src="${pageContext.request.contextPath}/plugins/excanvas.min.js"></script>
<script src="${pageContext.request.contextPath}/plugins/respond.min.js"></script>  
<![endif]-->   
<script src="${pageContext.request.contextPath}/plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/plugins/jquery.blockui.min.js" type="text/javascript"></script>  
<script src="${pageContext.request.contextPath}/plugins/jquery.cookie.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="${pageContext.request.contextPath}/plugins/flot/jquery.flot.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/plugins/flot/jquery.flot.resize.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/plugins/bootstrap-daterangepicker/date.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/plugins/bootstrap-daterangepicker/daterangepicker.js" type="text/javascript"></script>     
<script src="${pageContext.request.contextPath}/plugins/gritter/js/jquery.gritter.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/plugins/jquery.pulsate.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/plugins/fullcalendar/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/plugins/jquery-easy-pie-chart/jquery.easy-pie-chart.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/plugins/jquery.sparkline.min.js" type="text/javascript"></script>  
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="${pageContext.request.contextPath}/js/template/app.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/template/index.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/template/tasks.js" type="text/javascript"></script>        
<!-- END PAGE LEVEL SCRIPTS -->  
<script>
	jQuery(document).ready(function() {    
	   App.init(); // initlayout and core plugins
	   Index.init();
	   Index.initCalendar(); // init index page's custom scripts
	   Index.initCharts(); // init index page's custom scripts
	   Index.initChat();
	   Index.initMiniCharts();
	   Index.initDashboardDaterange();
	   Index.initIntro();
	   Tasks.initDashboardWidget();
	});
</script>
<!-- END JAVASCRIPTS -->
</body>
<!-- END BODY -->