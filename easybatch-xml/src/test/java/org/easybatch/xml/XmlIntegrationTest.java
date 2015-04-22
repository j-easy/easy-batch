package org.easybatch.xml;

import org.easybatch.core.api.ComputationalRecordProcessor;
import org.easybatch.core.api.Report;
import org.easybatch.core.api.Status;
import org.easybatch.core.impl.Engine;
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
public class XmlIntegrationTest {

    private static final String EXPECTED_DATA_SOURCE_NAME = "XML stream";

    @SuppressWarnings("unchecked")
    @Test
    public void testXmlProcessing() throws Exception {

        final InputStream xmlDataSource = this.getClass().getResourceAsStream("/persons.xml");
        final ComputationalRecordProcessor recordProcessor = new PersonProcessor();

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new XmlRecordReader("person", xmlDataSource))
                .mapper(new XmlRecordMapper<Person>(Person.class))
                .processor(recordProcessor)
                .build();

        Report report = engine.call();

        assertThat(report).isNotNull();
        assertThat(report.getTotalRecords()).isEqualTo(2);
        assertThat(report.getErrorRecordsCount()).isEqualTo(0);
        assertThat(report.getFilteredRecordsCount()).isEqualTo(0);
        assertThat(report.getIgnoredRecordsCount()).isEqualTo(0);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(0);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(2);
        assertThat(report.getStatus()).isEqualTo(Status.FINISHED);
        assertThat(report.getDataSource()).isEqualTo(EXPECTED_DATA_SOURCE_NAME);

        List<Person> persons = (List<Person>) recordProcessor.getComputationResult();

        assertThat(persons).isNotEmpty().hasSize(2);

        final Person person1 = persons.get(0);
        assertThat(person1.getId()).isEqualTo(1);
        assertThat(person1.getFirstName()).isEqualTo("foo");
        assertThat(person1.getLastName()).isEqualTo("bar");
        assertThat(person1.isMarried()).isTrue();

        final Person person2 = persons.get(1);
        assertThat(person2.getId()).isEqualTo(2);
        assertThat(person2.getFirstName()).isEqualTo("bar");
        assertThat(person2.getLastName()).isEqualTo("foo");
        assertThat(person2.isMarried()).isFalse();

    }

    private static class PersonProcessor implements ComputationalRecordProcessor<Person, Person, List<Person>> {

        private List<Person> persons = new ArrayList<Person>();

        @Override
        public Person processRecord(Person person) throws Exception {
            persons.add(person);
            return person;
        }

        @Override
        public List<Person> getComputationResult() {
            return persons;
        }

    }

}
