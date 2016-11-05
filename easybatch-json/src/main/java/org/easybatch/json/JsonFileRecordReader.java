/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.easybatch.json;

import org.easybatch.core.reader.AbstractFileRecordReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Record reader that reads Json records from an Json file with the following format:
 * <p>
 * <p>
 * [
 * {
 * // JSON object
 * },
 * {
 * // JSON object
 * }
 * ]
 * </p>
 * <p>
 * <p>This reader produces {@link JsonRecord} instances.</p>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JsonFileRecordReader extends AbstractFileRecordReader {

    private JsonRecordReader jsonRecordReader;

    /**
     * Create a new {@link JsonFileRecordReader}.
     *
     * <p>This reader produces {@link JsonRecord} instances.</p>
     */
    public JsonFileRecordReader(final File jsonFile) throws FileNotFoundException {
        super(jsonFile);
        jsonRecordReader = new JsonRecordReader(new FileInputStream(jsonFile));
    }

    @Override
    public void open() throws Exception {
        jsonRecordReader.open();
    }

    @Override
    public JsonRecord readRecord() throws Exception {
        return jsonRecordReader.readRecord();
    }

    @Override
    public void close() throws Exception {
        jsonRecordReader.close();
    }
}
