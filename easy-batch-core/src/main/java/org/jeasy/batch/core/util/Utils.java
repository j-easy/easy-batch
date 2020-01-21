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
package org.jeasy.batch.core.util;

import org.jeasy.batch.core.job.JobParameters;
import org.jeasy.batch.core.record.Record;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static java.lang.String.format;
import static java.lang.String.valueOf;

/**
 * Utilities class.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public abstract class Utils {

    public static final String LINE_SEPARATOR = System.getProperty("line.separator");

    public static final String FILE_SEPARATOR = System.getProperty("file.separator");

    public static final String JAVA_IO_TMPDIR = System.getProperty("java.io.tmpdir");

    public static final String JMX_MBEAN_NAME = "org.jeasy.batch.core.monitor:";

    public static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";

    public static final String NOT_APPLICABLE = "N/A";

    private Utils() {

    }

    public static void checkNotNull(Object argument, String argumentName) {
        if (argument == null) {
            throw new IllegalArgumentException(format("The %s must not be null", argumentName));
        }
    }

    public static void checkArgument(boolean assertion, String message) {
        if (!assertion) {
            throw new IllegalArgumentException(message);
        }
    }

    public static Map<String, Method> getGetters(final Class type) throws IntrospectionException {
        Map<String, Method> getters = new HashMap<>();
        BeanInfo beanInfo = Introspector.getBeanInfo(type);
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            getters.put(propertyDescriptor.getName(), propertyDescriptor.getReadMethod());
        }
        getters.remove("class"); //exclude property "class"
        return getters;
    }

    public static String formatTime(LocalDateTime time) {
        return DateTimeFormatter.ofPattern(DATE_FORMAT).format(time);
    }

    public static String formatDuration(Duration duration) {
        long days = duration.toDays();
        long hours = duration.toHours();
        long minutes = duration.toMinutes();
        long seconds = duration.getSeconds();
        long milliseconds = duration.toMillis();
        return (days > 0 ? days + "d " : "") +
                (hours > 0 ? (hours - TimeUnit.DAYS.toHours(days)) + "hr " : "") +
                (minutes > 0 ? (minutes - TimeUnit.HOURS.toMinutes(hours)) + "min " : "") +
                (seconds > 0 ? (seconds - TimeUnit.MINUTES.toSeconds(minutes)) + "sec " : "") +
                (milliseconds -TimeUnit.SECONDS.toMillis(seconds)) + "ms";
    }

    public static String formatErrorThreshold(final long errorThreshold) {
        return errorThreshold == JobParameters.DEFAULT_ERROR_THRESHOLD ? NOT_APPLICABLE : valueOf(errorThreshold);
    }

    /**
     * Extract the payload form each record.
     *
     * @param records the list of records
     * @param <P>     the type of payload
     * @return the list of payloads
     */
    public static <P> List<P> extractPayloads(final List<? extends Record<P>> records) {
        List<P> payloads = new ArrayList<>();
        for (Record<P> record : records) {
            payloads.add(record.getPayload());
        }
        return payloads;
    }

}
