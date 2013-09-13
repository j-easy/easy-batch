package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

import java.sql.Time;

/**
 * java.sql.Time type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class SqlTimeTypeConverter implements TypeConverter<Time> {

    /**
     * {@inheritDoc}
     */
    public Time convert(final String value) {
        return Time.valueOf(value);
    }

}
