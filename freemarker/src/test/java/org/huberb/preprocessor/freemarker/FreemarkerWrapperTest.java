/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.preprocessor.freemarker;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.huberb.preprocessor.testdata.PropertiesFromResourceName;
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
        Properties properties = new PropertiesFromResourceName().apply("newproperties.properties");

        StringWriter sw = new StringWriter();
        FreemarkerWrapper.FreemarkerRequest.Builder b = new FreemarkerWrapper.FreemarkerRequest.Builder();
        FreemarkerWrapper.FreemarkerRequest fmReq = b.out(sw).templateName("template123.txt").dataModel(properties).build();
        FreemarkerWrapper.FreemarkerResponse fmResp = new FreemarkerWrapper.FreemarkerResponse();

        Configuration configuration = new Configuration(Configuration.VERSION_2_3_21);
        configuration.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "/");
        FreemarkerWrapper.FreemarkerMerger fmMerger = new FreemarkerWrapper.FreemarkerMerger(configuration);
        fmMerger.process(fmReq, fmResp);

        String expected = ""
                + "Template123@@prop1-value is value1@prop2-value is value2@prop3-value is value3";

        String result = normalizeMultiLine(sw.toString());
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
        configuration.setClassLoaderForTemplateLoading(this.getClass().getClassLoader(), "/");
        FreemarkerWrapper.FreemarkerMerger fmMerger = new FreemarkerWrapper.FreemarkerMerger(configuration);
        fmMerger.process(fmReq, fmResp);

        String expected = ""
                + "Template123@@prop1-value is value1@prop2-value is value2@prop3-value is value3";

        String result = normalizeMultiLine(sw.toString());
        assertEquals(expected, result);
    }

    String normalizeMultiLine(String s) {
        String x = s.trim();
        x = x.replace("\r", "");
        x = x.replace('\n', '@');
        return x;
    }
}
