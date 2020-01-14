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
package org.easybatch.extensions.msexcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.easybatch.core.field.BeanFieldExtractor;
import org.easybatch.core.field.FieldExtractor;
import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.record.Record;

import java.beans.IntrospectionException;
import java.util.Calendar;
import java.util.Date;

/**
 * Marshaller of Java objects to {@link MsExcelRecord}s.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MsExcelRecordMarshaller<P> implements RecordMarshaller<Record<P>, MsExcelRecord> {

    private FieldExtractor<P> fieldExtractor;

    /**
     * Create a new {@link MsExcelRecordMarshaller}.
     *
     * @param type the POJO type
     * @param fields the fields to marshal
     * @throws IntrospectionException when an error occurs during bean introspection
     */
    public MsExcelRecordMarshaller(Class<P> type, String... fields) throws IntrospectionException {
        this.fieldExtractor = new BeanFieldExtractor<>(type, fields);
    }

    /**
     * {@inheritDoc}
     */
    public MsExcelRecord processRecord(Record<P> record) throws Exception {
        Row row = new MsExcelRow();
        Iterable<Object> values = fieldExtractor.extractFields(record.getPayload());
        int i = 0;
        for (Object value : values) {
            Cell cell = row.createCell(i++);
            setValue(cell, value);
        }
        return new MsExcelRecord(record.getHeader(), row);
    }

    private void setValue(Cell cell, Object value) {
        if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
            cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
        }
        if (value instanceof Byte || value instanceof Short || value instanceof Integer || value instanceof Long || value instanceof Float || value instanceof Double) {
            cell.setCellValue(Double.parseDouble(value.toString()));
            cell.setCellType(Cell.CELL_TYPE_NUMERIC);
        }
        if (value instanceof String) {
            cell.setCellValue((String) value);
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        if (value instanceof Date) {
            cell.setCellValue((Date) value);
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
        if (value instanceof Calendar) {
            cell.setCellValue((Calendar) value);
            cell.setCellType(Cell.CELL_TYPE_STRING);
        }
    }
}
