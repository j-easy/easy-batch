package org.easybatch.jdbc;

import org.junit.*;

import java.io.File;
import java.sql.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link JdbcRecordReader}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JdbcRecordReaderTest {

    private static final String DATABASE_URL = "jdbc:hsqldb:mem";

    private static final String DATA_SOURCE_NAME = "Connection URL: jdbc:hsqldb:mem | Query string: select * from tweet";

    private JdbcRecordReader jdbcRecordReader;

    private static Connection connection;

    private static String query;

    @BeforeClass
    public static void initDatabase() throws Exception {
        connection = DriverManager.getConnection(DATABASE_URL, "sa", "pwd");
        createTweetTable(connection);
        populateTweetTable(connection);
        query = "select * from tweet";
    }

    @Before
    public void setUp() throws Exception {
        jdbcRecordReader = new JdbcRecordReader(connection, query);
        jdbcRecordReader.open();
    }

    @Test
    public void whenThereIsNextRecord_thenShouldHaveNextRecord() throws Exception {
        assertThat(jdbcRecordReader.hasNextRecord()).isTrue();
    }

    @Test
    public void testReadNextRecord() throws Exception {
        jdbcRecordReader.hasNextRecord(); //should call this method to move the cursor forward to the first row
        JdbcRecord actual = jdbcRecordReader.readNextRecord();

        assertThat(actual).isNotNull();
        assertThat(actual.getHeader().getNumber()).isEqualTo(1);

        ResultSet payload = actual.getPayload();
        assertThat(payload.getInt(1)).isEqualTo(1);
        assertThat(payload.getString(2)).isEqualTo("foo");
        assertThat(payload.getString(3)).isEqualTo("easy batch rocks! #EasyBatch");
    }

    @Test
    public void testTotalRecordsNumber() throws Exception {
        assertThat(jdbcRecordReader.getTotalRecords()).isEqualTo(2);
    }

    @Test
    public void testMaxRowsParameter() throws Exception {
        jdbcRecordReader = new JdbcRecordReader(connection, query);
        jdbcRecordReader.setMaxRows(1);
        jdbcRecordReader.open();
        assertThat(jdbcRecordReader.getTotalRecords()).isEqualTo(1);
    }

    @Test
    public void testGetDataSourceName() throws Exception {
        System.out.println(jdbcRecordReader.getDataSourceName());
        assertThat(jdbcRecordReader.getDataSourceName()).isEqualTo(DATA_SOURCE_NAME);
    }

    @After
    public void tearDown() throws Exception {
        /*
         * The connection will be closed in @AfterClass method.
         * If done here, subsequent tests do not find the connection
         */
        //jdbcRecordReader.close();
    }

    @AfterClass
    public static void shutdownDatabase() throws Exception {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
        //delete hsqldb tmp files
        new File("mem.log").delete();
        new File("mem.properties").delete();
        new File("mem.script").delete();
        new File("mem.tmp").delete();
    }

    private static void createTweetTable(Connection connection) throws Exception {
        Statement statement = connection.createStatement();
        String query = "CREATE TABLE if not exists tweet (\n" +
                "  id integer NOT NULL PRIMARY KEY,\n" +
                "  user varchar(32) NOT NULL,\n" +
                "  message varchar(140) NOT NULL,\n" +
                ");";
        statement.executeUpdate(query);
        statement.close();
    }

    private static void populateTweetTable(Connection connection) throws Exception {
        executeQuery(connection, "INSERT INTO tweet VALUES (1,'foo','easy batch rocks! #EasyBatch');");
        executeQuery(connection, "INSERT INTO tweet VALUES (2,'bar','@foo I do confirm :-)');");
    }

    private static void executeQuery(Connection connection, String query) throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

}
