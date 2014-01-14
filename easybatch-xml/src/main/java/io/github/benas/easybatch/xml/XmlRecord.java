package io.github.benas.easybatch.xml;

import io.github.benas.easybatch.core.util.StringRecord;

/**
 * A class representing a record in an xml file.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class XmlRecord extends StringRecord {

    /**
     * Constructs an XmlRecord instance.
     * @param recordNumber the record number
     * @param rawContent the record raw xml content
     */
    public XmlRecord(long recordNumber, String rawContent) {
        super(recordNumber, rawContent);
    }

}
