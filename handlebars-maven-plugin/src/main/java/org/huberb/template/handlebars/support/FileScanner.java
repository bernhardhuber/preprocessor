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
import java.util.ArrayList;
import java.util.List;
import org.apache.maven.model.FileSet;
import org.codehaus.plexus.util.DirectoryScanner;
import org.codehaus.plexus.util.StringUtils;

/**
 *
 * @author berni3
 */
public class FileScanner {

    private FileSet theFileSet = new FileSet();

    public FileScanner() {
        theFileSet.setDirectory(new File(".").getAbsolutePath());
    }

    public FileScanner setUpDirectory(File directory) {
        theFileSet.setDirectory(directory.getAbsolutePath());
        return this;
    }

    public FileScanner setUpByFileSet(FileSet templateFiles) {
        theFileSet = templateFiles.clone();
        return this;
    }

    public FileScanner setUpByIncludes(String includesAsString) {
        final String[] includes = StringUtils.split(includesAsString, ",");
        for (String include : includes) {
            String trimmedIncludes = StringUtils.trim(include);
            theFileSet.addInclude(trimmedIncludes);
        }
        return this;
    }

    public List<File> scan() {
        final DirectoryScanner ds = new DirectoryScanner();
        ds.setIncludes(theFileSet.getIncludes().toArray(new String[0]));
        ds.setExcludes(theFileSet.getExcludes().toArray(new String[0]));
        ds.setBasedir(theFileSet.getDirectory());
        ds.scan();
        final String[] includedFiles = ds.getIncludedFiles();
        List<File> result = new ArrayList<>();
        for (int i = 0; i < includedFiles.length; i++) {
            File f = new File(theFileSet.getDirectory(), includedFiles[i]);
            result.add(f);
        }
        return result;
    }

}
