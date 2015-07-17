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

package org.easybatch.integration.gson;

import com.google.gson.Gson;
import org.easybatch.core.api.RecordMarshallingException;
import org.easybatch.core.processor.AbstractRecordMarshaller;

/**
 * Marshals a POJO to Json using Gson.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class GsonRecordMarshaller extends AbstractRecordMarshaller {

    private Gson gson;

    public GsonRecordMarshaller() {
        gson = new Gson();
    }

    /**
     * Create a Json record marshaller.
     *
     * @param gson a pre-configured Gson instance
     */
    public GsonRecordMarshaller(final Gson gson) {
        this.gson = gson;
    }

    @Override
    protected String marshal(Object record) throws RecordMarshallingException {
        return gson.toJson(record);
    }

}
