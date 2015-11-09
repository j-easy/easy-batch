/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

import java.util.Iterator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.easybatch.core.marshaller.RecordMarshaller;
import org.easybatch.core.marshaller.RecordMarshallingException;
import org.easybatch.core.record.StringRecord;

public class MsExcelRecordStringifier implements RecordMarshaller<MsExcelRecord, StringRecord> {

    public static final String DEFAULT_DELIMITER = ",";

    public static final String DEFAULT_QUALIFIER = "\"";
    
    private String delimiter;
    
    private String qualifier;

    public MsExcelRecordStringifier() {
        this(DEFAULT_DELIMITER, DEFAULT_QUALIFIER);
    }

    public MsExcelRecordStringifier(String delimiter, String qualifier) {
        this.delimiter = delimiter;
        this.qualifier = qualifier;
    }

    public StringRecord processRecord(MsExcelRecord msExcelRecord) throws RecordMarshallingException {
        Row row = msExcelRecord.getPayload();
        StringBuilder stringBuilder = new StringBuilder();
        Iterator<Cell> cellIterator = row.cellIterator();
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            stringBuilder.append(qualifier);
            switch(cell.getCellType()) {
                case Cell.CELL_TYPE_BOOLEAN:
                    stringBuilder.append(cell.getBooleanCellValue());
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    stringBuilder.append(cell.getNumericCellValue());
                    break;
                case Cell.CELL_TYPE_STRING:
                    stringBuilder.append(cell.getStringCellValue());
                    break;
                case Cell.CELL_TYPE_BLANK:
                    stringBuilder.append("");
                    break;
            }
            stringBuilder.append(qualifier);
            if (cellIterator.hasNext()) {
                stringBuilder.append(delimiter);
            }
        }
        return new StringRecord(msExcelRecord.getHeader(), stringBuilder.toString());
    }
}
