package net.benas.cb4j.tutorials.library;

import java.util.Date;

/**
 * JavaBean representing a book.
 * @author benas (md.benhassine@gmail.com)
 */
public class Book {

    private String isbn;

    private String title;

    private String author;

    private Date publicationDate;

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Date getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(Date publicationDate) {
        this.publicationDate = publicationDate;
    }

}
