/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
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

package io.github.benas.easybatch.core.test;

import io.github.benas.easybatch.core.mapper.ObjectMapper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;

/**
 * Unit test class for {@link io.github.benas.easybatch.core.mapper.ObjectMapper}.
 * @author benas (md.benhassine@gmail.com)
 */
public class ObjectMapperTest {

    private ObjectMapper mapper;


    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper<Person>(Person.class,
                new String[]{"firstName","lastName", "age", "birthDate", "isMarried"});
    }

    @Test
    public void testPersonTypeMapping() throws Exception {

        // prepare mock record fields
        String[] fields = new String[]{"foo", "bar", "30", "1990-12-12", "true"};

        // map record to Person bean
        Person person = (Person) mapper.mapObject(fields);

        // person bean must be not null and correctly populated
        Assert.assertNotNull(person);
        Assert.assertEquals("foo", person.getFirstName());
        Assert.assertEquals("bar", person.getLastName());
        Assert.assertEquals(30, person.getAge());
        Assert.assertEquals("1990-12-12", new SimpleDateFormat("yyyy-MM-dd").format(person.getBirthDate()));
        Assert.assertEquals(true, person.isMarried());
    }

    @Test
    public void testExtendedPersonTypeMapping() throws Exception {

        mapper = new ObjectMapper<ExtendedPerson>(ExtendedPerson.class,
                new String[]{"firstName","lastName", "age", "birthDate", "isMarried", "nickName"});

        // prepare mock record fields
        String[] fields = new String[]{"foo", "bar", "30", "1990-12-12", "true", "FB"};

        // map record to extended person bean
        ExtendedPerson extendedPerson = (ExtendedPerson) mapper.mapObject(fields);

        // extended person bean must be not null and correctly populated
        Assert.assertNotNull(extendedPerson);
        Assert.assertEquals("foo", extendedPerson.getFirstName());
        Assert.assertEquals("bar", extendedPerson.getLastName());
        Assert.assertEquals(30, extendedPerson.getAge());
        Assert.assertEquals("1990-12-12", new SimpleDateFormat("yyyy-MM-dd").format(extendedPerson.getBirthDate()));
        Assert.assertEquals(true, extendedPerson.isMarried());
        Assert.assertEquals("FB", extendedPerson.getNickName());
    }

    @After
    public void tearDown() throws Exception {
        mapper = null;
        System.gc();
    }

}



