package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * AtomicInteger type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class AtomicIntegerTypeConverter implements TypeConverter<AtomicInteger> {

    /**
     * {@inheritDoc}
     */
    public AtomicInteger convert(final String value) {
        return new AtomicInteger(Integer.valueOf(value));
    }

}
