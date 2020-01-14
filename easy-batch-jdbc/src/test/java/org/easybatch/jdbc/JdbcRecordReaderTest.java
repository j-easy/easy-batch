/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.easybatch.jdbc;

import org.easybatch.test.common.AbstractDatabaseTest;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.ResultSet;

import static org.assertj.core.api.Assertions.assertThat;

public class JdbcRecordReaderTest extends AbstractDatabaseTest {

    private String sqlQuery = "select * from tweet";
    private JdbcRecordReader jdbcRecordReader;

    @Before
    public void setUp() throws Exception {
        addScript("data.sql");
        super.setUp();
        jdbcRecordReader = new JdbcRecordReader(embeddedDatabase, sqlQuery);
    }

    @Test
    public void testReadRecord() throws Exception {
        jdbcRecordReader.open();
        JdbcRecord actual = jdbcRecordReader.readRecord();

        assertThat(actual).isNotNull();
        assertThat(actual.getHeader().getNumber()).isEqualTo(1);

        ResultSet payload = actual.getPayload();
        assertThat(payload.getInt(1)).isEqualTo(1);
        assertThat(payload.getString(2)).isEqualTo("foo");
        assertThat(payload.getString(3)).isEqualTo("easy batch rocks! #EasyBatch");
    }

    @Test
    public void testMaxRowsParameter() throws Exception {
        jdbcRecordReader = new JdbcRecordReader(embeddedDatabase, sqlQuery);
        jdbcRecordReader.setMaxRows(1);
        jdbcRecordReader.open();
        assertThat(jdbcRecordReader.readRecord()).isNotNull();
        assertThat(jdbcRecordReader.readRecord()).isNull();
    }

    @After
    public void tearDown() throws Exception {
        jdbcRecordReader.close();
        super.tearDown();
    }

}
