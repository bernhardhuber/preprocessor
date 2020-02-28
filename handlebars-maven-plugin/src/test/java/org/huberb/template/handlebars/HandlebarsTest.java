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
package org.huberb.template.handlebars;

import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Template;
import com.github.jknack.handlebars.context.MapValueResolver;
import java.io.IOException;
import java.util.Properties;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author berni3
 */
public class HandlebarsTest {

    public HandlebarsTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testHello() throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("Hello, {{this}}");
        assertEquals("Hello, Handlebars.java", template.apply("Handlebars.java"));
    }

    @Test
    public void testResolvingProperties() throws IOException {
        Handlebars handlebars = new Handlebars();
        Template template = handlebars.compileInline("{{ma}} + {{mb}}");
        Properties props = new Properties();
        props.put("ma", "maValue");
        props.put("mb", "mbValue");
        Context context = Context.newBuilder(props).resolver(MapValueResolver.INSTANCE).build();
        assertEquals("maValue + mbValue", template.apply(context));
    }
}
