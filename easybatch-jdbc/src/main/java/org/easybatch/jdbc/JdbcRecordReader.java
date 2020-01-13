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

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import static org.easybatch.core.util.Utils.checkArgument;
import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * A {@link RecordReader} that reads records from a database using JDBC API.
 *
 * This reader produces {@link JdbcRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JdbcRecordReader implements RecordReader {

    private static final Logger LOGGER = LoggerFactory.getLogger(JdbcRecordReader.class.getSimpleName());

    private DataSource dataSource;
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private String query;
    private String dataSourceName;
    private long currentRecordNumber;

    // parameters
    private int maxRows;
    private int queryTimeout;
    private int fetchSize;

    /**
     * Create a new {@link JdbcRecordReader}.
     *
     * @param dataSource to read data from
     * @param query      to fetch data
     */
    public JdbcRecordReader(final DataSource dataSource, final String query) {
        checkNotNull(dataSource, "data source");
        checkNotNull(query, "query");
        this.dataSource = dataSource;
        this.query = query;
    }

    @Override
    public void open() throws Exception {
        currentRecordNumber = 0;
        connection = dataSource.getConnection();
        statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        if (maxRows >= 1) {
            statement.setMaxRows(maxRows);
        }
        if (fetchSize >= 1) {
            statement.setFetchSize(fetchSize);
        }
        if (queryTimeout >= 1) {
            statement.setQueryTimeout(queryTimeout);
        }
        resultSet = statement.executeQuery(query);
        dataSourceName = getDataSourceName();
    }

    private boolean hasNextRecord() {
        try {
            return resultSet.next();
        } catch (SQLException e) {
            LOGGER.error("An exception occurred during checking the existence of next database record", e);
            return false;
        }
    }

    @Override
    public JdbcRecord readRecord() {
        if (hasNextRecord()) {
            Header header = new Header(++currentRecordNumber, dataSourceName, new Date());
            return new JdbcRecord(header, resultSet);
        } else {
            return null;
        }
    }

    private String getDataSourceName() {
        try {
            return "Connection URL: " + connection.getMetaData().getURL() + " | Query string: " + query;
        } catch (SQLException e) {
            LOGGER.error("Unable to get data source name", e);
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
    public void setMaxRows(final int maxRows) {
        checkArgument(maxRows >= 1, "max rows parameter must be greater than or equal to 1");
        this.maxRows = maxRows;
    }

    /**
     * Set the statement fetch size.
     *
     * @param fetchSize the fetch size to set
     */
    public void setFetchSize(final int fetchSize) {
        checkArgument(fetchSize >= 1, "fetch size parameter must be greater than or equal to 1");
        this.fetchSize = fetchSize;
    }

    /**
     * Set the statement query timeout.
     *
     * @param queryTimeout the query timeout in seconds
     */
    public void setQueryTimeout(final int queryTimeout) {
        checkArgument(queryTimeout >= 1, "query timeout parameter must be greater than or equal to 1");
        this.queryTimeout = queryTimeout;
    }
}
