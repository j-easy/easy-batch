# What is Easy Batch?

To better understand what Easy Batch is, let's see a quick definition of batch processing:

> "Batch processing is the execution of a series of **jobs** on a computer without manual intervention.
> All input **parameters** are predefined through **job control language**.
> This operating environment is termed as **'batch processing'** because the input data are collected into **batches** or sets of **records** and each batch is processed **as a unit**." - [Wikipedia](https://en.wikipedia.org/wiki/Batch_processing)

Easy Batch is a Java framework that provides abstractions for key concepts of batch processing:

* `Job`: a program executed without manual intervention
* `JobParameters`: set of parameters to configure a job
* `JobBuilder`: main entry point to configure jobs
* `JobExecutor`: main entry point to execute jobs
* `JobReport`: execution report with metrics and statistics about the job run
* `Record`: one item in the data source (line in a flat file, row in database table, tag in a Xml file, etc)
* `Batch`: a set of records processed as a unit

Easy Batch jobs are simple processing pipelines. You can process data one record at a time:

![Record processing](https://raw.githubusercontent.com/EasyBatch/easybatch-website/master/img/eb/record-processing.jpg)

Or in batches where each batch is processed as a unit:

![Batch processing](https://raw.githubusercontent.com/EasyBatch/easybatch-website/master/img/eb/batch-processing.jpg)

Easy Batch provides APIs to process data in both modes.
 
# Why Easy Batch?

Because writing batch applications requires a **lot** of boilerplate code: reading, writing, filtering, parsing and validating data, logging, reporting to name a few..
The idea is to free you from these tedious tasks and let you focus on your application's logic.

Let's see a quick example. Suppose you have the following `tweets.csv` file:

```
id,user,message
1,foo,hello
2,bar,@foo hi!
```

and you would like to transform these tweets to XML format. Here is how you can do that with Easy Batch:

```java
Job job = new JobBuilder()
         .reader(new FlatFileRecordReader("tweets.csv"))
         .filter(new HeaderRecordFilter())
         .mapper(new DelimitedRecordMapper(Tweet.class, "id", "user", "message"))
         .processor(new XmlRecordMarshaller(Tweet.class))
         .writer(new FileRecordWriter("tweets.xml"))
         .build();

JobReport report = JobExecutor.execute(job);
```

This example creates a job that:

* reads records one by one from the input file `tweets.csv`
* filter the header record
* map each record to an instance of the `Tweet` bean
* marshal the tweet to XML format
* and finally write this XML to an output file `tweets.xml`

At the end of execution, you get a report with statistics and metrics about the job run (Execution time, number of errors, etc).

All the boilerplate code of resources I/O, iterating through the data source, filtering and parsing records, mapping data to the domain object `Tweet`, writing output and reporting
 is handled by Easy Batch. Your code becomes declarative, intuitive, easy to read, understand, test and maintain.

## Quick links

|Item                  |Link                                                                          |
|----------------------|------------------------------------------------------------------------------|
|Project Home          | [http://www.easybatch.org](http://www.easybatch.org)                         |
|Continuous integration| [Build job @ Travis CI](https://travis-ci.org/EasyBatch/easybatch-framework) |
|Agile Board           | [Backlog items @ waffle.io](https://waffle.io/easybatch/easybatch-framework) |
|Code coverage         | [![Coverage](https://coveralls.io/repos/EasyBatch/easybatch-framework/badge.svg?style=flat&branch=master&service=github)](https://coveralls.io/github/EasyBatch/easybatch-framework?branch=master) |
|Sonar analysis        | [![Quality Gate](https://sonarqube.com/api/badges/gate?key=org.easybatch:easybatch)](https://sonarqube.com/overview?id=org.easybatch%3Aeasybatch) |
|Dependencies          | [![Dependencies](https://www.versioneye.com/user/projects/5589cb2e3133630061000022/badge.svg?style=flat)](https://www.versioneye.com/user/projects/5589cb2e3133630061000022) |

## Presentations, articles & blog posts

- :movie_camera: [Introduction to Easy Batch: the simple, stupid batch processing framework for Java](https://speakerdeck.com/benas/easy-batch)
- :newspaper: [First batch job on Podcastpedia.org using Easy Batch](http://www.codingpedia.org/ama/first-batch-job-on-podcastpedia-org-with-easybatch/)
- :newspaper: [Develop a Java batch application in less than 5 minutes using Easy Batch (in french) ](http://benassi.developpez.com/tutoriels/java/developper-batch-easybatch-5-minutes/)
- :memo: [How I reduced my Java batch application's code by 80% using Easy Batch](http://benas.github.io/2014/01/21/how-i-reduced-my-java-app-code-by-80-using-easy-batch.html)
- :memo: [Easy Batch vs Spring Batch : a Hello World comparison](http://benas.github.io/2014/03/03/spring-batch-vs-easy-batch-a-hello-world-comparison.html)
- :memo: [Easy Batch vs Spring Batch : a performance comparison](http://benas.github.io/2015/02/15/spring-batch-vs-easy-batch-a-performance-comparison.html)

## Current version

* Stable: 4.2.0 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.easybatch/easybatch-core/badge.svg?style=flat)](http://search.maven.org/#artifactdetails|org.easybatch|easybatch-core|4.2.0|)

* Development: 5.0.0-SNAPSHOT [![Build Status](https://travis-ci.org/EasyBatch/easybatch-framework.svg?branch=master)](https://travis-ci.org/EasyBatch/easybatch-framework)</td>

If you want to import the snapshot version, you need to use the following repository:

```xml
<repository>
    <id>ossrh</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
</repository>
```

## Contribution

You are welcome to contribute to the project with pull requests on GitHub.

If you found a bug or want to request a feature, please use the [issue tracker](https://github.com/easybatch/easybatch-framework/issues).

For any further question, you can use the [forum](https://groups.google.com/d/forum/easy-batch) or chat with the team on [Gitter](https://gitter.im/EasyBatch/easybatch-framework).

## Awesome contributors

* [ammachado](https://github.com/ammachado)
* [anandhi](https://github.com/anandhi)
* [AymanDF](https://github.com/AymanDF)
* [chellan](https://github.com/chellan)
* [gs-spadmanabhan](https://github.com/gs-spadmanabhan)
* [imranrajjad](https://github.com/imranrajjad)
* [jawher](https://github.com/jawher)
* [jlcanibe](https://github.com/jlcanibe)
* [natlantisprog](https://github.com/natlantisprog)
* [nicopatch](https://github.com/nicopatch)
* [nihed](https://github.com/nihed)
* [PascalSchumacher](https://github.com/PascalSchumacher)
* [Toilal](https://github.com/Toilal)
* [xenji](https://github.com/xenji)

Thank you all for your contributions!

## Acknowledgments

|JetBrains|YourKit|Travis CI|
|:-:|:-:|:-:|
|![IntelliJ IDEA](https://raw.githubusercontent.com/EasyBatch/easybatch-website/master/img/logo/intellijidea-logo.png)|![YourKit Java Profiler](https://www.yourkit.com/images/yklogo.png)|![Travis CI](https://cdn.travis-ci.com/images/logos/TravisCI-Full-Color-7f5db09495c8b09c21cb678c4de18d21.png)|
|Many thanks to [JetBrains](https://www.jetbrains.com/) for providing a free license of [IntelliJ IDEA](https://www.jetbrains.com/idea/) to kindly support the development of Easy Batch|Many thanks to [YourKit, LLC](https://www.yourkit.com/) for providing a free license of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/index.jsp) to kindly support the development of Easy Batch.|Many thanks to [Travis CI](https://travis-ci.org) for providing a free continuous integration service for open source projects.|

## License

Easy Batch is released under the [![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT).

```
The MIT License (MIT)

Copyright (c) 2016 Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
```
