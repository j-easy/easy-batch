package org.easybatch.extensions.yaml;

import org.easybatch.core.record.Header;
import org.easybatch.core.record.StringRecord;


public class YamlRecord extends StringRecord {

    /**
     * Create a {@link YamlRecord}.
     *
     * @param header  the record header
     * @param payload the record payload
     */
    public YamlRecord(Header header, String payload) {
        super(header, payload);
    }
}
