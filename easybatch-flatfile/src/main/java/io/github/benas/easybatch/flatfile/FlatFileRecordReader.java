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

import io.github.benas.easybatch.core.api.RecordReader;
import io.github.benas.easybatch.core.util.StringRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Scanner;

/**
 * A {@link RecordReader} implementation that read data from a flat file.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class FlatFileRecordReader implements RecordReader {

    /**
     * The current read record number.
     */
    private int currentRecordNumber;

    /**
     * The input file path.
     */
    private String input;

    /**
     * The input file encoding name.
     */
    private String charsetName;

    /**
     * Scanner to read input file.
     */
    private Scanner scanner;

    /**
     * A second scanner used to calculate the number of records in the input file.
     * The main scanner may be used instead but since the {@link Scanner} class does not have a method to rewind it to the
     * beginning of the file ( {@link java.util.Scanner#reset()} does not rewind the scanner), another scanner instance is needed.
     */
    private Scanner recordCounterScanner;

    /**
     * Constructs a flat file record reader.
     * @param input the input file path
     * @throws FileNotFoundException thrown if the file does not exist
     */
    public FlatFileRecordReader(final String input) throws FileNotFoundException {
        this(input, Charset.defaultCharset().name());
    }

    /**
     * Constructs a flat file record reader.
     * @param input the input file path
     * @param charsetName the encoding to use to read the file
     * @throws FileNotFoundException thrown if the file does not exist
     */
    public FlatFileRecordReader(final String input, final String charsetName) throws FileNotFoundException {
        this.input = input;
        this.charsetName = charsetName;

    }

    /**
     * {@inheritDoc}
     */
    public StringRecord readNextRecord() {
        currentRecordNumber++;
        return new StringRecord(currentRecordNumber, scanner.nextLine());
    }

    /**
     * {@inheritDoc}
     */
    public Integer getTotalRecords() {
        int totalRecords = 0;
        while (recordCounterScanner.hasNextLine()) {
            totalRecords++;
            recordCounterScanner.nextLine();
        }
        recordCounterScanner.close();
        return totalRecords;
    }

    @Override
    public String getDataSourceName() {
        return input;
    }

    /**
     * {@inheritDoc}
     */
    public void open() throws Exception {
        currentRecordNumber = 0;
        File file = new File(input);
        scanner = new Scanner(file, charsetName);
        recordCounterScanner = new Scanner(file);
    }

    /**
     * {@inheritDoc}
     */
    public boolean hasNextRecord() {
        return scanner.hasNextLine();
    }

    /**
     * {@inheritDoc}
     */
    public void close() {
        scanner.close();
    }
}
