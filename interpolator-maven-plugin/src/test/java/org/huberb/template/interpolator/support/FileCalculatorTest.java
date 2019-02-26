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
package org.huberb.template.interpolator.support;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import org.codehaus.plexus.util.FileUtils;
import org.codehaus.plexus.util.StringUtils;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author berni3
 */
public class FileCalculatorTest {

    public FileCalculatorTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of calculateFromInputFiles method, of class FileCalculator.
     */
    @Test
    public void testCalculateFromInputFiles() {
        String suffix = ".suffix";
        List<File> inputFiles = Arrays.asList(
                new File("aFile.txt.suffix"),
                new File("bFile.suffix"),
                new File("cFile.txt")
        );
        FileCalculator instance = new FileCalculator();
        List<FileCalculator.InputFileOutputFilePair> result = instance.calculateFromInputFiles(suffix, inputFiles);
        assertNotNull(result);
        assertEquals(3, result.size());

        Function<String, String> fnNormalize = (String fn) -> {
            File f = new File(".", fn);
            String abs = f.getAbsolutePath();
            String abs2 = FileUtils.normalize(StringUtils.replace(abs, "\\", "/"));
            String abs3 = StringUtils.replace(abs2, "/", File.separator);
            return abs3;
        };
        String[] data = {
            //---
            fnNormalize.apply("aFile.txt.suffix"),
            fnNormalize.apply("aFile.txt"),
            //---
            fnNormalize.apply("bFile.suffix"),
            fnNormalize.apply("bFile"),
            //---
            fnNormalize.apply("cFile.txt"),
            fnNormalize.apply("cFile.txt")};
        for (int i = 0; i < result.size(); i++) {
            String m = "" + i;
            int dataIndex = i * 2;

            assertTrue(m, result.get(i).getInputFile().isAbsolute());
            assertTrue(m, result.get(i).getOutputFile().isAbsolute());

            assertEquals(m, data[dataIndex], result.get(i).getInputFile().getAbsolutePath());
            assertEquals(m, data[dataIndex + 1], result.get(i).getOutputFile().getAbsolutePath());
        }
    }

    /**
     * Test of calculateFromInputFiles method, of class FileCalculator.
     */
    @Test
    public void testCalculateFromInputFiles_emptyInputFiles() {
        String suffix = ".suffix";
        List<File> inputFiles = new ArrayList<>();
        FileCalculator instance = new FileCalculator();
        List<FileCalculator.InputFileOutputFilePair> result = instance.calculateFromInputFiles(suffix, inputFiles);
        assertNotNull(result);
        assertEquals(true, result.isEmpty());
    }

}
