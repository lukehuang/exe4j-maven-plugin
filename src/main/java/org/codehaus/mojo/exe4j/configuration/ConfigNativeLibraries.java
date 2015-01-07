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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.codehaus.mojo.exe4j.configuration.ConfigJRESearchPath.JREPathLocation;

/**
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 */
public class ConfigNativeLibraries
{
    /**
     * ConfigNativeLibraries is made up of NativeLibraryLocation objects that
     * represent the different location types.
     */
    public static class NativeLibraryLocation
    {
        /**
         * the location value
         */
        private String value;

        /**
         * Default constructor
         */
        public NativeLibraryLocation()
        {
        }

        /**
         * Specialised constructor
         * 
         * @param value the location
         */
        public NativeLibraryLocation( String value )
        {
            this.value = value;
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
            return "<directory name=\"" + value + "\"/>";
        }
    }

    private List locations;

    public ConfigNativeLibraries()
    {
        this.locations = new ArrayList();
    }

    public void addLocation( NativeLibraryLocation location )
    {
        this.locations.add( location );
    }

    public String toString()
    {

        StringBuffer buffer = new StringBuffer();

        buffer.append( "<nativeLibraryDirectories>" );

        Iterator itr = locations.iterator();

        while ( itr.hasNext() )
        {
            NativeLibraryLocation location = (NativeLibraryLocation) itr.next();

            buffer.append( location.toString() );
        }

        buffer.append( "</nativeLibraryDirectories>" );

        return buffer.toString();

    }

}
