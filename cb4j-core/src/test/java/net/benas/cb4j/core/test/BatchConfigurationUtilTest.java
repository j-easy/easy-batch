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

package net.benas.cb4j.core.test;

import net.benas.cb4j.core.config.BatchConfigurationUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
/**
 * Unit tests for configuration utilities
 * @author benas (md.benhassine@gmail.com)
 */
public class BatchConfigurationUtilTest {

    /**
     * The file name to test
     */
    private String fileName;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void TestExtensionRemovalForRegularFileName(){
	    fileName = "myData.dat";
        String result = BatchConfigurationUtil.removeExtension(fileName);
        assertEquals("myData",result);
	}

    @Test
    public void TestExtensionRemovalForFileNameWithManyExtensions(){
        fileName = "myData.dat.data";
        String result = BatchConfigurationUtil.removeExtension(fileName);
        assertEquals("myData.dat",result);
    }

    @Test
    public void TestExtensionRemovalForFileNameWithoutExtension(){
        fileName = "myData";
        String result = BatchConfigurationUtil.removeExtension(fileName);
        assertEquals("myData",result);
    }

    @After
    public void tearDown() throws Exception {
        fileName = null;
        System.gc();
    }
}
