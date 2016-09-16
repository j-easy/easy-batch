/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import org.easybatch.core.converter.*;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.String.format;

/**
 * A helper class that maps a record to a domain object instance.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class ObjectMapper {

    /**
     * The logger.
     */
    private static final Logger LOGGER = Logger.getLogger(ObjectMapper.class.getName());

    /**
     * The target domain object class.
     */
    private Class recordClass;

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
     *
     * @param recordClass the target object type
     */
    public ObjectMapper(final Class recordClass) {
        this.recordClass = recordClass;
        initializeTypeConverters();
        initializeSetters();
    }

    /**
     * Map values to fields of the target object type.
     *
     * @param values fields values
     * @return A populated instance of the target type.
     * @throws Exception thrown if values cannot be mapped to target object fields
     */
    public Object mapObject(final Map<String, String> values) throws Exception {

        Object result = createInstance();

        // for each field
        for (Map.Entry<String, String> entry : values.entrySet()) {

            String field = entry.getKey();
            //get field raw value
            String value = values.get(field);

            Method setter = setters.get(field);
            if (setter == null) {
                LOGGER.log(Level.WARNING, "No public setter found for field {0}, this field will be set to null (if object type) or default value (if primitive type)", field);
                continue;
            }

            Class<?> type = setter.getParameterTypes()[0];
            TypeConverter typeConverter = typeConverters.get(type);
            if (typeConverter == null) {
                LOGGER.log(Level.WARNING,
                        "Type conversion not supported for type {0}, field {1} will be set to null (if object type) or default value (if primitive type)",
                        new Object[]{type, field});
                continue;
            }

            if (value == null) {
                LOGGER.log(Level.WARNING, "Attempting to convert null to type {0} for field {1}, this field will be set to null (if object type) or default value (if primitive type)", new Object[]{type, field});
                continue;
            }

            if (value.isEmpty()) {
                LOGGER.log(Level.FINE, "Attempting to convert an empty string to type {0} for field {1}, this field will be ignored", new Object[]{type, field});
                continue;
            }

            convertValue(result, field, value, setter, type, typeConverter);

        }

        return result;
    }

    private void initializeSetters() {
        setters = new HashMap<>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(recordClass);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            getSetters(propertyDescriptors);
        } catch (IntrospectionException e) {
            throw new BeanIntrospectionException("Unable to introspect target type " + recordClass.getName(), e);
        }
    }

    private void getSetters(PropertyDescriptor[] propertyDescriptors) {
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
            setters.put(propertyDescriptor.getName(), propertyDescriptor.getWriteMethod());
        }
        //exclude property "class"
        setters.remove("class");
    }

    private Object createInstance() throws Exception {
        try {
            return recordClass.newInstance();
        } catch (Exception e) {
            throw new Exception(format("Unable to create a new instance of target type %s", recordClass.getName()), e);
        }
    }

    private void convertValue(Object result, String field, String value, Method setter, Class<?> type, TypeConverter typeConverter) throws Exception {
        try {
            Object typedValue = typeConverter.convert(value);
            setter.invoke(result, typedValue);
        } catch (Exception e) {
            throw new Exception(format("Unable to convert %s to type %s for field %s", value, type, field), e);
        }
    }

    private void initializeTypeConverters() {
        typeConverters = new HashMap<>();
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

    public void registerTypeConverter(final TypeConverter typeConverter) {
        //retrieve the target class name of the converter
        Class<? extends TypeConverter> typeConverterClass = typeConverter.getClass();
        Type[] genericInterfaces = typeConverterClass.getGenericInterfaces();
        Type genericInterface = genericInterfaces[0];
        if (!(genericInterface instanceof ParameterizedType)) {
            LOGGER.log(Level.WARNING, "The type converter {0} should be a parametrized type", typeConverterClass.getName());
            return;
        }
        ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
        Type type = parameterizedType.getActualTypeArguments()[1];

        // register the converter
        try {
            Class clazz = Class.forName(getClassName(type));
            typeConverters.put(clazz, typeConverter);
        } catch (ClassNotFoundException e) {
            throw new TypeConverterRegistrationException("Unable to register custom type converter " + typeConverterClass.getName(), e);
        }
    }

    private String getClassName(Type actualTypeArgument) {
        return actualTypeArgument.toString().substring(6);
    }

}
