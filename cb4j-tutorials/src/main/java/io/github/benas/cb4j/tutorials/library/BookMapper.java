package io.github.benas.cb4j.tutorials.library;

import io.github.benas.cb4j.core.api.RecordMapper;
import io.github.benas.cb4j.core.api.RecordMappingException;
import io.github.benas.cb4j.core.model.Record;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A mapper implementation to map records to Book objects
 * @author benas (md.benhassine@gmail.com)
 */
public class BookMapper implements RecordMapper<Book> {

    private String datePattern = "yyyy-MM-dd";

    private DateFormat df = new SimpleDateFormat(datePattern);

    public Book mapRecord(Record record) throws RecordMappingException {

        Book book = new Book();

        book.setIsbn(record.getFieldContentByIndex(0));
        book.setTitle(record.getFieldContentByIndex(1));
        book.setAuthor(record.getFieldContentByIndex(2));

        //convert string date to java.util.Date object
        String stringPublicationDate = record.getFieldContentByIndex(3);
        Date publicationDate;
        try {
            publicationDate = df.parse(stringPublicationDate);
        } catch (ParseException e) {
            throw new RecordMappingException("Date value : '" + stringPublicationDate + "' cannot be parsed to date format : '" + datePattern + "'");
        }
        book.setPublicationDate(publicationDate);

        return book;
    }
}
