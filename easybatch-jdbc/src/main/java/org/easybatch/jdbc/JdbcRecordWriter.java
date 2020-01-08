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

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;
import org.easybatch.core.writer.RecordWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Write records to a relational database using the JDBC API.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JdbcRecordWriter implements RecordWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcRecordWriter.class.getSimpleName());

    private DataSource dataSource;
    private Connection connection;
    private String query;
    private PreparedStatementProvider preparedStatementProvider;

    /**
     * Create a new {@link JdbcRecordWriter}.
     *
     * @param dataSource                the JDBC data source
     * @param query                     the query to insert data
     * @param preparedStatementProvider the prepared statement provider to map data to the query parameters
     */
    public JdbcRecordWriter(final DataSource dataSource, final String query, final PreparedStatementProvider preparedStatementProvider) {
        checkNotNull(dataSource, "data source");
        checkNotNull(query, "query");
        checkNotNull(preparedStatementProvider, "prepared statement");
        this.dataSource = dataSource;
        this.query = query;
        this.preparedStatementProvider = preparedStatementProvider;
    }

    @Override
    public void open() throws Exception {
        connection = dataSource.getConnection();
        connection.setAutoCommit(false);
    }

    @Override
    public void writeRecords(Batch batch) throws Exception {
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            for (Record record : batch) {
                preparedStatementProvider.prepareStatement(preparedStatement, record.getPayload());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();
            connection.commit();
            LOGGER.debug("Transaction committed");
        } catch (SQLException e) {
            LOGGER.error("Unable to commit transaction", e);
            connection.rollback();
            throw e;
        } finally {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
        }
    }

    @Override
    public void close() throws Exception {
        if (connection != null) {
            LOGGER.info("Closing connection");
            connection.close();
        }
    }
}
