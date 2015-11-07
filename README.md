## What is Easy Batch?

Easy Batch is a framework that aims to simplify batch processing with Java. It frees you from tedious tasks such as reading, writing, filtering, parsing and validating data and lets you keep focus on your batch processing business logic.

Let's see a quick example. Suppose you have the following `tweets.csv` file:

```
id,user,message
1,foo,hello
2,bar,@foo hi!
```

and you would like to transform these tweets to XML format. Here is how you can do that with Easy Batch:

```java
Job job = JobBuilder()
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

## How does it work?

 Easy Batch jobs are simple processing pipelines. You can process data one record at a time (as in the previous example):

![Record processing](https://raw.githubusercontent.com/EasyBatch/easybatch-website/master/img/eb/record-processing.jpg)

 Or in batches where each batch is processed as a unit:

![Batch processing](https://raw.githubusercontent.com/EasyBatch/easybatch-website/master/img/eb/batch-processing.jpg)

 Easy Batch provides APIs to process data in both modes.

## Quick links

|Item                  |Link                                                                          |
|----------------------|------------------------------------------------------------------------------|
|Project Home          | [http://www.easybatch.org](http://www.easybatch.org)                         |
|Continuous integration| [Build job @ Travis CI](https://travis-ci.org/EasyBatch/easybatch-framework) |
|Agile Board           | [Backlog items @ waffle.io](https://waffle.io/easybatch/easybatch-framework) |
|Code coverage         | [![Coverage](https://coveralls.io/repos/EasyBatch/easybatch-framework/badge.svg?style=flat&branch=master&service=github)](https://coveralls.io/github/EasyBatch/easybatch-framework?branch=master) |
|Dependencies          | [![Dependencies](https://www.versioneye.com/user/projects/5589cb2e3133630061000022/badge.svg?style=flat)](https://www.versioneye.com/user/projects/5589cb2e3133630061000022) |

## Presentations, articles & blog posts

- :movie_camera: [Introduction to Easy Batch: the simple, stupid batch processing framework for Java](https://speakerdeck.com/benas/easy-batch)
- :newspaper: [First batch job on Podcastpedia.org using Easy Batch](http://www.codingpedia.org/ama/first-batch-job-on-podcastpedia-org-with-easybatch/)
- :newspaper: [Develop a Java batch application in less than 5 minutes using Easy Batch (in french) ](http://benassi.developpez.com/tutoriels/java/developper-batch-easybatch-5-minutes/)
- :memo: [How I reduced my Java batch application's code by 80% using Easy Batch](http://blog.mahmoud-benhassine.fr/2014/01/21/how-i-reduced-my-java-app-code-by-80%25-using-easy-batch.html)
- :memo: [Easy Batch vs Spring Batch : a Hello World comparison](http://blog.mahmoud-benhassine.fr/2014/03/03/spring-batch-vs-easy-batch:-a-hello-world-comparison.html)
- :memo: [Easy Batch vs Spring Batch : a performance comparison](http://blog.mahmoud-benhassine.fr/2015/02/15/spring-batch-vs-easy-batch:-a-performance-comparison.html)

## Current version

* Stable: 4.0.0 [![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.easybatch/easybatch-core/badge.svg?style=flat)](http://search.maven.org/#artifactdetails|org.easybatch|easybatch-core|4.0.0|)

* Development: 4.1.0-SNAPSHOT [![Build Status](https://travis-ci.org/EasyBatch/easybatch-framework.svg?branch=master)](https://travis-ci.org/EasyBatch/easybatch-framework)</td>

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
* [chellan](https://github.com/chellan)
* [gs-spadmanabhan](https://github.com/gs-spadmanabhan)
* [imranrajjad](https://github.com/imranrajjad)
* [jawher](https://github.com/jawher)
* [jlcanibe](https://github.com/jlcanibe)
* [natlantisprog](https://github.com/natlantisprog)
* [nicopatch](https://github.com/nicopatch)
* [nihed](https://github.com/nihed)
* [Toilal](https://github.com/Toilal)
* [xenji](https://github.com/xenji)

Thank you all for your contributions!

## Acknowledgments

|JetBrains|YourKit|Travis CI|
|:-:|:-:|:-:|
|![IntelliJ IDEA](https://www.jetbrains.com/idea/docs/logo_intellij_idea.png)|![YourKit Java Profiler](https://www.yourkit.com/images/yklogo.png)|![Travis CI](https://travis-ci.com/img/brand-standards/logo-downloads/TravisCI-Full-Color.png)|
|Many thanks to [JetBrains](https://www.jetbrains.com/) for providing a free license of [IntelliJ IDEA](https://www.jetbrains.com/idea/) to kindly support the development of Easy Batch|Many thanks to [YourKit, LLC](https://www.yourkit.com/) for providing a free license of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/index.jsp) to kindly support the development of Easy Batch.|Many thanks to [Travis CI](https://travis-ci.org) for providing a free continuous integration service for open source projects.|

## License

Easy Batch is released under the [![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT).

```
The MIT License (MIT)

Copyright (c) 2015 Mahmoud Ben Hassine (mahmoud@benhassine.fr)

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
