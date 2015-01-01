/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.json;

import org.easybatch.core.api.RecordReader;

import javax.json.Json;
import javax.json.JsonException;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Record reader that reads Json records from an array of Json objects:
 *
 * [
 *  {
 *      // JSON object
 *  },
 *  {
 *      // JSON object
 *  }
 * ]
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class JsonRecordReader implements RecordReader {

    private static final Logger LOGGER = Logger.getLogger(JsonRecordReader.class.getSimpleName());

    /**
     * The data source stream.
     */
    private InputStream inputStream;

    /**
     * The json parser used to read the json stream.
     */
    private JsonParser parser;

    /*
     *  The JsonParser API does not provide an equivalent of XMLEventReader.peek() ..
     *  So it is impossible to check the next element without reading it from the stream
     *  That's why a second parser is used to inspect next element at each iteration.. to be peer-reviewed!
     */
    /**
     * The json parser used to inspect the json stream.
     */
    private JsonParser inspectionParser;

    /*
     * JSR 353 is bogus: When you create 2 JSON parsers on the same input stream, it crashes.
     * See JSR353Test class to reproduce issues
     * That's why separate InputStream references are needed.. to review!
     */
    private InputStream inspectionInputStream;

    private InputStream totalRecordsCountingInputStream;

    /**
     * The Json generator factory.
     */
    private JsonGeneratorFactory jsonGeneratorFactory;

    /**
     * The current record number.
     */
    private int currentRecordNumber;

    private int objectDepth;

    private String key;

    public JsonRecordReader(InputStream inputStream, InputStream inspectionInputStream, InputStream totalRecordsCountingInputStream) {
        this.inputStream = inputStream;
        this.inspectionInputStream = inspectionInputStream;
        this.totalRecordsCountingInputStream = totalRecordsCountingInputStream;
        this.jsonGeneratorFactory = Json.createGeneratorFactory(new HashMap<String, Object>());
    }

    @Override
    public void open() throws Exception {
        inspectionParser = Json.createParser(inspectionInputStream);
        parser = Json.createParser(inputStream);
        parser.next();//move cursor to root array start
    }

    @Override
    public boolean hasNextRecord() {
        int depth = 0;
        while(inspectionParser.hasNext()) {
            JsonParser.Event event = inspectionParser.next();
            switch(event) {
                case START_OBJECT:
                    depth++;
                    break;
                case END_OBJECT:
                    depth--;
                    break;
            }
            if (JsonParser.Event.END_OBJECT.equals(event) && depth == 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public JsonRecord readNextRecord() throws Exception {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = jsonGeneratorFactory.createGenerator(stringWriter);
        do {
            moveToNextElement(jsonGenerator);
        } while(!isEndRootObject());
        jsonGenerator.close();
        return new JsonRecord(++currentRecordNumber, stringWriter.toString());
    }

    @Override
    public Integer getTotalRecords() {
        int totalRecords = 0;
        int depth = 0;
        JsonParser parser = null;
        try {
            parser = Json.createParser(totalRecordsCountingInputStream);
            parser.next();//move cursor to root array start
            while(parser.hasNext()) {
                JsonParser.Event event = parser.next();
                switch(event) {
                    case START_OBJECT:
                        depth++;
                        break;
                    case END_OBJECT:
                        depth--;
                        break;
                }
                if (JsonParser.Event.END_OBJECT.equals(event) && depth == 0) {
                    totalRecords++;
                }
            }
            return totalRecords;
        }   catch (JsonException e) {
            LOGGER.log(Level.SEVERE, "Unable to read data from json stream", e);
            return null;
        }   finally {
            if (parser != null) {
                parser.close();
            }
        }
    }

    @Override
    public String getDataSourceName() {
        return "Json stream: " + inputStream;
    }

    @Override
    public void close() throws Exception {
        parser.close();
        inspectionParser.close();
    }

    private boolean isEndRootObject() {
        return objectDepth == 0;
    }

    private void moveToNextElement(JsonGenerator jsonGenerator) {
        JsonParser.Event event = parser.next();
        switch(event) {
            case START_ARRAY:
                jsonGenerator.writeStartArray();
                break;
            case END_ARRAY:
                jsonGenerator.writeEnd();
                break;
            case START_OBJECT:
                objectDepth++;
                jsonGenerator.writeStartObject();
                break;
            case END_OBJECT:
                objectDepth--;
                jsonGenerator.writeEnd();
                break;
            case VALUE_FALSE:
                jsonGenerator.write(key, JsonValue.FALSE);
                break;
            case VALUE_NULL:
                jsonGenerator.write(key, JsonValue.NULL);
                break;
            case VALUE_TRUE:
                jsonGenerator.write(key, JsonValue.TRUE);
                break;
            case KEY_NAME:
                key = parser.getString();
                break;
            case VALUE_STRING:
                jsonGenerator.write(key, parser.getString());
                break;
            case VALUE_NUMBER:
                jsonGenerator.write(key, parser.getString());
                break;
        }
    }

}
