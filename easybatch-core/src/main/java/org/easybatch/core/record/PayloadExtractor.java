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

import java.util.ArrayList;
import java.util.List;

/**
 * Utility class to extract payloads from records and batches.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public abstract class PayloadExtractor {

    PayloadExtractor() {
        // private constructor
    }

    /**
     * Extract the payload form each record.
     *
     * @param records the list fo records
     * @param <P>     the type of payload
     * @return the list of payloads
     */
    public static <P> List<P> extractPayloads(final List<? extends Record<P>> records) {
        List<P> payloads = new ArrayList<>();
        for (Record<P> record : records) {
            payloads.add(record.getPayload());
        }
        return payloads;
    }

    /**
     * Extract the payload form a batch of records.
     *
     * @param batch the batch of records
     * @return the list of payloads
     */
    public static List<Object> extractPayloads(final Batch batch) {
        List<Record> records = batch.getPayload();
        List<Object> payloads = new ArrayList<>();
        for (Record record : records) {
            payloads.add(record.getPayload());
        }
        return payloads;
    }
}
