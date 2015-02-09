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

package org.easybatch.core.mapper;

import org.easybatch.core.api.TypeConverter;
import org.easybatch.core.mapper.converter.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A helper class that maps a record to a domain object instance.
 *
 * @param <T> the target domain object type
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ObjectMapper<T> {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ObjectMapper.class.getName());

    /**
     * The target domain object class.
     */
    private Class<? extends T> recordClass;

    /**
     * A map holding setter methods for each field.
     */
    private Map<String, Method> setters;

    /**
     * Type converters map.
     */
    private Map<Class, TypeConverter> typeConverters;

    /**
     * Construct an object mapper.
     * @param recordClass the target object type
     */
    public ObjectMapper(final Class<? extends T> recordClass) {
        this.recordClass = recordClass;
        initializeTypeConverters();
        initializeSetters();
    }

    /**
     * Initialize setters methods.
     */
    private void initializeSetters() {
        setters = new HashMap<String, Method>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(recordClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                setters.put(propertyDescriptor.getName(), propertyDescriptor.getWriteMethod());
            }
            //exclude property "class"
            setters.remove("class");
        } catch (IntrospectionException e) {
            LOGGER.log(Level.SEVERE, "Unable to introspect target type " + recordClass.getName(), e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Map a values to fields of the target object type.
     *
     * @param values fields values
     * @return A populated instance of the target type.
     * @throws Exception thrown if values cannot be mapped to target object fields
     */
    public T mapObject(final Map<String, String> values) throws Exception {

        T result = recordClass.newInstance();

        // for each field
        for (String field : values.keySet()) {

            //get field raw value
            String value = values.get(field);

            //convert the String raw value to the field type
            Object typedValue = null;
            Class<?> type = setters.get(field).getParameterTypes()[0];
            TypeConverter typeConverter = typeConverters.get(type);
            if (typeConverter != null) {
                try {
                    typedValue = typeConverter.convert(value);
                } catch (Exception e) {
                    throw new Exception("Unable to convert '" + value + "' to type " + type + " for field " + field, e);
                }
            } else {
                LOGGER.log(Level.WARNING,
                           "Type conversion not supported for type {0}, field {1} will be set to null (if object type) or default value (if primitive type)",
                            new Object[]{type, field});
            }

            //set the typed value to the object field
            setters.get(field).invoke(result, typedValue);
        }

        return result;
    }

    /**
     * Initialize default type converters.
     */
    private void initializeTypeConverters() {
        typeConverters = new HashMap<Class, TypeConverter>();
        typeConverters.put(AtomicInteger.class, new AtomicIntegerTypeConverter());
        typeConverters.put(AtomicLong.class, new AtomicLongTypeConverter());
        typeConverters.put(BigDecimal.class, new BigDecimalTypeConverter());
        typeConverters.put(BigInteger.class, new BigIntegerTypeConverter());
        typeConverters.put(Boolean.class, new BooleanTypeConverter());
        typeConverters.put(Boolean.TYPE, new BooleanTypeConverter());
        typeConverters.put(Byte.class, new ByteTypeConverter());
        typeConverters.put(Byte.TYPE, new ByteTypeConverter());
        typeConverters.put(Character.class, new CharacterTypeConverter());
        typeConverters.put(Character.TYPE, new CharacterTypeConverter());
        typeConverters.put(Double.class, new DoubleTypeConverter());
        typeConverters.put(Double.TYPE, new DoubleTypeConverter());
        typeConverters.put(Float.class, new FloatTypeConverter());
        typeConverters.put(Float.TYPE, new FloatTypeConverter());
        typeConverters.put(Integer.class, new IntegerTypeConverter());
        typeConverters.put(Integer.TYPE, new IntegerTypeConverter());
        typeConverters.put(Long.class, new LongTypeConverter());
        typeConverters.put(Long.TYPE, new LongTypeConverter());
        typeConverters.put(Short.class, new ShortTypeConverter());
        typeConverters.put(Short.TYPE, new ShortTypeConverter());
        typeConverters.put(java.util.Date.class, new DateTypeConverter());
        typeConverters.put(java.util.Calendar.class, new GregorianCalendarTypeConverter());
        typeConverters.put(java.util.GregorianCalendar.class, new GregorianCalendarTypeConverter());
        typeConverters.put(java.sql.Date.class, new SqlDateTypeConverter());
        typeConverters.put(java.sql.Time.class, new SqlTimeTypeConverter());
        typeConverters.put(java.sql.Timestamp.class, new SqlTimestampTypeConverter());
        typeConverters.put(String.class, new StringTypeConverter());
    }

    /**
     * Register a custom type converter.
     * @param typeConverter the type converter to user
     */
    public void registerTypeConverter(final TypeConverter typeConverter) {
        typeConverters.put(recordClass, typeConverter);
    }

}
