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

package io.github.benas.cb4j.core.impl;

import io.github.benas.cb4j.core.api.FieldValidator;
import io.github.benas.cb4j.core.api.RecordValidator;
import io.github.benas.cb4j.core.model.Field;
import io.github.benas.cb4j.core.model.Record;

import java.util.List;
import java.util.Map;

/**
 * Default implementation of the {@link RecordValidator} interface. Default is to consider a record as valid if all its fields are valid.<br/>
 *
 * Custom implementation can be registered with {@link io.github.benas.cb4j.core.config.BatchConfiguration}
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class DefaultRecordValidatorImpl implements RecordValidator {

    /**
     * Field validators used to validate a record.<br/>
     * The map key is the field index.<br/>
     * The map value is the list of validators to apply for this field. Validation order is the insertion order of validators in the list. If validators list is empty, all fields are supposed to be valid
     *
     */
    private Map<Integer, List<FieldValidator>> fieldValidators;

    public DefaultRecordValidatorImpl(Map<Integer, List<FieldValidator>> fieldValidators) {
        this.fieldValidators = fieldValidators;
    }

    /**
     * Validate a record.
     * @param record the record to validate
     * @return validation error if any or null if the record is valid
     *
     */
    public String validateRecord(Record record) {
        if (fieldValidators.isEmpty()) { //if no validators are registered, all fields are supposed to be valid
            return null;
        }

        for (Field field : record.getFields()) {
            List<FieldValidator> validators = fieldValidators.get(field.getIndex());
            if (validators != null && validators.size() > 0) {
                for (FieldValidator validator : validators) {
                    if (!validator.isValidField(field)) {
                        return "The field " + field + " is not valid according to validation constraint : [" + validator.getValidationConstraintDescription() + "]";
                    }
                }
            }
        }
        return null;
    }
}
