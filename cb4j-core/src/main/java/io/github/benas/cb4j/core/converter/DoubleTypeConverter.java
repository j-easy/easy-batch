package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

/**
 * Double type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class DoubleTypeConverter implements TypeConverter<Double> {

    /**
     * {@inheritDoc}
     */
    public Double convert(final String value) {
        return Double.valueOf(value);
    }

}
