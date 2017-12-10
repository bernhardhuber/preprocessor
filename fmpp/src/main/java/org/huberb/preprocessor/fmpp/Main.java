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

import fmpp.tools.CommandLine;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author berni3
 */
public class Main {

    public static void main(String[] args) {
        new FmppLauncher().runFmppCommandLine(args);
    }

    static class FmppLauncher {

        void runFmppCommandLine(String[] args) {
            CommandLine.main(args);
        }

    }

    static class FmppSingleInputOutputBuilder {

        File propFile;
        File inputFile;
        File outputFile;

        List<String> build() throws IOException {
            List<String> args = new ArrayList<>();
            args.add("-v");
            args.add(inputFile.getAbsolutePath());
            args.add("-o");
            args.add(outputFile.getAbsolutePath());
            args.add("-D");
            args.add("properties(" + propFile.getAbsolutePath() + ")");
            return args;
        }
    }

    static class FmppDirInputOutputBuilder {

        File propFile;
        File inputDir;
        File outputDir;

        List<String> build() {
            List<String> args = new ArrayList<>();
            args.add("-v");
            args.add("-S ");
            args.add(inputDir.getAbsolutePath());
            args.add("-O");
            args.add(outputDir.getAbsolutePath());
            args.add("-D");
            args.add("properties(" + propFile.getAbsolutePath() + ")");
            return args;
        }
    }
}
