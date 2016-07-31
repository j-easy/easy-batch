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

import java.util.Calendar;
import java.util.Date;
import org.apache.poi.ss.formula.FormulaParseException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Comment;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

class MsExcelCell implements Cell {
    
    private int columnIndex;
    
    private Object value;

    private String formula;

    private byte errorValue;
    
    private int type;

    private CellStyle style;

    private Comment comment;

    private Hyperlink hyperlink;

    public MsExcelCell(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    @Override
    public int getColumnIndex() {
        return columnIndex;
    }

    @Override
    public int getRowIndex() {
        return 0;
    }

    @Override
    public Sheet getSheet() {
        return null;
    }

    @Override
    public Row getRow() {
        return null;
    }

    @Override
    public void setCellType(int cellType) {
        this.type = cellType;
    }

    @Override
    public int getCellType() {
        return type;
    }

    @Override
    public int getCachedFormulaResultType() {
        return 0;
    }

    @Override
    public void setCellValue(double value) {
        this.value = value;
        setCellType(CELL_TYPE_NUMERIC);
    }

    @Override
    public void setCellValue(Date value) {
        this.value = value;
        setCellType(CELL_TYPE_STRING); // missing CELL_TYPE_DATE in Cell ??
    }

    @Override
    public void setCellValue(Calendar value) {
        this.value = value;
        setCellType(CELL_TYPE_STRING); // missing CELL_TYPE_CALENDAR in Cell ??
    }

    @Override
    public void setCellValue(RichTextString value) {
        this.value = value;
        setCellType(CELL_TYPE_STRING);// missing CELL_TYPE_RICH_TEXT_STRING in Cell ??
    }

    @Override
    public void setCellValue(String value) {
        this.value = value;
        setCellType(CELL_TYPE_STRING);
    }

    @Override
    public void setCellFormula(String formula) throws FormulaParseException {
        this.formula = formula;
        setCellType(CELL_TYPE_FORMULA);
    }

    @Override
    public String getCellFormula() {
        return formula;
    }

    @Override
    public double getNumericCellValue() {
        return (double) value;
    }

    @Override
    public Date getDateCellValue() {
        return (Date) value;
    }

    @Override
    public RichTextString getRichStringCellValue() {
        return (RichTextString) value;
    }

    @Override
    public String getStringCellValue() {
        return (String) value;
    }

    @Override
    public void setCellValue(boolean value) {
        this.value = value;
        setCellType(CELL_TYPE_BOOLEAN);
    }

    @Override
    public void setCellErrorValue(byte value) {
        errorValue = value;
    }

    @Override
    public boolean getBooleanCellValue() {
        return (boolean) value;
    }

    @Override
    public byte getErrorCellValue() {
        return errorValue;
    }

    @Override
    public void setCellStyle(CellStyle style) {
        this.style = style;
    }

    @Override
    public CellStyle getCellStyle() {
        return style;
    }

    @Override
    public void setAsActiveCell() {

    }

    @Override
    public CellAddress getAddress() {
        return null;
    }

    @Override
    public void setCellComment(Comment comment) {
        this.comment = comment;
    }

    @Override
    public Comment getCellComment() {
        return comment;
    }

    @Override
    public void removeCellComment() {
        comment = null;
    }

    @Override
    public Hyperlink getHyperlink() {
        return hyperlink;
    }

    @Override
    public void setHyperlink(Hyperlink hyperlink) {
        this.hyperlink = hyperlink;
    }

    @Override
    public void removeHyperlink() {
        this.hyperlink = null;
    }

    @Override
    public CellRangeAddress getArrayFormulaRange() {
        return null;
    }

    @Override
    public boolean isPartOfArrayFormulaGroup() {
        return false;
    }
}
