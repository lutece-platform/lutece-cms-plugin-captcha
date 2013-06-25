<%@ page errorPage="../ErrorPage.jsp" %>
<jsp:useBean id="captchaManagement" scope="session" class="fr.paris.lutece.plugins.captcha.web.CaptchaLutecePanelJspBean" />

<%
	captchaManagement.init( request, "CORE_GLOBAL_MANAGEMENT" ) ;
	response.sendRedirect( captchaManagement.updateDefaultCaptcha( request ) );
%>