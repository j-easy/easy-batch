/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.validation;

import org.easybatch.core.api.RecordValidationException;
import org.easybatch.core.api.RecordValidator;
import org.easybatch.core.util.Utils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

/**
 * An implementation of {@link RecordValidator} using JSR 303 API.
 *
 * @param <T> the object type this validator can validate.
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class BeanValidationRecordValidator<T> implements RecordValidator<T> {

    /**
     * The validator instance to use to validate objects.
     */
    private Validator validator;

    public BeanValidationRecordValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Override
    public T processRecord(T record) throws RecordValidationException {
        Set<ConstraintViolation<T>> constraintViolationSet = validator.validate(record);
        if (!constraintViolationSet.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder();
            for (ConstraintViolation<T> constraintViolation : constraintViolationSet) {
                stringBuilder
                        .append("Invalid value '").append(constraintViolation.getInvalidValue()).append("' ")
                        .append("for property '").append(constraintViolation.getPropertyPath()).append("' : ")
                        .append(constraintViolation.getMessage())
                        .append(Utils.LINE_SEPARATOR);
            }
            throw new RecordValidationException(stringBuilder.toString());
        }
        return record;
    }
}
