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
    public void open() throws Exception {
        currentRecordNumber = 0;
        statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        if (maxRowsEnabled) {
            statement.setMaxRows(maxRows);
        }
        resultSet = statement.executeQuery(query);
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
        long rowCount = 0;

        try {
            if (resultSet.last()) {
                rowCount = resultSet.getRow();
                resultSet.beforeFirst();
            }
            return rowCount;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "An exception occurred during fetching database record set size", e);
            return null;
        }
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
    public void close() throws Exception {
        if (resultSet != null) {
            resultSet.close();
        }
        if (statement != null) {
            statement.close();
        }
        if (connection != null) {
            connection.close();
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

}
