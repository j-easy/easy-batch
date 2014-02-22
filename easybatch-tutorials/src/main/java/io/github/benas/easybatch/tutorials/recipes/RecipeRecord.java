package io.github.benas.easybatch.tutorials.recipes;

import io.github.benas.easybatch.core.api.Record;

/**
 * Recipe record. This logical record represents a set of physical records in recipes.csv file.
 */
public class RecipeRecord implements Record<Recipe> {

    private int number;

    private Recipe recipe;

    public RecipeRecord(int number, Recipe recipe) {
        this.number = number;
        this.recipe = recipe;
    }

    @Override
    public int getNumber() {
        return number;
    }

    @Override
    public Recipe getRawContent() {
        return recipe;
    }

}
