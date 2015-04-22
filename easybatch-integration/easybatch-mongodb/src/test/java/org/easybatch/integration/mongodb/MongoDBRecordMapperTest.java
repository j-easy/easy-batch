package org.easybatch.integration.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.easybatch.core.api.Header;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link MongoDBRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class MongoDBRecordMapperTest {

    public static final String ID = "507f1f77bcf86cd799439011";
    public static final String FIRST_NAME = "foo";
    public static final String LAST_NAME = "bar";
    public static final Date DATE = new Date();
    public static final boolean MARRIED = false;

    private MongoDBRecordMapper<Person> mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new MongoDBRecordMapper<Person>(Person.class);
    }

    @Test
    public void testPersonMapping() throws Exception {

        DBObject personDBObject = new BasicDBObject()
                .append("_id", ID)
                .append("firstName", FIRST_NAME)
                .append("lastName", LAST_NAME)
                .append("birthDate", DATE)
                .append("married", MARRIED);
        MongoDBRecord mongoDBRecord = new MongoDBRecord(new Header(1l, "MongoDB", new Date()), personDBObject);

        Person person = mapper.mapRecord(mongoDBRecord);

        assertThat(person).isNotNull();
        assertThat(person.getId()).isEqualTo(ID);
        assertThat(person.getFirstName()).isEqualTo(FIRST_NAME);
        assertThat(person.getLastName()).isEqualTo(LAST_NAME);
        assertThat(person.getBirthDate()).isEqualTo(DATE);
        assertThat(person.isMarried()).isEqualTo(MARRIED);
    }
}
