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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Dictionary;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.codehaus.mojo.exe4j.configuration.ConfigClassPath;
import org.codehaus.mojo.exe4j.configuration.ConfigExecutableMode;
import org.codehaus.mojo.exe4j.configuration.ConfigJRESearchPath;
import org.codehaus.mojo.exe4j.configuration.ConfigJarExeMode;
import org.codehaus.mojo.exe4j.configuration.ConfigNativeLibraries;
import org.codehaus.mojo.exe4j.configuration.ConfigQuotedString;
import org.codehaus.mojo.exe4j.configuration.ConfigStatusLine;
import org.codehaus.mojo.exe4j.configuration.ConfigVersionLine;
import org.codehaus.mojo.exe4j.configuration.ConfigVersionString;
import org.codehaus.mojo.exe4j.configuration.ConfigJRESearchPath.JREPathLocation;
import org.codehaus.plexus.configuration.PlexusConfigurationException;

/**
 * Creates an exe4j assmbler config file and runs the exe4j compiler against it
 * to generate a Java application executable. Refer to
 * http://www.ej-technologies.com/index.html for more details regarding the
 * exe4j java exe assembler
 * 
 * @author <a href="mailto:john_h_allen@hotmail.com">John Allen</a>
 * @goal exe4j
 * @phase package
 * @requiresDependencyResolution test
 * @configurator override 
 */
public class Exe4JMojo
    extends AbstractExecuteMojo
{
    /**
     * resource directory location
     */
    private static final String RESOURCE_DIR = "/org/codehaus/mojo/exe4j/";

    /**
     * default exe4j config template file
     */
    private static final String DEFAULT_TEMPLATE = RESOURCE_DIR + "template-exe4j.xml";

    /**
     * Defines whether the JAR is external to the EXE (default mode) or
     * whether the JAR is compliled into the EXE. external mode is
     * suitable for all Java applications.
     * 
     * @parameter expression="${jarExeMode}" default-value="external"
     * @required
     */
    private ConfigJarExeMode jarExeMode;

    /**
     * Application's short name.
     * 
     * @parameter expression="${applicationShortName}"
     *            default-value="${project.artifactId}"
     * @required
     */
    private String applicationShortName;

    /**
     * The distribution source directory conatins is the reference point for
     * relative directories. If necessary, other specified directories will be
     * converted to be relative to this location.
     * 
     * @parameter expression="${distributionSourceDirectory}"
     *            default-value="${project.build.directory}"
     * @required
     */
    private File distributionSourceDirectory;

    /**
     * The generated executable will be copied to the executable directory. The
     * executable directory must be below the distribution source directory.
     * 
     * @parameter expression="${executableDirectory}" default-value="."
     * @required
     */
    private String executableDirectory;

    /**
     * Executable mode can be either "gui", "console" or "service".
     * 
     * gui: there is no terminal window associated with a GUI application, If
     * stdout and stderr are not redirected (see redirection), both streams are
     * inaccessible for the user. This corresponds to the behavior of javaw.exe
     * 
     * console: console application has an associated terminal window. If a
     * console application is opened from the Windows explorer, a new terminal
     * window is opened. If stdout and stderr are not redirected (see
     * redirection), both streams are printed on the terminal window. This
     * corresponds to the behavior of java.exe. Currently unsupported.
     * 
     * service: windows service runs independently of logged in users and can be
     * run even if no user is logged on. The main method will be called when the
     * service is started. The main method should return in a relatively short
     * time (about 20 seconds) to signal successful initialization to the
     * service manager. To handle the shutdown of the service, you can use the
     * Runtime.addShutdownHook() method to register a thread that will be
     * executed before the JVM is terminated. For information on how services
     * are installed or uninstalled, please see the help on service start
     * options. Currently unsupported.
     * 
     * @parameter expression="${executableMode}" default-value="gui"
     * @required
     */
    private ConfigExecutableMode executableMode;

    /**
     * Specifies the exe4j resources directory used as the default base location
     * for icon files and splash screen bitmaps, this is a mojo property not an
     * exe4j property.
     * 
     * @parameter expression="${resourcesDirectory}"
     *            default-value="${basedir}/src/exe4j/resources"
     * @required
     */
    private File resourcesDirectory;

    /**
     * Executable name. The desired executable name without the trailing .exe
     * extension
     * 
     * @parameter expression="${executableName}"
     *            default-value="${project.artifactId}"
     * @required
     */
    private String executableName;

    /**
     * Set to true to use an executable icon
     * 
     * @parameter expression="${useExecutableIcon}" default-value="false"
     * @required
     */
    private boolean useExecutableIcon;

    /**
     * If you would like a custom icon to be compiled into your executable,
     * supply an icon file (extension *.ico). Will default to
     * ${resourcesDirectory}/icons/${executableName}.ico if not supplied
     * 
     * @parameter expression="${executableIconFile}"
     */
    private File executableIconFile;

    /**
     * For some applications (especially GUI applications) you might want to
     * change the working directory to a specific directory relative to the
     * executable, for example to read config files that are in a fixed
     * location. To do so, set working directory to a directory relative to the
     * executableDirectory. To change the current directory to the same
     * directory where the executable is located, enter a single dot.
     * 
     * @parameter expression="${workingDirectory}" default-value="."
     * @required
     */
    private String workingDirectory;

    /**
     * If you true the generated exe4j executable can only be started once.
     * Subsequent user invocations will bring the application to the front. In
     * the Controller class of the exe4j launcher API you can register a startup
     * handler to receive the command line parameters. In this way, you can
     * handle file associations with a single application instance.
     * 
     * @parameter expression="${singleInstanceOnly}" default-value="false"
     * @required
     */
    private boolean singleInstanceOnly;

    /**
     * Executables created by exe4j can monitor whether the main method throws
     * an exception and show an error dialog in that case. This provides a
     * generic startup error notification facility for the developer that
     * handles a range of errors that would otherwise not be notified correctly.
     * For example, if an uncaught exception is thrown during application
     * startup, a GUI application might simply hang, leaving the user in the
     * dark about the reasons for the malfunction. With the error message
     * provided by the exe4j executable, reasons for startup errors are found
     * much more easily.
     * 
     * @parameter expression="${failIfExceptionThrownInMain}"
     *            default-value="true"
     * @required
     */
    private boolean failIfExceptionThrownInMain;

    /**
     * To redirect stderr to a file (assuming the streams are available for the
     * executableType thats been selected) set this parameter to true and then
     * define stdErrRedirectionFile parameter
     * 
     * @parameter expression="${redirectStdErr}" default-value="true"
     * @required
     */
    private boolean redirectStdErr;

    /**
     * To redirect stderr to a file (assuming the streams are available for the
     * executableType thats been selected) supply a file name. The file name is
     * interpreted relative to the executable.
     * 
     * @parameter expression="${stdErrRedirectionFile}"
     *            default-value="stderr.log"
     */
    private String stdErrRedirectionFile;

    /**
     * To redirect stdout to a file (assuming the streams are available for the
     * executableType thats been selected) set this parameter to true and then
     * define stdOutRedirectionFile parameter
     * 
     * @parameter expression="${redirectStdOut}" default-value="true"
     * @required
     */
    private boolean redirectStdOut;

    /**
     * To redirect stdout to a file (assuming the streams are available for the
     * executableMode thats been selected) supply a file name. The file name is
     * interpreted relative to the executable.
     * 
     * @parameter expression="${stdOutRedirectionFile}"
     *            default-value="stdout.log"
     */
    private String stdOutRedirectionFile;

    /**
     * Control whether or not to generate version information resource within
     * the executable. A version info resource will enable the Windows operating
     * system to determine meta information about your executable. This
     * information is displayed in various locations. For example, when opening
     * the property dialog for the executable in the Windows explorer, a
     * "Version" tab will be present in the property dialog if you have chosen
     * to generate the version info.
     * 
     * @parameter expression="${generateVersionInfo}" default-value="true"
     * @required
     */
    private boolean generateVersionInfo;

    /**
     * The product version must be composed of a maximum of 4 numbers, separated
     * by spaces, commas or dots. The product version is also used in the splash
     * screen version line config as a replacement value for the variable
     * %VERSION%.
     * 
     * Due to the restriction on version number formats a mask of "0.0.0.0" is
     * applied to the specified version number and if necessary truncated to
     * ensure it remains comprised of four numbers.
     * 
     * @parameter expression="${productVersion}"
     *            default-value="${project.version}"
     * @required
     */
    private ConfigVersionString productVersion;

    /**
     * To specify a version for the file which is a different from the product
     * version. If left empty the product version will be used for the file
     * version.
     * 
     * Due to the restriction on version number formats a mask of "0.0.0.0" is
     * applied to the specified version number and if necessary truncated to
     * ensure it remains comprised of four numbers.
     * 
     * @parameter expression="${fileVersion}"
     */
    private ConfigVersionString fileVersion;

    /**
     * Short internal name used to identify the application.
     * 
     * @parameter expression="${applicationShortInternalName}"
     *            default-value="${project.artifactId}"
     * @required
     */
    private String applicationShortInternalName;

    /**
     * Company name.
     * 
     * @parameter expression="${companyName}"
     *            default-value="${project.organization.name}"
     * @required
     */
    private String companyName;

    /**
     * File description.
     * 
     * @parameter expression="${fileDescription}"
     *            default-value="${project.description}"
     * @required
     */
    private String fileDescription;

    /**
     * Legal copyright text.
     * 
     * @parameter expression="${legalCopyrightText}"
     *            default-value="${project.organization.name}"
     */
    private String legalCopyrightText;

    /**
     * Fully qualified main class of your application.
     * 
     * @parameter expression="${mainClass}"
     * @required
     */
    private String mainClass;

    /**
     * JVM parameters you want to specify for the invocation of your Java
     * application (e.g. -Dmyapp.myproperty=true or -Xmx256m). There are two
     * runtime-variables you can use to specify runtime directories:
     * 
     * %EXE4J_EXEDIR% This is the directory where the executable is located.
     * %EXE4J_TEMPDIR% For the "JAR in EXE" mode, this variable will contain the
     * location of the temporary directory for the JAR files. In "regular mode"
     * this variable is not used.
     * 
     * In addition to these VM parameters, a parameter file in the same
     * directory as the executable is read and its contents are added to the
     * existing VM parameters. The name of this parameter file is the same as
     * the exe file with the extension .vmoptions. For example, if your exe file
     * is named hello.exe, the name of the VM parameter file is hello.vmoptions.
     * In this file, each line is interpreted as a single VM parameter. For
     * example, the contents of the VM parameter file could be: -Xmx128m -Xms32m
     * 
     * @parameter expression="${jvmParameters}"
     */
    private ConfigQuotedString jvmParameters;

    /**
     * Arguments for your main class. Arguments passed to the executable will be
     * appended to these arguments.
     * 
     * @parameter expression="${arguments}"
     */
    private ConfigQuotedString arguments;

    /**
     * To allow the user to specify JVM parameters through your application with
     * the syntax -J[VM parameter] (e.g. -J-Xmx512m)
     * 
     * @parameter expression="${allowJvmPassThrough}" default-value="true"
     * @required
     */
    private boolean allowJvmPassThrough;

    /**
     * class path entries can be scanDirectory, directory, archive or envVar.
     * Specify a lit of ClassPathEntry objects. If unspecified this will default
     * to a scanDirectory entry configured to search the workingDirectory
     * 
     * Note: You can use environment variables in the text field with the
     * following syntax: ${VARIABLE_NAME} where you replace VARIABLE_NAME with
     * the desired system environment variable.
     * 
     * Note: Not available if the "JAR in EXE" mode is being used.
     * 
     * @parameter expression="${classPath}"
     */
    private ConfigClassPath classPath;

    /**
     * If your application uses native libraries that you would lke to load with
     * a System.loadLibrary() call, the directory where the .dll is located must
     * be included in the PATH environment variable. You can add such
     * directories to the path by specifying them in this list of strings
     * 
     * @parameter expression="${nativeLibraries}"
     */
    private ConfigNativeLibraries nativeLibraries;

    /**
     * Minimum java version
     * 
     * @parameter expression="${minJavaVersion}" default-value="1.3"
     * @required
     */
    private String minJavaVersion;

    /**
     * Maximum java version
     * 
     * @parameter expression="${maxJavaVersion}" default-value=""
     */
    private String maxJavaVersion;

    /**
     * If you want to use JREs with a beta version number or JREs from an early
     * access release cycle set this to true.
     * 
     * @parameter expression="${allowBetaJREs}" default-value="false"
     * @required
     */
    private boolean allowBetaJREs;

    /**
     * If you only want JDKs to be used for launching your application and not
     * JREs set this to true.
     * 
     * @parameter expression="${allowOnlyJDKSs}" default-value="false"
     * @required
     */
    private boolean allowOnlyJDKSs;

    /**
     * JRE search sequence, a list of JRESearchPathEntry objects. If not
     * specified this will default to the windows registry, then JAVA_HOME and
     * JDK_HOME envVars.
     * 
     * @parameter expression="${jreSearchPath}"
     * @see JREPathLocation
     */
    private ConfigJRESearchPath jreSearchPath;

    /**
     * Configure the preferred VM that exe4j will choose to invoke your
     * application. This setting only influences the choice of the VM type after
     * a JRE has been selected according to the search sequence. Valuess can be
     * either "default", "client-hotspot" or "server-hotspot"
     * 
     * @parameter expression="${preferredVM}" default-value="default"
     * @required
     */
    private String preferredVM;

    /**
     * Set to true to display a splash screen.
     * 
     * @parameter expression="${useSplashScreen}" default-value="false"
     * @required
     */
    private boolean useSplashScreen;

    /**
     * Splash screen image file (PNG, BMP, or GIF), will default to
     * ${resourcesDirectory}/icons/${executableName}.png if not supplied
     * 
     * @parameter expression="${splashScreenFile}"
     */
    private File splashScreenFile;

    /**
     * If you want exe4j executable to monitor the state of your application and
     * hide the native splash screen as soon as a window is opened set this to
     * true. If you want to hide the splash screen programmatically, you can use
     * exe4j's launcher API.
     * 
     * @parameter expression="${hideSplashScreenOnStart}" default-value="true"
     * @required
     */
    private boolean hideSplashScreenOnStart;

    /**
     * Keep the splash screen on top of other windows opened by your
     * application.
     * 
     * @parameter expression="${splashScreenOnTop}" default-value="true"
     * @required
     */
    private boolean splashScreenOnTop;

    /**
     * Set to true to display a status line in the splash screen
     * 
     * @parameter expression="${useStatusLine}" default-value="true"
     */
    private boolean useStatusLine;

    /**
     * The status line configuration line, will default to TextLine("0.0.0",
     * "Arial", 8, 500, "", 20, 20)
     * 
     * @parameter expression="${statusLine}"
     */
    private ConfigStatusLine statusLine;

    /**
     * Set to true to display a version line in the splash screen
     * 
     * @parameter expression="${useVersionLine}" default-value="true"
     */
    private boolean useVersionLine;

    /**
     * The version line configuration line TextLine("0.0.0", "Arial", 8, 500,
     * "", 20, 40)
     * 
     * @parameter expression="${versionLine}"
     */
    private ConfigVersionLine versionLine;

    /**
     * The template exe4j config file to use. This config must be a valid exe4j
     * config file with substitution parameters (in the form ${param}) in all
     * the right places such that the substition engine can replace them with
     * their real values defined in this mojo.
     * 
     * If not supplied the default file in the resource bundled is used.
     * 
     * @parameter expression="${templateFile}"
     */
    private File templateFile;

    /**
     * The processed exe4j config file is written to this location before being
     * passed to exe4j. If not specified defaults to
     * ${executableDirectory}/${executableName}.exe4j
     * 
     * @parameter expression="${outputConfigFile}"
     *            default-value="${project.build.directory}/exe4j.xml"
     */
    private File outputConfigFile;

    /*
     * (non-Javadoc)
     * 
     * @see org.apache.maven.plugin.Mojo#execute()
     */
    public void execute()
        throws MojoExecutionException, MojoFailureException
    {

        initialise();

        // load config template

        String templateContent;

        try
        {
            if ( templateFile != null && templateFile.exists() )
            {
                templateContent = fileRead( templateFile );
            }
            else
            {
                templateContent = streamRead( getClass().getResourceAsStream( DEFAULT_TEMPLATE ) );
            }
        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "The exe4j template config file cannot be read", e );
        }

        // parse the template config file, substituting the ${VARNAME}
        // macros within for the Mojo parameter derived properties.

        String parsedConfig = replaceAll( templateContent, getProperties() );

        if ( getLog().isDebugEnabled() )
        {

            getLog().debug( "using exe4j config:\n" + parsedConfig );

        }

        // write to file store

        try
        {
            outputConfigFile.getParentFile().mkdirs();

            new File( distributionSourceDirectory.getAbsolutePath() + "/" + executableDirectory ).mkdirs();

            FileOutputStream fos = new FileOutputStream( outputConfigFile );

            fos.write( parsedConfig.getBytes() );

        }
        catch ( IOException e )
        {
            throw new MojoExecutionException( "The exe4j config file can not be written", e );
        }

        // invoke exe4j executor

        runExe4J( outputConfigFile );

    }

    /**
     * Initialise complex mojo parameters that havent been supplied.
     * 
     * @throws MojoExecutionException
     */
    private void initialise()
        throws MojoExecutionException
    {

        try
        {
            // default jre search locations are registry then standard JAVA env vars

            if ( jreSearchPath == null )
            {
                jreSearchPath = new ConfigJRESearchPath();

                jreSearchPath
                    .addLocation( new ConfigJRESearchPath.JREPathLocation(
                                                                           ConfigJRESearchPath.JREPathLocation.REGISTRY_TYPE,
                                                                           null ) );

                jreSearchPath
                    .addLocation( new ConfigJRESearchPath.JREPathLocation(
                                                                           ConfigJRESearchPath.JREPathLocation.ENVVAR_TYPE,
                                                                           "JAVA_HOME" ) );

                jreSearchPath
                    .addLocation( new ConfigJRESearchPath.JREPathLocation(
                                                                           ConfigJRESearchPath.JREPathLocation.ENVVAR_TYPE,
                                                                           "JDK_HOME" ) );
            }

            // default classpath location is to load any archive in the exe
            // directory.

            if ( classPath == null )
            {
                classPath = new ConfigClassPath();

                classPath
                    .addLocation( new ConfigClassPath.ClassPathLocation(
                                                                         ConfigClassPath.ClassPathLocation.SCAN_DIRECTORY_TYPE,
                                                                         workingDirectory, true ) );

                getLog().warn( "classPath not defined, using default: " + classPath );
            }

            // default native libs is empty

            if ( nativeLibraries == null )
            {
                nativeLibraries = new ConfigNativeLibraries();
            }
        }
        catch ( PlexusConfigurationException e )
        {
            // really shouldn't happen

            throw new MojoExecutionException( "Programmer error!", e );
        }

        // these may not be being used (ie their use is controlled via boolean cfg swicthes)
        // but we write them into the config file all the same so initialise them if they
        // jave not been supplied.

        if ( executableIconFile == null )
        {
            executableIconFile = new File( resourcesDirectory.getPath() + "/icons/" + executableName + ".ico" );
        }

        if ( splashScreenFile == null )
        {
            splashScreenFile = new File( resourcesDirectory.getPath() + "/images/" + executableName + ".png" );
        }

        if ( statusLine == null )
        {
            statusLine = new ConfigStatusLine();
        }

        if ( versionLine == null )
        {
            versionLine = new ConfigVersionLine();
        }

        if ( arguments == null )
        {
            arguments = new ConfigQuotedString( "" );
        }

        if ( fileVersion == null )
        {
            fileVersion = productVersion;
        }
    }

    /**
     * helper method that reads a file and returns its contents as a string
     * 
     * @param file the file to read
     * @return the files content
     * @throws IOException if an error is incountered
     */
    private String fileRead( File file )
        throws IOException
    {
        return streamRead( new FileInputStream( file ) );
    }

    /**
     * helper method that reads a stream and returns its contents as a string
     *  
     * @param in the input stream
     * @return the streams content
     * @throws IOException if an error is incountered
     */
    private String streamRead( InputStream in )
        throws IOException
    {
        StringBuffer buf = new StringBuffer();
        byte b[] = new byte[512];
        int count;
        while ( ( count = in.read( b ) ) > 0 )
            buf.append( new String( b, 0, count ) );
        in.close();
        return buf.toString();
    }

    /**
     * helper method that creates a properties object containing the
     * Exe4J XML representations of the Mojo's parameters
     * 
     * @return the properties ready for substitution into the Exe4J XML
     * config file
     * @throws Exe4JException raised if an invalid parameter has 
     * been supplied 
     */
    private Properties getProperties()
    {
        Properties props = new Properties();

        props.setProperty( "jarExeMode", jarExeMode.toString() );

        props.setProperty( "applicationShortName", applicationShortName );

        props.setProperty( "applicationShortInternalName", applicationShortInternalName );

        props.setProperty( "distributionSourceDirectory", distributionSourceDirectory.getAbsolutePath() );

        props.setProperty( "executableDirectory", executableDirectory );

        props.setProperty( "executableMode", executableMode.toString() );

        props.setProperty( "executableName", executableName );

        props.setProperty( "useExecutableIcon", Boolean.toString( useExecutableIcon ) );

        props.setProperty( "executableIconFile", executableIconFile.getAbsolutePath() );

        props.setProperty( "workingDirectory", workingDirectory );

        props.setProperty( "singleInstanceOnly", Boolean.toString( singleInstanceOnly ) );

        props.setProperty( "failIfExceptionThrownInMain", Boolean.toString( failIfExceptionThrownInMain ) );

        props.setProperty( "redirectStdErr", Boolean.toString( redirectStdErr ) );

        props.setProperty( "stdErrRedirectionFile", stdErrRedirectionFile );

        props.setProperty( "redirectStdOut", Boolean.toString( redirectStdOut ) );

        props.setProperty( "stdOutRedirectionFile", stdOutRedirectionFile );

        props.setProperty( "generateVersionInfo", Boolean.toString( generateVersionInfo ) );

        props.setProperty( "productVersion", productVersion.toString() );

        props.setProperty( "fileVersion", fileVersion.toString() );

        props.setProperty( "companyName", companyName );

        props.setProperty( "fileDescription", fileDescription );

        props.setProperty( "legalCopyrightText", legalCopyrightText );

        props.setProperty( "mainClass", mainClass );

        props.setProperty( "jvmParameters", jvmParameters.toString() );

        props.setProperty( "arguments", arguments.toString() );

        props.setProperty( "allowJvmPassThrough", Boolean.toString( allowJvmPassThrough ) );

        props.setProperty( "classPath", classPath.toString() );

        props.setProperty( "nativeLibraries", nativeLibraries.toString() );

        props.setProperty( "minJavaVersion", minJavaVersion );

        props.setProperty( "maxJavaVersion", maxJavaVersion != null ? maxJavaVersion : "" );

        props.setProperty( "allowBetaJREs", Boolean.toString( allowBetaJREs ) );

        props.setProperty( "allowOnlyJDKSs", Boolean.toString( allowOnlyJDKSs ) );

        props.setProperty( "jreSearchSequence", jreSearchPath.toString() );

        props.setProperty( "preferredVM", preferredVM );

        props.setProperty( "useSplashScreen", Boolean.toString( useSplashScreen ) );

        props.setProperty( "splashScreenFile", splashScreenFile.getAbsolutePath() );

        props.setProperty( "hideSplashScreenOnStart", Boolean.toString( hideSplashScreenOnStart ) );

        props.setProperty( "splashScreenOnTop", Boolean.toString( splashScreenOnTop ) );

        props.setProperty( "useStatusLine", Boolean.toString( useStatusLine ) );

        props.setProperty( "statusLine", statusLine.toString() );

        props.setProperty( "useVersionLine", Boolean.toString( useVersionLine ) );

        props.setProperty( "versionLine", versionLine.toString() );

        return props;

    }

    /**
     * Helper method that replaces all instances of ${foo} in the 
     * input string with the value contained in the properties 
     * object indexed by foo.
     * @param template input string
     * @param properties properties object containing the objects to 
     * use to replace the %{foo} tags in the input
     * @return the processed string
     */
    public static String replaceAll( String template, Dictionary properties )
    {
        Matcher matcher = Pattern.compile( "\\$\\{[a-zA-Z0-9]+\\}" ).matcher( template );

        StringBuffer result = new StringBuffer();

        int lastEnd = 0;

        while ( matcher.find() )
        {
            String match = matcher.group();
            int start = matcher.start();
            int end = matcher.end();

            String propertyName = matcher.group().substring( 2, match.length() - 1 );

            // TODO: cache these?

            Object value = properties.get( propertyName );

            String serialisedValue;

            if ( value == null )
            {
                serialisedValue = "";
            }
            else
            {
                serialisedValue = value.toString();
            }

            result.append( template.substring( lastEnd, start ) );
            result.append( serialisedValue );

            lastEnd = end;
        }

        result.append( template.substring( lastEnd, template.length() ) );

        return result.toString();
    }

}