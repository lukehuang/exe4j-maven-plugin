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
 * The Configuration for ClassPath
 * 
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 */
public class ConfigClassPath
{
    /**
     * ConfigClassPath is made up of ClassPathLocation objects that
     * represent the different location types.
     */
    public static class ClassPathLocation
    {
        /** 
         * a scan directory class path location type
         */
        public static final String SCAN_DIRECTORY_TYPE = "scanDirectory";

        /** 
         * a directory class path location type
         */
        public static final String DIRECTORY_TYPE = "directory";

        /** 
         * an archive class path location type
         */
        public static final String ARCHIVE_TYPE = "archive";

        /** 
         * an envar class path location type
         */
        public static final String ENVVAR_TYPE = "envVar";

        /**
         * the legal type values
         */
        private static final String[] LEGAL_TYPE_VALUES = {
            SCAN_DIRECTORY_TYPE,
            DIRECTORY_TYPE,
            ARCHIVE_TYPE,
            ENVVAR_TYPE };

        /**
         * the classpath entry type. 
         * @see LEGAL_TYPE_VALUES for details of the legal values this attribute can hold 
         */
        private String type;

        /**
         * the element value
         */
        private String value;

        /**
         * fail if not found, not relivant for envVar types
         */
        private boolean failIfNotFound;

        /**
         * Specialised constructor
         * 
         * @param type the type of classpath location
         * @throws PlexusConfigurationException raised if invalid type passed
         * @see isLegalType() 
         */
        public ClassPathLocation( String type )
            throws PlexusConfigurationException
        {
            setType( type );
        }

        /**
         * Specialised constructor
         * 
         * @param type the type of classpath location, see legalTypeValues
         * @param value the classpath location
         * @throws PlexusConfigurationException raised if invalid type passed
         * @see isLegalType() 
         */
        public ClassPathLocation( String type, String value )
            throws PlexusConfigurationException
        {
            setType( type );
            this.value = value;
        }

        /**
         * Specialised constructor
         * 
         * @param found fail if the classpath location is not found 
         *              (not relivant for envVar types)
         * @param type the type of classpath location, see legalTypeValues
         * @param value the classpath location/value
         * @throws PlexusConfigurationException raised if invalid type passed
         */
        public ClassPathLocation( String type, String value, boolean found )
            throws PlexusConfigurationException
        {
            setType( type );
            this.value = value;
            this.failIfNotFound = found;
        }

        /**
         * failIfNotFound accessor
         * 
         * @return the fail not found member
         */
        public boolean isFailIfNotFound()
        {
            return failIfNotFound;
        }

        /**
         * failIfNotFound mutator
         * 
         * @param failIfNotFound
         */
        public void setFailIfNotFound( boolean failIfNotFound )
        {
            this.failIfNotFound = failIfNotFound;
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
                throw new PlexusConfigurationException( "type '" + type + " is not a legal classPath location type" );
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
            if ( ENVVAR_TYPE.equals( type ) )
            {
                return "<envVar name=\"" + value + "\"/>";
            }
            else
            {
                StringBuffer buffer = new StringBuffer();

                buffer.append( "<" );
                buffer.append( type );
                buffer.append( " location=\"" );
                buffer.append( value.replaceAll( "\\\"", "&quot;" ) );
                buffer.append( "\"" );
                buffer.append( " failOnError=\"" );
                buffer.append( failIfNotFound );
                buffer.append( "\"" );
                buffer.append( "/>" );

                return buffer.toString();
            }
        }
    }

    private List locations;

    public ConfigClassPath()
    {
        this.locations = new ArrayList();
    }

    public void addLocation( ClassPathLocation location )
    {
        this.locations.add( location );
    }

    public String toString()
    {

        StringBuffer buffer = new StringBuffer();

        buffer.append( "<classPath>" );

        Iterator itr = locations.iterator();

        while ( itr.hasNext() )
        {
            ClassPathLocation location = (ClassPathLocation) itr.next();

            buffer.append( location.toString() );

        }

        buffer.append( "</classPath>" );

        return buffer.toString();

    }
}
