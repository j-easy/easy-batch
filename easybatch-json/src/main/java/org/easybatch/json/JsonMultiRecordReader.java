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

package org.easybatch.json;

import org.easybatch.core.api.Header;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordReadingException;
import org.easybatch.core.reader.AbstractMultiRecordReader;
import org.easybatch.core.record.MultiRecord;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Reads Json records in chunks.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JsonMultiRecordReader extends AbstractMultiRecordReader {

    /**
     * Create a Json multi-record reader.
     *
     * @param jsonInputStream the Json data source
     * @param chunkSize       the number of Json records to read at a time
     */
    public JsonMultiRecordReader(final InputStream jsonInputStream, final int chunkSize) {
        super(chunkSize, new JsonRecordReader(jsonInputStream));
    }

    @Override
    public MultiRecord readNextRecord() throws RecordReadingException {
        List<Record> records = new ArrayList<Record>();
        int items = 1;
        do {
            records.add(delegate.readNextRecord());
        } while (items++ < chunkSize && hasNextRecord());
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new MultiRecord(header, records);
    }

}
