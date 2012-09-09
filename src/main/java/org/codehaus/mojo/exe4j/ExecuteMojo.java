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

package org.codehaus.mojo.exe4j;

import java.io.File;

import org.apache.maven.plugin.MojoExecutionException;

/**
 * Runs the exe4j assmbler against the specified exe4j config file. Refer to
 * http://www.ej-technologies.com/index.html for more details regarding the
 * exe4j java exe assembler 
 * 
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 * @goal execute
 * @phase package
 * @requiresDependencyResolution test
 */
public class ExecuteMojo
    extends AbstractExecuteMojo
{
    /**
     * The exe4j config file is to be passed to the exe4j compiler.
     *  
     * @parameter expression="${configFile}"
     * @required
     */
    private File configFile;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException
    {

        if ( !configFile.canRead() )
        {
            throw new MojoExecutionException( "The exe4j config file '" + configFile.getAbsolutePath()
                + "' cannot be read" );
        }

        runExe4J( configFile );

    }

}