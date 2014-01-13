/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
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

package io.github.benas.easybatch.flatfile;

import io.github.benas.easybatch.core.api.Record;

/**
 * A {@link Record} implementation that has textual data as raw content.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class StringRecord implements Record<String> {

    /**
     * The record number in the input file.
     */
    protected long number;

    /**
     * The record raw content.
     */
    protected String rawContent;

    public StringRecord(final long number, final String rawContent) {
        this.number = number;
        this.rawContent = rawContent;
    }

    public long getNumber() {
        return number;
    }

    public String getRawContent() {
        return rawContent;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("StringRecord{");
        sb.append("number=").append(number);
        sb.append(", rawContent='").append(rawContent).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
