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

package org.easybatch.core.writer;

import org.easybatch.core.record.MultiRecord;
import org.easybatch.core.record.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.isCollection;
import static org.easybatch.core.util.Utils.isMultiRecord;

/**
 * Abstract class for all MultiRecordWriters.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public abstract class AbstractMultiRecordWriter extends AbstractRecordWriter {

    protected static final Logger LOGGER = Logger.getLogger(AbstractMultiRecordWriter.class.getName());

    @SuppressWarnings("unchecked")
    protected List getRecords(Object multiRecord) {
        List records = new ArrayList();
        if (isMultiRecord(multiRecord)) {
            List<Record> items = ((MultiRecord) multiRecord).getPayload();
            for (Record item : items) {
                records.add(item.getPayload());
            }
        } else if (isCollection(multiRecord)) {
            records.addAll((Collection) multiRecord);
        } else {
            LOGGER.warning("MultiRecordWriters accept only " + MultiRecord.class.getName() + " or " + Collection.class.getName() + " types");
        }
        return records;
    }

}
