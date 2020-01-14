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
package org.easybatch.extensions.gson;

import com.google.gson.Gson;
import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.record.Record;
import org.easybatch.json.JsonRecord;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Marshals a POJO to Json using <a href="https://code.google.com/p/google-gson/">Google Gson</a>.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class GsonRecordMarshaller<P> implements RecordMarshaller<Record<P>, JsonRecord> {

    private Gson gson;

    /**
     * Create a new {@link GsonRecordMarshaller}.
     */
    public GsonRecordMarshaller() {
        gson = new Gson();
    }

    /**
     * Create a new {@link GsonRecordMarshaller}.
     *
     * @param gson a pre-configured Gson instance
     */
    public GsonRecordMarshaller(final Gson gson) {
        checkNotNull(gson, "gson parameter");
        this.gson = gson;
    }

    @Override
    public JsonRecord processRecord(final Record<P> record) throws Exception {
        return new JsonRecord(record.getHeader(), gson.toJson(record.getPayload()));
    }

}
