package net.benas.cb4j.tutorials.orders;

/**
 * Created with IntelliJ IDEA.
 * User: Mahmoud
 * Date: 14/08/12
 * Time: 07:24
 * To change this template use File | Settings | File Templates.
 */
public class Receipt {

    private long storeCode;

    private String productCode;

    private float unitPrice;

    private int quantity;

    public long getStoreCode() {
        return storeCode;
    }

    public void setStoreCode(long storeCode) {
        this.storeCode = storeCode;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public float getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(float unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
