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
import org.codehaus.plexus.util.IOUtil;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author berni3
 */
public class InterpolateTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public InterpolateTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    void createFileContent(File f, String s) throws IOException {
        try (FileWriter fw = new FileWriter(f)) {
            fw.write(s);
        }
    }

    String retrieveFileContent(File f) throws IOException {
        String merged = "";
        try (FileReader fr = new FileReader(f)) {
            merged = IOUtil.toString(fr);
        }
        return merged;
    }

    @Test
    public void testMerge() throws IOException, Interpolate.InterpolateException {
        Interpolate instance = new Interpolate();

        File infile = folder.newFile("merge_infile");
        createFileContent(infile, "Hello, {{a}}");
        File outfile = new File(folder.getRoot(), "merge_outfile");
        //---
        instance.addKeyValue("a", "aValue");
        instance.merge(infile, outfile);

        String merged = retrieveFileContent(outfile);
        assertEquals("Hello, aValue", merged);
    }

    @Test
    public void testMerge_varyToken_dollarlbrace_rbrace() throws IOException, Interpolate.InterpolateException {
        Interpolate instance = new Interpolate();

        File infile = folder.newFile("merge_infile");
        createFileContent(infile, "Hello, ${a}");
        File outfile = new File(folder.getRoot(), "merge_outfile");
        //--
        instance.beginTokenEndToken("${", "}");
        instance.addKeyValue("a", "aValue2");
        instance.merge(infile, outfile);

        String merged = retrieveFileContent(outfile);
        assertEquals("Hello, aValue2", merged);
    }
    @Test
    public void testMerge_varyToken_hash_lrbracket_rbracket() throws IOException, Interpolate.InterpolateException {
        Interpolate instance = new Interpolate();

        File infile = folder.newFile("merge_infile");
        createFileContent(infile, "Hello, #[a]");
        File outfile = new File(folder.getRoot(), "merge_outfile");
        //--
        instance.beginTokenEndToken("#[", "]");
        instance.addKeyValue("a", "#[aValue2]");
        instance.merge(infile, outfile);

        String merged = retrieveFileContent(outfile);
        assertEquals("Hello, #[aValue2]", merged);
    }
    @Test
    public void testMerge_varyToken_Ampersand_Ampersand() throws IOException, Interpolate.InterpolateException {
        Interpolate instance = new Interpolate();

        File infile = folder.newFile("merge_infile");
        createFileContent(infile, "Hello, @a@");
        File outfile = new File(folder.getRoot(), "merge_outfile");
        //--
        instance.beginTokenEndToken("@", "@");
        instance.addKeyValue("a", "a@Value@3");
        instance.merge(infile, outfile);

        String merged = retrieveFileContent(outfile);
        assertEquals("Hello, a@Value@3", merged);
    }

}
