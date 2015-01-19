package org.easybatch.core.mapper.converter;

import org.junit.Before;
import org.junit.Test;

import java.util.GregorianCalendar;

import static org.fest.assertions.Assertions.assertThat;

/**
 * Test class for {@link GregorianCalendarTypeConverter}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class GregorianCalendarTypeConverterTest extends BaseConverterTest<GregorianCalendar> {

    @Before
    public void setUp() throws Exception {
        converter = new GregorianCalendarTypeConverter();
    }

    @Test
    public void whenInputValueIsLegalValue_ThenShouldReturnValidDate() {
        String date = "2015-01-01";
        GregorianCalendar convertedCalendar = converter.convert(date);
        assertThat(convertedCalendar).isNotNull();
        assertThat(convertedCalendar.getTime()).isNotNull();
        assertThat(convertedCalendar.getTime()).isEqualTo(java.sql.Date.valueOf(date));
    }

}
