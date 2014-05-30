package org.easybatch.xml;

import org.easybatch.core.util.StringRecord;

/**
 * A class representing a record in an xml file.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class XmlRecord extends StringRecord {

    /**
     * Constructs an XmlRecord instance.
     * @param recordNumber the record number
     * @param rawContent the record raw xml content
     */
    public XmlRecord(int recordNumber, String rawContent) {
        super(recordNumber, rawContent);
    }

}
