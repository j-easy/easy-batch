/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.core.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.easybatch.core.util.Utils.checkArgument;

/**
 * java.util.Date type converter.
 * Converts a String date (by default in the "yyyy-MM-dd" format) to a {@link java.util.Date} type.
 * Does not accept null or empty strings.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class DateTypeConverter implements TypeConverter<String, Date> {

    /**
     * The default date format.
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

    /**
     * The date format to use.
     */
    private String dateFormat;

    /**
     * Create a Date converter with the default format {@link org.easybatch.core.converter.DateTypeConverter#DEFAULT_DATE_FORMAT}
     */
    public DateTypeConverter() {
        this(DEFAULT_DATE_FORMAT);
    }

    /**
     * Create a Date converter with the specified date format.
     *
     * @param dateFormat the date format to use
     */
    public DateTypeConverter(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Date convert(final String value) {
        checkArgument(value != null, "Value to convert must not be null");
        checkArgument(!value.isEmpty(), "Value to convert must not be empty");
        try {
            return new SimpleDateFormat(dateFormat).parse(value);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Unable to convert value '" + value + "' to a Date object with format "
                    + dateFormat, e);
        }
    }

}
