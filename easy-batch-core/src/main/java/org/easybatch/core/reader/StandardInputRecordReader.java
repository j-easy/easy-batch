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
package org.easybatch.core.reader;

import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;

import java.util.Date;
import java.util.Scanner;

/**
 * A {@link RecordReader} that reads data from the standard input (useful for tests)
 * until a termination word is read (can be specified at construction time, "quit" by default).
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class StandardInputRecordReader implements RecordReader {

    private static final String DEFAULT_TERMINATION_WORD = "quit";

    /**
     * The standard input scanner.
     */
    private Scanner scanner;

    /**
     * The current record number.
     */
    private long recordNumber;

    /**
     * The word to type to terminate reading from the standard input.
     */
    private String terminationInput;

    /**
     * Create a new {@link StandardInputRecordReader} with default termination input equal to 'quit'.
     */
    public StandardInputRecordReader() {
        this(DEFAULT_TERMINATION_WORD);
    }

    /**
     * Create a new {@link StandardInputRecordReader} instance with a termination word.
     *
     * @param terminationInput the word to type to stop reading from the standard input.
     */
    public StandardInputRecordReader(final String terminationInput) {
        this.terminationInput = terminationInput;
    }

    @Override
    public void open() {
        scanner = new Scanner(System.in);
    }

    @Override
    public Record readRecord() throws Exception {
        String payload = scanner.nextLine();
        boolean stop = payload != null && !payload.isEmpty() && payload.equalsIgnoreCase(terminationInput);
        if (stop) {
            return null;
        }
        Header header = new Header(++recordNumber, getDataSourceName(), new Date());
        return new StringRecord(header, payload);
    }


    private String getDataSourceName() {
        return "Standard Input";
    }

    @Override
    public void close() {
        scanner.close();
    }

}
