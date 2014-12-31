/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.core.mapper;

import org.easybatch.core.beans.ExtendedPerson;
import org.easybatch.core.beans.Person;
import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Unit test class for {@link ObjectMapper}.
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ObjectMapperTest {

    @Test
    public void whenValuesAreValid_ThenTheMappedObjectShouldBeCorrectlyPopulated() throws Exception {

        ObjectMapper<Person> mapper = new ObjectMapper<Person>(Person.class,
                new String[]{"firstName", "lastName", "age", "birthDate", "isMarried"});

        // input values
        String[] fields = new String[]{"foo", "bar", "30", "1990-12-12", "true"};

        Person person = mapper.mapObject(fields);

        // person bean must be not null
        assertThat(person).isNotNull();

        // person bean must be correctly populated
        assertThat(person.getFirstName()).isEqualTo("foo");
        assertThat(person.getLastName()).isEqualTo("bar");
        assertThat(person.getAge()).isEqualTo(30);
        assertThat(new SimpleDateFormat("yyyy-MM-dd").format(person.getBirthDate())).isEqualTo("1990-12-12");
        assertThat(person.isMarried()).isTrue();
    }

    @Test
    public void whenValuesAreValid_ThenInheritedFieldsOfTheMappedObjectShouldBeCorrectlyPopulated() throws Exception {

        ObjectMapper<ExtendedPerson> mapper = new ObjectMapper<ExtendedPerson>(ExtendedPerson.class,
                new String[]{"firstName", "lastName", "age", "birthDate", "isMarried", "nickName"});

        // input values
        String[] fields = new String[]{"foo", "bar", "30", "1990-12-12", "true", "FB"};

        // map record to extended person bean
        ExtendedPerson extendedPerson = mapper.mapObject(fields);

        // extended person bean must be not null
        assertThat(extendedPerson).isNotNull();

        // extended person bean must be correctly populated
        assertThat(extendedPerson.getFirstName()).isEqualTo("foo");
        assertThat(extendedPerson.getLastName()).isEqualTo("bar");
        assertThat(extendedPerson.getAge()).isEqualTo(30);
        assertThat(new SimpleDateFormat("yyyy-MM-dd").format(extendedPerson.getBirthDate())).isEqualTo("1990-12-12");
        assertThat(extendedPerson.isMarried()).isTrue();
        assertThat(extendedPerson.getNickName()).isEqualTo("FB");
    }

}



