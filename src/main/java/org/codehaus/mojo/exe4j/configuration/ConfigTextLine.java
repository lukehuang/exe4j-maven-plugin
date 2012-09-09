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
 * Base class for the Mojo configuration parameter 
 * StatusLine and VersionLine
 * 
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 */
public abstract class ConfigTextLine
{

    private String text = "";

    private int xPos = 0;

    private int yPos = 0;

    private String font = "Arial";

    private int fontWeight = 500;

    private int fontSize = 8;

    private String fontColour = "0.0.0";

    protected ConfigTextLine()
    {
    }

    /**
     * Constructor
     * 
     * @param fontColour the font colour
     * @param fontName the font name
     * @param fontSize the font size 
     * @param fontWeight the font weight
     * @param text the text to use
     * @param xPos the x position
     * @param yPos the y position
     */
    protected ConfigTextLine( String fontColour, String fontName, int fontSize, int fontWieght, String text, int xPos,
                             int yPos )
    {
        this.fontColour = fontColour;
        this.font = fontName;
        this.fontSize = fontSize;
        this.fontWeight = fontWieght;
        this.text = text;
        this.xPos = xPos;
        this.yPos = yPos;
    }

    /**
     * @return the Exe4J XML attributes representation of this
     * text line object. 
     */
    protected String getAttributes()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append( "x=\"" );
        buffer.append( xPos );
        buffer.append( "\" y=\"" );
        buffer.append( yPos );
        buffer.append( "\" text=\"" );
        buffer.append( this.text.replaceAll( "\\\"", "\"" ) );
        buffer.append( "\" font=\"" );
        buffer.append( font );
        buffer.append( "\" fontSize=\"" );
        buffer.append( fontSize );
        buffer.append( "\" fontColor=\"" );
        buffer.append( fontColour );
        buffer.append( "\" fontWeight=\"" );
        buffer.append( fontWeight );
        buffer.append( "\"" );

        return buffer.toString();
    }

    /**
     * font accessor
     * @return Returns the font.
     */
    public String getFont()
    {
        return font;
    }

    /**
     * font mutator
     * @param font The font to set.
     */
    public void setFont( String font )
    {
        this.font = font;
    }

    /**
     * fontColour accessor
     * @return Returns the fontColour.
     */
    public String getFontColour()
    {
        return fontColour;
    }

    /**
     * fontColour mutator
     * @param fontColour The fontColour to set.
     */
    public void setFontColour( String fontColour )
    {
        this.fontColour = fontColour;
    }

    /**
     * fontSize accessor
     * @return Returns the fontSize.
     */
    public int getFontSize()
    {
        return fontSize;
    }

    /**
     * fontSize mutator
     * @param fontSize The fontSize to set.
     */
    public void setFontSize( int fontSize )
    {
        this.fontSize = fontSize;
    }

    /**
     * fontWeight accessor
     * @return Returns the fontWeight.
     */
    public int getFontWeight()
    {
        return fontWeight;
    }

    /**
     * fontWeight mutator
     * @param fontWeight The fontWeight to set.
     */
    public void setFontWeight( int fontWeight )
    {
        this.fontWeight = fontWeight;
    }

    /**
     * text accessor
     * @return Returns the text.
     */
    public String getText()
    {
        return text;
    }

    /**
     * text mutator
     * @param text The text to set.
     */
    public void setText( String text )
    {
        this.text = text;
    }

    /**
     * xPos accessor
     * @return Returns the xPos.
     */
    public int getXPos()
    {
        return xPos;
    }

    /**
     * xPos mutator
     * @param pos The xPos to set.
     */
    public void setXPos( int pos )
    {
        xPos = pos;
    }

    /**
     * yPos accessor
     * @return Returns the yPos.
     */
    public int getYPos()
    {
        return yPos;
    }

    /**
     * yPos mutator
     * @param pos The yPos to set.
     */
    public void setYPos( int pos )
    {
        yPos = pos;
    }

}
