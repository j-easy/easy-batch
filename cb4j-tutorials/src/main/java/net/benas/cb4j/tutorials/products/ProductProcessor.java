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

import net.benas.cb4j.core.api.RecordProcessor;

import java.util.*;

/**
 * A processor that will gather data and split products by category according to business requirement
 *  * @author benas (md.benhassine@gmail.com)
 */
public class ProductProcessor implements RecordProcessor<Product> {

    /**
     * Conversion rate of product price to local currency
     */
    private final double CONVERSION_RATE = 0.6;

    /**
     * A map defined as follow :
     *  - key = category_code
     *  - value = list of prices of products belonging to that category
     *  Min, Max and Avg prices will be computed on prices list for each category
     */
    private Map<Long, List<Double>> pricesByCategory = new HashMap<Long, List<Double>>();

    public void preProcessRecord(Product product) {
        /*
         * Before processing any product, the price should be converted to local currency
         */
        double price = product.getPrice();
        product.setPrice(price * CONVERSION_RATE);
    }

    public void processRecord(Product product) {
        /*
         * gather data according to product category
         */
        long category_code = product.getCategory_code();

        if (pricesByCategory.containsKey(category_code)) {
            pricesByCategory.get(category_code).add(product.getPrice());
        }else {
            List<Double> prices = new ArrayList<Double>();
            prices.add(product.getPrice());
            pricesByCategory.put(category_code, prices);
        }

    }

    public void postProcessRecord(Product typedRecord) {
        //no-op
    }

    /**
     * Return lower product price for each category
     * @return A map defined by :<br/>
     *  - key : category code<br/>
     *  - value : the lower product price for this category
     */
    public Map<Long, Double> getMinPricesByCategory() {
        Map<Long, Double> minPrices = new HashMap<Long, Double>();
        for (Map.Entry<Long, List<Double>> entry : pricesByCategory.entrySet()) {
            List<Double> prices = entry.getValue();
            Double minPrice = Collections.min(prices);
            minPrices.put(entry.getKey(), minPrice);
        }
        return minPrices;
    }

    /**
     * Return highest product price for each category
     * @return A map defined by :<br/>
     *  - key : category code<br/>
     *  - value : the highest product price for this category
     */
    public Map<Long, Double> getMaxPricesByCategory() {
        Map<Long, Double> maxPrices = new HashMap<Long, Double>();
        for (Map.Entry<Long, List<Double>> entry : pricesByCategory.entrySet()) {
            List<Double> prices = entry.getValue();
            Double maxPrice = Collections.max(prices);
            maxPrices.put(entry.getKey(), maxPrice);
        }
        return maxPrices;
    }

}
