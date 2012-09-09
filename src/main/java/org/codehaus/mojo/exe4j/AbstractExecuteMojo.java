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

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.codehaus.mojo.exe4j.tasks.CompilerTask;

/**
 * Base class for exe4j mojos
 * 
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 */
public abstract class AbstractExecuteMojo
    extends AbstractMojo
{
    /**
     * The exe4j CLI executable file, default assumes its on the system path.
     * 
     * @parameter expression="${exe4jcExe}" default-value="exe4jc.exe"
     * @required
     */
    private String exe4jcExe;

    /**
     * Do a trial run, this causes the '--test' CLI argument to be passed to the
     * exe4jc compiler.
     * 
     * @parameter expression="${trialRun}" default-value="false"
     * @required
     */
    private boolean trialRun;

    /**
     * Runs the exe4j compiler task using the supplied config file
     * 
     * @param configFile the exe4j XMl config file
     * @throws MojoExecutionException
     */
    public void runExe4J( File configFile )
        throws MojoExecutionException
    {

        if ( !configFile.canRead() )
        {
            throw new MojoExecutionException( "The exe4j config file cannot be read" );
        }

        // invoke exe4j executor

        CompilerTask executor = new CompilerTask( configFile.getAbsolutePath(), exe4jcExe, getLog() );

        executor.setVerbose( getLog().isDebugEnabled() );

        executor.setTrialRun( trialRun );

        executor.execute();

    }

}