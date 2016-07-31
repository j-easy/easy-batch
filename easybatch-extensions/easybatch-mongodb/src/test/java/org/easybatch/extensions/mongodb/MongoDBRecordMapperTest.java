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

package org.easybatch.extensions.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.bson.types.ObjectId;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class MongoDBRecordMapperTest {

    public static final ObjectId ID = ObjectId.get();
    public static final String FIRST_NAME = "foo";
    public static final String LAST_NAME = "bar";
    public static final Date DATE = new Date();
    public static final boolean MARRIED = false;

    private MongoDBRecordMapper<Person> mapper;

    @Mock
    private Header header;

    @Before
    public void setUp() throws Exception {
        mapper = new MongoDBRecordMapper<>(Person.class);
    }

    @Test
    public void testPersonMapping() throws Exception {

        DBObject personDBObject = new BasicDBObject()
                .append("_id", ID)
                .append("firstName", FIRST_NAME)
                .append("lastName", LAST_NAME)
                .append("birthDate", DATE)
                .append("married", MARRIED);
        MongoDBRecord mongoDBRecord = new MongoDBRecord(header, personDBObject);

        GenericRecord<Person> actual = mapper.processRecord(mongoDBRecord);
        assertThat(actual.getHeader()).isEqualTo(header);

        Person person = actual.getPayload();
        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(ID);
        assertThat(person.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(person.getLastName()).isEqualTo(LAST_NAME);
        assertThat(person.getBirthDate()).isEqualTo(DATE);
        assertThat(person.isMarried()).isEqualTo(MARRIED);
    }
}
