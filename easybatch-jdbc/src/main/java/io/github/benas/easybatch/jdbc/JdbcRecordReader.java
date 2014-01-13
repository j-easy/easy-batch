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

package io.github.benas.easybatch.jdbc;

import io.github.benas.easybatch.core.api.RecordReader;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * A {@link io.github.benas.easybatch.core.api.RecordReader} that reads records from a database using jdbc API.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class JdbcRecordReader implements RecordReader {

    /**
     * The database connection to use to read data.
     */
    private Connection connection;

    /**
     * The statement to use to read data.
     */
    private Statement statement;

    /**
     * The result set that will be returned.
     */
    private ResultSet resultSet;

    /**
     * The jdbc query to use to fetch data.
     */
    private String query;

    /**
     * The current record number.
     */
    private long currentRecordNumber;

    /**
     * Create a JdbcRecordReader instance.
     * @param connection the connection to use to read data
     * @param query the jdbc query to use to fetch data
     */
    public JdbcRecordReader(final Connection connection, final String query) {
        this.connection = connection;
        this.query = query;
    }

    @Override
    public void open() throws Exception {
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        resultSet = statement.executeQuery(query);
    }

    @Override
    public boolean hasNextRecord() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException("An exception occurred during reading database next record", e);
        }
    }

    @Override
    public JdbcRecord readNextRecord() {
        return new JdbcRecord(++currentRecordNumber, resultSet);
    }

    @Override
    public long getTotalRecords() {
        int rowCount = 0;

        try {
            if (resultSet.last()) {
                rowCount = resultSet.getRow();
                resultSet.beforeFirst();
            }
            return rowCount;
        } catch (SQLException e) {
            throw new RuntimeException("An exception occurred during fetching database record set size", e);
        }
    }

    @Override
    public void close() {
        try {
            if (resultSet != null) resultSet.close();
            if (statement != null) { statement.close(); }
            if (connection != null) connection.close();
        }   catch (SQLException e) {
            throw new RuntimeException("An exception occurred during database connection closing", e);
        }
    }

}
