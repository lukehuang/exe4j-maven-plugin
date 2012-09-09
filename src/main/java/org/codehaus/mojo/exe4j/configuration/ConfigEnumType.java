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

package org.codehaus.mojo.exe4j.configuration;

import java.util.HashMap;
import java.util.Map;

import org.codehaus.plexus.configuration.PlexusConfigurationException;

/**
 * Helper class that provides a set of constrained name-value
 * tuples, ie a enumerated value type. 
 * 
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 */
public class ConfigEnumType
{
    /**
     * Represents a enumerated value.
     * 
     * @author John
     */
    public static class Value
    {
        /**
         * the enum identifier 
         */
        public Object identifier;

        /**
         * the enum value
         */
        public Object value;

        /**
         * constructor
         *
         * @param identifier the identifier
         * @param value the enum value
         */
        public Value( Object identifier, Object value )
        {
            this.identifier = identifier;
            this.value = value;
        }

        /** 
         * @return the enum value
         */
        public String toString()
        {
            return value.toString();
        }

        /**
         * identifier accessor
         * @return Returns the identifier.
         */
        public Object getIdentifier()
        {
            return identifier;
        }

        /**
         * identifier mutator
         * @param identifier The identifier to set.
         */
        public void setIdentifier( Object identifier )
        {
            this.identifier = identifier;
        }

        /**
         * value accessor
         * @return Returns the value.
         */
        public Object getValue()
        {
            return value;
        }

        /**
         * value mutator
         * @param value The value to set.
         */
        public void setValue( Object value )
        {
            this.value = value;
        }

    }

    /**
     * the map of legal enum Values, indexed by Value.identifier
     */
    private Map enums = new HashMap();

    public ConfigEnumType()
    {
    }

    /**
     * constructor
     *
     * @param definition array of EnumType.Value
     */
    public ConfigEnumType( Value[] definition )
    {
        for ( int i = 0; i < definition.length; i++ )
        {
            enums.put( definition[i].identifier, definition[i].value );
        }
    }

    /**
     * checks if the supplied identifier represents a legal enum
     * 
     * @param value the identifier
     * @return true if a legal enum type
     */
    public boolean isLegal( Value value )
    {
        return enums.containsKey( value.identifier ) && enums.get( value.identifier ).equals( value.value );
    }

    /**
     * returns the Enum value field for the specified identifier
     * @param identifier the Enum identifier
     * @return the Enum value object
     * @throws Exe4JException if the supplied identifier is not a legal enum
     */
    public Object getValue( Object identifier )
        throws PlexusConfigurationException
    {
        Object value = enums.get( identifier );
        if ( value == null )
        {
            throw new PlexusConfigurationException( "enumerated type does not support identifier: "
                + identifier.toString() );
        }
        return value;
    }

}
