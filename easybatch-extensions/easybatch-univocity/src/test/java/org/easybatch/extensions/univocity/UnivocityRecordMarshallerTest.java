package org.easybatch.extensions.univocity;

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

    private UnivocityRecordMarshaller<TestBean> univocityRecordMarshaller;

    @Before
    public void setUp() throws Exception {
        univocityRecordMarshaller = new UnivocityRecordMarshaller<>(TestBean.class);
        TestBean foo = new TestBean();
        foo.setFirstName("foo");
        foo.setLastName("bar");
        foo.setAge(30);
        foo.setMarried(true);
        when(record.getHeader()).thenReturn(header);
        when(record.getPayload()).thenReturn(foo);
    }

    @Test
    public void processRecord() throws Exception {
        String expectedPayload = "\"foo\",\"bar\",\"30\",\"true\"";
        StringRecord actual = univocityRecordMarshaller.processRecord(record);

        assertThat(actual.getHeader()).isEqualTo(header);
        assertThat(actual.getPayload()).isEqualTo(expectedPayload);
    }

}