/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.huberb.preprocessor.testdata;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

/**
 *
 * @author berni3
 */
public class FileFromResourceName implements Function<String, File> {

    @Override
    public File apply(String resourceName) {
        final String prefix = createPrefix(resourceName);
        final String suffix = createSuffix(resourceName);
        try {
            File f = File.createTempFile(prefix, suffix);
            try (InputStream r = this.getClass().getClassLoader().getResourceAsStream(resourceName)) {
                Files.copy(r, f.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
            f.deleteOnExit();
            return f;
        } catch (IOException ex) {
            throw new IllegalArgumentException("Cannot create File for " + resourceName, ex);
        }
    }

    private String createPrefix(String resourceName) {
        final String result = "_"
                + resourceName.
                        replace('.', '_').
                        replace(':', '_').
                        replace('/', '_').
                        replace('\\', '_');
        return result;
    }

    private static final AtomicInteger count = new AtomicInteger(0);

    private String createSuffix(String resourceName) {
        final String result = "_"
                + count.addAndGet(1)
                + "_"
                + System.nanoTime();
        return result;
    }

}
