package io.github.benas.cb4j.core.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Utility class providing type conversion. This class is used by the {@link io.github.benas.cb4j.core.impl.DefaultRecordMapperImpl}
 * to convert raw data in CSV/FL records to typed data in domain objects.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class TypeConversionUtil {

    /**
     * Convert raw data from String to typed value.
     * @param value the String value to convert
     * @param type the target type
     * @return Converted value
     */
    public static Object convertValue(String value, Class type) {

        /*
         * String and Character types
         */
        if (type.equals(String.class)) {
            return value;
        }
        if (type.equals(Character.TYPE) || type.equals(Character.class)) {
            return value.charAt(0);
        }

        /*
         * Boolean type
         */
        if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
            return Boolean.valueOf(value);
        }

        /*
         * Numeric types
         */
        if (type.equals(Byte.TYPE) || type.equals(Byte.class)) {
            return Byte.valueOf(value);
        }
        if (type.equals(Short.TYPE) || type.equals(Short.class)) {
            return Short.valueOf(value);
        }
        if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return Integer.valueOf(value);
        }
        if (type.equals(Long.TYPE) || type.equals(Long.class)) {
            return Long.valueOf(value);
        }
        if (type.equals(Double.TYPE) || type.equals(Double.class)) {
            return Double.valueOf(value);
        }
        if (type.equals(Float.TYPE) || type.equals(Float.class)) {
            return Float.valueOf(value);
        }
        if (type.equals(BigInteger.class)) {
            return new BigInteger(value);
        }
        if (type.equals(BigDecimal.class)) {
            return new BigDecimal(value);
        }
        if (type.equals(AtomicLong.class)) {
            return new AtomicLong(Long.valueOf(value));
        }
        if (type.equals(AtomicInteger.class)) {
            return new AtomicInteger(Integer.valueOf(value));
        }

        /*
         * Date and time types
         */
        if (type.equals(java.util.Date.class)) {
            return new java.util.Date(Date.parse(value));
        }
        if (type.equals(java.sql.Date.class)) {
            return Date.valueOf(value);
        }
        if (type.equals(java.sql.Time.class)) {
            return Time.valueOf(value);
        }
        if (type.equals(java.sql.Timestamp.class)) {
            return Timestamp.valueOf(value);
        }
        if (type.equals(Calendar.class)) {
            return Calendar.getInstance();
        }

        /*
         * Return null for any unsupported type
         */
        return null;

    }

}
