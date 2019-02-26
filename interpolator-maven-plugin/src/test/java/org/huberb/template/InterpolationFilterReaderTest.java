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
package org.huberb.template;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.plexus.util.IOUtil;
import org.codehaus.plexus.util.InterpolationFilterReader;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author berni3
 */
public class InterpolationFilterReaderTest {

    public InterpolationFilterReaderTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void hello() throws IOException {
        Map<String, String> templateValues = new HashMap<>();
        templateValues.put("name", "nobody");
        String beginToken = "{{";
        String endToken = "}}";
        String interpolated = "";

        Map m = templateValues;
        try (StringReader in = new StringReader("Hello, {{name}}");
                InterpolationFilterReader ifr = new InterpolationFilterReader(in, m, beginToken, endToken);
                StringWriter sw = new StringWriter()) {

            IOUtil.copy(ifr, sw);
            sw.flush();
            interpolated = sw.toString();
        }
        assertEquals("Hello, nobody", interpolated);
    }
}
