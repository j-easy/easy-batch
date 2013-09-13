package io.github.benas.cb4j.core.converter;

import io.github.benas.cb4j.core.api.TypeConverter;

/**
 * Character type converter.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class CharacterTypeConverter implements TypeConverter<Character> {

    /**
     * {@inheritDoc}
     */
    public Character convert(final String value) {
        return value.charAt(0);
    }

}
