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

package org.easybatch.extensions.apache.common.csv;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.easybatch.core.writer.StandardOutputRecordWriter;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;

import java.io.FileReader;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobBuilder.aNewJob;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

public class ApacheCommonCsvSupportIntegrationTest {

    @Rule
    public final SystemOutRule systemOut = new SystemOutRule().enableLog();

    @Test
    public void testAllComponentsTogether() throws Exception {

        CSVFormat csvFormat = CSVFormat.DEFAULT.withHeader("id", "user", "message");
        CSVParser parser = new CSVParser(new FileReader(this.getClass().getResource("/tweets.csv").getFile()), csvFormat);

        aNewJob()
                .reader(new ApacheCommonCsvRecordReader(parser))
                .mapper(new ApacheCommonCsvRecordMapper(Tweet.class))
                .marshaller(new ApacheCommonCsvRecordMarshaller(Tweet.class, new String[]{"id", "user", "message"}, ';', '\''))
                .writer(new StandardOutputRecordWriter())
                .call();

        assertThat(systemOut.getLog()).isEqualTo("'1';'foo';'hello'" + LINE_SEPARATOR +
                "'2';'bar';'hey'" + LINE_SEPARATOR +
                "'3';'baz';'hi'" + LINE_SEPARATOR);
    }
}
