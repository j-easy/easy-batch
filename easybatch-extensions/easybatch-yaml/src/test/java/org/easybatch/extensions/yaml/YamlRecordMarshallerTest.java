package org.easybatch.extensions.yaml;

import org.easybatch.core.record.Record;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.easybatch.core.util.Utils.LINE_SEPARATOR;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class YamlRecordMarshallerTest {

    @Mock
    private Record<Contact> record;

    private YamlRecordMarshaller<Contact> recordMarshaller;

    @Before
    public void setUp() throws Exception {
        recordMarshaller = new YamlRecordMarshaller<>();
        when(record.getPayload()).thenReturn(new Contact("foo", 10));
    }

    @Test
    public void processRecord() throws Exception {
        YamlRecord yamlRecord = recordMarshaller.processRecord(record);

        assertThat(yamlRecord.getPayload()).isEqualTo("name: foo" + LINE_SEPARATOR + "age: 10");
    }
}