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

import org.easybatch.core.record.Batch;
import org.easybatch.core.record.Record;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

import static org.easybatch.core.util.Utils.isCollection;
import static org.easybatch.core.util.Utils.isBatch;

/**
 * Abstract class for all BatchWriters.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public abstract class AbstractBatchWriter extends AbstractRecordWriter {

    protected static final Logger LOGGER = Logger.getLogger(AbstractBatchWriter.class.getName());

    @SuppressWarnings("unchecked")
    protected List getRecords(Object batch) {
        List records = new ArrayList();
        if (isBatch(batch)) {
            List<Record> items = ((Batch) batch).getPayload();
            for (Record item : items) {
                records.add(item.getPayload());
            }
        } else if (isCollection(batch)) {
            records.addAll((Collection) batch);
        } else {
            LOGGER.warning("BatchWriters accept only " + Batch.class.getName() + " or " + Collection.class.getName() + " types");
        }
        return records;
    }

}
