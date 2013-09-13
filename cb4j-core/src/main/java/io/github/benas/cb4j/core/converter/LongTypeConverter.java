package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

/**
 * Long type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class LongTypeConverter implements TypeConverter<Long> {

    /**
     * {@inheritDoc}
     */
    public Long convert(final String value) {
        return Long.valueOf(value);
    }

}
