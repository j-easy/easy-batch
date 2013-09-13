package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * AtomicLong type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class AtomicLongTypeConverter implements TypeConverter<AtomicLong> {

    /**
     * {@inheritDoc}
     */
    public AtomicLong convert(final String value) {
        return new AtomicLong(Long.valueOf(value));
    }

}
