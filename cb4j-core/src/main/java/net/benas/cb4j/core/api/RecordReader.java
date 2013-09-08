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

/**
 * Interface for record reader.
 *
 * This interface is not intended to be implemented nor used by framework users
 *
 * @author benas (md.benhassine@gmail.com)
 */
public interface RecordReader {

    /**
     * Read the next record from input file if available.
     * @return the next record from input file
     */
    String readNextRecord();

    /**
     * Check if a next record is available from input file.
     * @return true if there is a next available record from input file, false else
     */
    boolean hasNextRecord();

    /**
     * Calculate and return total records number in the input file.<br/>
     * This method is used especially to calculate execution progress exposed as JMX attribute.
     * @return total records number
     */
    long getTotalRecordsNumber();

    /**
     * Return header record.
     * @return header record
     */
    String getHeaderRecord();

    /**
     * Close the reader.
     */
    void close();

}
