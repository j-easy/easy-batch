/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package org.easybatch.core.mapper.converter;

import org.easybatch.core.api.TypeConverter;

import java.util.Date;
import java.util.GregorianCalendar;

/**
 * {@link java.util.Calendar} type converter.
 * <p/>
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