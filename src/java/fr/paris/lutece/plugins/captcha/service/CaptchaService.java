package fr.paris.lutece.plugins.captcha.service;

import fr.paris.lutece.portal.service.captcha.ICaptchaService;
import fr.paris.lutece.portal.service.datastore.DatastoreService;
import fr.paris.lutece.portal.service.spring.SpringContextService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * Captcha service
 */
public class CaptchaService implements ICaptchaService
{
    private static final String DATASTORE_KEY_DEFAULT_CAPTCHA_ENGINE = "captcha.defaultProvider";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate( HttpServletRequest request )
    {
        List<ICaptchaEngine> listCaptchaEngine = SpringContextService.getBeansOfType( ICaptchaEngine.class );
        if ( listCaptchaEngine != null && listCaptchaEngine.size( ) > 0 )
        {
            String strDefaultCaptchaEngineName = getDefaultCaptchaEngineName( );
            for ( ICaptchaEngine captchaImpl : listCaptchaEngine )
            {
                if ( StringUtils.equals( strDefaultCaptchaEngineName, captchaImpl.getCaptchaEngineName( ) ) )
                {
                    return captchaImpl.validate( request );
                }
            }
        }
        // If there is no captcha implementation, we return true
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getHtmlCode( )
    {
        List<ICaptchaEngine> listCaptchaEngine = SpringContextService.getBeansOfType( ICaptchaEngine.class );
        if ( listCaptchaEngine != null && listCaptchaEngine.size( ) > 0 )
        {
            String strDefaultCaptchaEngineName = getDefaultCaptchaEngineName( );
            for ( ICaptchaEngine captchaImpl : listCaptchaEngine )
            {
                if ( StringUtils.equals( strDefaultCaptchaEngineName, captchaImpl.getCaptchaEngineName( ) ) )
                {
                    return captchaImpl.getHtmlCode( );
                }
            }
        }
        // If there is no captcha implementation, we return an empty string
        return StringUtils.EMPTY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getCaptchaEngineNameList( )
    {
        List<ICaptchaEngine> listCaptchaEngine = SpringContextService.getBeansOfType( ICaptchaEngine.class );
        if ( listCaptchaEngine != null && listCaptchaEngine.size( ) > 0 )
        {
            List<String> listCaptchaEngineNames = new ArrayList<String>( listCaptchaEngine.size( ) );
            for ( ICaptchaEngine captchaImpl : listCaptchaEngine )
            {
                listCaptchaEngineNames.add( captchaImpl.getCaptchaEngineName( ) );
            }
            return listCaptchaEngineNames;
        }
        return new ArrayList<String>( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDefaultCaptchaEngineName( )
    {
        String strDefaultCaptcha = DatastoreService.getDataValue( DATASTORE_KEY_DEFAULT_CAPTCHA_ENGINE,
                StringUtils.EMPTY );
        if ( StringUtils.isBlank( strDefaultCaptcha ) )
        {
            // If there is no default captcha engine, we get the first one from the captcha engine list
            List<ICaptchaEngine> listCaptchaEngine = SpringContextService.getBeansOfType( ICaptchaEngine.class );
            if ( listCaptchaEngine != null && listCaptchaEngine.size( ) > 0 )
            {
                for ( ICaptchaEngine captchaEngine : listCaptchaEngine )
                {
                    if ( StringUtils.isNotBlank( captchaEngine.getCaptchaEngineName( ) ) )
                    {
                        strDefaultCaptcha = captchaEngine.getCaptchaEngineName( );
                        // We save the default captcha
                        setDefaultCaptchaEngineName( strDefaultCaptcha );
                        break;
                    }
                }
            }
        }
        return strDefaultCaptcha;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultCaptchaEngineName( String strDefaultCaptchaEngine )
    {
        DatastoreService.setDataValue( DATASTORE_KEY_DEFAULT_CAPTCHA_ENGINE, strDefaultCaptchaEngine );
    }

}
