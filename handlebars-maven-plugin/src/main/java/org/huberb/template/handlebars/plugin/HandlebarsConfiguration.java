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
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.maven.model.FileSet;
import org.codehaus.plexus.util.StringUtils;

/**
 * Encapsulation for maven configurations.
 * 
 * @author berni3
 */
public class HandlebarsConfiguration {

    private FileSet templateFileSet;
    private final Map<String, String> propertyMap;
    private String beginToken;
    private String endToken;
    private String removeExtension;

    public HandlebarsConfiguration() {
        this.propertyMap = new HashMap<>();
        this.templateFileSet = new FileSet();
    }

    HandlebarsConfiguration propertiesFile(File propertyFile) throws ConfigurationException {
        Properties props = new Properties();
        try {
            props.load(new FileReader(propertyFile));
        } catch (IOException ioex) {
            throw new ConfigurationException("loading properties file " + propertyFile, ioex);
        }
        this.properties(props);

        return this;
    }

    HandlebarsConfiguration properties(Properties props) {
        props.forEach((Object k, Object v) -> {
            String key = (String) k;
            String value = (String) v;
            this.propertyMap.put(key, value);
        });
        return this;
    }

    HandlebarsConfiguration property(String k, String v) {
        propertyMap.put(k, v);
        return this;
    }

    HandlebarsConfiguration baseDir(File baseDir) {
        this.templateFileSet.setDirectory(baseDir.getAbsolutePath());
        return this;
    }

    HandlebarsConfiguration fileSet(FileSet fileSet) {
        this.templateFileSet = fileSet.clone();
        return this;
    }

    HandlebarsConfiguration includesExcludes(String includesAsCsv, String excludesAsCsv) {
        {
            //-- includes
            final String[] includes = StringUtils.split(includesAsCsv, ",");
            StringUtils.stripAll(includes);
            Arrays.asList(includes).forEach((include) -> templateFileSet.addInclude(include));
        }
        {
            //-- excludes
            final String[] excludes = StringUtils.split(excludesAsCsv, ",");
            StringUtils.stripAll(excludes);
            Arrays.asList(excludes).forEach((include) -> templateFileSet.addExclude(include));
        }
        return this;
    }

    HandlebarsConfiguration beginToken(String beginToken) {
        this.beginToken = StringUtils.trim(beginToken);
        return this;
    }

    HandlebarsConfiguration endToken(String endToken) {
        this.endToken = StringUtils.trim(endToken);
        return this;
    }

    HandlebarsConfiguration removeExtension(String removeExtension) {
        this.removeExtension = StringUtils.trim(removeExtension);
        return this;
    }

    /**
     * validate configuration
     * @throws ConfigurationException 
     */
    void validate() throws ConfigurationException {
        {
            boolean bBeginEndToken = true;
            bBeginEndToken = bBeginEndToken && StringUtils.isNotBlank(beginToken);
            bBeginEndToken = bBeginEndToken && StringUtils.isNotBlank(endToken);
            if (!bBeginEndToken) {
                throw new ConfigurationException("beginToken '" + beginToken + "', endToken '" + endToken + "' are invalid");
            }
        }
        {
            if (StringUtils.equals(beginToken, endToken)) {
                throw new ConfigurationException("beginToken '" + beginToken + "' == endToken '" + endToken + "' is not allowed");
            }
        }
        {
            boolean bRemoveExtension = false;
            bRemoveExtension = bRemoveExtension || removeExtension == null;
            bRemoveExtension = bRemoveExtension || StringUtils.equals(removeExtension, "");
            bRemoveExtension = bRemoveExtension || removeExtension.startsWith(".");
            if (!bRemoveExtension) {
                throw new ConfigurationException("removeExtension '" + removeExtension + "' is invalid ");
            }
        }
    }

    //---
    String toLogString() {
        String logString = String.format("beginToken: %s; "
                + "endToken: %s; "
                + "removeExtension: %s; "
                + "model properties: %s; "
                + "fileset: %s",
                this.getBeginToken(),
                this.getEndToken(),
                this.getRemoveExtension(),
                this.getPropertyMap(),
                this.getTemplateFileSet());
        return logString;
    }

    //---
    public FileSet getTemplateFileSet() {
        return templateFileSet;
    }

    public Map<String, String> getPropertyMap() {
        return propertyMap;
    }

    public String getBeginToken() {
        return beginToken;
    }

    public String getEndToken() {
        return endToken;
    }

    public String getRemoveExtension() {
        return removeExtension;
    }
    
    /**
     * Wrapper exception, thrown if configuration is bad.
     */
    static class ConfigurationException extends Exception {
        
        public ConfigurationException(String message) {
            super(message);
        }
        
        public ConfigurationException(String message, Throwable cause) {
            super(message, cause);
        }
        
    }

}
