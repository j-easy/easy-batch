package io.github.benas.easybatch.tutorials.recipes;

import io.github.benas.easybatch.core.api.AbstractRecordProcessor;

/**
 * Recipe processor.
 */
public class RecipeProcessor extends AbstractRecordProcessor<Recipe> {

    @Override
    public void processRecord(Recipe recipe) throws Exception {
        System.out.println("recipe = " + recipe);
    }

}
