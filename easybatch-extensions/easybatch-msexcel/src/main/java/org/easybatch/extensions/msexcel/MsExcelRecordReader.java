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
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.reader.RecordReaderClosingException;
import org.easybatch.core.reader.RecordReaderOpeningException;
import org.easybatch.core.reader.RecordReadingException;
import org.easybatch.core.record.Header;

/**
 * Reader that read data from a MS Excel sheet.
 * <strong>Only MS Excel XLSX format is supported</strong>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MsExcelRecordReader implements RecordReader {

    private File file;
    
    private XSSFSheet sheet;

    private Iterator<Row> rowIterator;

    private long recordNumber;

    /**
     * Create a new {@link MsExcelRecordReader}.
     *
     * @param file the input file
     * @throws IOException when an error occurs during file opening
     */
    public MsExcelRecordReader(final File file) throws IOException {
        this(file, 0);
    }

    /**
     * Create a new {@link MsExcelRecordReader}.
     *
     * @param file he input file
     * @param sheetIndex the sheet index
     * @throws IOException when an error occurs during file opening
     */
    public MsExcelRecordReader(final File file, final int sheetIndex) throws IOException {
        this.file = file;
        XSSFWorkbook workbook;
        try {
            workbook = new XSSFWorkbook(file);
            sheet = workbook.getSheetAt(sheetIndex);
        } catch (InvalidFormatException e) {
            throw new IOException("Invalid MsExcel file format. Only 'xlsx' is supported", e);
        }
    }

    public void open() throws RecordReaderOpeningException {
        recordNumber = 1;
        rowIterator = sheet.iterator();
    }

    public boolean hasNextRecord() {
        return rowIterator.hasNext();
    }

    public MsExcelRecord readNextRecord() throws RecordReadingException {
        return new MsExcelRecord(new Header(recordNumber++, getDataSourceName(), new Date()), rowIterator.next());
    }

    public Long getTotalRecords() {
        return (long) sheet.getPhysicalNumberOfRows();
    }

    public String getDataSourceName() {
        return String.format("Sheet '%s' in file %s", sheet.getSheetName(), file.getAbsolutePath());
    }

    public void close() throws RecordReaderClosingException {
        // no op
    }
}
