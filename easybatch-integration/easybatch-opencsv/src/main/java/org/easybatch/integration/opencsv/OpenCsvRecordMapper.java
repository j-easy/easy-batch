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

package org.easybatch.integration.opencsv;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordMapper;

import java.io.StringReader;
import java.util.List;

/**
 * A record mapper that uses <a href="http://opencsv.sourceforge.net">Open CSV</a> to map a delimited record to domain object.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class OpenCsvRecordMapper<T> implements RecordMapper<T> {

    private CSVReader openCsvReader;

    private char delimiter = ',';

    private char qualifier;

    private char escape;

    private int line;

    private boolean strictQualifiers;

    private boolean ignoreLeadingWhitespace;

    private ColumnPositionMappingStrategy<T> strategy;

    private CsvToBean csvToBean;

    public OpenCsvRecordMapper(Class<? extends T> recordClass, String[] columns) {
        this.strategy = new ColumnPositionMappingStrategy<T>();
        this.strategy.setType((Class<T>) recordClass);
        this.strategy.setColumnMapping(columns);
        this.csvToBean = new CsvToBean();
    }

    @Override
    public T mapRecord(Record record) throws Exception {
        String recordContent = (String) record.getRawContent();
        openCsvReader = new CSVReader(
                new StringReader(recordContent),
                delimiter,
                qualifier,
                escape,
                line,
                strictQualifiers,
                ignoreLeadingWhitespace);
        List list = csvToBean.parse(strategy, openCsvReader);
        return (T) list.get(0);
    }

    public void setDelimiter(final char delimiter) {
        this.delimiter = delimiter;
    }

    public void setQualifier(final char qualifier) {
        this.qualifier = qualifier;
    }

    public void setEscape(char escape) {
        this.escape = escape;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public void setStrictQualifiers(boolean strictQualifiers) {
        this.strictQualifiers = strictQualifiers;
    }

    public void setIgnoreLeadingWhitespace(boolean ignoreLeadingWhitespace) {
        this.ignoreLeadingWhitespace = ignoreLeadingWhitespace;
    }

}