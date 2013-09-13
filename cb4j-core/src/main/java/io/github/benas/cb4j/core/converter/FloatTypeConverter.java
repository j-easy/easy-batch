package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

/**
 * Float type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class FloatTypeConverter implements TypeConverter<Float> {

    /**
     * {@inheritDoc}
     */
    public Float convert(final String value) {
        return Float.valueOf(value);
    }

}
