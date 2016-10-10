/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.core.reader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;

public class FileRecordReaderTest {

    private FileRecordReader fileRecordReader;

    private Path emptyDirectory;

    @Before
    public void setUp() throws Exception {
        emptyDirectory = Paths.get("target/foo");
        Files.createDirectory(emptyDirectory);
    }

    @Test
    public void whenDirectoryIsNotEmpty_thenThereShouldBeANextRecordToRead() throws Exception {
        fileRecordReader = new FileRecordReader(new File("src/main/java/org/easybatch/core/reader"));
        fileRecordReader.open();
        assertThat(fileRecordReader.readRecord()).isNotNull(); // there is at least the FileRecordReader.java file
    }

    @Test
    public void whenDirectoryIsEmpty_thenThereShouldBeNoNextRecordToRead() throws Exception {
        fileRecordReader = new FileRecordReader(emptyDirectory);
        fileRecordReader.open();
        assertThat(fileRecordReader.readRecord()).isNull();
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenDirectoryDoesNotExist_thenShouldThrowAnIllegalArgumentException() throws Exception {
        fileRecordReader = new FileRecordReader(Paths.get("src/main/java/ImSureThisDirectoryDoesNotExist"));
        fileRecordReader.open();
    }

    @After
    public void tearDown() throws Exception {
        fileRecordReader.close();
        Files.delete(emptyDirectory);
    }

}
