package org.easybatch.jdbc.spring;

import org.easybatch.core.api.Header;
import org.easybatch.jdbc.JdbcRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Date;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link SpringJdbcRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class SpringJdbcRecordMapperTest {

    private SpringJdbcRecordMapper<Tweet> mapper;

    private JdbcRecord jdbcRecord;

    @Mock
    private ResultSet payload;
    @Mock
    private ResultSetMetaData metadata;

    @Before
    public void setUp() throws Exception {
        mapper = new SpringJdbcRecordMapper<Tweet>(Tweet.class);
        jdbcRecord = new JdbcRecord(new Header(1l, "ds", new Date()), payload);
    }

    @Test
    public void testMapRecord() throws Exception {
        when(payload.getMetaData()).thenReturn(metadata);
        when(metadata.getColumnCount()).thenReturn(3);
        when(metadata.getColumnLabel(1)).thenReturn("id");
        when(metadata.getColumnType(1)).thenReturn(Types.INTEGER);
        when(metadata.getColumnLabel(2)).thenReturn("user");
        when(metadata.getColumnType(2)).thenReturn(Types.VARCHAR);
        when(metadata.getColumnLabel(3)).thenReturn("message");
        when(metadata.getColumnType(3)).thenReturn(Types.VARCHAR);
        when(payload.getInt(1)).thenReturn(1);
        when(payload.getString(2)).thenReturn("foo");
        when(payload.getString(3)).thenReturn("message");

        Tweet tweet = mapper.mapRecord(jdbcRecord);

        assertThat(tweet).isNotNull();
        assertThat(tweet.getId()).isEqualTo(1);
        assertThat(tweet.getUser()).isEqualTo("foo");
        assertThat(tweet.getMessage()).isEqualTo("message");
    }
}
