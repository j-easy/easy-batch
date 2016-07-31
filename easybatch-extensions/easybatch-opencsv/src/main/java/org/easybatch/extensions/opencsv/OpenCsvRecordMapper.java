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

package org.easybatch.extensions.opencsv;

import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.record.GenericRecord;
import org.easybatch.core.record.StringRecord;

import java.io.StringReader;
import java.util.List;

/**
 * A record mapper that uses <a href="http://opencsv.sourceforge.net">Open CSV</a> to map a delimited record to domain object.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class OpenCsvRecordMapper<T> implements RecordMapper<StringRecord, GenericRecord<T>> {

    private CSVReader openCsvReader;

    private char delimiter = ',';

    private char qualifier;

    private boolean strictQualifiers;

    private ColumnPositionMappingStrategy<T> strategy;

    private CsvToBean csvToBean;

    /**
     * Create a {@link OpenCsvRecordMapper}.
     *
     * @param recordClass The target type
     * @param columns     Fields name in the same order as in the delimited record
     */
    public OpenCsvRecordMapper(Class<? extends T> recordClass, String... columns) {
        this.strategy = new ColumnPositionMappingStrategy<>();
        this.strategy.setType((Class<T>) recordClass);
        this.strategy.setColumnMapping(columns);
        this.csvToBean = new CsvToBean();
    }

    @Override
    public GenericRecord<T> processRecord(final StringRecord record) {
        String payload = record.getPayload();
        openCsvReader = new CSVReader(
                new StringReader(payload),
                delimiter,
                qualifier,
                strictQualifiers);
        List list = csvToBean.parse(strategy, openCsvReader);
        T mappedObject = (T) list.get(0);
        return new GenericRecord<>(record.getHeader(), mappedObject);
    }

    public void setDelimiter(final char delimiter) {
        this.delimiter = delimiter;
    }

    public void setQualifier(final char qualifier) {
        this.qualifier = qualifier;
    }

    public void setStrictQualifiers(boolean strictQualifiers) {
        this.strictQualifiers = strictQualifiers;
    }

}
