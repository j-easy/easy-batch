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

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.FILE_SEPARATOR;
import static org.easybatch.core.util.Utils.JAVA_IO_TMPDIR;

public class FileRecordReaderTest {

    private FileRecordReader fileRecordReader;

    private File dataSource;

    private File emptyDataSource;

    private File nonExistingDataSource;

    @Before
    public void setUp() throws Exception {
        dataSource = new File(JAVA_IO_TMPDIR);
        fileRecordReader = new FileRecordReader(dataSource);
        fileRecordReader.open();

        nonExistingDataSource = new File(JAVA_IO_TMPDIR + FILE_SEPARATOR + "ImSureThisDirectoryDoesNotExist");

        //create empty directory
        emptyDataSource = new File("easyBatchTestEmptyDirectory");
        emptyDataSource.mkdir();
    }

    @After
    public void tearDown() throws Exception {
        emptyDataSource.delete();
        fileRecordReader.close();
    }

    @Test
    public void whenTheDataSourceIsNotEmpty_ThenThereShouldBeANextRecordToRead() throws Exception {
        assertThat(fileRecordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void theDataSourceNameShouldBeEqualToTheDirectoryAbsolutePath() throws Exception {
        assertThat(fileRecordReader.getDataSourceName()).isEqualTo(dataSource.getAbsolutePath());
    }

    @Test(expected = IllegalArgumentException.class)
    public void whenTheDirectoryDoesNotExist_ThenShouldThrowAnIllegalArgumentException() throws Exception {
        fileRecordReader.close();
        fileRecordReader = new FileRecordReader(nonExistingDataSource);
        fileRecordReader.open();
    }

    /*
     * Empty directory tests
     */

    @Test
    public void whenTheDataSourceIsEmpty_ThenThereShouldBeNoNextRecordToRead() throws Exception {
        fileRecordReader.close();
        fileRecordReader = new FileRecordReader(emptyDataSource);
        fileRecordReader.open();
        assertThat(fileRecordReader.hasNextRecord()).isFalse();
    }

    @Test
    public void whenTheDataSourceIsEmpty_ThenTotalRecordsShouldBeEqualToZero() throws Exception {
        fileRecordReader.close();
        fileRecordReader = new FileRecordReader(emptyDataSource);
        fileRecordReader.open();
        assertThat(fileRecordReader.getTotalRecords()).isEqualTo(0);
    }

}
