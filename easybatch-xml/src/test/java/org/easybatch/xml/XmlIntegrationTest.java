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

package org.easybatch.xml;

import org.easybatch.core.api.ComputationalRecordProcessor;
import org.easybatch.core.api.Engine;
import org.easybatch.core.api.Report;
import org.easybatch.core.api.Status;
import org.easybatch.core.impl.EngineBuilder;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for xml processing.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@SuppressWarnings("unchecked")
public class XmlIntegrationTest {

    private static final String EXPECTED_DATA_SOURCE_NAME = "XML stream";

    @Test
    public void testPersonsProcessing() throws Exception {

        final InputStream xmlDataSource = getDataSource("/persons.xml");

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new XmlRecordReader("person", xmlDataSource))
                .mapper(new XmlRecordMapper<Person>(Person.class))
                .processor(new Processor<Person>())
                .build();

        Report report = engine.call();

        assertThatReportIsCorrect(report);

        List<Person> persons = (List<Person>) report.getBatchResult();

        assertThat(persons).isNotEmpty().hasSize(2);

        Person person = persons.get(0);
        assertThat(person.getId()).isEqualTo(1);
        assertThat(person.getFirstName()).isEqualTo("foo");
        assertThat(person.getLastName()).isEqualTo("bar");
        assertThat(person.isMarried()).isTrue();

        person = persons.get(1);
        assertThat(person.getId()).isEqualTo(2);
        assertThat(person.getFirstName()).isEqualTo("bar");
        assertThat(person.getLastName()).isEqualTo("foo");
        assertThat(person.isMarried()).isFalse();

    }

    @Test
    public void testMavenDependenciesProcessing() throws Exception {

        final InputStream xmlDataSource = getDataSource("/dependencies.xml");

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new XmlRecordReader("dependency", xmlDataSource))
                .mapper(new XmlRecordMapper<Dependency>(Dependency.class))
                .processor(new Processor<Dependency>())
                .build();

        Report report = engine.call();

        assertThatReportIsCorrect(report);

        List<Dependency> dependencies = (List<Dependency>) report.getBatchResult();

        assertThat(dependencies).isNotEmpty().hasSize(2);

        Dependency dependency = dependencies.get(0);
        assertThat(dependency).isNotNull();
        assertThat(dependency.getArtifactId()).isEqualTo("junit");
        assertThat(dependency.getGroupId()).isEqualTo("junit");
        assertThat(dependency.getVersion()).isEqualTo("4.12");
        assertThat(dependency.getScope()).isEqualTo("test");
        assertThat(dependency.getClassifier()).isNull();
        assertThat(dependency.getSystemPath()).isNull();
        assertThat(dependency.getType()).isNull();
        assertThat(dependency.getExclusions()).isNull();
        assertThat(dependency.isOptional()).isFalse();

        dependency = dependencies.get(1);
        assertThat(dependency).isNotNull();
        assertThat(dependency.getArtifactId()).isEqualTo("fake-core");
        assertThat(dependency.getGroupId()).isEqualTo("org.fake");
        assertThat(dependency.getVersion()).isEqualTo("1.0");
        assertThat(dependency.getScope()).isNull();
        assertThat(dependency.getClassifier()).isNull();
        assertThat(dependency.getSystemPath()).isNull();
        assertThat(dependency.getType()).isNull();
        assertThat(dependency.isOptional()).isTrue();

        Dependency.Exclusions exclusions = dependency.getExclusions();
        assertThat(exclusions).isNotNull();
        assertThat(exclusions.getExclusion()).hasSize(1);

        Exclusion exclusion = exclusions.getExclusion().get(0);
        assertThat(exclusion).isNotNull();
        assertThat(exclusion.getGroupId()).isNotNull().isEqualTo("some.excluded.dep");
        assertThat(exclusion.getArtifactId()).isNotNull().isEqualTo("dep-core");


    }

    @Test
    public void testXmlProcessingWithCustomNamespace() throws Exception {

        final InputStream xmlDataSource = getDataSource("/beans.xml");

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new XmlRecordReader("bean", xmlDataSource))
                .build();

        Report report = engine.call();

        assertThat(report).isNotNull();
        assertThat(report.getTotalRecords()).isEqualTo(2);

    }

    private void assertThatReportIsCorrect(Report report) {
        assertThat(report).isNotNull();
        assertThat(report.getTotalRecords()).isEqualTo(2);
        assertThat(report.getErrorRecordsCount()).isEqualTo(0);
        assertThat(report.getFilteredRecordsCount()).isEqualTo(0);
        assertThat(report.getIgnoredRecordsCount()).isEqualTo(0);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(0);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(2);
        assertThat(report.getStatus()).isEqualTo(Status.FINISHED);
        assertThat(report.getDataSource()).isEqualTo(EXPECTED_DATA_SOURCE_NAME);
    }

    private InputStream getDataSource(String name) {
        return this.getClass().getResourceAsStream(name);
    }

    private class Processor<T> implements ComputationalRecordProcessor<T, T, List<T>> {

        private List<T> items = new ArrayList<T>();

        @Override
        public T processRecord(T item) {
            items.add(item);
            return item;
        }

        @Override
        public List<T> getComputationResult() {
            return items;
        }

    }

}
