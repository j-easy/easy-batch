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

package net.benas.cb4j.core.impl;

import net.benas.cb4j.core.api.RecordMapper;
import net.benas.cb4j.core.api.RecordMappingException;
import net.benas.cb4j.core.model.Field;
import net.benas.cb4j.core.model.Record;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;
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
     * An array of methods holding setters that will be used to populate the returned instance.
     */
    private Method[] recordClassSetters;

    public DefaultRecordMapperImpl(String recordClassName, String[] headersMapping) throws ClassNotFoundException {

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
                Object typedValue = convertValue( content, recordClassSetters[index].getParameterTypes()[0]);

                //set the typed value to the object field
                recordClassSetters[index].invoke(instance, typedValue);

                /*
                 * Note : using java.lang.reflect.Field.set(instance, typedValue) throws an IllegalAccessException :
                 * Class net.benas.cb4j.core.impl.DefaultRecordMapperImpl can not access a member of class X with modifiers "private"
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
     * Convert raw data from String to typed value.
     * @param value the String value to convert
     * @param type the target type
     * @return Converted value
     */
    private Object convertValue(String value, Class type) {

        /*
         * String and Character types
         */
        if (type.equals(String.class)) {
            return value;
        }
        if (type.equals(Character.TYPE) || type.equals(Character.class)) {
            return value.charAt(0);
        }

        /*
         * Boolean type
         */
        if (type.equals(Boolean.TYPE) || type.equals(Boolean.class)) {
            return Boolean.valueOf(value);
        }

        /*
         * Numeric types
         */
        if (type.equals(Byte.TYPE) || type.equals(Byte.class)) {
            return Byte.valueOf(value);
        }
        if (type.equals(Short.TYPE) || type.equals(Short.class)) {
            return Short.valueOf(value);
        }
        if (type.equals(Integer.TYPE) || type.equals(Integer.class)) {
            return Integer.valueOf(value);
        }
        if (type.equals(Long.TYPE) || type.equals(Long.class)) {
            return Long.valueOf(value);
        }
        if (type.equals(Double.TYPE) || type.equals(Double.class)) {
            return Double.valueOf(value);
        }
        if (type.equals(Float.TYPE) || type.equals(Float.class)) {
            return Float.valueOf(value);
        }
        if (type.equals(BigInteger.class)) {
            return new BigInteger(value);
        }
        if (type.equals(BigDecimal.class)) {
            return new BigDecimal(value);
        }
        if (type.equals(AtomicLong.class)) {
            return new AtomicLong(Long.valueOf(value));
        }
        if (type.equals(AtomicInteger.class)) {
            return new AtomicInteger(Integer.valueOf(value));
        }

        /*
         * Date and time types
         */
        if (type.equals(java.util.Date.class)) {
            return new java.util.Date(Date.parse(value));
        }
        if (type.equals(java.sql.Date.class)) {
            return Date.valueOf(value);
        }
        if (type.equals(java.sql.Time.class)) {
            return Time.valueOf(value);
        }
        if (type.equals(java.sql.Timestamp.class)) {
            return Timestamp.valueOf(value);
        }
        if (type.equals(Calendar.class)) {
            return Calendar.getInstance();
        }

        /*
         * Return null for any unsupported type
         */
        return null;

    }
}
