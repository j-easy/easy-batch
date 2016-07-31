/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.xml;

import org.easybatch.core.job.JobReport;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;

@SuppressWarnings("unchecked")
public class XmlBatchReaderTest {

    private static final int BATCH_SIZE = 2;

    private XmlBatchReader xmlBatchReader;

    @Before
    public void setUp() throws Exception {
        xmlBatchReader = new XmlBatchReader(getDataSource("/data.xml"), "data", BATCH_SIZE);
    }

    @Test
    public void testXmlBatchProcessing() throws Exception {

        JobReport jobReport = aNewJob()
                .reader(xmlBatchReader)
                .processor(new RecordCollector())
                .call();

        List<Batch> batches = (List<Batch>) jobReport.getResult();

        assertThat(batches).hasSize(3);

        Batch batch1 = batches.get(0);
        List<Record> records = batch1.getPayload();
        assertThat(records).hasSize(2);
        assertThat(records.get(0).getHeader().getNumber()).isEqualTo(1L);
        assertThat(records.get(0).getPayload().toString()).isXmlEqualTo("<data>1</data>");
        assertThat(records.get(1).getHeader().getNumber()).isEqualTo(2L);
        assertThat(records.get(1).getPayload().toString()).isXmlEqualTo("<data>2</data>");

        Batch batch2 = batches.get(1);
        records = batch2.getPayload();
        assertThat(records).hasSize(2);
        assertThat(records.get(0).getHeader().getNumber()).isEqualTo(3L);
        assertThat(records.get(0).getPayload().toString()).isXmlEqualTo("<data>3</data>");
        assertThat(records.get(1).getHeader().getNumber()).isEqualTo(4L);
        assertThat(records.get(1).getPayload().toString()).isXmlEqualTo("<data>4</data>");

        Batch batch3 = batches.get(2);
        records = batch3.getPayload();
        assertThat(records).hasSize(1);
        assertThat(records.get(0).getHeader().getNumber()).isEqualTo(5L);
        assertThat(records.get(0).getPayload().toString()).isXmlEqualTo("<data>5</data>");
    }

    @Test
    public void testEmptyDataSource() throws Exception {
        xmlBatchReader = new XmlBatchReader(getDataSource("/empty-file.xml"), "data", BATCH_SIZE);
        JobReport jobReport = aNewJob()
                .reader(xmlBatchReader)
                .processor(new RecordCollector())
                .build().call();

        List<Batch> batches = (List<Batch>) jobReport.getResult();

        assertThat(batches).isNotNull().isEmpty();
    }

    private InputStream getDataSource(String name) {
        return this.getClass().getResourceAsStream(name);
    }
}
