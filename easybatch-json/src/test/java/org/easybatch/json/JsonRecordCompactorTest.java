/*
 *  The MIT License
 *
 *   Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package org.easybatch.json;

import org.easybatch.core.processor.RecordProcessingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;

@RunWith(MockitoJUnitRunner.class)
public class JsonRecordCompactorTest {

    private String payload, expectedPayload;

    private JsonRecordCompactor jsonRecordCompactor;

    @Before
    public void setUp() {
        jsonRecordCompactor = new JsonRecordCompactor();
    }

    @Test
    public void testCompactSimpleJsonRecord() throws RecordProcessingException {
        expectedPayload = "{\"name\":\"foo\",\"age\":30,\"married\":false}";
        payload = "{" + LINE_SEPARATOR + "  \"name\": \"foo\"," + LINE_SEPARATOR + "  \"age\": 30," + LINE_SEPARATOR + "  \"married\": false" + LINE_SEPARATOR + "}";

        String compactRecord = jsonRecordCompactor.compact(payload);
        assertThat(compactRecord).isNotNull().isEqualTo(expectedPayload);
    }

    @Test
    public void testCompactComplexJsonRecord() throws RecordProcessingException {
        expectedPayload = "{\"datasetid\":\"arbresalignementparis2010\",\"recordid\":\"0e6cfe03082224225c54690e5a700987ffb1310f\",\"fields\":{\"adresse\":\"AVENUE GAMBETTA\",\"hauteurenm\":0.0,\"espece\":\"Tilia tomentosa\",\"circonfere\":78.0,\"geom_x_y\":[48.8691944661,2.40210336054],\"dateplanta\":\"1971-02-27\"},\"geometry\":{\"type\":\"Point\",\"coordinates\":[2.40210336054,48.8691944661]},\"record_timestamp\":\"2014-09-11T14:39:16.131843\"}";
        payload = "{" + LINE_SEPARATOR + "  \"datasetid\": \"arbresalignementparis2010\"," + LINE_SEPARATOR + "  \"recordid\": \"0e6cfe03082224225c54690e5a700987ffb1310f\"," + LINE_SEPARATOR + "  \"fields\": {" + LINE_SEPARATOR + "    \"adresse\": \"AVENUE GAMBETTA\"," + LINE_SEPARATOR + "    \"hauteurenm\": 0.0," + LINE_SEPARATOR + "    \"espece\": \"Tilia tomentosa\"," + LINE_SEPARATOR + "    \"circonfere\": 78.0," + LINE_SEPARATOR + "    \"geom_x_y\": [" + LINE_SEPARATOR + "      48.8691944661," + LINE_SEPARATOR + "      2.40210336054" + LINE_SEPARATOR + "    ]," + LINE_SEPARATOR + "    \"dateplanta\": \"1971-02-27\"" + LINE_SEPARATOR + "  }," + LINE_SEPARATOR + "  \"geometry\": {" + LINE_SEPARATOR + "    \"type\": \"Point\"," + LINE_SEPARATOR + "    \"coordinates\": [" + LINE_SEPARATOR + "      2.40210336054," + LINE_SEPARATOR + "      48.8691944661" + LINE_SEPARATOR + "    ]" + LINE_SEPARATOR + "  }," + LINE_SEPARATOR + "  \"record_timestamp\": \"2014-09-11T14:39:16.131843\"" + LINE_SEPARATOR + "}";

        String compactRecord = jsonRecordCompactor.compact(payload);
        assertThat(compactRecord).isNotNull().isEqualTo(expectedPayload);
    }
}
