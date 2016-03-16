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

package org.easybatch.extensions.apache.common.csv;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.reader.RecordReaderClosingException;
import org.easybatch.core.record.Header;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Reader that uses <a href="http://commons.apache.org/proper/commons-csv/">Apache Common CSV</a>
 * to read {@link ApacheCommonCsvRecord} instances from a CSV data source.
 * <p/>
 * This reader produces {@link ApacheCommonCsvRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class ApacheCommonCsvRecordReader implements RecordReader {

    private CSVParser parser;

    private Iterator<CSVRecord> iterator;

    /**
     * Reader that uses <a href="http://commons.apache.org/proper/commons-csv/">Apache Common CSV</a>
     * to read {@link ApacheCommonCsvRecord} instances from a CSV data source.
     * <p/>
     * This reader produces {@link ApacheCommonCsvRecord} instances.
     */
    public ApacheCommonCsvRecordReader(final CSVParser parser) {
        checkNotNull(parser, "csv parser");
        this.parser = parser;
    }

    @Override
    public void open() {
        iterator = parser.iterator();
    }

    @Override
    public boolean hasNextRecord() {
        return iterator.hasNext();
    }

    @Override
    public ApacheCommonCsvRecord readNextRecord() {
        Header header = new Header(parser.getRecordNumber(), getDataSourceName(), new Date());
        return new ApacheCommonCsvRecord(header, iterator.next());
    }

    @Override
    public Long getTotalRecords() {
        return null;
    }

    @Override
    public String getDataSourceName() {
        return parser.toString();
    }

    @Override
    public void close() throws RecordReaderClosingException {
        try {
            parser.close();
        } catch (IOException e) {
            throw new RecordReaderClosingException("Unable to close record reader", e);
        }
    }

}
