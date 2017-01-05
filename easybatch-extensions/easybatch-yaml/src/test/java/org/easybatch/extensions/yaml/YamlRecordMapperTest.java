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
public class YamlRecordMapperTest {

    @Mock
    private YamlRecord record;

    private YamlRecordMapper<Contact> recordMapper;

    @Before
    public void setUp() throws Exception {
        recordMapper = new YamlRecordMapper<>();
        when(record.getPayload()).thenReturn("name: Foo" + LINE_SEPARATOR + "age: 28");
    }

    @Test
    public void processRecord() throws Exception {
        Record<Contact> actual = recordMapper.processRecord(record);

        assertThat(actual).isNotNull();
        Contact contact = actual.getPayload();

        assertThat(contact).isNotNull();
        assertThat(contact.getName()).isEqualTo("Foo");
        assertThat(contact.getAge()).isEqualTo(28);
    }

}