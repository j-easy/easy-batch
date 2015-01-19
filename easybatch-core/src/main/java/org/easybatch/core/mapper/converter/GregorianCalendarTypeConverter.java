package org.easybatch.core.mapper.converter;

import org.easybatch.core.api.TypeConverter;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * {@link java.util.Calendar} type converter.
 *
 * Used to convert a raw String into a Gregorian Calendar instance.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class GregorianCalendarTypeConverter implements TypeConverter<GregorianCalendar> {

    private DateTypeConverter dateTypeConverter;

    public GregorianCalendarTypeConverter() {
        this.dateTypeConverter = new DateTypeConverter();
    }

    public GregorianCalendarTypeConverter(String dateFormat) {
        this.dateTypeConverter = new DateTypeConverter(dateFormat);
    }

    @Override
    public GregorianCalendar convert(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Value to convert must not be null");
        }
        if (value.isEmpty()) {
            throw new IllegalArgumentException("Value to convert must not be empty");
        }
        Date date = dateTypeConverter.convert(value);
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        return gregorianCalendar;
    }
}