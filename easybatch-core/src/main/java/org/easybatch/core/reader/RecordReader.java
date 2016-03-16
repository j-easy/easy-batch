/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.core.reader;

import org.easybatch.core.record.Record;

/**
 * Interface for record reader.
 * This will be used by easy batch to <strong>sequentially</strong> read records from a data source.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public interface RecordReader {

    /**
     * Open the reader.
     *
     * @throws RecordReaderOpeningException thrown if an exception occurs during reader opening
     */
    void open() throws RecordReaderOpeningException;

    /**
     * Check if the reader has a next record.
     *
     * @return true if the reader has a next record, false else.
     */
    boolean hasNextRecord();

    /**
     * Read next record from the data source.
     *
     * @return the next record from the data source.
     * @throws RecordReadingException thrown if an exception occurs during reading next record
     */
    Record readNextRecord() throws RecordReadingException;

    /**
     * Get the total record number in the data source. This is useful to calculate execution progress.
     *
     * @return the total record number in the data source or null if the total records number cannot be
     * calculated in advance
     */
    Long getTotalRecords();

    /**
     * This method returns a human readable data source name to be shown in the batch report.
     *
     * @return the data source name this reader is reading data from
     */
    String getDataSourceName();

    /**
     * Close the reader.
     *
     * @throws RecordReaderClosingException thrown if an exception occurs during reader closing
     */
    void close() throws RecordReaderClosingException;

}
