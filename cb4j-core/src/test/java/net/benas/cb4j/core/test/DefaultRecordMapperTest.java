/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
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

package net.benas.cb4j.core.test;

import net.benas.cb4j.core.api.RecordMapper;
import net.benas.cb4j.core.impl.DefaultRecordMapperImpl;
import net.benas.cb4j.core.model.DsvRecord;
import net.benas.cb4j.core.model.Field;
import net.benas.cb4j.core.model.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.*;

/**
 * Unit test class for {@link net.benas.cb4j.core.impl.DefaultRecordMapperImpl}
 * @author benas (md.benhassine@gmail.com)
 */
public class DefaultRecordMapperTest {

    private RecordMapper recordMapper;

    private Record record;

    @Before
    public void setUp() throws Exception {
        recordMapper = new DefaultRecordMapperImpl("net.benas.cb4j.core.test.Person", new String[]{"firstName","lastName"});
        record = new DsvRecord(1,",","'");
    }

    @Test
    public void testMapHelloWorldType() throws Exception {

        // prepare mock record
        Field firstNameField = new Field(0, "hello");
        Field lastNameField = new Field(1, "world");
        record.getFields().add(firstNameField);
        record.getFields().add(lastNameField);

        // map record to Person bean
        Person person = (Person) recordMapper.mapRecord(record);

        // person bean must be not null and correctly populated
        assertNotNull(person);
        assertEquals(person.getFirstName(), "hello");
        assertEquals(person.getLastName(), "world");
    }

    @After
    public void tearDown() throws Exception {
        System.gc();
    }

}



