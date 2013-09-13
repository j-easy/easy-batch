package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

import java.sql.Timestamp;

/**
 * java.sql.Timestamp type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class SqlTimestampTypeConverter implements TypeConverter<Timestamp> {

    /**
     * {@inheritDoc}
     */
    public Timestamp convert(final String value) {
        return Timestamp.valueOf(value);
    }

}
