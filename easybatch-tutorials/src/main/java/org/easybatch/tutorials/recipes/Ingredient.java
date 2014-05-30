package org.easybatch.tutorials.recipes;

/**
 * Ingredient bean.
 */
public class Ingredient {

    private String name;

    private int quantity;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Ingredient{");
        sb.append("name='").append(name).append('\'');
        sb.append(", quantity=").append(quantity);
        sb.append('}');
        return sb.toString();
    }
}
