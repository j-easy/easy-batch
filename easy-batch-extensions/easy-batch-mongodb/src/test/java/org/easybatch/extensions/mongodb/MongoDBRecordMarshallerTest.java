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
package org.easybatch.extensions.mongodb;

import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.HashSet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MongoDBRecordMarshallerTest {

    private static final ObjectId ID = ObjectId.get();

    @Mock
    private Record<Person> record;
    @Mock
    private Header header;

    private MongoDBRecordMarshaller<Person> mongoDBRecordMarshaller;

    @Before
    public void setUp() throws Exception {
        Person person = new Person();
        person.setId(ID);
        person.setFirstName("foo");
        person.setLastName("bar");
        person.setMarried(true);
        when(record.getHeader()).thenReturn(header);
        when(record.getPayload()).thenReturn(person);

        mongoDBRecordMarshaller = new MongoDBRecordMarshaller<>(Person.class);
    }

    @Test
    public void processRecord() throws Exception {
        MongoDBRecord actual = mongoDBRecordMarshaller.processRecord(record);

        assertThat(actual.getHeader()).isEqualTo(header);
        DBObject payload = actual.getPayload();
        assertThat(payload.keySet()).isEqualTo(new HashSet<>(Arrays.asList("className", "_id", "firstName", "lastName", "married")));
        assertThat(payload.get("_id")).isEqualTo(ID);
        assertThat(payload.get("className")).isEqualTo("org.easybatch.extensions.mongodb.Person");
        assertThat(payload.get("firstName")).isEqualTo("foo");
        assertThat(payload.get("lastName")).isEqualTo("bar");
        assertThat(payload.get("married")).isEqualTo(true);
    }

}
