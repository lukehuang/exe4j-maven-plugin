/**
 * 
 */
package org.codehaus.mojo.exe4j.configuration;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 *
 */
public class ConfigVersionStringTest
    extends TestCase
{

    /*
     * Test method for 'org.codehaus.mojo.exe4j.configuration.ConfigVersionString.ConfigVersionString(String)'
     */
    public final void testConfigVersionStringString()
    {
        String expected = "1.0.0.0";
        ConfigVersionString actual = new ConfigVersionString( "1.a.b.c" );
        assertEquals( expected, actual.toString() );
    }

}
