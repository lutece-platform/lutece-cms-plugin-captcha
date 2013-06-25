package fr.paris.lutece.plugins.captcha.service;

import javax.servlet.http.HttpServletRequest;


/**
 * Interface for captcha engine implementations
 */
public interface ICaptchaEngine
{
    /**
     * Validate the captcha field
     * @param request The HTTP request
     * @return True if OK, otherwise false
     */
    boolean validate( HttpServletRequest request );

    /**
     * Gets the captcha HTML code
     * @return The captcha HTML code
     */
    String getHtmlCode( );

    /**
     * Get the name of the captcha engine
     * @return The name of the captcha engine
     */
    String getCaptchaEngineName( );
}
