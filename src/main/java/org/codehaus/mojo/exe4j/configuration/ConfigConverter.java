/* Copyright 2003-2005 : Project cobertura-maven-plugin */
package org.codehaus.mojo.exe4j.configuration;

import org.codehaus.plexus.component.configurator.ComponentConfigurationException;
import org.codehaus.plexus.component.configurator.ConfigurationListener;
import org.codehaus.plexus.component.configurator.converters.AbstractConfigurationConverter;
import org.codehaus.plexus.component.configurator.converters.lookup.ConverterLookup;
import org.codehaus.plexus.component.configurator.expression.ExpressionEvaluator;
import org.codehaus.plexus.configuration.PlexusConfiguration;
import org.codehaus.plexus.configuration.PlexusConfigurationException;
import org.codehaus.plexus.util.TypeFormat;

/**
 * The ConfigConvert for the Exe4JMojo
 * 
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 */
public class ConfigConverter
    extends AbstractConfigurationConverter
{

    public boolean canConvert( Class type )
    {
        return ( ConfigClassPath.class.isAssignableFrom( type ) || ConfigNativeLibraries.class.isAssignableFrom( type )
            || ConfigJRESearchPath.class.isAssignableFrom( type ) || ConfigStatusLine.class.isAssignableFrom( type )
            || ConfigVersionLine.class.isAssignableFrom( type ) || ConfigQuotedString.class.isAssignableFrom( type )
            || ConfigVersionString.class.isAssignableFrom( type ) || ConfigExecutableMode.class.isAssignableFrom( type ) || ConfigJarExeMode.class
            .isAssignableFrom( type ) );
    }

    public Object fromConfiguration( ConverterLookup converterLookup, PlexusConfiguration configuration, Class type,
                                    Class baseType, ClassLoader classLoader, ExpressionEvaluator expressionEvaluator,
                                    ConfigurationListener listener )
        throws ComponentConfigurationException
    {

        Class implementation = getClassForImplementationHint( type, configuration, classLoader );

        Object retValue = null;

        try
        {

            if ( implementation.isAssignableFrom( ConfigClassPath.class ) )
            {

                retValue = processClassPath( implementation, configuration, expressionEvaluator );

            }
            else if ( implementation.isAssignableFrom( ConfigJRESearchPath.class ) )
            {

                retValue = processJRESearchPath( implementation, configuration, expressionEvaluator );

            }
            else if ( implementation.isAssignableFrom( ConfigNativeLibraries.class ) )
            {

                retValue = processNativeLibraries( implementation, configuration, expressionEvaluator );

            }
            else if ( implementation.isAssignableFrom( ConfigStatusLine.class ) )
            {

                retValue = processTextLine( implementation, configuration, expressionEvaluator );

            }
            else if ( implementation.isAssignableFrom( ConfigVersionLine.class ) )
            {

                retValue = processTextLine( implementation, configuration, expressionEvaluator );

            }
            else if ( implementation.isAssignableFrom( ConfigVersionString.class ) )
            {

                retValue = processVersionString( implementation, configuration, expressionEvaluator );

            }
            else if ( implementation.isAssignableFrom( ConfigQuotedString.class ) )
            {

                retValue = processQuotedString( implementation, configuration, expressionEvaluator );

            }
            else if ( implementation.isAssignableFrom( ConfigJarExeMode.class ) )
            {

                retValue = processJarExeMode( implementation, configuration, expressionEvaluator );

            }
            else if ( implementation.isAssignableFrom( ConfigExecutableMode.class ) )
            {

                retValue = processExecutableMode( implementation, configuration, expressionEvaluator );

            }
        }
        catch ( PlexusConfigurationException e )
        {
            throw new ComponentConfigurationException( "Failed to parse configuration", e );
        }

        return retValue;
    }

    /**
     * @param implementation
     * @param configuration
     * @param expressionEvaluator
     * @return
     * @throws PlexusConfigurationException 
     */
    private Object processJarExeMode( Class implementation, PlexusConfiguration configuration,
                                     ExpressionEvaluator expressionEvaluator )
        throws PlexusConfigurationException
    {
        Object retValue = null;

        try
        {

            retValue = fromExpression( configuration, expressionEvaluator );

            if ( retValue instanceof String )
            {
                retValue = new ConfigJarExeMode( (String) retValue );
            }
            else
            {
                retValue = null;
            }

        }
        catch ( Exception e )
        {

            throw new PlexusConfigurationException( "Failed to process ConfigJarForExeMode configuration", e );

        }

        return retValue;

    }

    /**
     * @param implementation
     * @param configuration
     * @param expressionEvaluator
     * @return
     * @throws PlexusConfigurationException 
     */
    private Object processExecutableMode( Class implementation, PlexusConfiguration configuration,
                                         ExpressionEvaluator expressionEvaluator )
        throws PlexusConfigurationException
    {
        Object retValue = null;

        try
        {

            retValue = fromExpression( configuration, expressionEvaluator );

            if ( retValue instanceof String )
            {
                retValue = new ConfigExecutableMode( (String) retValue );
            }
            else
            {
                retValue = null;
            }

        }
        catch ( Exception e )
        {

            throw new PlexusConfigurationException( "Failed to process ConfigExecutableMode configuration", e );

        }

        return retValue;

    }

    /**
     * @param implementation
     * @param configuration
     * @param expressionEvaluator
     * @return
     * @throws PlexusConfigurationException 
     */
    private Object processQuotedString( Class implementation, PlexusConfiguration configuration,
                                       ExpressionEvaluator expressionEvaluator )
        throws PlexusConfigurationException
    {

        Object retValue = null;

        try
        {

            retValue = fromExpression( configuration, expressionEvaluator );

            if ( retValue instanceof String )
            {
                retValue = new ConfigQuotedString( (String) retValue );
            }
            else
            {
                retValue = null;
            }

        }
        catch ( Exception e )
        {

            throw new PlexusConfigurationException( "Failed to process ConfigQuotedString configuration", e );

        }

        return retValue;

    }

    /**
     * @param implementation
     * @param configuration
     * @param expressionEvaluator
     * @return
     * @throws PlexusConfigurationException 
     */
    private Object processVersionString( Class implementation, PlexusConfiguration configuration,
                                        ExpressionEvaluator expressionEvaluator )
        throws PlexusConfigurationException
    {

        Object retValue = null;

        try
        {

            retValue = fromExpression( configuration, expressionEvaluator );

            if ( retValue instanceof String )
            {
                retValue = new ConfigVersionString( (String) retValue );
            }
            else
            {
                retValue = null;
            }

        }
        catch ( Exception e )
        {

            throw new PlexusConfigurationException( "Failed to process ConfigVersionString configuration", e );

        }

        return retValue;

    }

    /**
     * @param implementation
     * @param configuration
     * @param expressionEvaluator
     * @return
     * @throws PlexusConfigurationException 
     */
    private Object processTextLine( Class implementation, PlexusConfiguration configuration,
                                   ExpressionEvaluator expressionEvaluator )
        throws PlexusConfigurationException
    {

        // this one does not support defaults as its a complex type.

        ConfigTextLine instance = null;

        if ( configuration.getAttributeNames().length > 1 )
        {

            try
            {
                // instantiate object

                instance = (ConfigTextLine) instantiateObject( implementation );

                String cfgValue = configuration.getAttribute( "fontColour", "0.0.0" ).trim();

                Object evaluatedExpr = expressionEvaluator.evaluate( cfgValue );

                cfgValue = evaluatedExpr == null ? cfgValue : evaluatedExpr.toString();

                instance.setFontColour( cfgValue );

                cfgValue = configuration.getAttribute( "font", "Arial" ).trim();

                evaluatedExpr = expressionEvaluator.evaluate( cfgValue );

                cfgValue = evaluatedExpr == null ? cfgValue : evaluatedExpr.toString();

                instance.setFont( cfgValue );

                cfgValue = configuration.getAttribute( "fontSize", "8" ).trim();

                evaluatedExpr = expressionEvaluator.evaluate( cfgValue );

                cfgValue = evaluatedExpr == null ? cfgValue : evaluatedExpr.toString();

                instance.setFontSize( TypeFormat.parseInt( cfgValue ) );

                cfgValue = configuration.getAttribute( "fontWeight", "500" ).trim();

                evaluatedExpr = expressionEvaluator.evaluate( cfgValue );

                cfgValue = evaluatedExpr == null ? cfgValue : evaluatedExpr.toString();

                instance.setFontWeight( TypeFormat.parseInt( cfgValue ) );

                if ( ( cfgValue = configuration.getAttribute( "text" ) ) != null )
                {
                    evaluatedExpr = expressionEvaluator.evaluate( cfgValue );

                    cfgValue = evaluatedExpr == null ? cfgValue : evaluatedExpr.toString();

                    instance.setText( cfgValue );
                }
                else
                {
                    throw new PlexusConfigurationException( "Missing required attribute 'text'" );
                }

                if ( ( cfgValue = configuration.getAttribute( "xPos" ) ) != null )
                {
                    evaluatedExpr = expressionEvaluator.evaluate( cfgValue );

                    cfgValue = evaluatedExpr == null ? cfgValue : evaluatedExpr.toString();

                    instance.setXPos( TypeFormat.parseInt( cfgValue ) );
                }
                else
                {
                    throw new PlexusConfigurationException( "Missing required attribute 'xPos'" );
                }

                if ( ( cfgValue = configuration.getAttribute( "yPos" ) ) != null )
                {
                    evaluatedExpr = expressionEvaluator.evaluate( cfgValue );

                    cfgValue = evaluatedExpr == null ? cfgValue : evaluatedExpr.toString();

                    instance.setYPos( TypeFormat.parseInt( cfgValue ) );
                }
                else
                {
                    throw new PlexusConfigurationException( "Missing required attribute 'yPos'" );
                }

            }
            catch ( Exception e )
            {

                throw new PlexusConfigurationException( "Failed to process ConfigTextLine configuration", e );

            }

        }
        return instance;
    }

    /**
     * @param implementation
     * @param configuration
     * @param expressionEvaluator
     * @return
     * @throws PlexusConfigurationException 
     */
    private Object processNativeLibraries( Class implementation, PlexusConfiguration configuration,
                                          ExpressionEvaluator expressionEvaluator )
        throws PlexusConfigurationException
    {

        ConfigNativeLibraries instance = null;

        try
        {

            PlexusConfiguration children[] = configuration.getChildren();

            if ( ( children != null ) && ( children.length > 0 ) )
            {

                // instantiate object

                instance = (ConfigNativeLibraries) instantiateObject( implementation );

                for ( int i = 0; i < children.length; i++ )
                {

                    // instantiate child location objects based on their config

                    PlexusConfiguration childConfig = children[i];

                    // location value

                    String cfgValue = childConfig.getValue();

                    Object evaluatedExpr = expressionEvaluator.evaluate( cfgValue );

                    cfgValue = evaluatedExpr == null ? cfgValue : evaluatedExpr.toString();

                    instance.addLocation( new ConfigNativeLibraries.NativeLibraryLocation( cfgValue ) );

                }
            }
        }
        catch ( Exception e )
        {

            throw new PlexusConfigurationException( "Failed to process ConfigTextLine configuration", e );

        }

        return instance;
    }

    /**
     * @param implementation
     * @param configuration
     * @param expressionEvaluator
     * @return
     * @throws PlexusConfigurationException 
     */
    private Object processJRESearchPath( Class implementation, PlexusConfiguration configuration,
                                        ExpressionEvaluator expressionEvaluator )
        throws PlexusConfigurationException
    {
        ConfigJRESearchPath instance = null;

        try
        {
            PlexusConfiguration children[] = configuration.getChildren();

            if ( ( children != null ) && ( children.length > 0 ) )
            {

                // instantiate object

                instance = (ConfigJRESearchPath) instantiateObject( implementation );

                for ( int i = 0; i < children.length; i++ )
                {

                    // instantiate child location objects based on their config

                    PlexusConfiguration childConfig = children[i];

                    String type = childConfig.getName();

                    // will barf if invalid type

                    ConfigJRESearchPath.JREPathLocation newLocation = new ConfigJRESearchPath.JREPathLocation( type );

                    // location value

                    String cfgValue = childConfig.getValue();

                    Object evaluatedExpr = expressionEvaluator.evaluate( cfgValue );

                    cfgValue = evaluatedExpr == null ? cfgValue : evaluatedExpr.toString();

                    newLocation.setValue( cfgValue );

                    instance.addLocation( newLocation );

                }
            }
        }
        catch ( Exception e )
        {

            throw new PlexusConfigurationException( "Failed to process ConfigJRESearchPath configuration", e );

        }

        return instance;
    }

    /**
     * @param evaluatedExpressionValue
     * @param implementation 
     * @param configuration
     * @param expressionEvaluator
     * @return
     * @throws PlexusConfigurationException 
     */
    private Object processClassPath( Class implementation, PlexusConfiguration configuration,
                                    ExpressionEvaluator expressionEvaluator )
        throws PlexusConfigurationException
    {

        ConfigClassPath instance = null;

        try
        {
            PlexusConfiguration children[] = configuration.getChildren();

            if ( ( children != null ) && ( children.length > 0 ) )
            {

                // instantiate object

                instance = (ConfigClassPath) instantiateObject( implementation );

                for ( int i = 0; i < children.length; i++ )
                {

                    // instantiate child location objects based on their config

                    PlexusConfiguration childConfig = children[i];

                    String type = childConfig.getName();

                    // will barf if invalid type

                    ConfigClassPath.ClassPathLocation newLocation = new ConfigClassPath.ClassPathLocation( type );

                    // fail not found value                                

                    String cfgValue = childConfig.getAttribute( "failIfNotFound", "true" ).trim();

                    Object evaluatedExpr = expressionEvaluator.evaluate( cfgValue );

                    cfgValue = evaluatedExpr == null ? cfgValue : evaluatedExpr.toString();

                    newLocation.setFailIfNotFound( TypeFormat.parseBoolean( cfgValue ) );

                    // location value

                    cfgValue = childConfig.getValue();

                    evaluatedExpr = expressionEvaluator.evaluate( cfgValue );

                    cfgValue = evaluatedExpr == null ? cfgValue : evaluatedExpr.toString();

                    newLocation.setValue( cfgValue );

                    instance.addLocation( newLocation );

                }
            }
        }
        catch ( Exception e )
        {

            throw new PlexusConfigurationException( "Failed to process ConfigClassPath configuration", e );

        }

        return instance;
    }

}