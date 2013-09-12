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

package io.github.benas.cb4j.core.validator.test;

import io.github.benas.cb4j.core.model.Field;
import io.github.benas.cb4j.core.validator.AfterDateFieldValidator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit Tests class for {@link io.github.benas.cb4j.core.validator.AfterDateFieldValidator}
 * @author benas (md.benhassine@gmail.com)
 */
public class AfterDateFieldValidatorTest {

    private AfterDateFieldValidator afterDateFieldValidator;

    private Field field;

    @Before
    public void setUp() throws Exception {
        Date today = new Date(); // we will validate dates after today
        afterDateFieldValidator = new AfterDateFieldValidator("yyyy/MM/dd", today);
    }

    @After
    public void tearDown() throws Exception {
        afterDateFieldValidator = null;
        field = null;
        System.gc();
    }

    @Test
    public void testValidDateValue() throws Exception {
        field = new Field(1,"2050/08/24"); // this date is after today
        assertTrue(afterDateFieldValidator.isValidField(field));
    }

    @Test
    public void testInValidDateValue() throws Exception {
        field = new Field(1,"2000/08/24"); // this date is before today
        assertFalse(afterDateFieldValidator.isValidField(field));
    }

}
