package org.easybatch.extensions.opencsv;

import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;
import org.easybatch.core.record.StringRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OpenCsvRecordMarshallerTest {

    @Mock
    private Header header;
    @Mock
    private Record<Foo> record;

    private OpenCsvRecordMarshaller<Foo> marshaller;

    @Before
    public void setUp() throws Exception {
        marshaller = new OpenCsvRecordMarshaller<>(Foo.class, "firstName", "lastName", "age", "married");
        Foo foo = new Foo("foo", "bar", 30, true);
        when(record.getHeader()).thenReturn(header);
        when(record.getPayload()).thenReturn(foo);
    }

    @Test
    public void processRecord() throws Exception {
        String expectedPayload = "\"foo\",\"bar\",\"30\",\"true\"";
        StringRecord actual = marshaller.processRecord(record);

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(actual.getPayload()).isEqualTo(expectedPayload);
    }

}