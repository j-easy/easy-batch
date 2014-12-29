/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.tutorials.beanValidation;

import org.easybatch.core.api.RecordProcessor;
import org.hibernate.HibernateException;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A product processor that inserts Product objects in a database.<br/>
 *
 * <strong>This implementation is kept simple for demonstration purpose. In production environment, you may define a
 * commit interval to avoid performance issues of committing the transaction after each record insertion.</strong>
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class ProductProcessor implements RecordProcessor<Product, Product> {

    private static final Logger LOGGER = Logger.getLogger(ProductProcessor.class.getName());

    public Product processRecord(Product product) throws Exception {
        DatabaseUtil.getCurrentSession().beginTransaction();
        try {
            DatabaseUtil.getCurrentSession().saveOrUpdate(product);
            DatabaseUtil.getCurrentSession().getTransaction().commit();
            LOGGER.log(Level.INFO, "Product {0} successfully persisted in the database", product);
        } catch (HibernateException e) {
            DatabaseUtil.getCurrentSession().getTransaction().rollback();
            throw new Exception("A database exception occurred during product persisting.", e);
        }
        return product;
    }

}