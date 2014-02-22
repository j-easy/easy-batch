package io.github.benas.easybatch.tutorials.recipes;

import io.github.benas.easybatch.core.api.Record;
import io.github.benas.easybatch.core.api.RecordReader;

import java.io.File;
import java.util.Scanner;

/**
 * Recipe reader.
 */
public class RecipeRecordReader implements RecordReader {

    public static final String SEPARATOR = ",";

    /**
     * Data source file.
     */
    private File file;

    /**
     * Scanner to read the input file.
     */
    private Scanner scanner;

    /**
     * The current read record number.
     */
    private int currentRecordNumber;

    public RecipeRecordReader(File file) {
        this.file = file;
    }

    @Override
    public void open() throws Exception {
        scanner = new Scanner(file);
    }

    @Override
    public boolean hasNextRecord() {
        return scanner.hasNextLine();
    }

    @Override
    public Record readNextRecord() throws Exception {
        currentRecordNumber++;
        Recipe recipe = new Recipe();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            if (line.startsWith("END")) {
                break;
            } else if (line.startsWith("R")) { //recipe line
                String[] strings = line.split(SEPARATOR);
                recipe.setName(strings[1]);
            } else if (line.startsWith("I")) { //ingredient line
                String[] strings = line.split(SEPARATOR);
                Ingredient ingredient = new Ingredient();
                ingredient.setName(strings[1]);
                ingredient.setQuantity(Integer.valueOf(strings[2].substring(0, strings[2].length() - 1)));//remove
                recipe.getIngredients().add(ingredient);
            }
        }
        return new RecipeRecord(currentRecordNumber, recipe);
    }

    @Override
    public Integer getTotalRecords() {
        // not implemented in this tutorial.
        return null;
    }

    @Override
    public String getDataSourceName() {
        return file.getAbsolutePath();
    }

    @Override
    public void close() throws Exception {
        scanner.close();
    }

}
