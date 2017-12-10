/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.preprocessor.interpolation;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import org.huberb.preprocessor.interpolation.InterpolationWrapper.InterpolationMerger;
import org.huberb.preprocessor.interpolation.InterpolationWrapper.Request;
import org.huberb.preprocessor.interpolation.InterpolationWrapper.Response;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author berni3
 */
public class InterpolationWrapperTest {

    public InterpolationWrapperTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testProcess() throws IOException {
        Map<String, String> map = new HashMap<>();
        map.put("a", "A");
        StringReader in = new StringReader("abc ${a} xyz");
        StringWriter out = new StringWriter();
        InterpolationWrapper.InterpolationMerger instance = new InterpolationMerger();
        Request request = new Request.Builder().
                dataModel(map).
                reader(in).
                writer(out).build();

        Response response = new Response();
        instance.process(request, response);
        assertEquals("abc A xyz", out.toString());
    }
}
