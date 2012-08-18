/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
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

package net.benas.cb4j.core.converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility service for type conversion.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class TypeConverter {

    /**
     * Convert a string value to a given type
     * @param value the string value to convert
     * @param targetType the target type to convert the string value to
     * @return a typed object converted from the string value
     * @throws TypeConversionException thrown if any type conversion occurs
     */
	public static Object getTypedFieldContent(final String value,Class targetType) throws TypeConversionException {

        assertNotNullAndNotEmpty(value);

		Object result = null;
		 try {
			if ((targetType == Integer.class) || (targetType == Integer.TYPE))
			     result = Integer.parseInt(value);
			 if ((targetType == Double.class) || (targetType == Double.TYPE))
			     result = Double.parseDouble(value);
			 if ((targetType == Boolean.class) || (targetType == Boolean.TYPE))
			     result = Boolean.parseBoolean(value);
			 if ((targetType == Byte.class) || (targetType == Byte.TYPE))
			     result = Byte.valueOf(value);
			 if ((targetType == Character.class) || (targetType == Character.TYPE))
			     result = (char) Long.parseLong(value);
			 if ((targetType == Short.class) || (targetType == Short.TYPE))
			     result = Short.parseShort(value);
			 if ((targetType == Long.class) || (targetType == Long.TYPE))
			     result = Long.parseLong(value);
			 if ((targetType == Float.class) || (targetType == Float.TYPE))
			     result = Float.parseFloat(value);
			 if (targetType == BigInteger.class)
			     result = BigInteger.valueOf(Long.parseLong(value));
			 if (targetType == BigDecimal.class)
			     result = BigDecimal.valueOf(Long.parseLong(value));
		} catch (NumberFormatException e) {
			throw new TypeConversionException("Type conversion exception of value : '" + value + "' to type : '" + targetType.getName() + "'");
		}
         
		return result;
	}

    /**
     * Convert a string value to a {@link Date}
     * @param value the string value representing a date
     * @param format the date format
     * @return a converted date object
     * @throws TypeConversionException thrown if any type conversion occurs
     */
	public static Date getDateTypedFieldContent(final String value,final String format) throws TypeConversionException {

        assertNotNullAndNotEmpty(value);

        DateFormat df = new SimpleDateFormat(format);
        Date result = null;
        try {
            result = df.parse(value);//not sure to get a valid date value here!
        } catch (ParseException e) {
            throw new TypeConversionException("Date type conversion exception of value : '" + value + "' to date format : '" + format + "'");
        }
        return result;
	}

    /**
     * private utility method to check null or empty values
     * @param value the value to check
     */
    private static void assertNotNullAndNotEmpty(final String value) {
        if (value == null)
            throw new IllegalArgumentException("Parameter value must not be null");

        if (value.length() == 0)
            throw new IllegalArgumentException("Parameter value must not be empty");
    }
   
}
