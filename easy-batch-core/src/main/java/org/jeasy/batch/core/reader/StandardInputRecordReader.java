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
package org.jeasy.batch.core.reader;

import org.jeasy.batch.core.record.StringRecord;
import org.jeasy.batch.core.record.Header;

import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * A {@link RecordReader} that reads data from the standard input (useful for tests)
 * until a termination word is read (can be specified at construction time, "quit" by default).
 *
 * This reader produces {@link StringRecord}s.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class StandardInputRecordReader implements RecordReader<String> {

    private static final String DEFAULT_TERMINATION_WORD = "quit";

    private Scanner scanner;
    private long recordNumber;
    private String terminationWord;

    /**
     * Create a new {@link StandardInputRecordReader} with default termination word equal to 'quit'.
     */
    public StandardInputRecordReader() {
        this(DEFAULT_TERMINATION_WORD);
    }

    /**
     * Create a new {@link StandardInputRecordReader} instance with a termination word.
     *
     * @param terminationWord the word to type to stop reading from the standard input.
     */
    public StandardInputRecordReader(final String terminationWord) {
        this.terminationWord = terminationWord;
    }

    @Override
    public void open() {
        scanner = new Scanner(System.in);
    }

    @Override
    public StringRecord readRecord() {
        String payload = scanner.nextLine();
        boolean stop = payload != null && !payload.isEmpty() && payload.equalsIgnoreCase(terminationWord);
        if (stop) {
            return null;
        }
        Header header = new Header(++recordNumber, getDataSourceName(), LocalDateTime.now());
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
