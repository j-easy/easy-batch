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

package org.easybatch.core.record;

import org.easybatch.core.api.Header;
import org.easybatch.core.api.Record;

import java.util.List;

/**
 * A multi-record is a record containing a list of records.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class MultiRecord extends GenericRecord<List<Record>> {
    
    public MultiRecord(Header header, List<Record> payload) {
        super(header, payload);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("MultiRecord: {");
        for (Record record : payload) {
            stringBuilder.append("\trecord = ");
            stringBuilder.append(record);
        }
        stringBuilder.append("}");
        return stringBuilder.toString();
    }
}
