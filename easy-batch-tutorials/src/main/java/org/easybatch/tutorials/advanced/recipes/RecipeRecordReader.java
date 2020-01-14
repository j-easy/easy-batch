/*
 * The MIT License
 *
 *  Copyright (c) 2016, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */

package org.easybatch.tutorials.advanced.recipes;

import org.easybatch.core.reader.RecordReader;
import org.easybatch.core.record.Header;
import org.easybatch.core.record.Record;

import java.io.File;
import java.util.Date;
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
    private long currentRecordNumber;

    public RecipeRecordReader(File file) {
        this.file = file;
    }

    @Override
    public void open() throws Exception {
        scanner = new Scanner(file);
    }

    public boolean hasNextRecord() {
        return scanner.hasNextLine();
    }

    @Override
    public Record readRecord() {
        if (!hasNextRecord()) {
            return null;
        }
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
                ingredient.setQuantity(Integer.valueOf(strings[2].substring(0, strings[2].length() - 1)));
                recipe.getIngredients().add(ingredient);
            }
        }
        Header header = new Header(currentRecordNumber, getDataSourceName(), new Date());
        return new RecipeRecord(header, recipe);
    }

    public String getDataSourceName() {
        return file.getAbsolutePath();
    }

    @Override
    public void close() {
        if (scanner != null) {
            scanner.close();
        }
    }

}
