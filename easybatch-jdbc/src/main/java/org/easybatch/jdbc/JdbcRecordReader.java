/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.jdbc;

import org.easybatch.core.api.Header;
import org.easybatch.core.api.RecordReader;
import org.easybatch.core.exception.RecordReaderClosingException;
import org.easybatch.core.exception.RecordReaderOpeningException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A {@link org.easybatch.core.api.RecordReader} that reads records from a database using jdbc API.
 * <p/>
 * This reader produces {@link JdbcRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JdbcRecordReader implements RecordReader {

    /**
     * The logger to use.
     */
    private static final Logger LOGGER = Logger.getLogger(JdbcRecordReader.class.getSimpleName());

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
     * Parameter to limit the number of fetched rows.
     */
    private int maxRows;
    private boolean maxRowsEnabled;

    /**
     * Parameter to set fetch size.
     */
    private int fetchSize;
    private boolean fetchSizeEnabled;

    /**
     * Parameter to set the query timeout.
     */
    private int queryTimeout;
    private boolean queryTimeoutEnabled;

    /**
     * The current record number.
     */
    private long currentRecordNumber;

    /**
     * Create a JdbcRecordReader instance.
     *
     * @param connection the connection to use to read data
     * @param query      the jdbc query to use to fetch data
     */
    public JdbcRecordReader(final Connection connection, final String query) {
        this.connection = connection;
        this.query = query;
    }

    @Override
    public void open() throws RecordReaderOpeningException {
        currentRecordNumber = 0;
        try {
            statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            if (maxRowsEnabled) {
                statement.setMaxRows(maxRows);
            }
            if (fetchSizeEnabled) {
                statement.setFetchSize(fetchSize);
            }
            if (queryTimeoutEnabled) {
                statement.setQueryTimeout(queryTimeout);
            }
            resultSet = statement.executeQuery(query);

        } catch (SQLException e) {
            throw new RecordReaderOpeningException("Unable to open record reader", e);
        }
    }

    @Override
    public boolean hasNextRecord() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An exception occurred during checking the existence of next database record", e);
            return false;
        }
    }

    @Override
    public JdbcRecord readNextRecord() {
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new JdbcRecord(header, resultSet);
    }

    @Override
    public Long getTotalRecords() {
        return null;
    }

    @Override
    public String getDataSourceName() {
        try {
            return "Connection URL: " + connection.getMetaData().getURL() + " | " +
                    "Query string: " + query;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Unable to get data source name", e);
            return "N/A";
        }
    }

    @Override
    public void close() throws RecordReaderClosingException {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            throw new RecordReaderClosingException("Unable to close record reader", e);
        }

    }

    /**
     * Set the maximum number of rows to fetch.
     *
     * @param maxRows the maximum number of rows to fetch
     */
    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
        this.maxRowsEnabled = true;
    }

    /**
     * Set the statement fetch size.
     *
     * @param fetchSize the fetch size to set
     */
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
        this.fetchSizeEnabled = true;
    }

    /**
     * Set the statement query timeout.
     *
     * @param queryTimeout the query timeout in seconds
     */
    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
        this.queryTimeoutEnabled = true;
    }
}
