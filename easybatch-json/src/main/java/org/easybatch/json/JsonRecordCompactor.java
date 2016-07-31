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

package org.easybatch.json;

import org.easybatch.core.processor.RecordCompactor;

import java.io.ByteArrayInputStream;

/**
 * Compacts a Json record payload.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JsonRecordCompactor extends RecordCompactor {

    @Override
    protected String compact(final String payload) {
        if (payload == null) {
            return null;
        }
        if (payload.trim().isEmpty()) {
            return EMPTY_STRING;
        }

        String dataSource = "[" + payload + "]";
        return doCompactPayload(dataSource);
    }

    private String doCompactPayload(final String dataSource) {
        String flatJson = EMPTY_STRING;
        JsonRecordReader jsonRecordReader = null;
        try {
            jsonRecordReader = new JsonRecordReader(new ByteArrayInputStream(dataSource.getBytes()));
            jsonRecordReader.open();
            if (jsonRecordReader.hasNextRecord()) {
                flatJson = jsonRecordReader.readNextRecord().getPayload();
            }
            return flatJson;
        } catch (Exception exception) {
            return EMPTY_STRING;
        } finally {
            if (jsonRecordReader != null) {
                jsonRecordReader.close();
            }
        }
    }
}