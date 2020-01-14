/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.validation;

import org.easybatch.core.record.Record;
import org.easybatch.core.util.Utils;
import org.easybatch.core.validator.RecordValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * An implementation of {@link RecordValidator} using JSR 303 API.
 * Records must be annotated with JSR 303 annotations.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class BeanValidationRecordValidator implements RecordValidator {

    private Validator validator;

    /**
     * Create a new {@link BeanValidationRecordValidator}.
     */
    public BeanValidationRecordValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public Record processRecord(Record record) throws Exception {
        Set<ConstraintViolation<Object>> constraintViolationSet = validator.validate(record.getPayload());
        if (!constraintViolationSet.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<Object> constraintViolation : constraintViolationSet) {
                stringBuilder
                        .append("Invalid value '").append(constraintViolation.getInvalidValue()).append("' ")
                        .append("for property '").append(constraintViolation.getPropertyPath()).append("' : ")
                        .append(constraintViolation.getMessage())
                        .append(Utils.LINE_SEPARATOR);
            }
            throw new Exception(stringBuilder.toString());
        }
        return record;
    }
}
