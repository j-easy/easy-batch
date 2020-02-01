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

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.jeasy.batch.core.record.Batch;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.writer.RecordWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.jeasy.batch.core.util.Utils.checkNotNull;

/**
 * Sends a Jms message to a given destination. This writer expects record payloads
 * of type {@link javax.jms.Message}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JmsRecordWriter implements RecordWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(JmsRecordWriter.class.getSimpleName());

    private ConnectionFactory connectionFactory;
    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;
    private Destination destination;

    /**
     * Create a new {@link JmsRecordWriter}.
     *
     * @param connectionFactory to use to create connections.
     * @param destination                  the target destination to write records to
     */
    public JmsRecordWriter(final ConnectionFactory connectionFactory, final Destination destination) {
        checkNotNull(connectionFactory, "connection factory");
        checkNotNull(destination, "destination");
        this.connectionFactory = connectionFactory;
        this.destination = destination;
    }

    @Override
    public void open() throws Exception {
        LOGGER.debug("Opening JMS connection");
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        messageProducer = session.createProducer(destination);
    }

    @Override
    public void writeRecords(Batch batch) throws Exception {
        for (Record record : batch) {
            messageProducer.send((Message) record.getPayload());
        }
    }

    @Override
    public void close() throws Exception {
        if (messageProducer != null) {
            messageProducer.close();
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
