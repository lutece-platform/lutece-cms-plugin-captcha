package fr.paris.lutece.plugins.captcha.web;

import fr.paris.lutece.plugins.captcha.service.CaptchaService;
import fr.paris.lutece.portal.service.admin.AdminUserService;
import fr.paris.lutece.portal.service.captcha.ICaptchaService;
import fr.paris.lutece.portal.service.i18n.I18nService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.globalmanagement.AbstractGMLutecePanel;
import fr.paris.lutece.portal.web.globalmanagement.GlobalManagementJspBean;
import fr.paris.lutece.util.html.HtmlTemplate;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 * Panel for global management page
 */
public class CaptchaLutecePanelJspBean extends AbstractGMLutecePanel
{
    /**
     * generated serial version UID
     */
    private static final long serialVersionUID = 8792952492908177294L;

    private static final String LABEL_TITLE_CAPTCHA = "captcha.globalmanagement.panelTitle";

    private static final String PARAMETER_CAPTCHA_ENGINE = "captcha_engine";

    private static final String MARK_CURRENT_CAPTCHA_ENGINE = "current_captcha_engine";
    private static final String MARK_LIST_CAPTCHA_ENGINE = "listCaptchaEngine";

    private static final String TEMPLATE_CAPTCHA_PANEL = "admin/plugins/captcha/panel_captcha_management.html";

    private ICaptchaService _captchaService;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPanelKey( )
    {
        return LABEL_TITLE_CAPTCHA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPanelTitle( )
    {
        return I18nService.getLocalizedString( LABEL_TITLE_CAPTCHA, AdminUserService.getLocale( getRequest( ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getPanelContent( )
    {
        ICaptchaService captchaService = getCaptchaService( );
        Map<String, Object> model = new HashMap<String, Object>( );
        model.put( MARK_CURRENT_CAPTCHA_ENGINE, captchaService.getDefaultCaptchaEngineName( ) );
        model.put( MARK_LIST_CAPTCHA_ENGINE, captchaService.getCaptchaEngineNameList( ) );
        
        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_CAPTCHA_PANEL,
                AdminUserService.getLocale( getRequest( ) ), model );
        
        return template.getHtml( );
    }

    /**
     * Returns the panel's order. This panel is a second panel.
     * @return 2
     */
    @Override
    public int getPanelOrder( )
    {
        return 2;
    }

    /**
     * Update the default captcha to use
     * @param request The request
     * @return The next URL to redirect to
     */
    public String updateDefaultCaptcha( HttpServletRequest request )
    {
        String strDefaultCaptcha = request.getParameter( PARAMETER_CAPTCHA_ENGINE );
        ICaptchaService captchaService = getCaptchaService( );
        captchaService.setDefaultCaptchaEngineName( strDefaultCaptcha );
        return AppPathService.getBaseUrl( request ) + GlobalManagementJspBean.JSP_URL_GLOBAL_MANAGEMENT;
    }

    /**
     * Get a captcha service instance
     * @return An instance of the captcha servcie
     */
    private ICaptchaService getCaptchaService( )
    {
        if ( _captchaService == null )
        {
            _captchaService = SpringContextService.getBean( CaptchaService.BEAN_NAME );
        }
        return _captchaService;
    }
}
