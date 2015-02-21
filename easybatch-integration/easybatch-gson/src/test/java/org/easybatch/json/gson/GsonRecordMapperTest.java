package org.easybatch.json.gson;

import com.google.gson.Gson;
import org.easybatch.core.api.Header;
import org.easybatch.core.api.Record;
import org.easybatch.json.JsonRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link GsonRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class GsonRecordMapperTest {

    private GsonRecordMapper<Tweet> mapper;

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        mapper = new GsonRecordMapper<Tweet>(gson, Tweet.class);
    }

    @Test
    public void testMapRecord() throws Exception {
        String jsonTweet = "{\"id\":1,\"user\":\"foo\",\"message\":\"Hello\"}";
        Record record = new JsonRecord(new Header(1l, "ds", new Date()), jsonTweet);
        Tweet tweet = mapper.mapRecord(record);

        assertThat(tweet.getId()).isEqualTo(1);
        assertThat(tweet.getUser()).isEqualTo("foo");
        assertThat(tweet.getMessage()).isEqualTo("Hello");
    }

    @Test
    public void testMapIncompleteRecord() throws Exception {
        String jsonTweet = "{\"id\":1,\"user\":\"foo\"}";
        Record record = new JsonRecord(new Header(1l, "ds", new Date()), jsonTweet);
        Tweet tweet = mapper.mapRecord(record);

        assertThat(tweet.getId()).isEqualTo(1);
        assertThat(tweet.getUser()).isEqualTo("foo");
        assertThat(tweet.getMessage()).isNull();
    }

    @Test
    public void testMapEmptyRecord() throws Exception {
        String jsonTweet = "{}";
        Record record = new JsonRecord(new Header(1l, "ds", new Date()), jsonTweet);
        Tweet tweet = mapper.mapRecord(record);

        assertThat(tweet.getId()).isEqualTo(0);
        assertThat(tweet.getUser()).isNull();
        assertThat(tweet.getMessage()).isNull();
    }

}
