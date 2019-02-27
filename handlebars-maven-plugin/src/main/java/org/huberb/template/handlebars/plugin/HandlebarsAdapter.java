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

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.MapValueResolver;
import com.github.jknack.handlebars.io.TemplateSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.codehaus.plexus.util.IOUtil;

/**
 *
 * @author berni3
 */
class HandlebarsAdapter {

    private String beginToken = "{{";
    private String endToken = "}}";

    private final Map<String, String> templateValues = new HashMap<>();

    HandlebarsAdapter beginTokenEndToken(String b, String e) {
        this.beginToken = b;
        this.endToken = e;
        return this;
    }

    HandlebarsAdapter addKeyValue(String k, String v) {
        this.templateValues.put(k, v);
        return this;
    }

    HandlebarsAdapter addMapKeyValues(Map<String, String> m) {
        this.templateValues.putAll(m);
        return this;
    }

    HandlebarsAdapter addProperties(Properties properties) {
        properties.forEach((Object k, Object v) -> this.templateValues.put((String) k, (String) v));
        return this;
    }

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
        try (FileWriter outfileWriter = new FileWriter(outfile)) {
            Map m = templateValues;
            Handlebars handlebars = new Handlebars();
            handlebars.setStartDelimiter(beginToken);
            handlebars.setEndDelimiter(endToken);

            Template template = handlebars.compile(new FileTemplateSource(infile));
            Context context = Context.newBuilder(m).resolver(MapValueResolver.INSTANCE).build();
            template.apply(context, outfileWriter);
        } catch (IOException ioex) {
            throw new InterpolateException("merge", ioex);
        }
    }

    static class FileTemplateSource implements TemplateSource {

        private File f;

        public FileTemplateSource(File f) {
            this.f = f;
        }

        @Override
        public String content(Charset charset) throws IOException {
            String content = "";
            try (FileInputStream fis = new FileInputStream(f)) {
                content = IOUtil.toString(fis, charset.name());
            }
            return content;
        }

        @Override
        public String filename() {
            return f.getName();
        }

        @Override
        public long lastModified() {
            return f.lastModified();
        }

    }

     static class InterpolateException extends Exception {

        public InterpolateException(String message) {
            super(message);
        }

        public InterpolateException(String message, Throwable cause) {
            super(message, cause);
        }

    }
}
