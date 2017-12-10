/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.preprocessor.simple;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.huberb.preprocessor.simple.SimpleWrapper.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author berni3
 */
public class SimpleWrapperTest {

    public SimpleWrapperTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testMappedMerge() throws IOException {
        Map<String, String> m = new HashMap<>();
        m.put("${quick}", "VERY QUICK");
        m.put("${brown}", "BRRROWN");

        StringWriter sw = new StringWriter();
        StringReader sr = new StringReader("The ${quick} ${brown} fox");
        SimpleWrapper.SimpleMerger instance = new SimpleWrapper.SimpleMerger();
        SimpleWrapper.Response res = new Response();
        SimpleWrapper.Request req = new SimpleWrapper.Request.Builder().
                dataModel(m).
                writer(sw).
                reader(sr).
                build();

        instance.merge(req, res);
        assertEquals("The VERY QUICK BRRROWN fox" + System.lineSeparator(), 
                sw.toString());
    }

    @Test
    public void testSimpleMerge() throws IOException {
        StringWriter sw = new StringWriter();
        StringReader sr = new StringReader("The ${quick} fox");
        SimpleWrapper.SimpleMerger instance = new SimpleWrapper.SimpleMerger();
        SimpleWrapper.Response res = new Response();
        SimpleWrapper.Request req = new SimpleWrapper.Request.Builder().
                dataModel("${quick}", "SO_QUICK").
                writer(sw).
                reader(sr).
                build();

        instance.merge(req, res);
        assertEquals("The SO_QUICK fox" + System.lineSeparator(), 
                sw.toString());
    }

    @Test
    public void testSimpleMerge_Multiline() throws IOException {
        StringWriter sw = new StringWriter();
        StringReader sr = new StringReader("The ${quick} brown fox" + System.lineSeparator()
                + "Are you ${quick}?"
        );

        SimpleWrapper.SimpleMerger instance = new SimpleWrapper.SimpleMerger();
        SimpleWrapper.Response res = new Response();
        SimpleWrapper.Request req = new SimpleWrapper.Request.Builder().
                dataModel("${quick}", "SO_QUICK").
                writer(sw).
                reader(sr).
                build();

        instance.merge(req, res);
        assertEquals("The SO_QUICK brown fox" + System.lineSeparator()
                + "Are you SO_QUICK?" + System.lineSeparator(),
                sw.toString());
    }
}
