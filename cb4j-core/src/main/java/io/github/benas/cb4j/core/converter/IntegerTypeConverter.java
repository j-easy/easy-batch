package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

/**
 * Integer type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class IntegerTypeConverter implements TypeConverter<Integer> {

    /**
     * {@inheritDoc}
     */
    public Integer convert(final String value) {
        return Integer.valueOf(value);
    }

}
