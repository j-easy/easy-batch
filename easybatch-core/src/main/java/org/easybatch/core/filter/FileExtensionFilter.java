/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.core.filter;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordFilter;
import org.easybatch.core.util.FileRecord;

import java.util.List;

/**
 * A filter that filters {@link FileRecord} having a payload file name ending with a given extension.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class FileExtensionFilter implements RecordFilter {

    /**
     * List of extensions to filter.
     */
    private List<String> extensions;

    /**
     * Construct a file extension filter.
     *
     * @param extensions a list of extensions. Example: .log
     */
    public FileExtensionFilter(List<String> extensions) {
        this.extensions = extensions;
    }

    @Override
    public boolean filterRecord(Record record) {
        FileRecord fileRecord = (FileRecord) record;
        for (String extension : extensions) {
            if (fileRecord.getPayload().getName().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

}
