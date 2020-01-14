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
package org.easybatch.test.common;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import java.sql.SQLException;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

public abstract class AbstractDatabaseTest {

    protected EmbeddedDatabaseBuilder embeddedDatabaseBuilder
                        = new EmbeddedDatabaseBuilder().setType(HSQL).addScript("schema.sql");
    protected EmbeddedDatabase embeddedDatabase;
    protected JdbcTemplate jdbcTemplate;

    protected void setUp() throws Exception {
        embeddedDatabase = embeddedDatabaseBuilder.build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
    }

    protected void tearDown() throws Exception {
        embeddedDatabase.shutdown();
    }

    protected void addScript(String script) {
        embeddedDatabaseBuilder.addScript(script);
    }

    protected int countRowsIn(String table) throws SQLException {
        return jdbcTemplate.queryForObject("select count(*) from " + table, Integer.class);
    }

}
