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

/**
 * Mojo configuration parameter for the Exe4J StatusLine 
 * property
 * 
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 */
public class ConfigStatusLine
    extends ConfigTextLine
{
    /**
     * Default constructor
     */
    public ConfigStatusLine()
    {
        super( "0.0.0", "Arial", 8, 500, "", 20, 20 );
    }

    /**
     * specialised constructor
     * 
     * @see ConfigTextLine for details
     *
     * @param fontColour
     * @param fontName
     * @param fontSize
     * @param fontWieght
     * @param text 
     * @param xPos
     * @param yPos
     */
    public ConfigStatusLine( String fontColour, String fontName, int fontSize, int fontWieght, String text, int xPos,
                            int yPos )
    {
        super( fontColour, fontName, fontSize, fontWieght, text, xPos, yPos );
    }

    /** 
     * nasty overloading of the Object::toString method but there you go.
     * 
     * @return the EXE4J XML configuration representation of the parameter 
     */
    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append( "<statusLine " );
        buffer.append( getAttributes() );
        buffer.append( "/>" );

        return buffer.toString();
    }
}
