/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

import org.easybatch.core.writer.AbstractMultiRecordWriter;
import org.easybatch.core.writer.RecordWritingException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Write a chunk of records to a database using the JDBC API.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JdbcMultiRecordWriter extends AbstractMultiRecordWriter {

    private Connection connection;

    private String query;

    private PreparedStatementProvider preparedStatementProvider;

    /**
     * Create a new JDBC multi record writer.
     *
     * @param connection                the JDBC connection
     * @param query                     the query to insert data
     * @param preparedStatementProvider the prepared statement provider to map data to the query parameters
     */
    public JdbcMultiRecordWriter(final Connection connection, final String query, final PreparedStatementProvider preparedStatementProvider) {
        checkNotNull(connection, "connection");
        checkNotNull(query, "query");
        checkNotNull(preparedStatementProvider, "prepared statement");
        this.connection = connection;
        this.query = query;
        this.preparedStatementProvider = preparedStatementProvider;
    }

    @Override
    public void writeRecord(final Object multiRecord) throws RecordWritingException {
        List records = getRecords(multiRecord);
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            for (Object record : records) {
                preparedStatementProvider.prepareStatement(preparedStatement, record);
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
        } catch (SQLException e) {
            throw new RecordWritingException("Unable to write multi record " + multiRecord + " to database", e);
        }
    }

}
