/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import org.easybatch.core.filter.RecordFilter;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Filter for {@link JmsPoisonRecord}s.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JmsPoisonRecordFilter implements RecordFilter<JmsRecord> {

    private static final Logger LOGGER = Logger.getLogger(JmsPoisonRecordFilter.class.getName());

    /**
     * Return true if the record should be filtered.
     *
     * @param record the record to filter
     * @return true if the record should be filtered, false else
     */
    @Override
    public JmsRecord processRecord(JmsRecord record) {
        boolean isPoison = false;
        Message payload = record.getPayload();
        try {
            if (payload != null) {
                String type = payload.getJMSType();
                if (type != null && !type.isEmpty()) {
                    isPoison = JmsPoisonMessage.TYPE.equals(type);
                }
            }
        } catch (JMSException e) {
            LOGGER.log(Level.WARNING, "Unable to get type of JMS message " + payload, e);
            return null;
        }
        if (record instanceof JmsPoisonRecord || isPoison || payload instanceof JmsPoisonMessage) {
            return null;
        }
        return record;
    }

}
