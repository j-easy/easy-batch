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

package net.benas.cb4j.core.validator;

import net.benas.cb4j.core.api.FieldValidator;
import net.benas.cb4j.core.model.Field;
import net.benas.cb4j.core.util.BatchConstants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

/**
 * Date Validator implementation.<br/>
 * This validator should be used to validate that field content has a valid date
 * @author benas (md.benhassine@gmail.com)
 */
public class DateFieldValidator implements FieldValidator {

    /**
     * Default date format to use
     */
    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";

    protected final Logger logger = Logger.getLogger(BatchConstants.LOGGER_CB4J);

    /**
     * The date format.
     */
    protected String dateFormat;

    /**
     * The date formatter
     */
    protected DateFormat simpleDateFormat;

    /**
     * Construct a date field validator with a {@link DateFormat}.<br/>
     * @param dateFormat the format used to validate date
     */
    public DateFieldValidator(String dateFormat) {
        this.dateFormat = dateFormat;
        try {
            simpleDateFormat = new SimpleDateFormat(dateFormat);
            simpleDateFormat.setLenient(false);
        } catch (Exception e) {
            logger.severe("Date format " + dateFormat + " is invalid, using default format " + DEFAULT_DATE_FORMAT);
            simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT);
        }
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValidField(final Field field) {
        try {
            simpleDateFormat.parse(field.getContent());
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public String getValidationConstraintDescription() {
        return "The field content should be a valid date with format: " + dateFormat;
    }

}
