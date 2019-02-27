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
package org.huberb.template.interpolator.plugin;

import java.io.File;
import java.util.List;
import java.util.Properties;
import org.apache.maven.model.FileSet;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.plexus.util.StringUtils;
import org.huberb.template.interpolator.plugin.Interpolate.InterpolateException;
import org.huberb.template.interpolator.support.FileCalculator;
import org.huberb.template.interpolator.support.FileCalculator.InputFileOutputFilePair;
import org.huberb.template.interpolator.support.FileScanner;

/**
 * Maven plugin for template processing using plexus-utils Interpolator.
 *
 * @author berni3
 */
@Mojo(name = "interpolator")
public class InterpolatorMojo extends AbstractMojo {

    /**
     * Template files. The files to apply interpolation on.
     */
    @Parameter
    private FileSet templateFiles;

    /**
     * Includes pattern.
     */
    @Parameter(property = "interpolator.includes")
    private String includes;

    /**
     * Property file.
     */
    @Parameter(property = "interpolator.propertyFile")
    private File propertiesFile;

    /**
     * Template values.
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
    @Parameter(property = "interpolator.removeExtension", defaultValue = ".j2")
    private String removeExtension;

    /**
     * Set this parameter for defining the interpolation begin token.
     *
     * Usually you want to use begin-tokens like {@code "${"}, or
     * {@code "{{"}.
     */
    @Parameter(property = "interpolator.beginToken", defaultValue = "{{")
    private String beginToken;

    /**
     * Set this parameter for defining the interpolation end token.
     *
     * Usually you want to use end-tokens like "<code>}</code>", or
     * "<code>}}</code>".
     */
    @Parameter(property = "interpolator.endToken", defaultValue = "}}")
    private String endToken;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {

        //---
        // extract configuration from @Parameter
        final InterpolatorConfiguration configuration = createConfiguration();
        validateConfiguration(configuration);
        logConfiguration(configuration);

        //---
        // calculate all input-files.
        final List<File> inputFiles = new FileScanner().setUpByFileSet(configuration.getTemplateFileSet()).scan();
        this.getLog().debug("inputFiles " + inputFiles);

        //---
        // calculate for each input-file the appropriate output-file.
        final FileCalculator fileCalculator = new FileCalculator();
        final List<InputFileOutputFilePair> ifofpList = fileCalculator.calculateFromInputFiles(
                configuration.getRemoveExtension(),
                inputFiles);
        this.getLog().debug("inputFileOutputFilePair " + ifofpList);

        //---
        // process input-file to output-file
        for (InputFileOutputFilePair ifofp : ifofpList) {
            File infile = ifofp.getInputFile();
            File outfile = ifofp.getOutputFile();
            try {
                this.getLog().debug("interpolate " + infile + " -> " + outfile);
                final Interpolate interpolate = new Interpolate().
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

    InterpolatorConfiguration createConfiguration() throws MojoExecutionException {

        final InterpolatorConfiguration configuration = new InterpolatorConfiguration();

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
        } catch (InterpolatorConfiguration.ConfigurationException ex) {
            throw new MojoExecutionException("setup configuration", ex);
        }
        return configuration;
    }

    void validateConfiguration(InterpolatorConfiguration configuration) throws MojoExecutionException {
        try {
            configuration.validate();
        } catch (InterpolatorConfiguration.ConfigurationException ex) {
            throw new MojoExecutionException("validate configuration", ex);
        }
    }

    void logConfiguration(InterpolatorConfiguration configuration) {
        this.getLog().debug("configuration: " + configuration.toLogString());
    }
}
