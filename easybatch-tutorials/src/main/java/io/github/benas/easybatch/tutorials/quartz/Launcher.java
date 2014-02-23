/*
 * The MIT License
 *
 *   Copyright (c) 2014, benas (md.benhassine@gmail.com)
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

package io.github.benas.easybatch.tutorials.quartz;

import io.github.benas.easybatch.core.filter.RecordNumberEqualsToRecordFilter;
import io.github.benas.easybatch.core.impl.EasyBatchEngine;
import io.github.benas.easybatch.core.impl.EasyBatchEngineBuilder;
import io.github.benas.easybatch.flatfile.FlatFileRecordReader;
import io.github.benas.easybatch.flatfile.dsv.DelimitedRecordMapper;
import io.github.benas.easybatch.integration.quartz.EasyBatchScheduler;
import io.github.benas.easybatch.tutorials.common.Greeting;
import io.github.benas.easybatch.tutorials.common.GreetingProcessor;
import io.github.benas.easybatch.validation.BeanValidationRecordValidator;

import java.io.File;
import java.util.Date;

/**
 * Main class to run the Hello World tutorial repeatedly every minute using easy batch - quartz integration module.<br/>
 *
 * The {@link io.github.benas.easybatch.integration.quartz.EasyBatchScheduler} API lets you schedule easy batch executions as follows :
 * <ul>
 * <li>At a fixed point of time using {@link io.github.benas.easybatch.integration.quartz.EasyBatchScheduler#scheduleAt(java.util.Date)}</li>
 * <li>Repeatedly with predefined interval using {@link io.github.benas.easybatch.integration.quartz.EasyBatchScheduler#scheduleAtWithInterval(java.util.Date, int)}</li>
 * <li>Using unix cron-like expression with {@link io.github.benas.easybatch.integration.quartz.EasyBatchScheduler#scheduleCron(String)}</li>
 * </ul>
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class Launcher {

    public static void main(String[] args) throws Exception {

        // Build an easy batch engine
        EasyBatchEngine easyBatchEngine = new EasyBatchEngineBuilder()
                .registerRecordReader(new FlatFileRecordReader(new File(args[0])))
                .registerRecordFilter(new RecordNumberEqualsToRecordFilter(1))
                .registerRecordMapper(new DelimitedRecordMapper<Greeting>(Greeting.class, new String[]{"id", "name"}))
                .registerRecordValidator(new BeanValidationRecordValidator<Greeting>())
                .registerRecordProcessor(new GreetingProcessor())
                .build();

        // schedule the engine to start now and run every minute
        EasyBatchScheduler easyBatchScheduler = new EasyBatchScheduler(easyBatchEngine);
        easyBatchScheduler.scheduleAtWithInterval(new Date(), 1);
        easyBatchScheduler.start();

    }

}