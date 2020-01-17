/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.jeasy.batch.extensions.apache.common.csv;

import org.jeasy.batch.core.processor.RecordCollector;
import org.jeasy.batch.core.reader.StringRecordReader;
import org.jeasy.batch.core.util.Utils;
import org.jeasy.batch.test.common.Tweet;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.jeasy.batch.core.job.JobBuilder.aNewJob;
import static org.jeasy.batch.core.util.Utils.LINE_SEPARATOR;

public class ApacheCommonCsvSupportIntegrationTest {

    @Test
    public void testAllComponentsTogether() throws Exception {
        String dataSource = "1,foo,hello" + LINE_SEPARATOR + "2,bar,hey" + LINE_SEPARATOR + "3,baz,hi";

        RecordCollector<String> recordCollector = new RecordCollector<>();
        aNewJob()
                .reader(new StringRecordReader(dataSource))
                .mapper(new ApacheCommonCsvRecordMapper<>(Tweet.class, "id", "user", "message"))
                .marshaller(new ApacheCommonCsvRecordMarshaller<>(Tweet.class, new String[]{"id", "user", "message"}, ';', '\''))
                .processor(recordCollector)
                .build().call();


        List<String> records = Utils.extractPayloads(recordCollector.getRecords());

        assertThat(records).containsExactly("'1';'foo';'hello'", "'2';'bar';'hey'", "'3';'baz';'hi'");
    }
}
