/*
 * The MIT License
 *
 *   Copyright (c) 2021, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import org.jeasy.batch.core.processor.RecordProcessor;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.record.StringRecord;

import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import static org.jeasy.batch.core.util.Utils.checkNotNull;

/**
 * Processor that creates a {@link JmsRecord} with {@link TextMessage} payload from a {@link StringRecord}.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JmsMessageTransformer implements RecordProcessor<String, Message> {

    private Session session;

    /**
     * Create a new {@link JmsMessageTransformer}.
     *
     * @param session to create text messages
     */
    public JmsMessageTransformer(final Session session) {
        checkNotNull(session, "session");
        this.session = session;
    }

    @Override
    public JmsRecord processRecord(final Record<String> record) throws Exception {
        TextMessage message = session.createTextMessage();
        message.setText(record.getPayload());
        return new JmsRecord(record.getHeader(), message);
    }
}
