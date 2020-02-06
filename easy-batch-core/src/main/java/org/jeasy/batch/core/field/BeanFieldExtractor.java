/*
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
package org.jeasy.batch.core.field;

import org.jeasy.batch.core.converter.TypeConverter;
import org.jeasy.batch.core.mapper.TypeConverterRegistrationException;
import org.jeasy.batch.core.util.Utils;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Use JavaBean convention with {@link java.beans.Introspector} to extract
 * properties values from the payload of a record with optional formatting
 * by registering a custom {@link TypeConverter}.
 *
 * @author Rémi Alvergnat (toilal.dev@gmail.com)
 * @author Mahmoud Ben Hassine
 */
public class BeanFieldExtractor<P> implements FieldExtractor<P> {

    private final String[] fields;
    private final Map<String, Method> getters;
    private final Map<Class<?>, TypeConverter<?, String>> typeConverters;

    /**
     * Create a new {@link BeanFieldExtractor}.
     *
     * @param type of the bean
     * @param fields to extract
     */
    public BeanFieldExtractor(final Class<P> type, final String... fields) {
        try {
            this.getters = Utils.getGetters(type);
        } catch (IntrospectionException exception) {
            throw new IllegalArgumentException(exception);
        }
        if (fields.length == 0) {
            this.fields = this.getters.keySet().toArray(new String[0]);
        } else {
            this.fields = fields;
        }
        typeConverters = new HashMap<>();
    }

    @Override
    public Iterable<Object> extractFields(final P payload) throws Exception {
        Object[] values = new Object[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Object value = getValue(fields[i], payload);
            values[i] = format(value);
        }
        return Arrays.asList(values);
    }

    protected Object getValue(final String field, final P object) throws InvocationTargetException, IllegalAccessException {
        return getters.get(field).invoke(object);
    }

    private Object format(Object value) {
        if (value == null) {
            return null;
        }
        TypeConverter typeConverter = typeConverters.get(value.getClass());
        if (typeConverter != null) {
            return typeConverter.convert(value);
        }
        return value;
    }

    /**
     * Register a {@link TypeConverter} used to format fields.
     *
     * @param typeConverter to register
     */
    public void registerTypeConverter(final TypeConverter<?, String> typeConverter) {
        //retrieve the target class name of the converter
        // FIXME : find a clean way for this + make it work with lambdas
        Class<? extends TypeConverter> typeConverterClass = typeConverter.getClass();
        Type[] genericInterfaces = typeConverterClass.getGenericInterfaces();
        Type genericInterface = genericInterfaces[0];
        if (!(genericInterface instanceof ParameterizedType)) {
            throw new TypeConverterRegistrationException("The type converter" + typeConverterClass.getName() + " should be a parametrized type");
        }
        ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
        Type type = parameterizedType.getActualTypeArguments()[0];

        // register the converter
        try {
            Class<?> clazz = Class.forName(getClassName(type));
            typeConverters.put(clazz, typeConverter);
        } catch (ClassNotFoundException e) {
            throw new TypeConverterRegistrationException("Unable to register custom type converter " + typeConverterClass.getName(), e);
        }
    }

    private String getClassName(Type actualTypeArgument) {
        // FIXME : find a clean way for this
        return actualTypeArgument.toString().substring(6);
    }
}
