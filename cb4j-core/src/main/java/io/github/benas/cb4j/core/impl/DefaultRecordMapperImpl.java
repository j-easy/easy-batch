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

import io.github.benas.cb4j.core.api.TypeConverter;
import io.github.benas.cb4j.core.api.RecordMapper;
import io.github.benas.cb4j.core.api.RecordMappingException;
import io.github.benas.cb4j.core.converter.*;
import io.github.benas.cb4j.core.model.Field;
import io.github.benas.cb4j.core.model.Record;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * A default mapper implementation which uses headers to map values to record field.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class DefaultRecordMapperImpl implements RecordMapper {

    /**
     * The record class type.
     */
    private Class recordClass;

    /**
     * Type converters map.
     */
    private Map<Class, TypeConverter> typeConverters;

    /**
     * An array of methods holding setters that will be used to populate the returned instance.
     */
    private Method[] recordClassSetters;

    public DefaultRecordMapperImpl(String recordClassName, String[] headersMapping) throws ClassNotFoundException {

        initTypeConverters();
        recordClass = Class.forName(recordClassName);
        recordClassSetters = new Method[headersMapping.length];
        Method[] methods = recordClass.getDeclaredMethods();

        /*
         * Initialize setters that will be used to populate the returned instance.
         * This introspection should be done only once for evident performance reason.
         */
        for(int i = 0 ;i < headersMapping.length; i++ ){
            String setterName = "set" + headersMapping[i].substring(0, 1).toUpperCase() + headersMapping[i].substring(1);//javabean naming convention
            for (Method method : methods) {
                if (method.getName().equals(setterName)) {
                    recordClassSetters[i] = method;
                }
            }
        }
    }

    public DefaultRecordMapperImpl(String recordClassName, String[] headersMapping, Map<Class, TypeConverter> typeConverters) throws ClassNotFoundException {

        this(recordClassName, headersMapping);
        this.typeConverters.putAll(typeConverters);

    }

    /**
     * {@inheritDoc}
     */
    public Object mapRecord(Record record) throws RecordMappingException {

        List<Field> fields = record.getFields();

        Object instance;
        try {
            //create a new instance of the returned object
            instance = recordClass.newInstance();

            // for each field
            for (Field field : fields) {

                //get field content and index
                String content = field.getContent();
                int index = field.getIndex();

                //convert the String raw value to field type
                Object typedValue = null;
                Class<?> type = recordClassSetters[index].getParameterTypes()[0];
                TypeConverter typeConverter = typeConverters.get(type);
                if (typeConverter != null) {
                    typedValue = typeConverter.convert(content);
                }

                //set the typed value to the object field
                recordClassSetters[index].invoke(instance, typedValue);

                /*
                 * Note : using java.lang.reflect.Field.set(instance, typedValue) throws an IllegalAccessException :
                 * Class io.github.benas.cb4j.core.impl.DefaultRecordMapperImpl can not access a member of class X with modifiers "private"
                 * even if a setter is provided
                 */
            }
        } catch (InstantiationException e) {
            throw new RecordMappingException("An exception occurred during record mapping : could not create a new instance.", e);
        } catch (IllegalAccessException e) {
            throw new RecordMappingException("An exception occurred during record mapping : could not access class/field.", e);
        } catch (InvocationTargetException e) {
            throw new RecordMappingException("An exception occurred during record mapping : could not invoke setter.", e);
        }

        return instance;
    }

    /**
     * Initialize default type converters.
     */
    private void initTypeConverters() {
        typeConverters = new HashMap<Class, TypeConverter>();
        typeConverters.put(AtomicInteger.class, new AtomicIntegerTypeConverter());
        typeConverters.put(AtomicLong.class, new AtomicLongTypeConverter());
        typeConverters.put(BigDecimal.class, new BigDecimalTypeConverter());
        typeConverters.put(BigInteger.class, new BigIntegerTypeConverter());
        typeConverters.put(Boolean.class, new BooleanTypeConverter());
        typeConverters.put(Byte.class, new ByteTypeConverter());
        typeConverters.put(Calendar.class, new CalendarTypeConverter());
        typeConverters.put(Character.class, new CharacterTypeConverter());
        typeConverters.put(Double.class, new DoubleTypeConverter());
        typeConverters.put(Float.class, new FloatTypeConverter());
        typeConverters.put(Integer.class, new IntegerTypeConverter());
        typeConverters.put(Long.class, new LongTypeConverter());
        typeConverters.put(Short.class, new ShortTypeConverter());
        typeConverters.put(java.util.Date.class, new DateTypeConverter());
        typeConverters.put(java.sql.Date.class, new SqlDateTypeConverter());
        typeConverters.put(java.sql.Time.class, new SqlTimeTypeConverter());
        typeConverters.put(java.sql.Timestamp.class, new SqlTimestampTypeConverter());
        typeConverters.put(String.class, new StringTypeConverter());
    }

}
