/**
 * The MIT License
 *
 *   Copyright (c) 2020, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */
package org.easybatch.core.record;

import java.io.File;

/**
 * Record representing a file in a directory.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class FileRecord extends GenericRecord<File> {

    /**
     * Create a new {@link FileRecord}.
     *
     * @param header  the record header
     * @param payload the record payload
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * It will be replaced with a constructor that accepts a {@code java.nio.file.Path}.
     */
    // TODO update FileExtensionFilter accordingly for v6 when the payload becomes a Path
    @Deprecated
    public FileRecord(final Header header, final File payload) {
        super(header, payload);
    }

    @Override
    public String toString() {
        return "Record: {" +
                "header=" + header +
                ", payload=" + payload.getAbsolutePath() +
                '}';
    }

}
