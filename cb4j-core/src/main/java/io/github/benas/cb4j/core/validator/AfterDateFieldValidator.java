/*
 * The MIT License
 *
 *  Copyright (c) 2013, benas (md.benhassine@gmail.com)
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

package io.github.benas.cb4j.core.validator;

import io.github.benas.cb4j.core.api.FieldValidator;
import io.github.benas.cb4j.core.model.Field;

import java.util.Date;

/**
 * This validator should be used to validate that field content is a valid date after a given date.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class AfterDateFieldValidator extends DateFieldValidator implements FieldValidator {

    /**
     * The higher date.
     */
    protected Date date;

    public AfterDateFieldValidator(String dateFormat, Date date) {
        super(dateFormat);
        this.date = date;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValid(final Field field) {
        return super.isValid(field) && new Date(field.getContent()).after(date);
    }

    /**
     * {@inheritDoc}
     */
    public String getValidationConstraintDescription() {
        return "The field content should be a valid date after : " + date;
    }

}
