package org.easybatch.tutorials.recipes;

import org.easybatch.core.api.RecordProcessor;

/**
 * Recipe processor.
 */
public class RecipeProcessor implements RecordProcessor<Recipe, Recipe> {

    @Override
    public Recipe processRecord(Recipe recipe) throws Exception {
        System.out.println("recipe = " + recipe);
        return recipe;
    }

}
