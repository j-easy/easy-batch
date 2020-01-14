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
package org.easybatch.core.reader;

import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;

import java.util.Date;

public class UnreliableRecordReader implements RecordReader {

    public static final String DATA_SOURCE_NAME = "dummy source";
    public static final int TOTAL_RECORDS = 3;
    private int attempt;
    private int nbRecord;

    @Override
    public void open() throws Exception {
        attempt = 0;
        nbRecord = 0;
    }

    @Override
    public Record readRecord() throws Exception {
        if (++nbRecord > TOTAL_RECORDS) {
            return null;
        }
        attempt++;
        if (attempt >= 3) {
            return new StringRecord(new Header((long) nbRecord, DATA_SOURCE_NAME, new Date()), "r" + nbRecord);
        } else {
            throw new Exception("Data source has gone!");
        }
    }

    @Override
    public void close() throws Exception {

    }
}
