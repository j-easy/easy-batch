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
package org.easybatch.xml;

import org.easybatch.core.reader.AbstractFileRecordReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * A record reader that reads xml records from an xml file.
 *
 * This reader produces {@link XmlRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class XmlFileRecordReader extends AbstractFileRecordReader {

    private XmlRecordReader xmlRecordReader;
    private String rootElementName;

    /**
     * Create a new {@link XmlFileRecordReader}.
     *
     * @param xmlFile         to read
     * @param rootElementName to match records
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link XmlFileRecordReader#XmlFileRecordReader(java.nio.file.Path, java.lang.String)} instead
     */
    @Deprecated
    public XmlFileRecordReader(final File xmlFile, final String rootElementName) {
        this(xmlFile, rootElementName, Charset.defaultCharset().name());
    }

    /**
     * Create a new {@link XmlFileRecordReader}.
     *
     * @param xmlFile         to read
     * @param rootElementName to match records
     * @param charset to use to read the file
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link XmlFileRecordReader#XmlFileRecordReader(java.nio.file.Path, java.lang.String, java.nio.charset.Charset)} instead
     */
    @Deprecated
    public XmlFileRecordReader(final File xmlFile, final String rootElementName, final String charset) {
        super(xmlFile, Charset.forName(charset));
        this.rootElementName = rootElementName;
    }

    /**
     * Create a new {@link XmlFileRecordReader}.
     *
     * @param xmlFile         to read
     * @param rootElementName to match records
     */
    public XmlFileRecordReader(final Path xmlFile, final String rootElementName) {
        this(xmlFile, rootElementName, Charset.defaultCharset());
    }

    /**
     * Create a new {@link XmlFileRecordReader}.
     *
     * @param xmlFile         to read
     * @param rootElementName to match records
     * @param charset to use to read the file
     */
    public XmlFileRecordReader(final Path xmlFile, final String rootElementName, final Charset charset) {
        super(xmlFile, charset);
        this.rootElementName = rootElementName;
    }

    @Override
    public void open() throws Exception {
        xmlRecordReader = new Reader(file, rootElementName, charset.name());
        xmlRecordReader.open();
    }

    @Override
    public XmlRecord readRecord() throws Exception {
        return xmlRecordReader.readRecord();
    }

    @Override
    public void close() throws Exception {
        xmlRecordReader.close();
    }

    // XmlFileRecordReader should return the file name as data source instead of the inherited "Xml stream"
    private static class Reader extends XmlRecordReader {

        private File file;

        Reader(File file, String rootElementName, String encoding) throws FileNotFoundException {
            super(rootElementName, new FileInputStream(file), encoding);
            this.file = file;
        }

        @Override
        protected String getDataSourceName() {
            return file.getAbsolutePath();
        }
    }
}
