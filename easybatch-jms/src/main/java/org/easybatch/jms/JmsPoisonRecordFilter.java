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

package org.easybatch.jms;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordFilter;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Filter jms poison records.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JmsPoisonRecordFilter implements RecordFilter {

    private static final Logger LOGGER = Logger.getLogger(JmsPoisonRecordFilter.class.getName());

    /**
     * Return true if the record should be filtered (skipped).
     *
     * @param record the record to filter
     * @return true if the record should be filtered (skipped)
     */
    @Override
    public boolean filterRecord(Record record) {
        JmsRecord jmsRecord = (JmsRecord) record;
        boolean isPoison = false;
        Message payload = jmsRecord.getPayload();
        try {
            String type = payload.getJMSType();
            if (type != null && !type.isEmpty()) {
                isPoison = JmsPoisonMessage.TYPE.equals(type);
            }
        } catch (JMSException e) {
            LOGGER.log(Level.WARNING, "Unable to get type of JMS message " + payload, e);
            return false;
        }
        return isPoison || payload instanceof JmsPoisonMessage;
    }

}
