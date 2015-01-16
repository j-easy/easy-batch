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

package org.easybatch.flatfile.apache.common.csv;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordReader;

import java.util.Iterator;

/**
 * Reader that uses <a href="http://commons.apache.org/proper/commons-csv/">Apache Common CSV</a>
 * to read {@link ApacheCommonCsvRecord} instances from a CSV data source.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ApacheCommonCsvRecordReader implements RecordReader {

    private CSVParser parser;

    private Iterator<CSVRecord> iterator;

    public ApacheCommonCsvRecordReader(CSVParser parser) {
        this.parser = parser;
    }

    @Override
    public void open() throws Exception {
        iterator = parser.iterator();
    }

    @Override
    public boolean hasNextRecord() {
        return iterator.hasNext();
    }

    @Override
    public Record readNextRecord() throws Exception {
        return new ApacheCommonCsvRecord((int) parser.getCurrentLineNumber(), iterator.next());
    }

    @Override
    public Integer getTotalRecords() {
        return null;
    }

    @Override
    public String getDataSourceName() {
        return parser.toString();
    }

    @Override
    public void close() throws Exception {
        parser.close();
    }

}
