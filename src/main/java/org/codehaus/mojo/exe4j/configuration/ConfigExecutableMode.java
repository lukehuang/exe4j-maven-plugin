/*
 * Copyright 2005 John H Allen.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * 
 */
package org.codehaus.mojo.exe4j.configuration;

import org.codehaus.plexus.configuration.PlexusConfigurationException;

/**
 * Configuration class for the executable mode.
 * 
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 */
public class ConfigExecutableMode
{
    /**
     * Defines the legal values that the executable mode can hold.
     */
    private static final ConfigEnumType TYPE_DEFINITION = new ConfigEnumType( new ConfigEnumType.Value[] {
        new ConfigEnumType.Value( "gui", "1" ),
        new ConfigEnumType.Value( "console", "2" ),
        new ConfigEnumType.Value( "service", "3" ) } );

    private String value;

    public ConfigExecutableMode()
    {
        super();
    }

    
    /**
     * constructor that initialise the config class to the supplied 
     * ConfigEnum identified by the supplied identifier
     *
     * @param identifier the enum value, see TYPE_DEFINITION for details
     * @throws PlexusConfigurationException
     */
    public ConfigExecutableMode( String identifier )
        throws PlexusConfigurationException
    {
        super();
        setValue( identifier );
    }

    public String toString()
    {
        return value;
    }

    /**
     * value accessor
     * @return Returns the value.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * value mutator
     * @param value The value to set.
     * @throws PlexusConfigurationException 
     */
    public final void setValue( final String identifier )
        throws PlexusConfigurationException
    {
        this.value = TYPE_DEFINITION.getValue( identifier ).toString();
    }

}
