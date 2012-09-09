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

/**
 * Config object that represents a Windows .exe version info string
 * (must be 4 numerics seperated by periods)
 * 
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 */
public class ConfigVersionString
{
    private String value;

    public ConfigVersionString()
    {
    }

    public ConfigVersionString( String value )
    {
        setValue( value );
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
     */
    public void setValue( String value )
    {
        String[] versionElements = value.split( "\\." );

        StringBuffer formattedVersion = new StringBuffer();

        for ( int i = 0; i < 4; i++ )
        {
            if ( versionElements.length > i )
            {
                String token = versionElements[i].trim();

                try
                {
                    int version = Integer.parseInt( token );

                    formattedVersion.append( Integer.toString( version ) );
                }
                catch ( NumberFormatException e )
                {
                    formattedVersion.append( "0" );
                }
            }
            else
            {
                formattedVersion.append( "0" );
            }

            if ( i < 3 )
            {
                formattedVersion.append( "." );
            }
        }

        this.value = formattedVersion.toString();
    }
}
