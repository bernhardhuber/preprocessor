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
package org.huberb.template.support;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

/**
 *
 * @author berni3
 */
public class FileScannerTest {

    @Rule
    public TemporaryFolder folder = new TemporaryFolder();

    public FileScannerTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of scan method, of class FileScanner.
     */
    @Test
    public void testScan() throws IOException {
        //---
        this.folder.newFile("a.txt");
        this.folder.newFile("b.bbb");
        FileScanner instance = new FileScanner();
        instance.setUpDirectory(this.folder.getRoot().getAbsoluteFile());
        instance.setUpByIncludes("*.txt");
        List<File> result = instance.scan();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(new File(this.folder.getRoot(), "a.txt"), result.get(0));
    }

    /**
     * Test of scan method, of class FileScanner.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testScan_sub1Folder() throws IOException {
        //---
        File rootDir = this.folder.getRoot();
        assertTrue(new File(rootDir, "a.txt").createNewFile());
        assertTrue(new File(rootDir, "b.bbb").createNewFile());
        File sub1Dir = this.folder.newFolder("sub1");
        assertTrue(new File(sub1Dir, "sub1a.txt").createNewFile());
        assertTrue(new File(sub1Dir, "sub1b.bbb").createNewFile());

        FileScanner instance = new FileScanner();
        instance.setUpDirectory(this.folder.getRoot().getAbsoluteFile());
        instance.setUpByIncludes("**/*.txt");
        List<File> result = instance.scan();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(new File(rootDir, "a.txt"), result.get(0));
        assertEquals(new File(sub1Dir, "sub1a.txt"), result.get(1));
    }

}
