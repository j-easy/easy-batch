/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import java.util.List;

import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

/**
 * A batch contains a list of records.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class Batch extends GenericRecord<List<Record>> {

    /**
     * Create a {@link Batch}.
     *
     * @param header  the batch header
     * @param payload the batch payload
     */
    public Batch(Header header, List<Record> payload) {
        super(header, payload);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("Batch: {");
        stringBuilder.append(LINE_SEPARATOR);
        stringBuilder.append("header=[").append(header).append("],");
        stringBuilder.append(LINE_SEPARATOR);
        stringBuilder.append("payload=[").append(LINE_SEPARATOR);
        for (Record record : payload) {
            stringBuilder.append("\t");
            stringBuilder.append(record);
            stringBuilder.append(LINE_SEPARATOR);
        }
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }
}
