package org.easybatch.flatfile;

import org.easybatch.core.api.ComputationalRecordProcessor;
import org.easybatch.core.api.Report;
import org.easybatch.core.api.Status;
import org.easybatch.core.impl.Engine;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.flatfile.dsv.DelimitedRecordMapper;
import org.easybatch.flatfile.flr.FixedLengthRecordMapper;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for flat file processing.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class FlatFileIntegrationTest {

    @SuppressWarnings("unchecked")
    @Test
    public void testCsvProcessing() throws Exception {

        File dataSource = new File(getFileUri("/persons.csv"));
        final ComputationalRecordProcessor personProcessor = new PersonProcessor();

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new DelimitedRecordMapper(Person.class, new String[]{"firstName", "lastName", "age", "birthDate", "married"}))
                .processor(personProcessor)
                .build();

        Report report = engine.call();

        assertReportIsValid(report);

        List<Person> persons = (List<Person>) personProcessor.getComputationResult();

        assertPersons(persons);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCsvSubRecordProcessing() throws Exception {

        File dataSource = new File(getFileUri("/persons.csv"));
        final ComputationalRecordProcessor personProcessor = new PersonProcessor();

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new DelimitedRecordMapper(Person.class, new Integer[]{2, 4}, new String[]{"age", "married"}))
                .processor(personProcessor)
                .build();

        Report report = engine.call();

        assertReportIsValid(report);

        List<Person> persons = (List<Person>) personProcessor.getComputationResult();

        assertPersonsFieldSubsetMapping(persons);

    }

    /*
     * Test field names convention over configuration
     */

    @SuppressWarnings("unchecked")
    @Test
    public void whenFieldNamesAreNotSpecified_thenTheyShouldBeRetrievedFromTheHeaderRecord() throws Exception {

        File dataSource = new File(getFileUri("/persons_with_header.csv"));
        final ComputationalRecordProcessor personProcessor = new PersonProcessor();

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new DelimitedRecordMapper(Person.class))
                .processor(personProcessor)
                .build();

        Report report = engine.call();

        assertReportWithIgnoredHeaderRecord(report);

        List<Person> persons = (List<Person>) personProcessor.getComputationResult();

        assertPersons(persons);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void whenOnlySubsetOfFieldsAreSpecified_thenOnlyCorrespondingFieldsShouldBeMapped() throws Exception {

        File dataSource = new File(getFileUri("/persons_with_header.csv"));
        final ComputationalRecordProcessor personProcessor = new PersonProcessor();

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new DelimitedRecordMapper(Person.class, new Integer[]{2, 4}))
                .processor(personProcessor)
                .build();

        Report report = engine.call();

        assertReportWithIgnoredHeaderRecord(report);

        List<Person> persons = (List<Person>) personProcessor.getComputationResult();

        assertPersonsFieldSubsetMapping(persons);

    }

    /*
     * Test fixed-length record processing
     */

    @SuppressWarnings("unchecked")
    @Test
    public void testFlrProcessing() throws Exception {

        File dataSource = new File(getFileUri("/persons.flr"));
        final ComputationalRecordProcessor personProcessor = new PersonProcessor();

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new FixedLengthRecordMapper(Person.class, new int[]{4, 4, 2, 10, 1}, new String[]{"firstName", "lastName", "age", "birthDate", "married"}))
                .processor(personProcessor)
                .build();

        Report report = engine.call();

        assertReportIsValid(report);

        List<Person> persons = (List<Person>) personProcessor.getComputationResult();

        assertThat(persons).isNotEmpty().hasSize(2);

        final Person person1 = persons.get(0);
        assertThat(person1.getFirstName()).isEqualTo("foo ");
        assertThat(person1.getLastName()).isEqualTo("bar ");
        assertThat(person1.getAge()).isEqualTo(30);
        assertThat(person1.isMarried()).isTrue();

        final Person person2 = persons.get(1);
        assertThat(person2.getFirstName()).isEqualTo("toto");
        assertThat(person2.getLastName()).isEqualTo("titi");
        assertThat(person2.getAge()).isEqualTo(15);
        assertThat(person2.isMarried()).isFalse();

    }

    private void assertReportIsValid(Report report) {
        assertReport(report);
        assertThat(report.getTotalRecords()).isEqualTo(2);
        assertThat(report.getIgnoredRecordsCount()).isEqualTo(0);
    }

    private void assertReportWithIgnoredHeaderRecord(Report report) {
        assertReport(report);
        assertThat(report.getTotalRecords()).isEqualTo(3);
        assertThat(report.getIgnoredRecordsCount()).isEqualTo(1);
    }

    private void assertReport(Report report) {
        assertThat(report).isNotNull();
        assertThat(report.getErrorRecordsCount()).isEqualTo(0);
        assertThat(report.getFilteredRecordsCount()).isEqualTo(0);
        assertThat(report.getRejectedRecordsCount()).isEqualTo(0);
        assertThat(report.getSuccessRecordsCount()).isEqualTo(2);
        assertThat(report.getStatus()).isEqualTo(Status.FINISHED);
    }

    private void assertPersonsFieldSubsetMapping(List<Person> persons) {
        assertThat(persons).isNotEmpty().hasSize(2);

        final Person person1 = persons.get(0);
        assertThat(person1.getFirstName()).isNull();
        assertThat(person1.getLastName()).isNull();
        assertThat(person1.getBirthDate()).isNull();
        assertThat(person1.getAge()).isEqualTo(30);
        assertThat(person1.isMarried()).isTrue();

        final Person person2 = persons.get(1);
        assertThat(person2.getFirstName()).isNull();
        assertThat(person2.getLastName()).isNull();
        assertThat(person2.getBirthDate()).isNull();
        assertThat(person2.getAge()).isEqualTo(15);
        assertThat(person2.isMarried()).isFalse();
    }

    private void assertPersons(List<Person> persons) {
        assertThat(persons).isNotEmpty().hasSize(2);

        final Person person1 = persons.get(0);
        assertThat(person1.getFirstName()).isEqualTo("foo");
        assertThat(person1.getLastName()).isEqualTo("bar");
        assertThat(person1.getAge()).isEqualTo(30);
        assertThat(person1.isMarried()).isTrue();

        final Person person2 = persons.get(1);
        assertThat(person2.getFirstName()).isEqualTo("bar");
        assertThat(person2.getLastName()).isEqualTo("foo");
        assertThat(person2.getAge()).isEqualTo(15);
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

    private URI getFileUri(String fileName) throws URISyntaxException {
        return this.getClass().getResource(fileName).toURI();
    }

}
