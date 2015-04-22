package org.easybatch.json;

import javax.json.Json;
import javax.json.stream.JsonParser;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Test case to show issues with jsr 353 RI.
 */
public class JSR353Test {

    //@org.junit.Test
    public void test1() {
        String json = "{\"id\":\"1\",\"name\":\"foo\",\"isMarried\":true}";
        InputStream is = new ByteArrayInputStream(json.getBytes());
        JsonParser p1 = Json.createParser(is);
        p1.next();
        JsonParser p2 = Json.createParser(is);

        /*

         javax.json.JsonException: Cannot auto-detect encoding, not enough chars
            at org.glassfish.json.UnicodeDetectingInputStream.detectEncoding(UnicodeDetectingInputStream.java:131)
            at org.glassfish.json.UnicodeDetectingInputStream.<init>(UnicodeDetectingInputStream.java:76)
            at org.glassfish.json.JsonParserImpl.<init>(JsonParserImpl.java:76)
            at org.glassfish.json.JsonProviderImpl.createParser(JsonProviderImpl.java:83)
            at javax.json.Json.createParser(Json.java:104)
            at org.easybatch.json.JsonFactoryTest.testJsonParserCreation(JsonFactoryTest.java:25)

        */

    }

    //@org.junit.Test
    public void test2() {
        String json = "{\"id\":\"1\",\"name\":\"foo\",\"isMarried\":true}";
        InputStream is = new ByteArrayInputStream(json.getBytes());
        JsonParser p1 = Json.createParser(is);
        JsonParser p2 = Json.createParser(is);
        p2.next();
        /*

        javax.json.stream.JsonParsingException: Invalid token=STRING at (line no=1, column no=3, offset=2). Expected tokens are: [CURLYOPEN, SQUAREOPEN]
            at org.glassfish.json.JsonParserImpl.parsingException(JsonParserImpl.java:238)
            at org.glassfish.json.JsonParserImpl.access$1200(JsonParserImpl.java:61)
            at org.glassfish.json.JsonParserImpl$NoneContext.getNextEvent(JsonParserImpl.java:232)
            at org.glassfish.json.JsonParserImpl$StateIterator.next(JsonParserImpl.java:172)
            at org.glassfish.json.JsonParserImpl.next(JsonParserImpl.java:149)
            at org.easybatch.json.JSR353Test.test2(JSR353Test.java:47)

        should create a new input stream for each parser?
            JsonParser p1 = Json.createParser(new ByteArrayInputStream(json.getBytes()));
            JsonParser p2 = Json.createParser(new ByteArrayInputStream(json.getBytes()));
         */
    }

}
