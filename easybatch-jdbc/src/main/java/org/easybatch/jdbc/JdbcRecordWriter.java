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

import org.easybatch.core.api.RecordProcessingException;
import org.easybatch.core.writer.AbstractRecordWriter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Write records to a database using the JDBC API.
 * 
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JdbcRecordWriter extends AbstractRecordWriter {

    private Connection connection;

    private String query;

    private PreparedStatementProvider preparedStatementProvider;

    /**
     * Create a new JDBC writer.
     * 
     * @param connection the JDBC connection
     * @param query the query to insert data
     * @param preparedStatementProvider the prepared statement provider to map data to the query parameters
     */
    public JdbcRecordWriter(Connection connection, String query, PreparedStatementProvider preparedStatementProvider) {
        this.connection = connection;
        this.query = query;
        this.preparedStatementProvider = preparedStatementProvider;
    }

    @Override
    public void writeRecord(final Object record) throws RecordProcessingException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatementProvider.prepareStatement(preparedStatement, record);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RecordProcessingException("Unable to write record " + record + " to database", e);
        }
    }

}
