package org.easybatch.tutorials.recipes;

import java.util.ArrayList;
import java.util.List;

/**
 * Recipe bean.
 */
public class Recipe {

    private String name;

    private List<Ingredient> ingredients = new ArrayList<Ingredient>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Recipe{");
        sb.append("name='").append(name).append('\'');
        sb.append(", ingredients=").append(ingredients);
        sb.append('}');
        return sb.toString();
    }
}
