package org.easybatch.extensions.yaml;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.easybatch.core.util.Utils;

import java.io.InputStream;
import java.util.Date;
import java.util.Scanner;

public class YamlRecordReader implements RecordReader {

    private Scanner scanner;

    private InputStream inputStream;

    private long currentRecordNumber;

    public YamlRecordReader(InputStream inputStream) {
        this.inputStream = inputStream;
    }

    @Override
    public void open() throws Exception {
        this.currentRecordNumber = 0;
        this.scanner = new Scanner(inputStream);
    }

    @Override
    public YamlRecord readRecord() throws Exception {
        if (!scanner.hasNextLine()) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (!line.equals("---")) {
                stringBuilder.append(line);
                stringBuilder.append(Utils.LINE_SEPARATOR);
            } else {
                break;
            }
        }
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        return new YamlRecord(header, stringBuilder.toString());
    }

    @Override
    public void close() throws Exception {
        if (scanner != null) {
            scanner.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }

    private String getDataSourceName() {
        return "YAML stream";
    }
}
