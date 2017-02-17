package org.easybatch.xml;

import org.easybatch.core.reader.AbstractFileRecordReader;
import org.easybatch.core.reader.AbstractMultiFileRecordReader;

import java.io.File;
import java.util.List;

/**
 * Reader for multiple xml files in one shot.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class MultiXmlFileRecordReader extends AbstractMultiFileRecordReader {

    private String rootElementName;

    /**
     * Create a new {@link MultiXmlFileRecordReader}.
     *
     * @param files to read
     */
    public MultiXmlFileRecordReader(List<File> files, String rootElementName) {
        super(files);
        this.rootElementName = rootElementName;
    }

    @Override
    protected AbstractFileRecordReader createReader() throws Exception {
        return new XmlFileRecordReader(currentFile, rootElementName);
    }
}
