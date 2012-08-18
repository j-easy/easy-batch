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

package net.benas.cb4j.core.converter.test;

import net.benas.cb4j.core.converter.TypeConversionException;
import net.benas.cb4j.core.converter.TypeConverter;
import net.benas.cb4j.core.model.Field;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;

/**
 * Unit tests of type conversion service
 * @author benas (md.benhassine@gmail.com)
 */
public class TypeConverterTest {

    private Field field;

    @Before
    public void setUp() throws Exception {
        field = new Field(1,null);
    }

    @After
    public void tearDown() throws Exception {
        field = null;
        System.gc();
    }

    @Test
    public void testIntegerTypeConversionOK() throws Exception{
        field.setContent("123");
        Integer i = (Integer) TypeConverter.getTypedFieldContent(field.getContent(), Integer.class);
        assertNotNull(i);
    }

    @Test
    public void testIntTypeConversionOK() throws Exception{
        field.setContent("123");
        int i = (Integer) TypeConverter.getTypedFieldContent(field.getContent(), Integer.TYPE);
        assertNotNull(i);
    }

    @Test(expected = TypeConversionException.class)
    public void testIntegerTypeConversionKO() throws Exception{
        field.setContent("123x");
        TypeConverter.getTypedFieldContent(field.getContent(), Integer.class);
    }

    @Test
    public void testDateTypeConversionOK() throws Exception{
        field.setContent("20110203");
        Date date = TypeConverter.getDateTypedFieldContent(field.getContent(), "yyyyMMdd");
        assertNotNull(date);
    }

    @Test(expected = TypeConversionException.class)
    public void testDateTypeConversionKO() throws Exception{
        field.setContent("x20110203x");
        TypeConverter.getDateTypedFieldContent(field.getContent(), "yyyyMMdd");
    }
    

}
