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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Date Format Validator implementation.<br/>
 * This validator should be used to validate that field content has a valid date format
 * @author benas (md.benhassine@gmail.com)
 */
public class DateFormatFieldValidator implements FieldValidator {

    /**
     * The date format to use.
     */
    protected String dateFormat;

    public DateFormatFieldValidator(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValidField(final Field field) {
        DateFormat df = new SimpleDateFormat(dateFormat);
        try {
            df.parse(field.getContent());
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public String getValidationRuleDescription() {
        return "The field content should be a date with format : " + dateFormat;
    }

}
