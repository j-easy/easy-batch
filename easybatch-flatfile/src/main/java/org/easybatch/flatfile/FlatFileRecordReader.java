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

package org.easybatch.flatfile;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.StringRecord;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Scanner;

/**
 * A {@link RecordReader} implementation that read data from a flat file.
 * <p/>
 * This reader produces {@link StringRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class FlatFileRecordReader implements RecordReader {

    /**
     * The current read record number.
     */
    private long currentRecordNumber;

    /**
     * The input file.
     */
    private File input;

    /**
     * The input file encoding name.
     */
    private String charsetName;

    /**
     * Scanner to read input file.
     */
    private Scanner scanner;

    /**
     * Constructs a flat file record reader.
     *
     * @param fileName the input file
     * @throws FileNotFoundException thrown if the file does not exist
     */
    public FlatFileRecordReader(final String fileName) throws FileNotFoundException {
        this(new File(fileName), Charset.defaultCharset().name());
    }

    /**
     * Constructs a flat file record reader.
     *
     * @param input the input file
     * @throws FileNotFoundException thrown if the file does not exist
     */
    public FlatFileRecordReader(final File input) throws FileNotFoundException {
        this(input, Charset.defaultCharset().name());
    }

    /**
     * Constructs a flat file record reader.
     *
     * @param input       the input file
     * @param charsetName the encoding to use to read the file
     * @throws FileNotFoundException thrown if the file does not exist
     */
    public FlatFileRecordReader(final File input, final String charsetName) throws FileNotFoundException {
        this.input = input;
        this.charsetName = charsetName;

    }

    @Override
    public StringRecord readRecord() {
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        if (scanner.hasNextLine()) {
            return new StringRecord(header, scanner.nextLine());
        } else {
            return null;
        }
    }

    private String getDataSourceName() {
        return input.getAbsolutePath();
    }

    @Override
    public void open() throws Exception {
        currentRecordNumber = 0;
        scanner = new Scanner(new FileInputStream(input), charsetName);
    }

    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }

}
