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
package org.huberb.template.handlebars.support;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.codehaus.plexus.util.FileUtils;

/**
 * Calculate outputFiles for each given inputFile.
 *
 * @author berni3
 */
public class FileCalculator {

    public List<InputFileOutputFilePair> calculateFromInputFiles(String suffix, List<File> inputFiles) {
        final Set<File> processedInputFiles = new HashSet<>();
        final List<InputFileOutputFilePair> result = new ArrayList<>();
        for (File inputFile : inputFiles) {
            //---
            final File normalizedInputFile;
            try {
                normalizedInputFile = inputFile.getCanonicalFile();
            } catch (IOException ex) {
                continue;
            }

            //---
            if (processedInputFiles.contains(normalizedInputFile)) {
                continue;
            } else {
                processedInputFiles.add(normalizedInputFile);
            }

            final String outputname = FileUtils.basename(normalizedInputFile.getAbsolutePath(), suffix);
            final File inputFileParentFile = normalizedInputFile.getParentFile();

            //---
            final InputFileOutputFilePair inputFileOutpuFilePair = new InputFileOutputFilePair(
                    normalizedInputFile.getAbsoluteFile(),
                    FileUtils.resolveFile(inputFileParentFile, outputname)
            );
            result.add(inputFileOutpuFilePair);
        }
        return result;
    }

    /**
     * Wrapping class for pair of inputFile, outputFile.
     *
     */
    public static class InputFileOutputFilePair {

        private final File inputFile;
        private final File outputFile;

        public InputFileOutputFilePair(File inputFile, File outputFile) {
            this.inputFile = inputFile;
            this.outputFile = outputFile;
        }

        public File getInputFile() {
            return this.inputFile;
        }

        public File getOutputFile() {
            return this.outputFile;
        }

        @Override
        public String toString() {
            return "InputFileOutputFilePair{"
                    + "inputFile=" + inputFile + ", "
                    + "outputFile=" + outputFile + '}';
        }
    }
}
