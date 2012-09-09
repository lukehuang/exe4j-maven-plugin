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

package org.codehaus.mojo.exe4j.tasks;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.logging.Log;
import org.codehaus.plexus.util.cli.CommandLineException;
import org.codehaus.plexus.util.cli.CommandLineUtils;
import org.codehaus.plexus.util.cli.Commandline;
import org.codehaus.plexus.util.cli.DefaultConsumer;

/**
 * Simple Exe4J CLI wrapper class 
 * 
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 */
public class CompilerTask
{
    /**
     * the Exe4J CLI location
     */
    private String exe4jcFile = "exe4jc.exe";

    /**
     * The working directory
     */
    private String workingDirectory = ".";

    /**
     * the Exe4J configuration file
     */
    private String configFile = "application.exe4j";

    /**
     * A MavenProject logger (lame coupling)
     */
    private Log log;

    /**
     * Instructs exe4jc to be more verbose
     */
    private boolean verbose = false;

    /**
     * Instructs exe4jc to be quiet
     */
    private boolean quiet = false;

    /**
     * If set overrides the output directory specified in the config
     * file
     */
    private String outputDirectoryOverride;

    /**
     * If set overrides the version specified in the config file
     */
    private String versionOverride;

    /**
     * Instructs exe4jc to perform a dummy run and not actually generate
     * the exe4j java wrapper exe
     */
    private boolean trialRun = false;

    /**
     * constructor
     *
     * @param configFile config file
     * @param exe4jcFile exe4jc location
     * @param log logger to use
     */
    public CompilerTask( String configFile, String exe4jcFile, Log log )
    {
        super();

        this.configFile = configFile;
        this.exe4jcFile = exe4jcFile;
        this.log = log;
    }

    /**
     * constructor
     *
     * @param configFile config file
     * @param exe4jcFile exe4jc location
     * @param workingDir working directory
     * @param log logger to use
     */
    public CompilerTask( String configFile, String exe4jcFile, String workingDir, Log log )
    {
        super();

        this.configFile = configFile;
        this.exe4jcFile = exe4jcFile;
        this.workingDirectory = workingDir;
        this.log = log;
    }

    /**
     * run the exe4jc compiler
     * 
     * @throws MojoExecutionException raised if an error is encountered
     */
    public void execute()
        throws MojoExecutionException
    {
        Commandline cmd = new Commandline();

        cmd.setWorkingDirectory( workingDirectory );
        cmd.setExecutable( exe4jcFile );

        if ( quiet )
        {
            cmd.createArgument().setValue( "--quiet" );
        }
        else if ( verbose )
        {
            cmd.createArgument().setValue( "--verbose" );
        }
        if ( outputDirectoryOverride != null )
        {
            cmd.createArgument().setValue( "--destination " + outputDirectoryOverride );
        }
        if ( versionOverride != null )
        {
            cmd.createArgument().setValue( "--release " + versionOverride );
        }
        if ( trialRun )
        {
            cmd.createArgument().setValue( "--test" );
        }

        cmd.createArgument().setValue( configFile );

        log.debug( "Executing: " + Commandline.toString( cmd.getCommandline() ) );

        CommandLineUtils.StringStreamConsumer err = new CommandLineUtils.StringStreamConsumer();

        try
        {
            int exitCode = CommandLineUtils.executeCommandLine( cmd, new DefaultConsumer(), err );

            if ( exitCode != 0 )
            {
                throw new MojoExecutionException( "exe4jc exited with code: " + exitCode + ", output: "
                    + err.getOutput() );
            }
        }
        catch ( CommandLineException e )
        {
            throw new MojoExecutionException( "Unable to execute exe4jc command", e );
        }

    }

    /**
     * configFile accessor
     * @return Returns the configFile.
     */
    public String getConfigFile()
    {
        return configFile;
    }

    /**
     * configFile mutator
     * @param configFile The configFile to set.
     */
    public void setConfigFile( String configFile )
    {
        this.configFile = configFile;
    }

    /**
     * exe4jcFile accessor
     * @return Returns the exe4jcFile.
     */
    public String getExe4jcFile()
    {
        return exe4jcFile;
    }

    /**
     * exe4jcFile mutator
     * @param exe4jcFile The exe4jcFile to set.
     */
    public void setExe4jcFile( String exe4jcFile )
    {
        this.exe4jcFile = exe4jcFile;
    }

    /**
     * log accessor
     * @return Returns the log.
     */
    public Log getLog()
    {
        return log;
    }

    /**
     * log mutator
     * @param log The log to set.
     */
    public void setLog( Log log )
    {
        this.log = log;
    }

    /**
     * outputDirectoryOverride accessor
     * @return Returns the outputDirectoryOverride.
     */
    public String getOutputDirectoryOverride()
    {
        return outputDirectoryOverride;
    }

    /**
     * outputDirectoryOverride mutator
     * @param outputDirectoryOverride The outputDirectoryOverride to set.
     */
    public void setOutputDirectoryOverride( String outputDirectoryOverride )
    {
        this.outputDirectoryOverride = outputDirectoryOverride;
    }

    /**
     * quiet accessor
     * @return Returns the quiet.
     */
    public boolean isQuiet()
    {
        return quiet;
    }

    /**
     * quiet mutator
     * @param quiet The quiet to set.
     */
    public void setQuiet( boolean quiet )
    {
        this.quiet = quiet;
    }

    /**
     * trialRun accessor
     * @return Returns the trialRun.
     */
    public boolean isTrialRun()
    {
        return trialRun;
    }

    /**
     * trialRun mutator
     * @param trialRun The trialRun to set.
     */
    public void setTrialRun( boolean trialRun )
    {
        this.trialRun = trialRun;
    }

    /**
     * verbose accessor
     * @return Returns the verbose.
     */
    public boolean isVerbose()
    {
        return verbose;
    }

    /**
     * verbose mutator
     * @param verbose The verbose to set.
     */
    public void setVerbose( boolean verbose )
    {
        this.verbose = verbose;
    }

    /**
     * versionOverride accessor
     * @return Returns the versionOverride.
     */
    public String getVersionOverride()
    {
        return versionOverride;
    }

    /**
     * versionOverride mutator
     * @param versionOverride The versionOverride to set.
     */
    public void setVersionOverride( String versionOverride )
    {
        this.versionOverride = versionOverride;
    }

    /**
     * workingDirectory accessor v
     * @return Returns the workingDirectory.
     */
    public String getWorkingDirectory()
    {
        return workingDirectory;
    }

    /**
     * workingDirectory mutator
     * @param workingDirectory The workingDirectory to set.
     */
    public void setWorkingDirectory( String workingDirectory )
    {
        this.workingDirectory = workingDirectory;
    }

}
