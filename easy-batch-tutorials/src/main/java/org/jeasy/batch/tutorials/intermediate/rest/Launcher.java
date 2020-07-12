package org.jeasy.batch.tutorials.intermediate.rest;

import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jeasy.batch.core.job.Job;
import org.jeasy.batch.core.job.JobBuilder;
import org.jeasy.batch.core.job.JobExecutor;
import org.jeasy.batch.core.writer.FileRecordWriter;
import org.jeasy.batch.json.JsonRecordMapper;
import org.jeasy.batch.json.JsonRecordReader;

/**
 *
 * Easy Batch does not provide a REST API reader or writer (See https://github.com/j-easy/easy-batch/issues/286)
 * because REST APIs differ significantly in terms of how they manage pagination,
 * authentication, content type, etc.
 *
 * That said, it is always possible to get data from the REST endpoint using
 * the input stream of a {@link java.net.URL} and use Easy Batch components
 * to read/write data.
 *
 * This tutorial shows how to use Easy Batch to generate a release notes for an
 * Easy Batch milestone (eating our own dog food :-)).We will create a job that reads
 * data from a REST endpoint (Github API in this case), transform it and write it
 * to a file.
 *
 */
public class Launcher {

    public static final String GITHUB_API_URL =
            "https://api.github.com/repos/j-easy/easy-batch/issues?milestone=%s&state=all";

    public static void main(String[] args) throws Exception {

        String version = args.length != 0 ? args[0] : "14"; // v6.0.0
        Path outputFile = Paths.get(args.length != 0 ? args[1] : "release-notes.md");
        URL url = new URL(String.format(GITHUB_API_URL, version));

        try (InputStream inputStream = url.openStream();
             JobExecutor jobExecutor = new JobExecutor()) {
            Job job = new JobBuilder()
                    .named("generate release notes job")
                    .reader(new JsonRecordReader(inputStream))
                    .mapper(new JsonRecordMapper<>(Issue.class))
                    .processor(new IssueFormatter())
                    .writer(new FileRecordWriter(outputFile))
                    .build();
            jobExecutor.execute(job);
        }

    }
}
