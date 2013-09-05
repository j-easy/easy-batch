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

/**
 * This validator should be used to validate that field content length is greater than the specified length.
 * @author benas (md.benhassine@gmail.com)
 */
public class MinLengthFieldValidator extends NotEmptyFieldValidator implements FieldValidator {

    /**
     * Field content minimal length.
     */
    protected int minLength;

    public MinLengthFieldValidator(int minLength) {
        this.minLength = minLength;
    }

    /**
     * {@inheritDoc}
     */
    public boolean isValidField(Field field) {
        return super.isValidField(field) && field.getContent().length() >= minLength;
    }

    /**
     * {@inheritDoc}
     */
    public String getValidationConstraintDescription() {
        return "The field content should have a minimal length of " + minLength;
    }
}
