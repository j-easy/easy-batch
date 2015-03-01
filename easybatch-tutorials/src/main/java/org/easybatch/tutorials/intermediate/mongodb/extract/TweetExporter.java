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

import com.thoughtworks.xstream.XStream;
import org.easybatch.core.api.RecordProcessor;

import java.io.OutputStream;

/**
 * A processor that writes tweets to an output stream.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
public class TweetExporter implements RecordProcessor<Tweet, Tweet> {

    private OutputStream outputStream;

    private XStream xStream;

    public TweetExporter(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.xStream = new XStream();
        xStream.alias("tweet", Tweet.class);
    }

    @Override
    public Tweet processRecord(Tweet tweet) throws Exception {
        outputStream.write(xStream.toXML(tweet).getBytes());
        outputStream.write("\n".getBytes());
        return tweet;
    }

}
