package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

import java.util.Date;

/**
 * java.util.Date type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class DateTypeConverter implements TypeConverter<Date> {

    /**
     * {@inheritDoc}
     */
    public Date convert(final String value) {
        return new Date(Date.parse(value));
    }

}
