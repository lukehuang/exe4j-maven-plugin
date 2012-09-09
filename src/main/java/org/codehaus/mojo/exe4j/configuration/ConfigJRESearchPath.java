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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.plexus.configuration.PlexusConfigurationException;

/**
 * The Configuration for JRESearchPath
 * 
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 */
public class ConfigJRESearchPath
{
    /**
     * ConfigJRESearchPath is made up of JREPathLocation objects that
     * represent the different location types.
     */
    public static class JREPathLocation
    {
        /** 
         * a registry jre search path location type
         */
        public static final String REGISTRY_TYPE = "registry";

        /** 
         * a directory jre search path location type
         */
        public static final String DIRECTORY_TYPE = "directory";

        /** 
         * an envar jre search path location type
         */
        public static final String ENVVAR_TYPE = "envVar";

        /**
         * the legal type values
         */
        private static final String[] LEGAL_TYPE_VALUES = { REGISTRY_TYPE, DIRECTORY_TYPE, ENVVAR_TYPE };

        /**
         * the jre search path entry type. 
         * @see LEGAL_TYPE_VALUES for details of the legal values this attribute can hold 
         */
        private String type;

        /**
         * the element value
         */
        private String value;

        /**
         * Specialised constructor
         * 
         * @param type the type of location
         * @throws PlexusConfigurationException raised if invalid type passed
         * @see isLegalType() 
         */
        public JREPathLocation( String type )
            throws PlexusConfigurationException
        {
            setType( type );
        }

        /**
         * Specialised constructor
         * 
         * @param type the type of location
         * @param value the location
         * @throws PlexusConfigurationException raised if invalid type passed
         * @see isLegalType() 
         */
        public JREPathLocation( String type, String value )
            throws PlexusConfigurationException
        {
            setType( type );
            this.value = value;
        }

        /**
         * type accessor
         * 
         * @return type member
         */
        public String getType()
        {
            return type;
        }

        /**
         * type mutator
         * 
         * @param type the type setting, must be a type as defined by legalTypeValues 
         * @throws PlexusConfigurationException raised if invalid type passed
         */
        public void setType( String type )
            throws PlexusConfigurationException
        {
            if ( !isLegalType( type ) )
            {
                throw new PlexusConfigurationException( "type '" + type + " is not a legal jreSearchPath location type" );
            }

            this.type = type;

        }

        public boolean isLegalType( String type )
        {
            boolean legal = false;
            for ( int i = 0; i < LEGAL_TYPE_VALUES.length; i++ )
            {
                if ( LEGAL_TYPE_VALUES[i].equals( type ) )
                {
                    legal = true;
                    break;
                }
            }
            return legal;
        }

        /**
         * value accessor
         * 
         * @return value
         */
        public String getValue()
        {
            return value;
        }

        /**
         * value mutator
         * 
         * @param value the value
         */
        public void setValue( String value )
        {
            this.value = value;
        }

        /** 
         * nasty overloading of the Object::toString method but there you go.
         * 
         * @return the EXE4J XML configuration representation of the parameter 
         */
        public String toString()
        {
            if ( REGISTRY_TYPE.equals( type ) )
            {
                return "<registry/>";
            }
            else if ( DIRECTORY_TYPE.equals( type ) )
            {
                return "<directory location=\"" + value + "\"/>";
            }
            else
            {
                return "<envVar name=\"" + value + "\"/>";
            }
        }
    }

    private List locations;

    public ConfigJRESearchPath()
    {
        this.locations = new ArrayList();
    }

    public void addLocation( JREPathLocation location )
    {
        this.locations.add( location );
    }

    public String toString()
    {

        StringBuffer buffer = new StringBuffer();

        buffer.append( "<searchSequence>" );

        Iterator itr = locations.iterator();

        while ( itr.hasNext() )
        {
            JREPathLocation location = (JREPathLocation) itr.next();

            buffer.append( location.toString() );

        }

        buffer.append( "</searchSequence>" );

        return buffer.toString();

    }
}
