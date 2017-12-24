/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.preprocessor.testdata;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Properties;
import java.util.function.Function;

/**
 *
 * @author berni3
 */
public class PropertiesFromResourceName implements Function<String, Properties> {

    @Override
    public Properties apply(String resourceName) {
        Properties properties = new Properties();
        try (Reader r = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(resourceName))) {
            properties.load(r);
        } catch (IOException ioex) {
            throw new IllegalArgumentException("Cannot load " + resourceName, ioex);
        }
        return properties;
    }
}
