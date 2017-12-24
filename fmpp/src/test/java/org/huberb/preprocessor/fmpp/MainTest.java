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
package org.huberb.preprocessor.fmpp;

import java.io.File;
import java.io.IOException;
import java.util.List;
import org.huberb.preprocessor.fmpp.Main.FmppSingleInputOutputBuilder;
import org.huberb.preprocessor.testdata.FileFromResourceName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author berni3
 */
public class MainTest {

    public MainTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of main method, of class Main.
     */
    @Test
    public void testMain() throws IOException {
        FmppSingleInputOutputBuilder fmppSingleInputOutputBuilder = new FmppSingleInputOutputBuilder();
        fmppSingleInputOutputBuilder.inputFile = new FileFromResourceName().apply("template123.txt");
        fmppSingleInputOutputBuilder.propFile = new FileFromResourceName().apply("newproperties.properties");
        fmppSingleInputOutputBuilder.outputFile = new File("target/_template123.txt");
        List<String> argList = fmppSingleInputOutputBuilder.build();
        String[] args = argList.toArray(new String[argList.size()]);

        Main.main(args);
    }

}
