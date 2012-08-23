/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
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

package net.benas.cb4j.tutorials.products;

import net.benas.cb4j.core.api.RecordMapper;
import net.benas.cb4j.core.api.RecordMappingException;
import net.benas.cb4j.core.model.Record;

/**
 * A mapper implementation to map a CSV record to a {@link Product} bean
 * @author benas (md.benhassine@gmail.com)
 */
public class ProductMapper implements RecordMapper<Product> {

    public Product mapRecord(Record record) throws RecordMappingException {

        Product product = new Product();

        product.setProductId(Long.parseLong(record.getContentByIndex(0)));
        product.setCategory_code(Long.parseLong(record.getContentByIndex(1)));
        product.setName(record.getContentByIndex(2));
        product.setPrice(Double.parseDouble(record.getContentByIndex(3)));

        return product;
    }
}
