/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
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

package net.benas.cb4j.core.util;

/**
 * CB4J constants class.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class BatchConstants {

    /*
     * Configuration parameters names
     */
    public static final String INPUT_DATA_PATH = "input.data.path";

    public static final String INPUT_DATA_ENCODING = "input.data.encoding";

    public static final String INPUT_DATA_SKIP_HEADER = "input.data.skipHeader";

    public static final String INPUT_FIELD_SEPARATOR = "input.field.separator";

    public static final String INPUT_FIELD_TRIM = "input.field.trim";

    public static final String INPUT_FIELD_ENCLOSING_CHAR = "input.field.enclosingCharacter";

    public static final String INPUT_RECORD_SIZE = "input.record.size";

    public static final String OUTPUT_DATA_IGNORED = "output.data.ignored";

    public static final String OUTPUT_DATA_REJECTED = "output.data.rejected";

    /*
     * Default values for optional parameters
     */
    public static final String DEFAULT_REJECTED_SUFFIX = "-rejected.log";

    public static final String DEFAULT_IGNORED_SUFFIX = "-ignored.log";

    public static final String DEFAULT_FIELD_SEPARATOR = ",";

    public static final String DEFAULT_FIELD_ENCLOSING_CHAR = "";

    public static final boolean DEFAULT_FIELD_TRIM = true;

    public static final boolean DEFAULT_SKIP_HEADER = false;

    public static final String DEFAULT_FILE_ENCODING = System.getProperty("file.encoding");

    /*
     * Loggers names
     */
    public static final String LOGGER_CB4J = "cb4j";

    public static final String LOGGER_CB4J_IGNORED = "cb4j-ignored";

    public static final String LOGGER_CB4J_REJECTED = "cb4j-rejected";

}
