/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package org.easybatch.core.reader;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.Report;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.MultiRecord;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.impl.EngineBuilder.aNewEngine;
import static org.easybatch.core.util.Utils.FILE_SEPARATOR;
import static org.easybatch.core.util.Utils.JAVA_IO_TMPDIR;

/**
 * Test class for {@link FileMultiRecordReader}.
 * 
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class FileMultiRecordReaderTest {

    private static final int CHUNK_SIZE = 2;

    private File dataSource;

    private File test1, test2, test3, test4;

    private FileMultiRecordReader fileMultiRecordReader;

    @Before
    public void setUp() throws Exception {
        // TODO use https://github.com/google/jimfs when moving to Java 7
        dataSource = new File(JAVA_IO_TMPDIR + FILE_SEPARATOR + "testFileMultiRecordReader");
        dataSource.mkdir();
        createTestFiles(dataSource);
        fileMultiRecordReader = new FileMultiRecordReader(dataSource, CHUNK_SIZE);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testMultiFileReading() throws Exception {
        Report report = aNewEngine()
                .reader(fileMultiRecordReader)
                .processor(new RecordCollector<MultiRecord>())
                .build().call();

        List<MultiRecord> multiRecords = (List<MultiRecord>) report.getJobResult();

        assertThat(multiRecords).isNotNull().hasSize(2);

        MultiRecord chunk1 = multiRecords.get(0);
        MultiRecord chunk2 = multiRecords.get(1);

        List<Record> records = chunk1.getPayload();
        assertThat(records.size()).isEqualTo(2);

        records = chunk2.getPayload();
        assertThat(records.size()).isEqualTo(2);
    }

    @After
    public void tearDown() throws Exception {
        cleanUpTestFiles();
    }

    private void createTestFiles(File dataSource) throws IOException {
        String basePath = dataSource.getAbsolutePath() + FILE_SEPARATOR;
        test1 = new File(basePath + "test1.txt");
        test2 = new File(basePath + "test2.txt");
        test3 = new File(basePath + "test3.txt");
        test4 = new File(basePath + "test4.txt");
        test1.createNewFile();
        test2.createNewFile();
        test3.createNewFile();
        test4.createNewFile();
    }

    private void cleanUpTestFiles() {
        test1.delete();
        test2.delete();
        test3.delete();
        test4.delete();
        dataSource.delete();
    }

}