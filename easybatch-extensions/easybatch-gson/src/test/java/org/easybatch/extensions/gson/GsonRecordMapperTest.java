/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.extensions.gson;

import com.google.gson.Gson;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.Header;
import org.easybatch.json.JsonRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GsonRecordMapperTest {

    private GsonRecordMapper<Tweet> mapper;

    @Mock
    private Header header;
    @Mock
    private JsonRecord record;

    @Before
    public void setUp() throws Exception {
        Gson gson = new Gson();
        mapper = new GsonRecordMapper<>(gson, Tweet.class);
        when(record.getHeader()).thenReturn(header);
    }

    @Test
    public void testMapRecord() throws Exception {
        String jsonTweet = "{\"id\":1,\"user\":\"foo\",\"message\":\"Hello\"}";
        when(record.getPayload()).thenReturn(jsonTweet);
        GenericRecord<Tweet> actual = mapper.processRecord(record);
        Tweet tweet = actual.getPayload();

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(tweet.getId()).isEqualTo(1);
        assertThat(tweet.getUser()).isEqualTo("foo");
        assertThat(tweet.getMessage()).isEqualTo("Hello");
    }

    @Test
    public void testMapIncompleteRecord() throws Exception {
        String jsonTweet = "{\"id\":1,\"user\":\"foo\"}";
        when(record.getPayload()).thenReturn(jsonTweet);
        GenericRecord<Tweet> actual = mapper.processRecord(record);
        Tweet tweet = actual.getPayload();

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(tweet.getId()).isEqualTo(1);
        assertThat(tweet.getUser()).isEqualTo("foo");
        assertThat(tweet.getMessage()).isNull();
    }

    @Test
    public void testMapEmptyRecord() throws Exception {
        String jsonTweet = "{}";
        when(record.getPayload()).thenReturn(jsonTweet);
        GenericRecord<Tweet> actual = mapper.processRecord(record);
        Tweet tweet = actual.getPayload();

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(tweet.getId()).isEqualTo(0);
        assertThat(tweet.getUser()).isNull();
        assertThat(tweet.getMessage()).isNull();
    }

}
