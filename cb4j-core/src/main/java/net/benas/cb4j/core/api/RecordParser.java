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

package net.benas.cb4j.core.api;

import net.benas.cb4j.core.model.Record;

/**
 * Interface for record parser.<br/>
 *
 * This interface is not intended to be implemented nor used by framework users
 *
 * @author benas (md.benhassine@gmail.com)
 */
public interface RecordParser {

    /**
     * Checks if a record is well formed (fields number as expected).
     * @param record the record to check
     * @return true if the record is well formed, false else
     */
    boolean isWellFormed(final String record);

    /**
     * Getter for the actual record size.<br/>
     * This method is used to report the cause of ignoring a record when it's actual size is not equal to the expected record size
     * @param record the record to use
     * @return record size
     */
    int getRecordSize(final String record);

    /**
     * Parses a string and return a parsed {@link Record}.
     * @param record a string containing the CSV record
     * @param recordNumber the record number
     * @return a parsed {@link Record}
     */
    Record parseRecord(final String record, long recordNumber);

}
