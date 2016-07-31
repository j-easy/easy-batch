/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.core.reader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StandardInputRecordReaderTest {

    private static final String EXPECTED_DATA_SOURCE_NAME = "Standard Input";

    private StandardInputRecordReader standardInputRecordReader;

    @Before
    public void setUp() throws Exception {
        standardInputRecordReader = new StandardInputRecordReader();
        standardInputRecordReader.open();
    }

    @Test
    public void testGetTotalRecords() throws Exception {
        assertThat(standardInputRecordReader.getTotalRecords()).isNull();
    }

    @Test
    public void testGetDataSourceName() throws Exception {
        assertThat(standardInputRecordReader.getDataSourceName()).isEqualTo(EXPECTED_DATA_SOURCE_NAME);
    }

    @After
    public void tearDown() throws Exception {
        standardInputRecordReader.close();
    }

}
