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
package org.easybatch.core.field;

import org.easybatch.core.beans.Gender;
import org.easybatch.core.beans.Person;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

public class BeanFieldExtractorTest {

    private Person person;

    private BeanFieldExtractor<Person> fieldExtractor;

    @Before
    public void setUp() throws Exception {
        person = new Person();
        person.setFirstName("myFirstName");
        person.setLastName("myLastName");
        person.setBirthDate(new Date());
        person.setAge(33);
        person.setGender(Gender.FEMALE);
        person.setMarried(true);
    }

    @Test
    public void whenFieldsIsEmpty_ThenItShouldExtractAllPropertiesValues() throws Exception {
        fieldExtractor = new BeanFieldExtractor<>(Person.class);
        Iterable<Object> values = fieldExtractor.extractFields(person);
        assertThat(values).hasSize(6);
        assertThat(values).containsOnlyOnce(
                person.getFirstName(), person.getLastName(), person.getBirthDate(),
                person.getAge(), person.getGender(), person.isMarried());
    }

    @Test
    public void whenFieldsIsNotEmpty_ThenItShouldExtractAllPropertiesValuesInRightOrder() throws Exception {
        fieldExtractor = new BeanFieldExtractor<>(Person.class, "lastName", "age", "married");
        Iterable<Object> values = fieldExtractor.extractFields(person);
        assertThat(values).containsExactly(person.getLastName(), person.getAge(), person.isMarried());
    }

}
