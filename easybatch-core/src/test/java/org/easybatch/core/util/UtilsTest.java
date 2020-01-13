/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.core.util;

import org.easybatch.core.beans.Person;
import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobParameters.DEFAULT_ERROR_THRESHOLD;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UtilsTest {

    private static long TIME;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, Calendar.JANUARY, 1, 1, 0, 0);
        TIME = calendar.getTime().getTime();
    }

    @Mock
    private Object payload1, payload2;
    @Mock
    private Record<Object> record1, record2;

    @Before
    public void setUp() throws Exception {
        when(record1.getPayload()).thenReturn(payload1);
        when(record2.getPayload()).thenReturn(payload2);
    }

    @Test
    public void testGetGetters() throws Exception {
        Map<String, Method> getters = Utils.getGetters(Person.class);

        assertThat(getters)
                .hasSize(6)
                .containsKeys("firstName", "lastName", "age", "birthDate", "married", "gender");
    }

    @Test
    public void testFormatTime() throws Exception {
        assertThat(Utils.formatTime(TIME)).isEqualTo("2015-01-01 01:00:00");
    }

    @Test
    public void testFormatDuration() throws Exception {
        assertThat(Utils.formatDuration(100)).isEqualTo("0d 0hr 0min 0sec 100ms");
        assertThat(Utils.formatDuration(1000)).isEqualTo("0d 0hr 0min 1sec 0ms");
        assertThat(Utils.formatDuration(60 * 2 * 1000)).isEqualTo("0d 0hr 2min 0sec 0ms");
        assertThat(Utils.formatDuration(60 * 60 * 2 * 1000)).isEqualTo("0d 2hr 0min 0sec 0ms");
        assertThat(Utils.formatDuration((24 * 60 * 60 * 2 * 1000))).isEqualTo("2d 0hr 0min 0sec 0ms");
        // in the extremely unlikely event that the job takes more than a year
        assertThat(Utils.formatDuration(31622400000L)).isEqualTo("366d 0hr 0min 0sec 0ms");
    }

    @Test
    public void testFormatErrorThreshold() throws Exception {
        assertThat(Utils.formatErrorThreshold(1)).isEqualTo("1");
        assertThat(Utils.formatErrorThreshold(5)).isEqualTo("5");
        assertThat(Utils.formatErrorThreshold(DEFAULT_ERROR_THRESHOLD)).isEqualTo("N/A");
    }

    @Test
    public void testExtractPayloadsFromListOfRecords() throws Exception {
        List<Object> list = Utils.extractPayloads(Arrays.asList(record1, record2));
        assertThat(list).containsExactly(payload1, payload2);
    }

}
