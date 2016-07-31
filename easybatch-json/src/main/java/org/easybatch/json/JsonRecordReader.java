/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.reader.RecordReadingException;
import org.easybatch.core.record.Header;

import javax.json.Json;
import javax.json.JsonValue;
import javax.json.stream.JsonGenerationException;
import javax.json.stream.JsonGenerator;
import javax.json.stream.JsonGeneratorFactory;
import javax.json.stream.JsonParser;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.HashMap;

import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * Record reader that reads Json records from an array of Json objects:
 *
 *<p>
 * [
 *  {
 *      // JSON object
 *  },
 *  {
 *      // JSON object
 *  }
 * ]
 * </p>
 *
 * <p>This reader produces {@link JsonRecord} instances.</p>
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class JsonRecordReader implements RecordReader {

    /**
     * The data source stream.
     */
    private InputStream inputStream;

    /**
     * The json parser used to read the json stream.
     */
    private JsonParser parser;

    /**
     * The Json generator factory.
     */
    private JsonGeneratorFactory jsonGeneratorFactory;

    /**
     * The current record number.
     */
    private long currentRecordNumber;

    private JsonParser.Event currentEvent;

    private JsonParser.Event nextEvent;

    private int arrayDepth;

    private int objectDepth;

    private String key;

    /**
     * Record reader that reads Json records from an array of Json objects:
     *
     *<p>
     * [
     *  {
     *      // JSON object
     *  },
     *  {
     *      // JSON object
     *  }
     * ]
     * </p>
     *
     * <p>This reader produces {@link JsonRecord} instances.</p>
     */
    public JsonRecordReader(final InputStream inputStream) {
        checkNotNull(inputStream, "input stream");
        this.inputStream = inputStream;
        this.jsonGeneratorFactory = Json.createGeneratorFactory(new HashMap<String, Object>());
    }

    @Override
    public void open() {
        parser = Json.createParser(inputStream);
    }

    @Override
    public boolean hasNextRecord() {
        if (parser.hasNext()) {
            currentEvent = parser.next();
            if (JsonParser.Event.START_ARRAY.equals(currentEvent)) {
                arrayDepth++;
            }
            if (JsonParser.Event.END_ARRAY.equals(currentEvent)) {
                arrayDepth--;
            }
            if (JsonParser.Event.KEY_NAME.equals(currentEvent)) {
                key = parser.getString();
            }
        }

        if (parser.hasNext()) {
            nextEvent = parser.next();
            if (JsonParser.Event.START_ARRAY.equals(nextEvent)) {
                arrayDepth++;
            }
            if (JsonParser.Event.END_ARRAY.equals(nextEvent)) {
                arrayDepth--;
            }
            if (JsonParser.Event.KEY_NAME.equals(nextEvent)) {
                key = parser.getString();
            }
        }
        if (JsonParser.Event.START_ARRAY.equals(currentEvent) && JsonParser.Event.END_ARRAY.equals(nextEvent) && arrayDepth == 0) {
            return false;
        }
        if (JsonParser.Event.END_ARRAY.equals(currentEvent) && arrayDepth == 1 && objectDepth == 0) {
            return false;
        }
        return true;
    }

    @Override
    public JsonRecord readNextRecord() throws RecordReadingException {
        StringWriter stringWriter = new StringWriter();
        JsonGenerator jsonGenerator = jsonGeneratorFactory.createGenerator(stringWriter);
        try {
            writeRecordStart(jsonGenerator);
            do {
                moveToNextElement(jsonGenerator);
            } while (!isEndRootObject());
            if (arrayDepth != 2) {
                jsonGenerator.writeEnd();
            }
            jsonGenerator.close();
            Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
            return new JsonRecord(header, stringWriter.toString());
        } catch (javax.json.stream.JsonParsingException e) {
            throw new RecordReadingException(e);
        }
    }

    @Override
    public Long getTotalRecords() {
        return null;
    }

    @Override
    public String getDataSourceName() {
        return "Json stream";
    }

    @Override
    public void close() {
        parser.close();
    }

    private boolean isEndRootObject() {
        return objectDepth == 0;
    }

    private void writeRecordStart(JsonGenerator jsonGenerator) {
        if (currentEvent.equals(JsonParser.Event.START_ARRAY)) {
            if (arrayDepth != 1) {
                jsonGenerator.writeStartArray();
            }
            arrayDepth++;
        }
        if (currentEvent.equals(JsonParser.Event.START_OBJECT)) {
            jsonGenerator.writeStartObject();
            objectDepth++;
        }
        if (nextEvent.equals(JsonParser.Event.START_ARRAY)) {
            jsonGenerator.writeStartArray();
            arrayDepth++;
        }
        if (nextEvent.equals(JsonParser.Event.START_OBJECT)) {
            jsonGenerator.writeStartObject();
            objectDepth++;
        }
    }

    private void moveToNextElement(JsonGenerator jsonGenerator) {
        JsonParser.Event event = parser.next();
        /*
         * The jsonGenerator is stateful and its current context (array/object) is not public
         * => There is no way to query it to know when to use write() or write(key) methods.
         * The idea to track its state with two boolean inArray and inObject has been tried and was not successful
         */
        switch (event) {
            case START_ARRAY:
                try {
                    jsonGenerator.writeStartArray();
                } catch (JsonGenerationException e) {
                    jsonGenerator.writeStartArray(key);
                }
                break;
            case END_ARRAY:
                jsonGenerator.writeEnd();
                break;
            case START_OBJECT:
                objectDepth++;
                try {
                    jsonGenerator.writeStartObject();
                } catch (Exception e) {
                    jsonGenerator.writeStartObject(key);
                }
                break;
            case END_OBJECT:
                objectDepth--;
                jsonGenerator.writeEnd();
                break;
            case VALUE_FALSE:
                try {
                    jsonGenerator.write(JsonValue.FALSE);
                } catch (Exception e) {
                    jsonGenerator.write(key, JsonValue.FALSE);
                }
                break;
            case VALUE_NULL:
                try {
                    jsonGenerator.write(JsonValue.NULL);
                } catch (Exception e) {
                    jsonGenerator.write(key, JsonValue.NULL);
                }
                break;
            case VALUE_TRUE:
                try {
                    jsonGenerator.write(JsonValue.TRUE);
                } catch (Exception e) {
                    jsonGenerator.write(key, JsonValue.TRUE);
                }
                break;
            case KEY_NAME:
                key = parser.getString();
                break;
            case VALUE_STRING:
                try {
                    jsonGenerator.write(parser.getString());
                } catch (Exception e) {
                    jsonGenerator.write(key, parser.getString());
                }
                break;
            case VALUE_NUMBER:
                try {
                    jsonGenerator.write(parser.getBigDecimal());
                } catch (Exception e) {
                    jsonGenerator.write(key, parser.getBigDecimal());
                }
                break;
            default:
                break;
        }
    }

}
