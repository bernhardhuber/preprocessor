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
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;

/**
 * Merger using {@code org.codehaus.plexus.util.InterpolationFilterReader}.
 *
 * @author berni3
 */
class Interpolate {

    private String beginToken = "{{";
    private String endToken = "}}";

    private final Map<String, String> templateValues = new HashMap<>();

    /**
     * Define begin, and end token.
     *
     * @param b
     * @param e
     * @return
     */
    Interpolate beginTokenEndToken(String b, String e) {
        this.beginToken = b;
        this.endToken = e;
        return this;
    }

    /**
     * Add a key, value pair to model.
     *
     * @param k
     * @param v
     * @return
     */
    Interpolate addKeyValue(String k, String v) {
        this.templateValues.put(k, v);
        return this;
    }

    /**
     * Add all key, value pairs of the map m to the model.
     *
     * @param m
     * @return
     */
    Interpolate addMapKeyValues(Map<String, String> m) {
        this.templateValues.putAll(m);
        return this;
    }

    /**
     * Add all key, value pairs of the properties to the model.
     *
     * @param properties
     * @return
     */
    Interpolate addProperties(Properties properties) {
        properties.forEach((Object k, Object v) -> this.templateValues.put((String) k, (String) v));
        return this;
    }

    /**
     * Merge infile with key-value model to outfile.
     *
     * @param infile
     * @param outfile
     * @throws InterpolateException
     */
    void merge(File infile, File outfile) throws InterpolateException {
        boolean infileValidate = infile.isFile();
        infileValidate = infileValidate && infile.canRead();
        if (!infileValidate) {
            throw new InterpolateException("infile " + infile + " is not valid");
        }
        boolean outfileValidate = !outfile.exists();
        if (!outfileValidate) {
            throw new InterpolateException("outfile " + outfile + " is not valid");
        }
        Map m = templateValues;
        try (FileReader fr = new FileReader(infile);
                InterpolationFilterReader ifr = new InterpolationFilterReader(fr, m, beginToken, endToken);
                Writer fw = new FileWriter(outfile)) {
            IOUtil.copy(ifr, fw);
        } catch (IOException ioex) {
            throw new InterpolateException("merge", ioex);
        }
    }

    /**
     * Wrapping Exception for Interpolate merging.
     */
    static class InterpolateException extends Exception {

        InterpolateException(String message) {
            super(message);
        }

        InterpolateException(String message, Throwable cause) {
            super(message, cause);
        }

    }
}
