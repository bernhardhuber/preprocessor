/*
 * Copyright 2019 berni3.
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
package org.huberb.template.handlebars.plugin;

import java.io.File;
import java.util.List;
import java.util.Properties;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;
import org.huberb.template.handlebars.plugin.HandlebarsAdapter.InterpolateException;
import org.huberb.template.handlebars.support.FileCalculator;
import org.huberb.template.handlebars.support.FileCalculator.InputFileOutputFilePair;
import org.huberb.template.handlebars.support.FileScanner;

/**
 * Maven plugin for template processing using handlebars.
 *
 * @author berni3
 */
@Mojo(name = "handlebars")
public class HandlebarsMojo extends AbstractMojo {

    /**
     * Template files. The files to apply interpolation on.
     */
    @Parameter
    private FileSet templateFiles;

    /**
     * Includes pattern.
     */
    @Parameter(property = "handlebars.includes")
    private String includes;

    /**
     * Property file.
     */
    @Parameter(property = "handlebars.propertyFile")
    private File propertiesFile;

    /**
     * Template values
     *
     */
    @Parameter
    private Properties properties;

    /**
     * Set this parameter if you want the plugin to remove an unwanted extension
     * when saving result.
     *
     * For example {@code "foo.xml.bar"} will become {@code "foo.xml"} if
     * removeExtension = {@code '.bar'}.
     *
     * Null and empty means no substition.
     */
    @Parameter(property = "handlebars.removeExtension", defaultValue = ".j2")
    private String removeExtension;

    /**
     * Set this parameter for defining the interpolation begin token.
     *
     * Usually you want to use begin-tokens like {@code "${"}, or
     * {@code "{{"}.
     */
    @Parameter(property = "handlebars.beginToken", defaultValue = "{{")
    private String beginToken;

    /**
     * Set this parameter for defining the interpolation end token.
     *
     * Usually you want to use end-tokens like "<code>}</code>", or
     * "<code>}}</code>".
     */
    @Parameter(property = "handlebars.endToken", defaultValue = "}}")
    private String endToken;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        //---
        final HandlebarsConfiguration configuration = createConfiguration();
        validateConfiguration(configuration);
        logDebug("configuration: " + configuration.toLogString());
        

        //---
        final List<File> inputFiles = new FileScanner().setUpByFileSet(configuration.getTemplateFileSet()).scan();
        logDebug("inputFiles " + inputFiles);

        //---
        final FileCalculator fileCalculator = new FileCalculator();
        final List<InputFileOutputFilePair> ifofpList = fileCalculator.calculateFromInputFiles(
                configuration.getRemoveExtension(),
                inputFiles);
        logDebug("inputFileOutputFilePair " + ifofpList);

        //---
        for (InputFileOutputFilePair ifofp : ifofpList) {
            File infile = ifofp.getInputFile();
            File outfile = ifofp.getOutputFile();
            try {
                logDebug("interpolate " + infile + " -> " + outfile);
                final HandlebarsAdapter interpolate = new HandlebarsAdapter().
                        beginTokenEndToken(
                                configuration.getBeginToken(),
                                configuration.getEndToken()).
                        addMapKeyValues(configuration.getPropertyMap());
                interpolate.merge(infile, outfile);
            } catch (InterpolateException ex) {
                throw new MojoExecutionException("execute ", ex);
            }
        }
    }

    HandlebarsConfiguration createConfiguration() throws MojoExecutionException {
      final  HandlebarsConfiguration configuration = new HandlebarsConfiguration();
        try {
            configuration.baseDir(new File("."));
            if (this.templateFiles != null) {
                configuration.fileSet(templateFiles);
            }
            if (this.includes != null) {
                configuration.includesExcludes(this.includes, "");
            }
            if (this.properties != null) {
                configuration.properties(properties);
            }
            if (this.propertiesFile != null) {
                configuration.propertiesFile(propertiesFile);
            }
            if (StringUtils.isNotBlank(this.beginToken)) {
                configuration.beginToken(this.beginToken);
            }
            if (StringUtils.isNotBlank(this.endToken)) {
                configuration.endToken(this.endToken);
            }
            if (StringUtils.isNotEmpty(this.removeExtension)) {
                configuration.removeExtension(this.removeExtension);
            }
        } catch (HandlebarsConfiguration.ConfigurationException ex) {
            throw new MojoExecutionException("setup configuration", ex);
        }
        return configuration;
    }

    void validateConfiguration(HandlebarsConfiguration configuration) throws MojoExecutionException {
        try {
            configuration.validate();
        } catch (HandlebarsConfiguration.ConfigurationException ex) {
            throw new MojoExecutionException("validate configuration", ex);
        }
    }
 
    void logDebug(String m, Object... args) {
        new LogWrapper(this).log(LogWrapper.LogWrapperLevel.DEBUG, m, args);
    }

    //---
    static class LogWrapper {

        enum LogWrapperLevel {
            DEBUG, INFO, WARN, ERROR
        };
        private final AbstractMojo abstractMojo;

        public LogWrapper(AbstractMojo abstractMojo) {
            this.abstractMojo = abstractMojo;
        }

        void log(LogWrapperLevel level, String m, Object... args) {
            if (isEnabled(level)) {

                final String formatted;
                if (args == null || args.length == 0) {
                    formatted = m;
                } else {
                    formatted = String.format(m, args);
                }
                logIt(level, formatted);
            }
        }

        boolean isEnabled(LogWrapperLevel level) {
            boolean enabled = false;
            final Log log = abstractMojo.getLog();
            enabled = enabled || (level == LogWrapperLevel.ERROR && log.isErrorEnabled());
            enabled = enabled || (level == LogWrapperLevel.WARN && log.isWarnEnabled());

            enabled = enabled || (level == LogWrapperLevel.INFO && log.isInfoEnabled());
            enabled = enabled || (level == LogWrapperLevel.DEBUG && log.isDebugEnabled());
            return enabled;
        }

        void logIt(LogWrapperLevel level, String formatted) {
            final Log log = abstractMojo.getLog();
            switch (level) {
                case ERROR:
                    log.debug(formatted);
                    break;
                case WARN:
                    log.debug(formatted);
                    break;
                case INFO:
                    log.debug(formatted);
                    break;
                case DEBUG:
                    log.debug(formatted);
                    break;
            }

        }
    }
}
