package io.github.benas.easybatch.core.util;

import io.github.benas.easybatch.core.api.Record;
import io.github.benas.easybatch.core.api.RecordReader;

import java.util.Scanner;

/**
 * A convenient {@link RecordReader} that reads data from a String.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class StringRecordReader implements RecordReader {

    /**
     * The current read record number.
     */
    private long currentRecordNumber;

    /**
     * Scanner to read input String.
     */
    private Scanner scanner;

    /**
     * A second scanner used to calculate the number of records in the input file.
     * The main scanner may be used instead but since the {@link Scanner} class does not have a method to rewind it to the
     * beginning of the file ( {@link java.util.Scanner#reset()} does not rewind the scanner), another scanner instance is needed.
     */
    private Scanner recordCounterScanner;

    /**
     * The content of the String data source.
     */
    private String content;

    /**
     * Constructs a StringRecordReader.
     * @param content The String data source
     */
    public StringRecordReader(final String content) {
        this.content = content;
    }

    @Override
    public void open() throws Exception {
        currentRecordNumber = 0;
        scanner = new Scanner(content);
        recordCounterScanner = new Scanner(content);
    }

    @Override
    public boolean hasNextRecord() {
        return scanner.hasNextLine();
    }

    @Override
    public Record readNextRecord() {
        currentRecordNumber++;
        return new StringRecord(currentRecordNumber, scanner.nextLine());
    }

    @Override
    public long getTotalRecords() {
        long totalRecords = 0;
        while (recordCounterScanner.hasNextLine()) {
            totalRecords++;
            recordCounterScanner.nextLine();
        }
        recordCounterScanner.close();
        return totalRecords;
    }

    @Override
    public String getDataSourceName() {
        return "In-Memory String";
    }

    @Override
    public void close() {
        scanner.close();
    }
}
