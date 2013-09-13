package io.github.benas.cb4j.core.api;

/**
 * Interface for type converter used to convert raw textual data in CSV/FL records to typed data in domain objects.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public interface TypeConverter<T> {

    /**
     * Convert raw data from textual to typed value.
     * @param value the String value to convert
     * @return Converted value
     */
    T convert(final String value);

}
