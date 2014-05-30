package org.easybatch.tutorials.recipes;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordMapper;

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
