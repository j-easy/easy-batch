package org.easybatch.flatfile;

import org.easybatch.core.api.Job;
import org.easybatch.core.api.JobReport;
import org.easybatch.core.api.JobStatus;
import org.easybatch.core.api.TypeConverter;
import org.easybatch.core.converter.DateTypeConverter;
import org.easybatch.core.filter.HeaderRecordFilter;
import org.easybatch.core.impl.JobBuilder;
import org.easybatch.core.processor.RecordCollector;
import org.junit.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SuppressWarnings("unchecked")
public class FlatFileIntegrationTest {

    @Test
    public void testCsvProcessing() throws Exception {

        File dataSource = new File(getFileUri("/persons.csv"));

        Job job = JobBuilder.aNewJob()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new DelimitedRecordMapper(Person.class, new String[]{"firstName", "lastName", "age", "birthDate", "married"}))
                .processor(new RecordCollector<Person>())
                .build();

        JobReport jobReport = job.call();

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(2);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.FINISHED);
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(2);

        List<Person> persons = (List<Person>) jobReport.getResult();

        assertPersons(persons);

    }

    @Test
    public void testCsvSubRecordProcessing() throws Exception {

        File dataSource = new File(getFileUri("/persons.csv"));

        Job job = JobBuilder.aNewJob()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new DelimitedRecordMapper(Person.class, new Integer[]{2, 4}, new String[]{"age", "married"}))
                .processor(new RecordCollector<Person>())
                .build();

        JobReport jobReport = job.call();

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(2);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.FINISHED);
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(2);

        List<Person> persons = (List<Person>) jobReport.getResult();

        assertPersonsFieldSubsetMapping(persons);

    }

    /*
     * Test field names convention over configuration
     */

    @Test
    public void whenFieldNamesAreNotSpecified_thenTheyShouldBeRetrievedFromTheHeaderRecord() throws Exception {

        File dataSource = new File(getFileUri("/persons_with_header.csv"));

        Job job = JobBuilder.aNewJob()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new DelimitedRecordMapper(Person.class))
                .processor(new RecordCollector<Person>())
                .build();

        JobReport jobReport = job.call();

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(1);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(2);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.FINISHED);
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(3);

        List<Person> persons = (List<Person>) jobReport.getResult();

        assertPersons(persons);

    }

    @Test
    public void whenOnlySubsetOfFieldsAreSpecified_thenOnlyCorrespondingFieldsShouldBeMapped() throws Exception {

        File dataSource = new File(getFileUri("/persons_with_header.csv"));

        Job job = JobBuilder.aNewJob()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new DelimitedRecordMapper(Person.class, new Integer[]{2, 4}))
                .processor(new RecordCollector<Person>())
                .build();

        JobReport jobReport = job.call();

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(1);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(2);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.FINISHED);
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(3);

        List<Person> persons = (List<Person>) jobReport.getResult();

        assertPersonsFieldSubsetMapping(persons);

    }

    @Test
    public void testComplaintsDataProcessing() throws Exception {

        //source: http://catalog.data.gov/dataset/consumer-complaint-database
        File dataSource = new File(getFileUri("/complaints.csv"));

        DelimitedRecordMapper recordMapper = new DelimitedRecordMapper(Complaint.class,
                new String[]{"id", "product", "subProduct", "issue", "subIssue", "state", "zipCode", "channel",
                        "receivedDate", "sentDate", "company", "companyResponse", "timelyResponse", "consumerDisputed"});
        recordMapper.registerTypeConverter(new TypeConverter<String, Channel>() {
            @Override
            public Channel convert(String value) {
                return Channel.valueOf(value.toUpperCase());
            }
        });
        recordMapper.registerTypeConverter(new DateTypeConverter("MM/dd/yyyy"));

        Job job = JobBuilder.aNewJob()
                .reader(new FlatFileRecordReader(dataSource))
                .filter(new HeaderRecordFilter())
                .mapper(recordMapper)
                .processor(new RecordCollector<Complaint>())
                .build();

        JobReport jobReport = job.call();

        assertThat(jobReport).isNotNull();

        List<Complaint> complaints = (List<Complaint>) jobReport.getResult();

        assertThat(complaints).isNotEmpty().hasSize(10);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
        assertComplaint(complaints.get(0),
                1355160, "Student loan", "Non-federal student loan",
                "Dealing with my lender or servicer", null, "NJ",
                "08807", Channel.WEB,
                simpleDateFormat.parse("04/30/2015"), simpleDateFormat.parse("04/30/2015"),
                "Transworld Systems Inc.", "In progress", true, false);

        assertComplaint(complaints.get(9),
                1351334, "Money transfers", "International money transfer",
                "Money was not available when promised", null, "TX", "78666", Channel.PHONE,
                simpleDateFormat.parse("04/28/2015"), simpleDateFormat.parse("04/29/2015"),
                "MoneyGram", "In progress", true, false);

    }

    /*
     * Test fixed-length record processing
     */

    @Test
    public void testFlrProcessing() throws Exception {

        File dataSource = new File(getFileUri("/persons.flr"));

        Job job = JobBuilder.aNewJob()
                .reader(new FlatFileRecordReader(dataSource))
                .mapper(new FixedLengthRecordMapper(Person.class, new int[]{4, 4, 2, 10, 1},
                        new String[]{"firstName", "lastName", "age", "birthDate", "married"}))
                .processor(new RecordCollector<Person>())
                .build();

        JobReport jobReport = job.call();

        assertThat(jobReport).isNotNull();
        assertThat(jobReport.getMetrics().getErrorCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getFilteredCount()).isEqualTo(0);
        assertThat(jobReport.getMetrics().getSuccessCount()).isEqualTo(2);
        assertThat(jobReport.getStatus()).isEqualTo(JobStatus.FINISHED);
        assertThat(jobReport.getMetrics().getTotalCount()).isEqualTo(2);

        List<Person> persons = (List<Person>) jobReport.getResult();

        assertThat(persons).isNotEmpty().hasSize(2);

        assertPerson(persons.get(0), "foo ", "bar ", 30, true);
        assertPerson(persons.get(1), "toto", "titi", 15, false);

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
