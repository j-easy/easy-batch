/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.core.field;

import org.easybatch.core.util.Utils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

/**
 * Use JavaBean convention with {@link java.beans.Introspector} to extract properties values from records of a specified class.
 *
 * @author Rémi Alvergnat (toilal.dev@gmail.com)
 */
public class BeanRecordFieldExtractor implements RecordFieldExtractor {

    private final String[] fields;

    private final Map<String, Method> getters;

    public BeanRecordFieldExtractor(final Class type, final String... fields) throws IntrospectionException {
        this.getters = Utils.getGetters(type);
        if (fields.length == 0) {
            this.fields = this.getters.keySet().toArray(new String[this.getters.size()]);
        } else {
            this.fields = fields;
        }
    }

    @Override
    public Iterable<Object> extractFields(final Object record) throws RecordFieldExtractionException {
        Object[] values = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            try {
                values[i] = getValue(fields[i], record);
            } catch (Exception e) {
                throw new RecordFieldExtractionException("Unable to extract fields from record " + record, e);
            }
        }
        return Arrays.asList(values);
    }

    protected Object getValue(final String field, final Object object) throws InvocationTargetException, IllegalAccessException {
        return getters.get(field).invoke(object);
    }
}
