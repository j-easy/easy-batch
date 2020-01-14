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
package org.easybatch.extensions.yaml;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobBuilder;
import org.easybatch.core.job.JobExecutor;
import org.easybatch.core.processor.RecordCollector;
import org.easybatch.core.reader.IterableRecordReader;
import org.easybatch.core.record.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileInputStream;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

public class YamlSupportIntegrationTest {

    private JobExecutor jobExecutor;

    @Before
    public void setUp() throws Exception {
        jobExecutor = new JobExecutor();
    }

    @After
    public void tearDown() throws Exception {
        jobExecutor.shutdown();
    }

    @Test
    public void yamlReadingAndMappingIntegrationTest() throws Exception {
        RecordCollector<Contact> recordCollector = new RecordCollector<>();

        Job job = JobBuilder.aNewJob()
                .reader(new YamlRecordReader(new FileInputStream("src/test/resources/contacts.yml")))
                .mapper(new YamlRecordMapper<>(Contact.class))
                .processor(recordCollector)
                .build();

        jobExecutor.execute(job);

        List<Record<Contact>> records = recordCollector.getRecords();
        assertThat(records).hasSize(3);

        Contact contact = records.get(0).getPayload();

        assertThat(contact.getName()).isEqualTo("Foo");
        assertThat(contact.getAge()).isEqualTo(28);

        contact = records.get(1).getPayload();

        assertThat(contact.getName()).isEqualTo("Bar");
        assertThat(contact.getAge()).isEqualTo(25);

        contact = records.get(2).getPayload();

        assertThat(contact.getName()).isEqualTo("Baz");
        assertThat(contact.getAge()).isEqualTo(30);
    }

    @Test
    public void yamlMarshallingIntegrationTest() throws Exception {
        List<Contact> contacts = Arrays.asList(new Contact("Foo", 28), new Contact("Bar", 30));

        RecordCollector<String> recordCollector = new RecordCollector<>();

        Job job = JobBuilder.aNewJob()
                .reader(new IterableRecordReader(contacts))
                .marshaller(new YamlRecordMarshaller<Contact>())
                .processor(recordCollector)
                .build();

        jobExecutor.execute(job);

        List<Record<String>> records = recordCollector.getRecords();
        assertThat(records).hasSize(2);

        Record<String> record = records.get(0);

        assertThat(record).isNotNull();
        assertThat(record.getPayload()).isEqualTo("!org.easybatch.extensions.yaml.Contact" + LINE_SEPARATOR +
                "name: Foo" + LINE_SEPARATOR +
                "age: 28" + LINE_SEPARATOR);

        record = records.get(1);

        assertThat(record).isNotNull();
        assertThat(record.getPayload()).isEqualTo("!org.easybatch.extensions.yaml.Contact" + LINE_SEPARATOR +
                "name: Bar" + LINE_SEPARATOR +
                "age: 30" + LINE_SEPARATOR);

    }
}
