package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

import java.math.BigDecimal;

/**
 * BigDecimal type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class BigDecimalTypeConverter implements TypeConverter<BigDecimal> {

    /**
     * {@inheritDoc}
     */
    public BigDecimal convert(final String value) {
        return new BigDecimal(value);
    }

}
