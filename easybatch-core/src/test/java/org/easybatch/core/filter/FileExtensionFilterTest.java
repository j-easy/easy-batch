/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.core.filter;

import org.easybatch.core.util.FileRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test class for {@link FileExtensionFilter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class FileExtensionFilterTest {

    private static String FILE_SEPARATOR = System.getProperty("file.separator");

    private FileExtensionFilter filter;

    private FileRecord txtRecord, xmlRecord, mdRecord;

    @Before
    public void setUp() throws Exception {
        filter = new FileExtensionFilter(Arrays.asList(".txt", ".xml"));
        File currentDirectory = new File("");
        txtRecord = new FileRecord(1, new File(currentDirectory.getAbsoluteFile() + FILE_SEPARATOR + "CHANGELOG.txt"));
        xmlRecord = new FileRecord(2, new File(currentDirectory.getAbsoluteFile() + FILE_SEPARATOR + "pom.xml"));
        mdRecord = new FileRecord(3, new File(currentDirectory.getAbsoluteFile() + FILE_SEPARATOR +"README.md"));
    }

    @Test
    public void whenTheFileNameEndsWithOneOfTheGivenExtensions_ThenItShouldBeFiltered() {
        assertThat(filter.filterRecord(txtRecord)).isTrue();
        assertThat(filter.filterRecord(xmlRecord)).isTrue();
    }

    @Test
    public void whenTheFileNameDoesNotEndWithOneOfTheGivenExtensions_ThenItShouldBeFiltered() {
        assertThat(filter.filterRecord(mdRecord)).isFalse();
    }

}
