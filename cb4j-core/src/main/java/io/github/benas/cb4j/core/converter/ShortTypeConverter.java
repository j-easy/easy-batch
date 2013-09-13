package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

/**
 * Short type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class ShortTypeConverter implements TypeConverter<Short> {

    /**
     * {@inheritDoc}
     */
    public Short convert(final String value) {
        return Short.valueOf(value);
    }

}
