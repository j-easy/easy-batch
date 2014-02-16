package io.github.benas.easybatch.tutorials.recipes;

import io.github.benas.easybatch.core.api.Record;
import io.github.benas.easybatch.core.api.RecordMapper;

/**
 * Recipe Mapper.
 */
public class RecipeMapper implements RecordMapper<Recipe> {

    @Override
    public Recipe mapRecord(Record record) throws Exception {
        RecipeRecord recipeRecord = (RecipeRecord) record;
        return recipeRecord.getRawContent();
    }

}
