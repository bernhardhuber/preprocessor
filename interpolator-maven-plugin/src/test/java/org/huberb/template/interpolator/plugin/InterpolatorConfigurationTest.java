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
import org.junit.After;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author berni3
 */
public class InterpolatorConfigurationTest {

    public InterpolatorConfigurationTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of validate method, of class InterpolatorConfiguration.
     * 
     * @throws org.huberb.template.interpolator.plugin.InterpolatorConfiguration.ConfigurationException
     */
    @Test
    public void testValidate() throws InterpolatorConfiguration.ConfigurationException {
        InterpolatorConfiguration instance = new InterpolatorConfiguration();
        instance.baseDir(new File(".")).
                beginToken("{(").
                endToken(")}").
                removeExtension(".mm");
        instance.validate();
    }

    /**
     * Test of getBeginToken method, of class InterpolatorConfiguration.
     */
    @Test
    public void testGetter() {
        InterpolatorConfiguration instance = new InterpolatorConfiguration();
        instance.baseDir(new File(".")).
                beginToken("((").
                endToken("))").
                removeExtension(".mm").
                includesExcludes("*.inc", "*.exc").
                property("a", "aValue");

        assertEquals("((", instance.getBeginToken());
        assertEquals("))", instance.getEndToken());
        assertEquals("aValue", instance.getPropertyMap().get("a"));
        assertEquals(".mm", instance.getRemoveExtension());
        assertEquals("*.exc", instance.getTemplateFileSet().getExcludes().get(0));
        assertEquals("*.inc", instance.getTemplateFileSet().getIncludes().get(0));
        assertEquals(new File(".").getAbsolutePath(), instance.getTemplateFileSet().getDirectory());
    }

}
