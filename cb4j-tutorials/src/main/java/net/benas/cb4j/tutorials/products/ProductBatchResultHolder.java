package net.benas.cb4j.tutorials.products;

import net.benas.cb4j.core.api.BatchResultHolder;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Product batch execution result holder.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class ProductBatchResultHolder implements BatchResultHolder<Map<Long, List<Double>>> {

    /**
     * A map defined as follow :
     *  - key = category_code
     *  - value = list of prices of products belonging to that category
     *  Min and Max prices will be computed on prices list for each category
     */
    private Map<Long, List<Double>> pricesByCategory = new HashMap<Long, List<Double>>();

    /**
     * {@inheritDoc}
     */
    public Map<Long, List<Double>> getResult() {
        return pricesByCategory;
    }

    /**
     * Return lowest product price for each category
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