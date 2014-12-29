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
import org.easybatch.core.api.RecordReader;

import java.util.Scanner;

/**
 * A convenient {@link RecordReader} that reads data from the standard input (useful for tests).
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class CliRecordReader implements RecordReader {

    /**
     * The standard input scanner.
     */
    private Scanner scanner;

    /**
     * The current record number.
     */
    private int recordNumber;

    /**
     * The word to type to terminate reading from the standard input.
     */
    private String terminationInput;

    /**
     * The stop reading flag.
     */
    private boolean stop;

    /**
     * Default constructor with default termination input equals to 'quit'.
     */
    public CliRecordReader() {
        this("quit");
    }

    /**
     * Constructs a CliRecordReader instance with a termination word.
     * @param terminationInput the word to type to stop reading from the standard input.
     */
    public CliRecordReader(String terminationInput) {
        this.terminationInput = terminationInput;
    }

    @Override
    public void open() throws Exception {
        scanner = new Scanner(System.in);
    }

    @Override
    public boolean hasNextRecord() {
        return !stop;
    }

    @Override
    public Record readNextRecord() {
        String input = scanner.nextLine();
        stop = input != null && !input.isEmpty() && input.equalsIgnoreCase(terminationInput);
        if (stop) {
            return new PoisonRecord();
        }
        return new StringRecord(++recordNumber, input);
    }

    @Override
    public Integer getTotalRecords() {
        // total record cannot be calculated upfront
        return null;
    }

    @Override
    public String getDataSourceName() {
        return "Standard Input";
    }

    @Override
    public void close() {
        scanner.close();
    }

}
