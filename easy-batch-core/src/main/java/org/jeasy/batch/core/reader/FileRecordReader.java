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
package org.jeasy.batch.core.reader;

import org.jeasy.batch.core.util.Utils;
import org.jeasy.batch.core.record.FileRecord;
import org.jeasy.batch.core.record.Header;

import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.nio.file.Files.isRegularFile;

/**
 * A {@link RecordReader} that reads files in a directory.
 *
 * This reader produces {@link FileRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class FileRecordReader implements RecordReader {

    private Path directory;
    private Iterator<Path> iterator;
    private long currentRecordNumber;
    private boolean recursive;

    /**
     * Create a new {@link FileRecordReader}.
     *
     * @param path to read files from
     */
    public FileRecordReader(final Path path) {
        this(path, false);
    }

    /**
     * Create a new {@link FileRecordReader}.
     *
     * @param path to read files from
     *  @param recursive if the reader should be recursive or not
     */
    public FileRecordReader(final Path path, final boolean recursive) {
        Utils.checkNotNull(path, "path");
        this.directory = path;
        this.recursive = recursive;
    }

    @Override
    public void open() throws Exception {
        checkDirectory();
        Stream<Path> pathStream = recursive ? Files.walk(directory) : Files.list(directory);
        iterator = pathStream.filter(path -> isRegularFile(path)).iterator();
        currentRecordNumber = 0;
    }

    private void checkDirectory() {
        Utils.checkArgument(Files.exists(directory), format("Directory %s does not exist.", getDataSourceName()));
        Utils.checkArgument(Files.isDirectory(directory), format("%s is not a directory.", getDataSourceName()));
        Utils.checkArgument(Files.isReadable(directory), format("Unable to read files from directory %s. Permission denied.", getDataSourceName()));
    }

    @Override
    public FileRecord readRecord() {
        Header header = new Header(++currentRecordNumber, getDataSourceName(), new Date());
        if (iterator.hasNext()) {
            return new FileRecord(header, iterator.next());
        } else {
            return null;
        }
    }

    private String getDataSourceName() {
        return directory.toAbsolutePath().toString();
    }

    @Override
    public void close() {
        // no op
    }

}
