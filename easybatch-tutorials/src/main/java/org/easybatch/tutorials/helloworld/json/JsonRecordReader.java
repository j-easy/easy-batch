/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.tutorials.helloworld.json;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordReader;

import java.io.File;
import java.util.Scanner;

/**
 * A custom record reader to read successive Json records in the following format :
 *
 * {
 *     // JSON data
 * }
 *
 * This implementation is kept simple for demonstration purpose.
 * One should use JSON-P API (JSR 353) to read and parse JSON data.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JsonRecordReader implements RecordReader {

    /**
     * Data source file.
     */
    private File file;

    /**
     * Scanner to read the input file.
     */
    private Scanner scanner;

    /**
     * The current read record number.
     */
    private int currentRecordNumber;

    public JsonRecordReader(File file) {
        this.file = file;
    }

    @Override
    public void open() throws Exception {
        scanner = new Scanner(file);
    }

    @Override
    public boolean hasNextRecord() {
        return scanner.hasNextLine();
    }

    @Override
    public Record readNextRecord() throws Exception {
        StringBuilder stringBuilder = new StringBuilder("");
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            stringBuilder.append(line);
            if (line.equals("}")) {
                break;
            }
        }

        return new JsonRecord(++currentRecordNumber, stringBuilder.toString());
    }

    @Override
    public Integer getTotalRecords() {
        // not implemented in this tutorial.
        return null;
    }

    @Override
    public String getDataSourceName() {
        return file.getAbsolutePath();
    }

    @Override
    public void close() throws Exception {
        scanner.close();
    }
}
