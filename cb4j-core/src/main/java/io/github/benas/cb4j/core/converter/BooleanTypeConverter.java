package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

/**
 * Boolean type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class BooleanTypeConverter implements TypeConverter<Boolean> {

    /**
     * {@inheritDoc}
     */
    public Boolean convert(final String value) {
        return Boolean.valueOf(value);
    }

}
