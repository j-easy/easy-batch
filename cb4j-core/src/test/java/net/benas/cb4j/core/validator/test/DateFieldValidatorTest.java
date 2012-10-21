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

package net.benas.cb4j.core.validator.test;

import net.benas.cb4j.core.model.Field;
import net.benas.cb4j.core.validator.DateFieldValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit Tests class for {@link net.benas.cb4j.core.validator.DateFieldValidator}
 * @author benas (md.benhassine@gmail.com)
 */
public class DateFieldValidatorTest {

    private DateFieldValidator dateFieldValidator;

    private Field field;

    @Before
    public void setUp() throws Exception {
        dateFieldValidator = new DateFieldValidator("yyyy/MM/dd");
        field = new Field(1,null);
    }

    @After
    public void tearDown() throws Exception {
        dateFieldValidator = null;
        field = null;
        System.gc();
    }

    @Test
    public void testValidDateFormatAndValue() throws Exception {
        field.setContent("2011/08/24"); //format and value ok
        assertTrue(dateFieldValidator.isValidField(field));
    }

    @Test
    public void testValidDateValueButInvalidFormat() throws Exception {
        field.setContent("2011-08-24"); //value ok but not expected format
        assertFalse(dateFieldValidator.isValidField(field));
    }

    @Test
    public void testValidDateFormatButInvalidValue() throws Exception {
        field.setContent("2011/13/32"); //format ok but invalid value
        assertFalse(dateFieldValidator.isValidField(field));
    }

    @Test
    public void testInvalidDateFieldValidatorConstruction() throws Exception {
        dateFieldValidator = new DateFieldValidator("blah!"); //invalid format, should use the default one
        field.setContent("24/08/2011"); //format and value ok
        assertTrue(dateFieldValidator.isValidField(field));

    }

}
