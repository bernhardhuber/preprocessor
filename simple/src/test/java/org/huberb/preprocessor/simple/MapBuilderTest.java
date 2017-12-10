/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.preprocessor.simple;

import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author berni3
 */
public class MapBuilderTest {

    public MapBuilderTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of delimiter method, of class MapBuilder.
     */
    @Test
    public void testDelimiter_String_String() {

        String b = "#{";
        String e = "}";
        MapBuilder instance = new MapBuilder();
        Map<String, String> result = instance.delimiter(b, e).add("k", "v").build();
        assertEquals("v", result.get("#{k}"));

    }

    /**
     * Test of delimiter method, of class MapBuilder.
     */
    @Test
    public void testDelimiter_String() {

        MapBuilder instance = new MapBuilder();
        Map<String, String> result = instance.delimiter("#{*}").add("k", "v").build();
        assertEquals("v", result.get("#{k}"));
    }

    /**
     * Test of add method, of class MapBuilder.
     */
    @Test
    public void testAdd() {
        MapBuilder instance = new MapBuilder();
        Map<String, String> result = instance.add("a", "A").add("b", "B").build();
        assertEquals("A", result.get("${a}"));
        assertEquals("B", result.get("${b}"));
        assertEquals(null, result.get("${c}"));
    }

    /**
     * Test of build method, of class MapBuilder.
     */
    @Test
    public void testBuild() {
        MapBuilder instance = new MapBuilder();
        Map<String, String> result = instance.build();
        assertEquals(0, result.size());
    }

}
