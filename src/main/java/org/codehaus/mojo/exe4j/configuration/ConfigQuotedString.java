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
 * String wrapper that converts any contained quotes '"' into
 * XML quote entities 
 * 
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 */
public class ConfigQuotedString
{
    private String value;

    public ConfigQuotedString()
    {
    }

    public ConfigQuotedString( String value )
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
     * value mutator, replaces contained quotes '"' with their xml
     * entity notations: &amp;quot;
     * @param value The value to set.
     */
    public void setValue( String value )
    {
        this.value = value.replaceAll( "\\\"", "&quot;" );
        ;
    }

}
