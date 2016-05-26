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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

class MsExcelRow implements Row {

    private List<Cell> cells;

    private Iterator<Cell> iterator;

    public MsExcelRow() {
        cells = new ArrayList<>();
        iterator = cells.iterator();
    }

    @Override
    public Cell createCell(int column) {
        Cell cell = new MsExcelCell(column);
        cells.add(cell);
        return cell;
    }

    @Override
    public Cell createCell(int column, int type) {
        return createCell(column);
    }

    @Override
    public void removeCell(Cell cell) {
        cells.remove(cell);
    }

    @Override
    public void setRowNum(int rowNum) {

    }

    @Override
    public int getRowNum() {
        return 0;
    }

    @Override
    public Cell getCell(int cellnum) {
        return cells.get(cellnum);
    }

    @Override
    public Cell getCell(int cellnum, MissingCellPolicy policy) {
        return getCell(cellnum);
    }

    @Override
    public short getFirstCellNum() {
        return 0;
    }

    @Override
    public short getLastCellNum() {
        return (short) cells.size();
    }

    @Override
    public int getPhysicalNumberOfCells() {
        return cells.size();
    }

    @Override
    public void setHeight(short height) {

    }

    @Override
    public void setZeroHeight(boolean zHeight) {

    }

    @Override
    public boolean getZeroHeight() {
        return false;
    }

    @Override
    public void setHeightInPoints(float height) {

    }

    @Override
    public short getHeight() {
        return 0;
    }

    @Override
    public float getHeightInPoints() {
        return 0;
    }

    @Override
    public boolean isFormatted() {
        return false;
    }

    @Override
    public CellStyle getRowStyle() {
        return null;
    }

    @Override
    public void setRowStyle(CellStyle style) {

    }

    @Override
    public Iterator<Cell> cellIterator() {
        return iterator;
    }

    @Override
    public Sheet getSheet() {
        return null;
    }

    @Override
    public int getOutlineLevel() {
        return 0;
    }

    @Override
    public Iterator<Cell> iterator() {
        return iterator;
    }
}
