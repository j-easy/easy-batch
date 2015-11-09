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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.easybatch.core.writer.RecordWriter;
import org.easybatch.core.writer.RecordWritingException;

public class MsExcelRecordWriter implements RecordWriter<MsExcelRecord> {

    private File file;

    private HSSFWorkbook workbook;

    private HSSFSheet sheet;

    public MsExcelRecordWriter(File file) throws IOException {
        this(file, 0);
    }

    public MsExcelRecordWriter(File file, int sheetIndex) throws IOException {
        this.file = file;
        workbook = new HSSFWorkbook(new FileInputStream(file));
        sheet = workbook.getSheetAt(sheetIndex);
    }

    public MsExcelRecord processRecord(MsExcelRecord msExcelRecord) throws RecordWritingException {
        HSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        // fill in row
        Iterator<Cell> cellIterator = msExcelRecord.getPayload().cellIterator();
        int i = 0;
        while (cellIterator.hasNext()) {
            Cell next = cellIterator.next();
            HSSFCell cell = row.createCell(i++);
            setValue(cell, next);
        }
        // write workbook (not efficient, Apache PIO does not allow writing rows in iterative mode)
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            workbook.write(fileOutputStream);
            return msExcelRecord;
        } catch (IOException e) {
            throw new RecordWritingException("Unable to write record " + msExcelRecord, e);
        }
    }

    private void setValue(HSSFCell cell, Cell next) {
        switch(next.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                cell.setCellValue(next.getBooleanCellValue());
                break;
            case Cell.CELL_TYPE_NUMERIC:
                cell.setCellValue(next.getNumericCellValue());
                break;
            case Cell.CELL_TYPE_STRING:
                cell.setCellValue(next.getStringCellValue());
                break;
            case Cell.CELL_TYPE_FORMULA:
                cell.setCellValue(next.getCellFormula());
                break;
        }
    }
}
