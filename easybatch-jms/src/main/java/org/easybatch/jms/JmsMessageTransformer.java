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

package org.easybatch.jms;

import org.easybatch.core.api.RecordProcessingException;
import org.easybatch.core.api.RecordProcessor;
import org.easybatch.core.record.StringRecord;

import javax.jms.JMSException;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

/**
 * Convenient processor that creates a Jms {@link TextMessage} from the payload of a {@link StringRecord}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JmsMessageTransformer implements RecordProcessor<StringRecord, TextMessage> {

    private QueueSession queueSession;

    /**
     * Create a Jms message transformer.
     *
     * @param queueSession the queue session to create text messages
     */
    public JmsMessageTransformer(QueueSession queueSession) {
        this.queueSession = queueSession;
    }

    @Override
    public TextMessage processRecord(StringRecord record) throws RecordProcessingException {
        TextMessage message;
        try {
            message = queueSession.createTextMessage();
            message.setText(record.getPayload());
            return message;
        } catch (JMSException e) {
            throw new RecordProcessingException("Unable to create text message from record " + record, e);
        }
    }
}