/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.core.util;

import org.easybatch.core.api.Record;

/**
 * A {@link Record} implementation that has textual data as raw content.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class StringRecord implements Record<String> {

    /**
     * The record number.
     */
    protected int recordNumber;

    /**
     * The record raw content.
     */
    protected String rawContent;

    public StringRecord(final int recordNumber, final String rawContent) {
        this.recordNumber = recordNumber;
        this.rawContent = rawContent;
    }

    public int getNumber() {
        return recordNumber;
    }

    public String getRawContent() {
        return rawContent;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StringRecord{");
        sb.append("number=").append(recordNumber);
        sb.append(", rawContent='").append(rawContent).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
