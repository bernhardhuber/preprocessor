/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.preprocessor.interpolation;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;
import java.util.Properties;
import org.codehaus.plexus.interpolation.AbstractValueSource;
import org.codehaus.plexus.interpolation.InterpolatorFilterReader;
import org.codehaus.plexus.interpolation.MapBasedValueSource;
import org.codehaus.plexus.interpolation.PropertiesBasedValueSource;
import org.codehaus.plexus.interpolation.StringSearchInterpolator;

/**
 *
 * @author berni3
 */
public class InterpolationWrapper {

    public static class Request {

        private Reader in;
        private Writer out;
        private AbstractValueSource dataModel;

        public static class Builder {

            private final Request request = new Request();

            public Builder reader(Reader in) {
                this.request.in = in;
                return this;
            }

            public Builder writer(Writer out) {
                this.request.out = out;
                return this;
            }

            public Builder dataModel(Map<String, String> m) {
                request.dataModel = new MapBasedValueSource(m);
                return this;
            }

            public Builder dataModel(Properties props) {
                request.dataModel = new PropertiesBasedValueSource(props);
                return this;
            }

            public Request build() {
                return request;
            }
        }

    }

    static class Response {
    }

    static class InterpolationMerger {

        void process(Request request, Response response) throws IOException {

            final StringSearchInterpolator interpolator = new StringSearchInterpolator();
            interpolator.addValueSource(request.dataModel);
            try (Reader in = request.in) {
                final InterpolatorFilterReader r = new InterpolatorFilterReader(in, interpolator);
                try (Writer out = request.out) {
                    writeFromTo(r, out);
                }
            }

        }

        void writeFromTo(Reader r, Writer w) throws IOException {
            final char[] charBuf = new char[256];
            int l;
            while ((l = r.read(charBuf)) != -1) {
                w.write(charBuf, 0, l);
            }

        }
    }
}
