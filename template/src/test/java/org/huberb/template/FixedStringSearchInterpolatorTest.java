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

import java.util.Properties;
import org.codehaus.plexus.interpolation.fixed.FixedStringSearchInterpolator;
import org.codehaus.plexus.interpolation.fixed.ObjectBasedValueSource;
import org.codehaus.plexus.interpolation.fixed.PropertiesBasedValueSource;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author berni3
 */
public class FixedStringSearchInterpolatorTest {

    public FixedStringSearchInterpolatorTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

 
    @Test
    public void hello() {
        ObjectBasedValueSource vs = new ObjectBasedValueSource("PlexusInterpolation");
        FixedStringSearchInterpolator fssi = FixedStringSearchInterpolator.create("{{", "}}", vs);
        assertEquals("Hello, {{this}}", fssi.interpolate("Hello, {{this}}"));
        
    }
    @Test
    public void hello2() {
        Properties props = new Properties();
        props.put("ma", "maValue");
        props.put("mb", "mbValue");
        PropertiesBasedValueSource propertiesBasedValueSource = new PropertiesBasedValueSource(props);
        FixedStringSearchInterpolator fssi = FixedStringSearchInterpolator.create("{{", "}}", propertiesBasedValueSource);
        assertEquals("maValue + mbValue", fssi.interpolate("{{ma}} + {{mb}}"));
    }
}
