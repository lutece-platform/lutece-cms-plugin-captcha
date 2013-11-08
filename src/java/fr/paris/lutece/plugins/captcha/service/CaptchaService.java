/*
 * Copyright (c) 2002-2013, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
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
    public static final String BEAN_NAME = "captcha.captchaService";

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
