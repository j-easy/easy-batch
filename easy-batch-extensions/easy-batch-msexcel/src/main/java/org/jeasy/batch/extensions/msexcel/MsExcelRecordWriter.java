/*
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
package org.jeasy.batch.extensions.msexcel;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jeasy.batch.core.record.Batch;
import org.jeasy.batch.core.record.Record;
import org.jeasy.batch.core.writer.RecordWriter;

import java.io.FileOutputStream;
import java.nio.file.Path;

/**
 * Writer that write {@link MsExcelRecord} to a file.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MsExcelRecordWriter implements RecordWriter<Row> {

    private Path path;
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    /**
     * Create a new {@link MsExcelRecordWriter}.
     *
     * @param path      to the output file
     * @param sheetName the sheet name
	 */
    public MsExcelRecordWriter(final Path path, final String sheetName) {
        this.path = path;
        workbook = new XSSFWorkbook();
        sheet = workbook.createSheet(sheetName);
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

    @Override
    public void writeRecords(Batch<Row> batch) throws Exception {
        for (Record<Row> record : batch) {
            XSSFRow row = sheet.createRow(sheet.getLastRowNum() + 1);
            int i = 0;
            int lastCellNum = record.getPayload().getLastCellNum();
            for (int index = 0; index < lastCellNum; index++) {
                Cell nextCell = record.getPayload().getCell(index);
                XSSFCell cell = row.createCell(i++);
                setValue(cell, nextCell);
            }
        }
        // FIXME trying to use an output stream as a field does not work (See commit message that added this line)
        FileOutputStream fileOutputStream = new FileOutputStream(path.toFile());
        workbook.write(fileOutputStream);
    }

    @Override
    public void close() throws Exception {
        workbook.close();
    }
}
