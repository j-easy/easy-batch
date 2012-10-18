/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
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

package net.benas.cb4j.core.model;

/**
 * A model class representing a CSV field.
 * @author benas (md.benhassine@gmail.com)
 */
public final class Field {

    /**
     * The field's index in CSV record.
     */
    private int index;

    /**
     * The field's raw content.
     */
    private String content;

    public Field(int index, String content) {
        this.index = index;
        this.content = content;
    }

    /**
     * Get the field's raw content.
     * @return raw content of the field
     */
    public String getContent() {
        return content;
    }

    /**
     * Setter for content
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * Get the field's index in CSV record. Indexes are zero-based.
     * @return the index of the field in the CSV record
     */
    public int getIndex() {
        return index;
    }

    /**
     * Setter for field index
     * @param index field index to set
     */
    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Field");
        sb.append("[index=").append(index);
        sb.append(", content='").append(content).append('\'');
        sb.append(']');
        return sb.toString();
    }
}
