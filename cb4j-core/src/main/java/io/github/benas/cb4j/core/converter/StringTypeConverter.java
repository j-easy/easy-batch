package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

/**
 * String type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class StringTypeConverter implements TypeConverter<String> {

    /**
     * {@inheritDoc}
     */
    public String convert(final String value) {
        return value;
    }

}
