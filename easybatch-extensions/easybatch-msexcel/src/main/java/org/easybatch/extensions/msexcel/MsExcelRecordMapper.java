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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.easybatch.core.mapper.ObjectMapper;
import org.easybatch.core.mapper.RecordMapper;
import org.easybatch.core.mapper.RecordMappingException;
import org.easybatch.core.record.GenericRecord;

/**
 * Mapper that unmarshal MS Excel records to Java objects.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MsExcelRecordMapper implements RecordMapper<MsExcelRecord, GenericRecord> {

    private String[] fields;
    
    private ObjectMapper objectMapper;

    /**
     * Create a new {@link MsExcelRecordMapper}.
     *
     * @param type the target object type
     * @param fields the fields to unmarshal
     */
    public MsExcelRecordMapper(final Class type, final String... fields) {
        this.fields = fields;
        objectMapper = new ObjectMapper(type);
    }

    @SuppressWarnings("unchecked")
    public GenericRecord processRecord(MsExcelRecord msExcelRecord) throws RecordMappingException {
        Object unmarshalledObject = objectMapper.mapObject(toMap(msExcelRecord.getPayload()));
        return new GenericRecord(msExcelRecord.getHeader(), unmarshalledObject);
    }

    private Map<String, String> toMap(final Row row) {
        Map<String, String> map = new HashMap<>();
        Iterator<Cell> cellIterator = row.cellIterator();
        int i = 0;
        while (cellIterator.hasNext()) {
            Cell cell = cellIterator.next();
            map.put(fields[i++], getCellValue(cell));
        }
        return map;
    }
    
    private String getCellValue(Cell cell) {
        switch(cell.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case Cell.CELL_TYPE_NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
        }
        return "";
    }
}
