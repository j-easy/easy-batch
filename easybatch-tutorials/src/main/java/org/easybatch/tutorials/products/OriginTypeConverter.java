package org.easybatch.tutorials.products;

import org.easybatch.core.converter.TypeConverter;

/**
 * Custom converter that convert string origin to a value of the {@link Origin} enumeration.
 *
 * @author Mahmoud Ben Hassine (md.benhassine@gmail.com)
 */
public class OriginTypeConverter implements TypeConverter<Origin> {

    @Override
    public Origin convert(String value) {
        return "0".equals(value) ? Origin.NATIONAL : Origin.INTERNATIONAL;
    }

}
