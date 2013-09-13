package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

import java.math.BigInteger;

/**
 * BigInteger type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class BigIntegerTypeConverter implements TypeConverter<BigInteger> {

    /**
     * {@inheritDoc}
     */
    public BigInteger convert(final String value) {
        return new BigInteger(value);
    }

}
