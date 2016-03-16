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

package org.easybatch.core.record;

/**
 * Interface for input records.<br/>
 * <p/>
 * A record can be:
 * <ul>
 * <li>A line in a flat file</li>
 * <li>A tag in a xml file</li>
 * <li>A row in a database table</li>
 * <li>A file in a directory</li>
 * <li>A message in a queue</li>
 * <li>etc</li>
 * </ul>
 *
 * @param <P> The record's payload type.
 * @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 */
public interface Record<P> {

    /**
     * Return the record's header.
     *
     * @return the record's header.
     */
    Header getHeader();

    /**
     * Return the record's payload.
     *
     * @return the record's payload.
     */
    P getPayload();

}
