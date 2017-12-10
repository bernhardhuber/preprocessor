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
package org.huberb.preprocessor.simple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author berni3
 */
public class SimpleWrapper {

    public static class Request {

        private Map<String, String> map;
        private Reader r;
        private Writer w;

        public static class Builder {

            private final Request request;

            public Builder() {
                request = new Request();
                request.map = new HashMap<>();
            }

            public Builder reader(Reader r) {
                this.request.r = r;
                return this;
            }

            public Builder writer(Writer w) {
                this.request.w = w;
                return this;
            }

            public Builder dataModel(Map<String, String> map) {
                request.map.putAll(map);
                return this;
            }

            public Builder dataModel(String k, String v) {
                request.map.put(k, v);
                return this;
            }

            public Request build() {
                return request;
            }
        }
    }

    public static class Response {
    }

    public static class SimpleMerger {

        public void merge(Request req, Response res) throws IOException {
            try (Writer w = req.w) {
                try (BufferedReader r = _createBufferedReaderFrom(req.r)) {
                    for (String line; (line = r.readLine()) != null;) {
                        String mergedLine = _merge(line, req.map);
                        w.append(mergedLine + System.lineSeparator());
                    }
                }
            }
        }

        protected String _merge(String input, Map<String, String> map) {
            String result = input;
            for (Entry<String, String> e : map.entrySet()) {
                result = result.replace(e.getKey(), e.getValue());
            }
            return result;

        }

        protected BufferedReader _createBufferedReaderFrom(Reader r) {
            final BufferedReader br;
            if (r instanceof BufferedReader) {
                br = (BufferedReader) r;
            } else {
                br = new BufferedReader(r);
            }
            return br;
        }
    }
}
