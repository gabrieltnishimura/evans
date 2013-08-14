<!-- BEGIN JAVASCRIPTS(Load javascripts at bottom, this will reduce page load time) -->
<!-- BEGIN CORE PLUGINS -->
<script src="../../plugins/jquery-1.10.1.min.js" type="text/javascript"></script>
<script src="../../plugins/jquery-migrate-1.2.1.min.js" type="text/javascript"></script>
<!-- IMPORTANT! Load jquery-ui-1.10.1.custom.min.js before bootstrap.min.js to fix bootstrap tooltip conflict with jquery ui tooltip -->
<script src="../../plugins/jquery-ui/jquery-ui-1.10.1.custom.min.js" type="text/javascript"></script>      
<script src="../../plugins/bootstrap/js/bootstrap.min.js" type="text/javascript"></script>
<script src="../../plugins/bootstrap-hover-dropdown/twitter-bootstrap-hover-dropdown.min.js" type="text/javascript" ></script>
<!--[if lt IE 9]>
<script src="../../plugins/excanvas.min.js"></script>
<script src="../../plugins/respond.min.js"></script>  
<![endif]-->   
<script src="../../plugins/jquery-slimscroll/jquery.slimscroll.min.js" type="text/javascript"></script>
<script src="../../plugins/jquery.blockui.min.js" type="text/javascript"></script>  
<script src="../../plugins/jquery.cookie.min.js" type="text/javascript"></script>
<script src="../../plugins/uniform/jquery.uniform.min.js" type="text/javascript" ></script>
<!-- END CORE PLUGINS -->
<!-- BEGIN PAGE LEVEL PLUGINS -->
<script src="../../plugins/jqvmap/jqvmap/jquery.vmap.js" type="text/javascript"></script>   
<script src="../../plugins/jqvmap/jqvmap/maps/jquery.vmap.russia.js" type="text/javascript"></script>
<script src="../../plugins/jqvmap/jqvmap/maps/jquery.vmap.world.js" type="text/javascript"></script>
<script src="../../plugins/jqvmap/jqvmap/maps/jquery.vmap.europe.js" type="text/javascript"></script>
<script src="../../plugins/jqvmap/jqvmap/maps/jquery.vmap.germany.js" type="text/javascript"></script>
<script src="../../plugins/jqvmap/jqvmap/maps/jquery.vmap.usa.js" type="text/javascript"></script>
<script src="../../plugins/jqvmap/jqvmap/data/jquery.vmap.sampledata.js" type="text/javascript"></script>  
<script src="../../plugins/flot/jquery.flot.js" type="text/javascript"></script>
<script src="../../plugins/flot/jquery.flot.resize.js" type="text/javascript"></script>
<script src="../../plugins/jquery.pulsate.min.js" type="text/javascript"></script>
<script src="../../plugins/bootstrap-daterangepicker/date.js" type="text/javascript"></script>
<script src="../../plugins/bootstrap-daterangepicker/daterangepicker.js" type="text/javascript"></script>     
<script src="../../plugins/gritter/js/jquery.gritter.js" type="text/javascript"></script>
<script src="../../plugins/fullcalendar/fullcalendar/fullcalendar.min.js" type="text/javascript"></script>
<script src="../../plugins/jquery-easy-pie-chart/jquery.easy-pie-chart.js" type="text/javascript"></script>
<script src="../../plugins/jquery.sparkline.min.js" type="text/javascript"></script>  
<!-- END PAGE LEVEL PLUGINS -->
<!-- BEGIN PAGE LEVEL SCRIPTS -->
<script src="../../js/template/app.js" type="text/javascript"></script>
<script src="../../js/template/index.js" type="text/javascript"></script>
<script src="../../js/template/tasks.js" type="text/javascript"></script>        
<!-- END PAGE LEVEL SCRIPTS -->  
<script>
	jQuery(document).ready(function() {    
	   App.init(); // initlayout and core plugins
	   Index.init();
	   Index.initJQVMAP(); // init index page's custom scripts
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
</html>