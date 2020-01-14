/**
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
package org.easybatch.extensions.yaml;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Header;
import org.easybatch.core.util.Utils;

import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Scanner;

/**
 * Reader for Yaml records delimited by "---".
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class YamlRecordReader implements RecordReader {

    private Scanner scanner;
    private String charset;
    private InputStream inputStream;
    private long currentRecordNumber;

    /**
     * Create a new {@link YamlRecordReader}.
     *
     * @param inputStream to read records from
     */
    public YamlRecordReader(InputStream inputStream) {
        this(inputStream, Charset.defaultCharset().name());
    }

    /**
     * Create a new {@link YamlRecordReader}.
     *
     * @param inputStream to read records from
     * @param charset of the input stream
     */
    public YamlRecordReader(InputStream inputStream, String charset) {
        this.inputStream = inputStream;
        this.charset = charset;
    }

    @Override
    public void open() throws Exception {
        this.currentRecordNumber = 0;
        this.scanner = new Scanner(inputStream, charset);
    }

    @Override
    public YamlRecord readRecord() throws Exception {
        if (!scanner.hasNextLine()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (!line.equals("---")) {
                stringBuilder.append(line);
                stringBuilder.append(Utils.LINE_SEPARATOR);
            } else {
                break;
            }
        }
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new YamlRecord(header, stringBuilder.toString());
    }

    @Override
    public void close() throws Exception {
        if (scanner != null) {
            scanner.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    protected String getDataSourceName() {
        return "YAML stream";
    }
}
