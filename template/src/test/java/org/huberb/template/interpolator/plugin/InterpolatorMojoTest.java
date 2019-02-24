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

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import org.apache.maven.plugin.testing.MojoRule;
import org.apache.maven.plugin.testing.WithoutMojo;
import org.codehaus.plexus.util.IOUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;

/**
 *
 * @author berni3
 */
public class InterpolatorMojoTest {

    @Rule
    public MojoRule rule = new MojoRule() {
        @Override
        protected void before() {
        }

        @Override
        protected void after() {
        }
    };

    public InterpolatorMojoTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    String retrieveFileContent(File f) throws IOException {
        String merged = "";
        try (FileReader fr = new FileReader(f)) {
            merged = IOUtil.toString(fr);
        }
        return merged;
    }

    String replaceLineEndingBySpace(String s) {
        String r = StringUtils.trim(s);
        r = StringUtils.remove(r, '\r');
        r = StringUtils.replace(r, "\n", " ");
        return r;
    }

    /**
     * Test of execute method, of class InterpolationMojo.
     */
    @Test
    public void testExecute() throws Exception {
        File hello_a_txt = new File("target/test-classes/unit/interpolator/hello_a.txt");
        hello_a_txt.delete();
        File lines_abc_txt = new File("target/test-classes/unit/interpolator/lines_abc.txt");
        lines_abc_txt.delete();

        assertTrue(new File("target/test-classes/unit/interpolator/hello_a.txt.j2").exists());
        assertTrue(new File("target/test-classes/unit/interpolator/lines_abc.txt.j2").exists());

        File pom = new File("target/test-classes/unit/interpolator/pom.xml");
        assertNotNull(pom);
        assertTrue(pom.exists());

        InterpolatorMojo interpolatorMojo = (InterpolatorMojo) rule.lookupMojo("interpolator", pom);
        assertNotNull(interpolatorMojo);
        interpolatorMojo.execute();

        assertTrue(hello_a_txt.exists());
        assertEquals("Hello, aValue", replaceLineEndingBySpace(retrieveFileContent(hello_a_txt)));

        assertTrue(lines_abc_txt.exists());
        assertEquals("line1: aValue line2: bValue line3: cValue", replaceLineEndingBySpace(retrieveFileContent(lines_abc_txt)));
    }

    /**
     * Do not need the MojoRule.
     */
    @WithoutMojo
    @Test
    public void testSomethingWhichDoesNotNeedTheMojoAndProbablyShouldBeExtractedIntoANewClassOfItsOwn() {
        // ...
    }
}
