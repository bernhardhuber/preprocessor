/*
 * Copyright 2017 berni3.
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
package org.huberb.preprocessor.freemarker;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;

/**
 *
 * @author berni3
 */
public class FreemarkerWrapper {

    static class FreemarkerRequest {

        private Writer out;
        private Object dataModel;
        private String templateName;

        public Writer getOut() {
            return out;
        }

        public Object getDataModel() {
            return dataModel;
        }

        public String getTemplateName() {
            return templateName;
        }

        static class Builder {

            FreemarkerRequest freemarkerRequest = new FreemarkerRequest();

            Builder out(Writer w) {
                this.freemarkerRequest.out = w;
                return this;
            }

            Builder dataModel(Properties dm) {
                this.freemarkerRequest.dataModel = dm;
                return this;
            }

            Builder dataModel(Map<String, String> dm) {
                this.freemarkerRequest.dataModel = dm;
                return this;
            }

            Builder templateName(String templateName) {
                this.freemarkerRequest.templateName = templateName;
                return this;
            }

            FreemarkerRequest build() {
                return this.freemarkerRequest;
            }
        }
    }

    static class FreemarkerResponse {
    }

    static class FreemarkerMerger {

        private final Configuration configuration;

        public FreemarkerMerger(Configuration configuration) {
            this.configuration = configuration;
        }

        public void process(FreemarkerRequest req, FreemarkerResponse resp) throws IOException, TemplateException {
            String templateName = req.templateName;
            Template template = this.configuration.getTemplate(templateName);
            template.process(req.dataModel, req.out);
        }
    }
}
