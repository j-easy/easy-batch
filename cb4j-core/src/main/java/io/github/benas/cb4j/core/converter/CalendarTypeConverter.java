package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

import java.util.Calendar;

/**
 * Calendar type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class CalendarTypeConverter implements TypeConverter<Calendar> {

    /**
     * {@inheritDoc}
     */
    public Calendar convert(final String value) {
        return Calendar.getInstance();
    }

}
