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
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.easybatch.core.writer.RecordWriter;
import org.easybatch.core.writer.RecordWritingException;

/**
 * Writer that write {@link MsExcelRecord} to a file.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MsExcelRecordWriter implements RecordWriter<MsExcelRecord> {

    private File file;

    private XSSFWorkbook workbook;

    private XSSFSheet sheet;

    /**
     * Create a new {@link MsExcelRecordWriter}.
     *
     * @param file the output file
     * @param sheetName the sheet name
     * @throws IOException when an error occurs during record writing
     */
    public MsExcelRecordWriter(final File file, final String sheetName) throws IOException {
        this.file = file;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(sheetName);
    }

    /**
     * {@inheritDoc}
     */
    public MsExcelRecord processRecord(MsExcelRecord msExcelRecord) throws RecordWritingException {
        XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
        Row payload = msExcelRecord.getPayload();
        int i = 0;
        int lastCellNum = payload.getLastCellNum();
        for (int index = 0; index < lastCellNum; index++) {
            Cell nextCell = payload.getCell(index);
            XSSFCell cell = row.createCell(i++);
            setValue(cell, nextCell);
        }
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)) {
            workbook.write(fileOutputStream);
            return msExcelRecord;
        } catch (IOException e) {
            throw new RecordWritingException("Unable to write record " + msExcelRecord, e);
        }
    }

    private void setValue(XSSFCell cell, Cell next) {
        switch (next.getCellType()) {
            case Cell.CELL_TYPE_BOOLEAN:
                cell.setCellValue(next.getBooleanCellValue());
                cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                break;
            case Cell.CELL_TYPE_NUMERIC:
                cell.setCellValue(next.getNumericCellValue());
                cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                break;
            case Cell.CELL_TYPE_STRING:
                cell.setCellValue(next.getStringCellValue());
                cell.setCellType(Cell.CELL_TYPE_STRING);
                break;
            case Cell.CELL_TYPE_FORMULA:
                cell.setCellValue(next.getCellFormula());
                cell.setCellType(Cell.CELL_TYPE_FORMULA);
                break;
        }
    }
}
