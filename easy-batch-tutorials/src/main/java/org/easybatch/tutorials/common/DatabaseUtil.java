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

package org.easybatch.tutorials.common;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.io.FileUtils;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

/**
 * Utility class for embedded database.
 */
public class DatabaseUtil {

    private static EmbeddedDatabase embeddedDatabase;
    private static JdbcTemplate jdbcTemplate;

    public static DataSource getDataSource() {
        return embeddedDatabase;
    }

    public static void startEmbeddedDatabase() {
        embeddedDatabase = new EmbeddedDatabaseBuilder()
                .setType(HSQL)
                .addScript("db/schema.sql")
                .addScript("db/data.sql")
                .build();
        jdbcTemplate = new JdbcTemplate(embeddedDatabase);
    }

    public static void deleteAllTweets() {
        jdbcTemplate.update("delete from tweet");
    }

    public static void dumpTweetTable() {
        System.out.println("Loading tweets from the database...");
        jdbcTemplate.query("select * from tweet", new RowCallbackHandler() {
            @Override
            public void processRow(ResultSet resultSet) throws SQLException {
                System.out.println(
                        "Tweet : id= " + resultSet.getString("id") + " | " +
                                "user= " + resultSet.getString("user") + " | " +
                                "message= " + resultSet.getString("message")
                );
            }
        });
    }

    public static void cleanUpWorkingDirectory() {
        //delete hsqldb tmp files
        FileUtils.deleteQuietly(new File("mem.log"));
        FileUtils.deleteQuietly(new File("mem.properties"));
        FileUtils.deleteQuietly(new File("mem.script"));
        FileUtils.deleteQuietly(new File("mem.tmp"));
    }

}
