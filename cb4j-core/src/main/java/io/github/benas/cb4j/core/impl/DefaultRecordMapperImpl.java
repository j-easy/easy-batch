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

import io.github.benas.cb4j.core.api.RecordMapper;
import io.github.benas.cb4j.core.api.RecordMappingException;
import io.github.benas.cb4j.core.model.Field;
import io.github.benas.cb4j.core.model.Record;
import io.github.benas.cb4j.core.util.TypeConversionUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

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
                Object typedValue = TypeConversionUtil.convertValue(content, recordClassSetters[index].getParameterTypes()[0]);

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

}
