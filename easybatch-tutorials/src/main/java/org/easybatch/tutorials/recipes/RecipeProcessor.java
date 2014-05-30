package org.easybatch.tutorials.recipes;

import org.easybatch.core.api.AbstractRecordProcessor;

/**
 * Recipe processor.
 */
public class RecipeProcessor extends AbstractRecordProcessor<Recipe> {

    @Override
    public void processRecord(Recipe recipe) throws Exception {
        System.out.println("recipe = " + recipe);
    }

}
