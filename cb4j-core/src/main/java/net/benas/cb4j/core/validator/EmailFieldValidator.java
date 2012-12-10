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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Email validator implementation.<br/>
 * This validator should be used to validate that field content has a valid email format
 * @author benas (md.benhassine@gmail.com)
 */
public class EmailFieldValidator implements FieldValidator {

    /**
     * {@inheritDoc}
     */
    public boolean isValidField(final Field field) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Za-z]{2,4}$";
        String inputStr = field.getContent();
        return inputStr.matches(expression);
    }

    /**
     * {@inheritDoc}
     */
    public String getValidationRuleDescription() {
        return "The field content should be a valid email address format";
    }

}
