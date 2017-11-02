/**
 * The MIT License
 *
 *   Copyright (c) 2017, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.easybatch.core.job.JobParameters;

/**
 * Utilities class.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public abstract class Utils {

  public static final String LINE_SEPARATOR  = System.getProperty("line.separator");

  public static final String FILE_SEPARATOR  = System.getProperty("file.separator");

  public static final String JAVA_IO_TMPDIR  = System.getProperty("java.io.tmpdir");

  public static final String JMX_MBEAN_NAME  = "org.easybatch.core.monitor:";

  public static final String DATE_FORMAT     = "yyyy-MM-dd hh:mm:ss";

  public static final String DURATION_FORMAT = "%sd %shr %smin %ssec %sms";

  public static final String NOT_APPLICABLE  = "N/A";

  private static final int   BUF_SIZE        = 1024;

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
    getters.remove("class"); // exclude property "class"
    return getters;
  }

  public static String formatTime(long time) {
    return new SimpleDateFormat(DATE_FORMAT).format(new Date(time));
  }

  public static String formatDuration(long duration) {
    return String.format(DURATION_FORMAT, MILLISECONDS.toDays(duration), MILLISECONDS.toHours(duration) % 24, MILLISECONDS.toMinutes(duration) % 60,
        MILLISECONDS.toSeconds(duration) % 60, duration % 1000);
  }

  public static String formatErrorThreshold(final long errorThreshold) {
    return errorThreshold == JobParameters.DEFAULT_ERROR_THRESHOLD ? NOT_APPLICABLE : valueOf(errorThreshold);
  }

  // from:https://stackoverflow.com/questions/453018/number-of-lines-in-a-file-in-java
  public static int countRows(File input) {
    try (InputStream is = new BufferedInputStream(new FileInputStream(input), BUF_SIZE)) {
      byte[] buf = new byte[BUF_SIZE];
      int count = 0;
      int readChars = 0;
      boolean endsWithoutNewLine = false;
      while ((readChars = is.read(buf)) != -1) {
        for (int i = 0; i < readChars; ++i) {
          if (buf[i] == '\n') {
            ++count;
          }
        }
        endsWithoutNewLine = buf[readChars - 1] != '\n';
      }
      if (endsWithoutNewLine) {
        ++count;
      }
      return count;
    } catch (IOException e) {
      return 0;
    }
  }

}
