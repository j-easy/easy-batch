package org.easybatch.flatfile;

import org.easybatch.core.api.Engine;
import org.easybatch.core.api.Report;
import org.easybatch.core.api.Status;
import org.easybatch.core.api.TypeConverter;
import org.easybatch.core.converter.DateTypeConverter;
import org.easybatch.core.filter.HeaderRecordFilter;
import org.easybatch.core.impl.EngineBuilder;
import org.easybatch.core.processor.RecordCollector;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration test for flat file processing.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@SuppressWarnings("unchecked")
public class FlatFileIntegrationTest {

    @Test
    public void testCsvProcessing() throws Exception {

        File dataSource = new File(getFileUri("/persons.csv"));

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new DelimitedRecordMapper(Person.class, new String[]{"firstName", "lastName", "age", "birthDate", "married"}))
                .processor(new RecordCollector<Person>())
                .build();

        Report report = engine.call();

        assertReportIsCorrect(report);

        List<Person> persons = (List<Person>) report.getBatchResult();

        assertPersons(persons);

    }

    @Test
    public void testCsvSubRecordProcessing() throws Exception {

        File dataSource = new File(getFileUri("/persons.csv"));

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new DelimitedRecordMapper(Person.class, new Integer[]{2, 4}, new String[]{"age", "married"}))
                .processor(new RecordCollector<Person>())
                .build();

        Report report = engine.call();

        assertReportIsCorrect(report);

        List<Person> persons = (List<Person>) report.getBatchResult();

        assertPersonsFieldSubsetMapping(persons);

    }

    /*
     * Test field names convention over configuration
     */

    @Test
    public void whenFieldNamesAreNotSpecified_thenTheyShouldBeRetrievedFromTheHeaderRecord() throws Exception {

        File dataSource = new File(getFileUri("/persons_with_header.csv"));

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new DelimitedRecordMapper(Person.class))
                .processor(new RecordCollector<Person>())
                .build();

        Report report = engine.call();

        assertReportWithIgnoredHeaderRecord(report);

        List<Person> persons = (List<Person>) report.getBatchResult();

        assertPersons(persons);

    }

    @Test
    public void whenOnlySubsetOfFieldsAreSpecified_thenOnlyCorrespondingFieldsShouldBeMapped() throws Exception {

        File dataSource = new File(getFileUri("/persons_with_header.csv"));

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new DelimitedRecordMapper(Person.class, new Integer[]{2, 4}))
                .processor(new RecordCollector<Person>())
                .build();

        Report report = engine.call();

        assertReportWithIgnoredHeaderRecord(report);

        List<Person> persons = (List<Person>) report.getBatchResult();

        assertPersonsFieldSubsetMapping(persons);

    }

    @Test
    public void testComplaintsDataProcessing() throws Exception {

        //source: http://catalog.data.gov/dataset/consumer-complaint-database
        File dataSource = new File(getFileUri("/complaints.csv"));

        DelimitedRecordMapper recordMapper = new DelimitedRecordMapper(Complaint.class,
                new String[]{"id", "product", "subProduct", "issue", "subIssue", "state", "zipCode", "channel",
                        "receivedDate", "sentDate", "company", "companyResponse", "timelyResponse", "consumerDisputed"});
        recordMapper.registerTypeConverter(new TypeConverter<Channel>() {
            @Override
            public Channel convert(String value) {
                return Channel.valueOf(value.toUpperCase());
            }
        });
        recordMapper.registerTypeConverter(new DateTypeConverter("MM/dd/yyyy"));

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new FlatFileRecordReader(dataSource))
                .filter(new HeaderRecordFilter())
                .mapper(recordMapper)
                .processor(new RecordCollector<Complaint>())
                .build();

        Report report = engine.call();

        assertThat(report).isNotNull();

        List<Complaint> complaints = (List<Complaint>) report.getBatchResult();

        assertThat(complaints).isNotEmpty().hasSize(10);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        assertComplaint(complaints.get(0),
                1355160, "Student loan", "Non-federal student loan",
                "Dealing with my lender or servicer", "", "NJ",
                "08807", Channel.WEB,
                simpleDateFormat.parse("04/30/2015"), simpleDateFormat.parse("04/30/2015"),
                "Transworld Systems Inc.", "In progress", true, false);

        assertComplaint(complaints.get(9),
                1351334, "Money transfers", "International money transfer",
                "Money was not available when promised", "", "TX", "78666", Channel.PHONE,
                simpleDateFormat.parse("04/28/2015"), simpleDateFormat.parse("04/29/2015"),
                "MoneyGram", "In progress", true, false);

    }

    /*
     * Test fixed-length record processing
     */

    @Test
    public void testFlrProcessing() throws Exception {

        File dataSource = new File(getFileUri("/persons.flr"));

        Engine engine = EngineBuilder.aNewEngine()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new FixedLengthRecordMapper(Person.class, new int[]{4, 4, 2, 10, 1},
                        new String[]{"firstName", "lastName", "age", "birthDate", "married"}))
                .processor(new RecordCollector<Person>())
                .build();

        Report report = engine.call();

        assertReportIsCorrect(report);

        List<Person> persons = (List<Person>) report.getBatchResult();

        assertThat(persons).isNotEmpty().hasSize(2);

        assertPerson(persons.get(0), "foo ", "bar ", 30, true);
        assertPerson(persons.get(1), "toto", "titi", 15, false);

    }

    private void assertReportIsCorrect(Report report) {
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

        Person person = persons.get(0);
        assertThat(person.getFirstName()).isNull();
        assertThat(person.getLastName()).isNull();
        assertThat(person.getBirthDate()).isNull();
        assertThat(person.getAge()).isEqualTo(30);
        assertThat(person.isMarried()).isTrue();

        person = persons.get(1);
        assertThat(person.getFirstName()).isNull();
        assertThat(person.getLastName()).isNull();
        assertThat(person.getBirthDate()).isNull();
        assertThat(person.getAge()).isEqualTo(15);
        assertThat(person.isMarried()).isFalse();
    }

    private void assertPersons(List<Person> persons) {
        assertThat(persons).isNotEmpty().hasSize(2);
        assertPerson(persons.get(0), "foo", "bar", 30, true);
        assertPerson(persons.get(1), "bar", "foo", 15, false);
    }

    private void assertPerson(Person person, String firstName, String lastName, int age, boolean married) {
        assertThat(person.getFirstName()).isEqualTo(firstName);
        assertThat(person.getLastName()).isEqualTo(lastName);
        assertThat(person.getAge()).isEqualTo(age);
        assertThat(person.isMarried()).isEqualTo(married);
    }

    private void assertComplaint(Complaint complaint, int id, String product, String subProduct, String issue, String subIssue,
                                 String state, String zipCode, Channel channel, Date receivedDate, Date sentDate,
                                 String company, String companyResponse, boolean timelyResponse, boolean consumerDisputed) {

        assertThat(complaint).isNotNull();
        assertThat(complaint.getId()).isEqualTo(id);
        assertThat(complaint.getProduct()).isEqualTo(product);
        assertThat(complaint.getSubProduct()).isEqualTo(subProduct);
        assertThat(complaint.getIssue()).isEqualTo(issue);
        assertThat(complaint.getSubIssue()).isEqualTo(subIssue);
        assertThat(complaint.getState()).isEqualTo(state);
        assertThat(complaint.getZipCode()).isEqualTo(zipCode);
        assertThat(complaint.getChannel()).isEqualTo(channel);
        assertThat(complaint.getReceivedDate()).isEqualTo(receivedDate);
        assertThat(complaint.getSentDate()).isEqualTo(sentDate);
        assertThat(complaint.getCompany()).isEqualTo(company);
        assertThat(complaint.getCompanyResponse()).isEqualTo(companyResponse);
        assertThat(complaint.isTimelyResponse()).isEqualTo(timelyResponse);
        assertThat(complaint.isConsumerDisputed()).isEqualTo(consumerDisputed);
    }

    private URI getFileUri(String fileName) throws URISyntaxException {
        return this.getClass().getResource(fileName).toURI();
    }

}
