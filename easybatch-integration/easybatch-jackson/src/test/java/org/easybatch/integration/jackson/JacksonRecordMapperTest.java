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

package org.easybatch.integration.jackson;

import org.codehaus.jackson.map.ObjectMapper;
import org.easybatch.core.api.Header;
import org.easybatch.core.api.Record;
import org.easybatch.json.JsonRecord;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link JacksonRecordMapper}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JacksonRecordMapperTest {

    private JacksonRecordMapper<Tweet> mapper;

    @Before
    public void setUp() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        mapper = new JacksonRecordMapper<Tweet>(objectMapper, Tweet.class);
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
