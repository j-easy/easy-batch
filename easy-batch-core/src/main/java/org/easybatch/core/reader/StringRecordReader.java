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
import org.easybatch.core.record.StringRecord;

import java.util.Date;
import java.util.Scanner;

/**
 * A {@link RecordReader} that reads data from a String.
 *
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
     * The content of the String data source.
     */
    private String dataSource;

    /**
     * Create a new {@link StringRecordReader}.
     *
     * @param dataSource The String data source
     */
    public StringRecordReader(final String dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void open() {
        currentRecordNumber = 1;
        scanner = new Scanner(dataSource);
    }

    @Override
    public StringRecord readRecord() {
        Header header = new Header(currentRecordNumber++, getDataSourceName(), new Date());
        String payload = scanner.hasNextLine() ? scanner.nextLine() : null;
        return payload == null ? null : new StringRecord(header, payload);
    }

    private String getDataSourceName() {
        return "In-Memory String";
    }

    @Override
    public void close() {
        scanner.close();
    }
}
