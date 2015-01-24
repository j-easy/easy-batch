package org.easybatch.integration.mongodb;

import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import org.easybatch.core.api.Record;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link MongoDBRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class MongoDBRecordReaderTest {

    public static final int TOTAL_RECORDS = 10;

    public static final String DATA_SOURCE_NAME = "things";

    private MongoDBRecordReader reader;

    @Mock
    private DBCollection collection;

    @Mock
    private DBObject query;

    @Mock
    private DBCursor cursor;

    @Mock
    private DBObject dbObject;

    @Before
    public void setUp() throws Exception {
        reader = new MongoDBRecordReader(collection, query);

        when(collection.find(query)).thenReturn(cursor);
        when(cursor.hasNext()).thenReturn(true);
        when(cursor.next()).thenReturn(dbObject);
        when(cursor.count()).thenReturn(TOTAL_RECORDS);
        when(collection.getName()).thenReturn(DATA_SOURCE_NAME);

        reader.open();
    }

    @Test
    public void testHasNextRecord() throws Exception {
        boolean actual = reader.hasNextRecord();

        assertThat(actual).isTrue();
        verify(cursor).hasNext();
    }

    @Test
    public void testGetNextRecord() throws Exception {
        Record record = reader.readNextRecord();

        assertThat(record.getNumber()).isEqualTo(1);
        assertThat(record.getPayload()).isEqualTo(dbObject);
        verify(cursor).next();
    }

    @Test
    public void testGetTotalRecords() throws Exception {
        Integer totalRecords = reader.getTotalRecords();

        assertThat(totalRecords).isEqualTo(TOTAL_RECORDS);
        verify(cursor).count();
    }

    @Test
    public void testGetDataSourceName() throws Exception {
        String dataSourceName = reader.getDataSourceName();

        assertThat(dataSourceName).isEqualTo("MongoDB collection: " + DATA_SOURCE_NAME);
        verify(collection).getName();
    }

    @After
    public void tearDown() throws Exception {
        reader.close();
    }
}
