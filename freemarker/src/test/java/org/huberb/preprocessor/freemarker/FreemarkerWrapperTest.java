/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.preprocessor.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author berni3
 */
public class FreemarkerWrapperTest {

    public FreemarkerWrapperTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testFreemarkerMergerProperties() throws IOException, TemplateException {
        Properties properties = new Properties();
        properties.load(
                new BufferedReader(
                        new FileReader(
                                new File("target/test-classes/newproperties.properties")
                        )
                )
        );

        StringWriter sw = new StringWriter();
        FreemarkerWrapper.FreemarkerRequest.Builder b = new FreemarkerWrapper.FreemarkerRequest.Builder();
        FreemarkerWrapper.FreemarkerRequest fmReq = b.out(sw).templateName("template123.txt").dataModel(properties).build();
        FreemarkerWrapper.FreemarkerResponse fmResp = new FreemarkerWrapper.FreemarkerResponse();

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
        configuration.setDirectoryForTemplateLoading(new File("target/test-classes"));
        FreemarkerWrapper.FreemarkerMerger fmMerger = new FreemarkerWrapper.FreemarkerMerger(configuration);
        fmMerger.process(fmReq, fmResp);

        String expected = ""
                + "Template123" + System.lineSeparator()
                + "" + System.lineSeparator()
                + "prop1-value is value1" + System.lineSeparator()
                + "prop2-value is value2" + System.lineSeparator()
                + "prop3-value is value3" + System.lineSeparator();
        expected = expected.trim().replaceAll("\r\n", "@");

        String result = sw.toString().trim().replaceAll("[\r\n]", "@");
        assertEquals(expected, result);
    }

    @Test
    public void testFreemarkerMergerMap() throws IOException, TemplateException {

        Map<String, String> dm = new HashMap<>();
        dm.put("prop1", "value1");
        dm.put("prop2", "value2");
        dm.put("prop3", "value3");

        StringWriter sw = new StringWriter();
        FreemarkerWrapper.FreemarkerRequest.Builder b = new FreemarkerWrapper.FreemarkerRequest.Builder();
        FreemarkerWrapper.FreemarkerRequest fmReq = b.out(sw).templateName("template123.txt").dataModel(dm).build();

        FreemarkerWrapper.FreemarkerResponse fmResp = new FreemarkerWrapper.FreemarkerResponse();
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
        configuration.setDirectoryForTemplateLoading(new File("target/test-classes"));
        FreemarkerWrapper.FreemarkerMerger fmMerger = new FreemarkerWrapper.FreemarkerMerger(configuration);
        fmMerger.process(fmReq, fmResp);

        String expected = ""
                + "Template123" + System.lineSeparator()
                + "" + System.lineSeparator()
                + "prop1-value is value1" + System.lineSeparator()
                + "prop2-value is value2" + System.lineSeparator()
                + "prop3-value is value3" + System.lineSeparator();
        expected = expected.trim().replaceAll("\r\n", "@");

        String result = sw.toString().trim().replaceAll("[\r\n]", "@");
        assertEquals(expected, result);
    }
}
