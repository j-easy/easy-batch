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

import org.easybatch.core.record.FileRecord;
import org.easybatch.core.record.Header;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.lang.String.format;
import static java.nio.file.Files.isRegularFile;
import static org.easybatch.core.util.Utils.checkArgument;
import static org.easybatch.core.util.Utils.checkNotNull;

/**
 * A {@link RecordReader} that reads files in a directory.
 *
 * This reader produces {@link FileRecord} instances.
 *
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public class FileRecordReader implements RecordReader {

    private File directory;
    private Iterator<File> iterator;
    private long currentRecordNumber;
    private boolean recursive;

    /**
     * Create a new {@link FileRecordReader}.
     *
     * @param directory to read files from
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link FileRecordReader#FileRecordReader(java.nio.file.Path)} instead
     */
    @Deprecated
    public FileRecordReader(final File directory) {
        this(directory, false);
    }

    /**
     * Create a new {@link FileRecordReader}.
     *
     * @param directory to read files from
     * @param recursive if the reader should be recursive or not
     * @deprecated This constructor is deprecated since v5.3 and will be removed in v6.
     * Use {@link FileRecordReader#FileRecordReader(java.nio.file.Path, boolean)} instead
     */
    @Deprecated
    public FileRecordReader(final File directory, final boolean recursive) {
        checkNotNull(directory, "directory");
        this.directory = directory;
        this.recursive = recursive;
    }

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
        checkNotNull(path, "path");
        this.directory = path.toFile();
        this.recursive = recursive;
    }

    @Override
    public void open() throws Exception {
        checkDirectory();
        iterator = getFiles(directory).listIterator();
        currentRecordNumber = 0;
    }

    private List<File> getFiles(final File directory) throws IOException {
        int maxDepth = recursive ? Integer.MAX_VALUE : 1;
        FilesCollector filesCollector = new FilesCollector();
        EnumSet<FileVisitOption> fileVisitOptions = EnumSet.noneOf(FileVisitOption.class);
        Files.walkFileTree(directory.toPath(), fileVisitOptions, maxDepth, filesCollector);
        return filesCollector.getFiles();
    }

    private void checkDirectory() {
        checkArgument(directory.exists(), format("Directory %s does not exist.", getDataSourceName()));
        checkArgument(directory.isDirectory(), format("%s is not a directory.", getDataSourceName()));
        checkArgument(directory.canRead(), format("Unable to read files from directory %s. Permission denied.", getDataSourceName()));
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
        return directory.getAbsolutePath();
    }

    @Override
    public void close() {
        // no op
    }

    private static class FilesCollector implements FileVisitor<Path> {

        private List<File> files = new ArrayList<>();

        FilesCollector() {
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
            if (isRegularFile(file)) {
                files.add(file.toFile());
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exception) throws IOException {
            if (exception != null) {
                throw exception;
            }
            return FileVisitResult.CONTINUE;
        }

        @Override
        public FileVisitResult postVisitDirectory(Path dir, IOException exception) throws IOException {
            if (exception != null) {
                throw exception;
            }
            return FileVisitResult.CONTINUE;
        }

        public List<File> getFiles() {
            return files;
        }
    }
}
