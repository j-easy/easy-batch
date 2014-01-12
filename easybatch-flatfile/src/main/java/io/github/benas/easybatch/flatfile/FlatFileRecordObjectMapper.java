/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
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

package io.github.benas.easybatch.flatfile;

import io.github.benas.easybatch.flatfile.converter.*;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A helper class that maps a flat file record to a domain object.
 *
 * @param <T> the target domain object type
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class FlatFileRecordObjectMapper<T> {

    /**
     * The logger.
     */
    private Logger logger = Logger.getLogger(FlatFileRecordObjectMapper.class.getName());

    /**
     * The target domain object class.
     */
    private Class<? extends T> recordClass;

    /**
     * An array of methods holding setters that will be used to populate the returned instance of the domain object.
     */
    private Method[] recordClassSetters;

    /**
     * An array of the names of the properties of the domain object in the same order they appear in the record.
     */
    private String[] headersMapping;

    /**
     * Type converters map.
     */
    private Map<Class, TypeConverter> typeConverters;

    public FlatFileRecordObjectMapper(final Class<? extends T> recordClass, final String[] headersMapping) {
        initTypeConverters();
        this.recordClass = recordClass;
        this.headersMapping = headersMapping;
        recordClassSetters = new Method[headersMapping.length];
        Method[] methods = recordClass.getDeclaredMethods();

        // Initialize setters that will be used to populate the returned instance.
        for (int i = 0; i < headersMapping.length; i++) {
            String setterName = "set" + headersMapping[i].substring(0, 1).toUpperCase() + headersMapping[i].substring(1); //javabean naming convention
            for (Method method : methods) {
                if (method.getName().equals(setterName)) {
                    recordClassSetters[i] = method;
                }
            }
        }
    }

    public T mapObject(final List<FlatFileField> flatFileFields) throws Exception {

        T result = recordClass.newInstance();

        // for each field
        for (FlatFileField flatFileField : flatFileFields) {

            //get field content and index
            String content = flatFileField.getRawContent();
            int index = flatFileField.getIndex();

            //convert the String raw value to field recordClass
            Object typedValue = null;
            Class<?> type = recordClassSetters[index].getParameterTypes()[0];
            TypeConverter typeConverter = typeConverters.get(type);
            if (typeConverter != null) {
                try {
                    typedValue = typeConverter.convert(content);
                } catch (Exception e) {
                    throw new Exception("Unable to convert '" + content + "' to type " + type + " for field " + headersMapping[index]);
                }
            } else {
                logger.log(Level.WARNING, "Type conversion not supported for type " + type + ", field " + headersMapping[index] + " will be set to null");
            }

            //set the typed value to the object field
            recordClassSetters[index].invoke(result, typedValue);

        }

        return result;
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
        typeConverters.put(java.sql.Date.class, new SqlDateTypeConverter());
        typeConverters.put(java.sql.Time.class, new SqlTimeTypeConverter());
        typeConverters.put(java.sql.Timestamp.class, new SqlTimestampTypeConverter());
        typeConverters.put(String.class, new StringTypeConverter());
    }

    public Map<Class, TypeConverter> getTypeConverters() {
        return typeConverters;
    }

    /**
     * Register a custom type converter.
     * @param type the target type
     * @param typeConverter the type converter to user
     */
    public void registerTypeConverter(final Class type, final TypeConverter typeConverter) {
        typeConverters.put(type, typeConverter);
    }

}
