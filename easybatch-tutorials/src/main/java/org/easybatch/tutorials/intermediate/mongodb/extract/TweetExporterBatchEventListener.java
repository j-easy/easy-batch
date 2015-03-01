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

package org.easybatch.tutorials.intermediate.mongodb.extract;

import org.easybatch.core.api.event.batch.BatchProcessEventListener;

import java.io.IOException;
import java.io.OutputStream;

/**
 * A batch event listener to write a wrapper tag around tweets and close the output stream.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class TweetExporterBatchEventListener implements BatchProcessEventListener {

    private OutputStream outputStream;

    public TweetExporterBatchEventListener(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    @Override
    public void beforeBatchStart() {
        try {
            outputStream.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n".getBytes());
            outputStream.write("<tweets>\n".getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Unable to write output stream header", e);
        }
    }

    @Override
    public void afterBatchEnd() {
        try {
            outputStream.write("</tweets>".getBytes());
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to close output stream", e);
        }
    }

    @Override
    public void onException(Throwable throwable) {
    }
}
