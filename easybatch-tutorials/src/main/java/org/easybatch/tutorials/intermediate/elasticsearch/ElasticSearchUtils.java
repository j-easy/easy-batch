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

package org.easybatch.tutorials.intermediate.elasticsearch;

import org.apache.commons.io.FileUtils;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.node.Node;

import java.io.File;

import static org.easybatch.core.util.Utils.FILE_SEPARATOR;
import static org.easybatch.core.util.Utils.JAVA_IO_TMPDIR;
import static org.elasticsearch.node.NodeBuilder.nodeBuilder;

/**
 * Elastic Search utilities.
 */
public class ElasticSearchUtils {

    private static final String ES_DATA_DIRECTORY = JAVA_IO_TMPDIR + FILE_SEPARATOR + "es-data";

    public static Node startEmbeddedNode(){

        ImmutableSettings.Builder elasticSearchSettings = ImmutableSettings.settingsBuilder()
                .put("http.enabled", "false")
                .put("path.data", ES_DATA_DIRECTORY);

        return nodeBuilder()
                .local(true)
                .settings(elasticSearchSettings.build())
                .node();
    }

    public static void stopEmbeddedNode(Node node) {
        node.close();
        deleteElasticSearchDataDirectory();

    }

    private static void deleteElasticSearchDataDirectory() {
        FileUtils.deleteQuietly(new File(ES_DATA_DIRECTORY));
    }

}
