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
package org.jeasy.batch.extensions.yaml;

import org.jeasy.batch.core.reader.AbstractFileRecordReader;
import org.jeasy.batch.core.reader.RecordReader;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * {@link RecordReader} that reads yaml records delimited by "---" from a given file.
 *
 * This reader produces {@link YamlRecord} instances.
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class YamlFileRecordReader extends AbstractFileRecordReader {

    private YamlRecordReader yamlRecordReader;

    /**
     * Create a new {@link YamlFileRecordReader}.
     *
     * @param yamlFile to read records from
     */
    public YamlFileRecordReader(final Path yamlFile) {
        this(yamlFile, Charset.defaultCharset());
    }

    /**
     * Create a new {@link YamlFileRecordReader}.
     *
     * @param yamlFile to read records from
     * @param charset of the input file
     */
    public YamlFileRecordReader(final Path yamlFile, final Charset charset) {
        super(yamlFile, charset);
    }

    @Override
    public void open() throws Exception {
        yamlRecordReader = new Reader(path, charset);
        yamlRecordReader.open();
    }

    @Override
    public YamlRecord readRecord() throws Exception {
        return yamlRecordReader.readRecord();
    }

    @Override
    public void close() throws Exception {
        yamlRecordReader.close();
    }

    // YamlFileRecordReader should return the file name as data source instead of the inherited "Yaml stream"
    private static class Reader extends YamlRecordReader {

        private Path path;

        Reader(Path path, Charset charset) throws FileNotFoundException {
            super(new FileInputStream(path.toFile()), charset);
            this.path = path;
        }

        @Override
        protected String getDataSourceName() {
            return path.toAbsolutePath().toString();
        }
    }
}
