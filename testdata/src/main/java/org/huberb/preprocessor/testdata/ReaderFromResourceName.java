/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.preprocessor.testdata;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.function.Function;

/**
 *
 * @author berni3
 */
public class ReaderFromResourceName implements Function<String, Reader> {

    @Override
    public Reader apply(String resourceName) {

        try (Reader r = new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream(resourceName))) {
            return r;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Cannot create Reader for " + resourceName, ex);
        }
    }

}
