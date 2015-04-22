package org.easybatch.core.api;

import java.util.Date;

/**
 * The record header contains metadata about the record.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class Header {

    /**
     * The physical record number in the data source (if defined).
     */
    private Long number;

    /**
     * The data source name from which this record has been read.
     */
    private String source;

    /**
     * The date at which the record has been read.
     */
    private Date creationDate;

    /**
     * @param number       physical record number in the data source (if defined).
     * @param source       data source name from which this record has been read.
     * @param creationDate date at which the record has been read.
     */
    public Header(Long number, String source, Date creationDate) {
        this.number = number;
        this.source = source;
        this.creationDate = creationDate;
    }

    /**
     * Return the physical record number in the data source (if defined).
     */
    public Long getNumber() {
        return number;
    }

    /**
     * Return the data source name from which this record has been read.
     */
    public String getSource() {
        return source;
    }

    /**
     * Return the date at which the record has been read.
     */
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("[");
        sb.append("number=").append(number);
        sb.append(", source=\"").append(source).append('\"');
        sb.append(", creationDate=\"").append(creationDate);
        sb.append("\"]");
        return sb.toString();
    }
}
