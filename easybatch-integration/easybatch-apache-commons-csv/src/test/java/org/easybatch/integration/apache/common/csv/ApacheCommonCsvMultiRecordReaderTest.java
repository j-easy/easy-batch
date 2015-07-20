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

package org.easybatch.integration.apache.common.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.easybatch.core.api.Engine;
import org.easybatch.core.api.Report;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.record.MultiRecord;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.impl.EngineBuilder.aNewEngine;

/**
 * Test class for {@link ApacheCommonCsvMultiRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ApacheCommonCsvMultiRecordReaderTest {

    private static final int CHUNK_SIZE = 2;

    private ApacheCommonCsvMultiRecordReader apacheCommonCsvMultiRecordReader;

    @Before
    public void setUp() throws Exception {
        CSVFormat csvFormat = CSVFormat.DEFAULT;
        CSVParser parser = new CSVParser(new FileReader(new File(getDataSource("/tweets.csv").getFile())), csvFormat);
        apacheCommonCsvMultiRecordReader = new ApacheCommonCsvMultiRecordReader(parser, CHUNK_SIZE);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testChunkProcessing() throws Exception {

        Engine engine = aNewEngine()
                .reader(apacheCommonCsvMultiRecordReader)
                .processor(new RecordCollector<MultiRecord>())
                .build();

        Report report = engine.call();
        assertThat(report.getTotalRecords()).isEqualTo(2);

        List<MultiRecord> multiRecords = (List<MultiRecord>) report.getBatchResult();
        assertThat(multiRecords).isNotNull().hasSize(2);

        MultiRecord chunk1 = multiRecords.get(0);
        assertThat(chunk1.getPayload().size()).isEqualTo(2);
        CSVRecord tweet = (CSVRecord) chunk1.getPayload().get(0).getPayload();
        assertThat(tweet).isNotNull();
        assertThat(tweet.get(0)).isEqualTo("1");
        assertThat(tweet.get(1)).isEqualTo("foo");
        assertThat(tweet.get(2)).isEqualTo("hello");

        tweet = (CSVRecord) chunk1.getPayload().get(1).getPayload();
        assertThat(tweet).isNotNull();
        assertThat(tweet.get(0)).isEqualTo("2");
        assertThat(tweet.get(1)).isEqualTo("bar");
        assertThat(tweet.get(2)).isEqualTo("hey");

        MultiRecord chunk2 = multiRecords.get(1);
        assertThat(chunk2.getPayload().size()).isEqualTo(1);
        tweet = (CSVRecord) chunk2.getPayload().get(0).getPayload();
        assertThat(tweet).isNotNull();
        assertThat(tweet.get(0)).isEqualTo("3");
        assertThat(tweet.get(1)).isEqualTo("baz");
        assertThat(tweet.get(2)).isEqualTo("hi");
    }

    private URL getDataSource(String name) {
        return this.getClass().getResource(name);
    }

}
