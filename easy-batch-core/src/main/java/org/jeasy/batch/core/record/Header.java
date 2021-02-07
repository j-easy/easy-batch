/*
 * The MIT License
 *
 *   Copyright (c) 2021, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.jeasy.batch.core.record;

import java.time.LocalDateTime;

/**
 * The record header contains metadata about the record.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class Header {

    private Long number;
    private String source;
    private LocalDateTime creationDate;
    private boolean scanned;

    /**
     * @param number       physical record number in the data source (if defined).
     * @param source       data source name from which this record has been read.
     * @param creationDate date at which the record has been read.
     */
    public Header(Long number, String source, LocalDateTime creationDate) {
        this.number = number;
        this.source = source;
        this.creationDate = creationDate;
    }

    /**
     * @return the physical record number in the data source (if defined).
     */
    public Long getNumber() {
        return number;
    }

    /**
     * @return the data source name from which this record has been read.
     */
    public String getSource() {
        return source;
    }

    /**
     * @return the date at which the record has been read.
     */
    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    /**
     * Return true if the record has been part of a batch scanning operation.
     *
     * @return true if the record has been part of a batch scanning operation.
     */
    public boolean isScanned() {
        return scanned;
    }

    public void setScanned(boolean scanned) {
        this.scanned = scanned;
    }

    @Override
    public String toString() {
        return "number=" + number +
                ", source=\"" + source + '\"' +
                ", creationDate=\"" + creationDate + '\"' +
                ", scanned=\"" + scanned + '\"';
    }
}
