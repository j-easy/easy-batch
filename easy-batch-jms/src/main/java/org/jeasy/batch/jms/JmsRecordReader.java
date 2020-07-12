/*
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
package org.jeasy.batch.jms;

import java.time.LocalDateTime;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.Session;

import org.jeasy.batch.core.reader.RecordReader;
import org.jeasy.batch.core.record.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.jeasy.batch.core.util.Utils.checkArgument;
import static org.jeasy.batch.core.util.Utils.checkNotNull;

/**
 * A record reader that reads records from a JMS destination.
 *
 * This reader produces {@link JmsRecord} instances with a payload of type {@link Message}.
 *
 * It will stop reading records after a given timeout (defaults to {@link #DEFAULT_TIMEOUT}).
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JmsRecordReader implements RecordReader<Message> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmsRecordReader.class.getSimpleName());

    private long currentRecordNumber;
    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageConsumer messageConsumer;
    private Destination destination;
    private long timeout;

    /**
     * Default timeout after which the reader will return {@code null}.
     */
    public static final long DEFAULT_TIMEOUT = 60000;

    /**
     * Create a new {@link JmsRecordReader}.
     *
     * @param connectionFactory to use to create connections
     * @param destination                  to read records from
     */
    public JmsRecordReader(final ConnectionFactory connectionFactory, final Destination destination) {
        this(connectionFactory, destination, DEFAULT_TIMEOUT);
    }

    /**
     * Create a new {@link JmsRecordReader}.
     *
     * @param connectionFactory to use to create connections
     * @param destination                  to read records from
     * @param timeout                in milliseconds after which the reader will return {@code null}
     */
    public JmsRecordReader(final ConnectionFactory connectionFactory, final Destination destination, final long timeout) {
        checkNotNull(connectionFactory, "connection factory");
        checkNotNull(destination, "destination");
        checkArgument(timeout > 0, "timeout must be positive");
        this.connectionFactory = connectionFactory;
        this.destination = destination;
        this.timeout = timeout;
    }

    @Override
    public void open() throws Exception {
        LOGGER.debug("Opening JMS connection");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        messageConsumer = session.createConsumer(destination);
        connection.start();
    }

    @Override
    public JmsRecord readRecord() throws Exception {
        Message message = messageConsumer.receive(timeout); // return null when timed out (See its javadoc)
        if (message == null) {
            return null;
        }
        Header header = new Header(++currentRecordNumber, getDataSourceName(), LocalDateTime.now());
        return new JmsRecord(header, message);
    }

    private String getDataSourceName() {
        return "JMS destination: " + destination.toString();
    }

    @Override
    public void close() throws Exception {
        if (messageConsumer != null) {
            messageConsumer.close();
        }
        if (session != null) {
            session.close();
        }
        if (connection != null) {
            LOGGER.debug("Closing JMS connection");
            connection.close();
        }
    }

}
