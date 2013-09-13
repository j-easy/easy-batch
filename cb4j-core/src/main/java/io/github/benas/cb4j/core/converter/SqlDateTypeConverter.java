package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

import java.sql.Date;

/**
 * java.sql.Date type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class SqlDateTypeConverter implements TypeConverter<Date> {

    /**
     * {@inheritDoc}
     */
    public Date convert(final String value) {
        return Date.valueOf(value);
    }

}
