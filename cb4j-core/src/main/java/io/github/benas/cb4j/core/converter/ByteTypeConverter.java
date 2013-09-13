package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

/**
 * Byte type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class ByteTypeConverter implements TypeConverter<Byte> {

    /**
     * {@inheritDoc}
     */
    public Byte convert(final String value) {
        return Byte.valueOf(value);
    }

}
