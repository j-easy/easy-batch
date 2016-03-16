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

import org.easybatch.core.record.Header;
import org.easybatch.core.record.StringRecord;

import java.util.Date;
import java.util.Scanner;

/**
 * A convenient {@link RecordReader} that reads data from a String.
 * <p/>
 * This reader produces {@link StringRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class StringRecordReader implements RecordReader {

    /**
     * The current read record number.
     */
    private long currentRecordNumber;

    /**
     * Scanner to read input String.
     */
    private Scanner scanner;

    /**
     * A second scanner used to calculate the number of records in the input file.
     * The main scanner may be used instead but since the {@link Scanner} class does not have a method to rewind it to the
     * beginning of the file ( {@link java.util.Scanner#reset()} does not rewind the scanner), another scanner instance is needed.
     */
    private Scanner recordCounterScanner;

    /**
     * The content of the String data source.
     */
    private String dataSource;

    /**
     * Create a {@link StringRecordReader}.
     *
     * @param dataSource The String data source
     */
    public StringRecordReader(final String dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void open() {
        currentRecordNumber = 0;
        scanner = new Scanner(dataSource);
        recordCounterScanner = new Scanner(dataSource);
    }

    @Override
    public boolean hasNextRecord() {
        return scanner.hasNextLine();
    }

    @Override
    public StringRecord readNextRecord() {
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new StringRecord(header, scanner.nextLine());
    }

    @Override
    public Long getTotalRecords() {
        long totalRecords = 0;
        while (recordCounterScanner.hasNextLine()) {
            totalRecords++;
            recordCounterScanner.nextLine();
        }
        recordCounterScanner.close();
        return totalRecords;
    }

    @Override
    public String getDataSourceName() {
        return "In-Memory String";
    }

    @Override
    public void close() {
        scanner.close();
    }
}
