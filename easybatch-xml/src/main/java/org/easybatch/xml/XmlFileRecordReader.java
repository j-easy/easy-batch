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

package org.easybatch.xml;

import org.easybatch.core.reader.AbstractFileRecordReader;
import org.easybatch.core.record.Record;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * A record reader that reads xml records from an xml file.
 * <p/>
 * This reader produces {@link XmlRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class XmlFileRecordReader extends AbstractFileRecordReader {

    private XmlRecordReader xmlRecordReader;

    public XmlFileRecordReader(final String rootElementName, final File xmlFile) throws FileNotFoundException {
        super(xmlFile);
        xmlRecordReader = new XmlRecordReader(rootElementName, new FileInputStream(xmlFile));
    }

    @Override
    public void open() throws Exception {
        xmlRecordReader.open();
    }

    @Override
    public Record readRecord() throws Exception {
        return xmlRecordReader.readRecord();
    }

    @Override
    public void close() throws Exception {
        xmlRecordReader.close();
    }
}
