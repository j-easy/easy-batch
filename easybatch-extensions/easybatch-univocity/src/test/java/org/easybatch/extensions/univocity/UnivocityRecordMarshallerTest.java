package org.easybatch.extensions.univocity;

import com.univocity.parsers.csv.CsvWriterSettings;
import com.univocity.parsers.fixed.FixedWidthFields;
import com.univocity.parsers.fixed.FixedWidthWriterSettings;
import com.univocity.parsers.tsv.TsvWriterSettings;
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
public class UnivocityRecordMarshallerTest {

    @Mock
    private Header header;
    @Mock
    private Record<TestBean> record;

    private UnivocityCsvRecordMarshaller<TestBean> csvRecordMarshaller;
    private UnivocityTsvRecordMarshaller<TestBean> tsvRecordMarshaller;
    private UnivocityFixedWidthRecordMarshaller<TestBean> fixedWidthRecordMarshaller;

    @Before
    public void setUp() throws Exception {
        setUpCsvMarshaller();
        setUpTsvMarshaller();
        setUpFixedWidthMarshaller();
        TestBean foo = new TestBean();
        foo.setFirstName("foo");
        foo.setLastName("bar");
        foo.setAge(30);
        foo.setMarried(true);
        when(record.getHeader()).thenReturn(header);
        when(record.getPayload()).thenReturn(foo);
    }

    private void setUpCsvMarshaller() throws Exception {
        CsvWriterSettings settings = new CsvWriterSettings();
        settings.setQuoteAllFields(true);
        csvRecordMarshaller = new UnivocityCsvRecordMarshaller<>(TestBean.class, settings,  "firstName", "lastName", "age", "married");
    }

    private void setUpTsvMarshaller() throws Exception {
        TsvWriterSettings settings = new TsvWriterSettings();
        tsvRecordMarshaller = new UnivocityTsvRecordMarshaller<>(TestBean.class, settings,  "firstName", "lastName", "age", "married");
    }

    private void setUpFixedWidthMarshaller() throws Exception {
        FixedWidthWriterSettings settings = new FixedWidthWriterSettings(new FixedWidthFields(4,4,3,4));
        fixedWidthRecordMarshaller = new UnivocityFixedWidthRecordMarshaller<>(TestBean.class, settings,  "firstName", "lastName", "age", "married");
    }


    @Test
    public void processRecordIntoCsv() throws Exception {
        String expectedPayload = "\"foo\",\"bar\",\"30\",\"true\"\n";
        StringRecord actual = csvRecordMarshaller.processRecord(record);

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(actual.getPayload()).isEqualTo(expectedPayload);
    }

    @Test
    public void processRecordIntoTsv() throws Exception {
        String expectedPayload = "foo\tbar\t30\ttrue\n";
        StringRecord actual = tsvRecordMarshaller.processRecord(record);

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(actual.getPayload()).isEqualTo(expectedPayload);
    }

    @Test
    public void processRecordIntoFixedWidth() throws Exception {
        String expectedPayload = "foo bar 30 true\n";
        StringRecord actual = fixedWidthRecordMarshaller.processRecord(record);

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(actual.getPayload()).isEqualTo(expectedPayload);
    }

}