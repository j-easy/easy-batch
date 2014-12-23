package org.easybatch.core.util;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordReader;

import java.util.Scanner;

/**
 * A convenient {@link RecordReader} that reads data from the standard input (useful for tests).
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class CliRecordReader implements RecordReader {

    /**
     * The standard input scanner.
     */
    private Scanner scanner;

    /**
     * The current record number.
     */
    private int recordNumber;

    /**
     * The word to type to terminate reading from the standard input.
     */
    private String terminationInput;

    /**
     * The stop reading flag.
     */
    private boolean stop;

    /**
     * Default constructor with default termination input equals to 'quit'.
     */
    public CliRecordReader() {
        this("quit");
    }

    /**
     * Constructs a CliRecordReader instance with a termination word.
     * @param terminationInput the word to type to stop reading from the standard input.
     */
    public CliRecordReader(String terminationInput) {
        this.terminationInput = terminationInput;
    }

    @Override
    public void open() throws Exception {
        scanner = new Scanner(System.in);
    }

    @Override
    public boolean hasNextRecord() {
        return !stop;
    }

    @Override
    public Record readNextRecord() {
        String input = scanner.nextLine();
        stop = input != null && !input.isEmpty() && input.equalsIgnoreCase(terminationInput);
        if (stop) {
            return new PoisonRecord();
        }
        return new StringRecord(++recordNumber, input);
    }

    @Override
    public Integer getTotalRecords() {
        // total record cannot be calculated upfront
        return null;
    }

    @Override
    public String getDataSourceName() {
        return "Standard Input";
    }

    @Override
    public void close() {
        scanner.close();
    }

}
