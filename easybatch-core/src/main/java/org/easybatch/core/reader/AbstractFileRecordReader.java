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
package org.easybatch.core.reader;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.file.Path;

/**
 * Abstract class for all file readers.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public abstract class AbstractFileRecordReader implements RecordReader {

    @Deprecated
    protected File file;

    protected Charset charset;

    /**
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link AbstractFileRecordReader#AbstractFileRecordReader(java.nio.file.Path)} instead.
     * @param file to read data from
     */
    @Deprecated
    protected AbstractFileRecordReader(File file) {
        this(file, Charset.defaultCharset());
    }

    /**
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link AbstractFileRecordReader#AbstractFileRecordReader(java.nio.file.Path, java.nio.charset.Charset)}
     * @param file to read data from
     * @param charset of the input file
     */
    @Deprecated
    public AbstractFileRecordReader(File file, Charset charset) {
        this.file = file;
        this.charset = charset;
    }

    /**
     * @param path to read data from
     */
    protected AbstractFileRecordReader(Path path) {
        this(path, Charset.defaultCharset());
    }

    /**
     * @param path to read data from
     * @param charset of the input file
     */
    public AbstractFileRecordReader(Path path, Charset charset) {
        this.file = path.toFile();
        this.charset = charset;
    }

    /**
     * @deprecated This getter is deprecated since v5.3 and will be removed in v6.
     * It will be replaced by a getter that returns {@code java.nio.file.Path}
     * @return the input file
     */
    @Deprecated
    public File getFile() {
        return file;
    }

    public Charset getCharset() {
        return charset;
    }
}
