package org.easybatch.core.util;

import org.easybatch.core.beans.Person;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.Calendar;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.job.JobParameters.DEFAULT_ERROR_THRESHOLD;

public class UtilsTest {

    private static long TIME;

    static {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, Calendar.JANUARY, 1, 1, 0, 0);
        TIME = calendar.getTime().getTime();
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
        assertThat(Utils.formatDuration(100)).isEqualTo("0hr 0min 0sec 100ms");
        assertThat(Utils.formatDuration(1000)).isEqualTo("0hr 0min 1sec 0ms");
        assertThat(Utils.formatDuration(60 * 2 * 1000)).isEqualTo("0hr 2min 0sec 0ms");
        assertThat(Utils.formatDuration(60 * 60 * 2 * 1000)).isEqualTo("2hr 0min 0sec 0ms");
    }

    @Test
    public void testFormatErrorThreshold() throws Exception {
        assertThat(Utils.formatErrorThreshold(1)).isEqualTo("1");
        assertThat(Utils.formatErrorThreshold(5)).isEqualTo("5");
        assertThat(Utils.formatErrorThreshold(DEFAULT_ERROR_THRESHOLD)).isEqualTo("N/A");
    }

}