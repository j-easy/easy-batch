***

<div align="center">
    <b><em>Easy Batch</em></b><br>
    The simple, stupid batch framework for Java&trade;
</div>

<div align="center">

[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT)
[![Coverage](https://coveralls.io/repos/j-easy/easy-batch/badge.svg?style=flat&branch=master&service=github)](https://coveralls.io/github/j-easy/easy-batch?branch=master)
[![Build Status](https://travis-ci.org/j-easy/easy-batch.svg?branch=master)](https://travis-ci.org/j-easy/easy-batch)
[![Build status](https://ci.appveyor.com/api/projects/status/pwpfbmmew717wtgn/branch/master?svg=true)](https://ci.appveyor.com/project/benas/easy-batch/branch/master)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.easybatch/easybatch-core/badge.svg?style=flat)](http://search.maven.org/#artifactdetails|org.easybatch|easybatch-core|5.3.0|)
[![Javadoc](https://www.javadoc.io/badge/org.easybatch/easybatch-core.svg)](http://www.javadoc.io/doc/org.easybatch/easybatch-core)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/j-easy/easy-batch)

</div>

***

## Latest news

* 14/01/2020: Version 5.3.0 is finally out! See the change log [here](https://github.com/j-easy/easy-batch/releases).

# What is Easy Batch?

Easy Batch is a framework that aims to simplify batch processing with Java.
Writing batch applications requires a **lot** of boilerplate code: reading, writing, filtering, parsing and validating data, logging, reporting to name a few..
The idea is to free you from these tedious tasks and let you focus on your application's logic.

# How does it work?

Easy Batch jobs are simple processing pipelines. Records are read in sequence from a data source, processed in pipeline and written in batches to a data sink:

![batch processing](https://raw.githubusercontent.com/wiki/j-easy/easy-batch/images/batch-processing.png)

The framework provides `Record` and `Batch` APIs to abstract data format and process records in a consistent way regardless of the data source type.

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
         .marshaller(new XmlRecordMarshaller(Tweet.class))
         .writer(new FileRecordWriter("tweets.xml"))
         .batchSize(10)
         .build();

JobExecutor jobExecutor = new JobExecutor();
JobReport report = jobExecutor.execute(job);
jobExecutor.shutdown();
```

This example creates a job that:

* reads records one by one from the input file `tweets.csv`
* filter the header record
* map each record to an instance of the `Tweet` bean
* marshal the tweet to XML format
* and finally write XML records in batches of 10 to the output file `tweets.xml`

At the end of execution, you get a report with statistics and metrics about the job run (Execution time, number of errors, etc).

All the boilerplate code of resources I/O, iterating through the data source, filtering and parsing records, mapping data to the domain object `Tweet`, writing output and reporting
 is handled by Easy Batch. Your code becomes declarative, intuitive, easy to read, understand, test and maintain.

## Presentations, articles & blog posts

- :movie_camera: [Introduction to Easy Batch: the simple, stupid batch processing framework for Java](https://speakerdeck.com/benas/easy-batch)
- :newspaper: [First batch job on Podcastpedia.org using Easy Batch](http://www.codingpedia.org/ama/first-batch-job-on-podcastpedia-org-with-easybatch/)
- :newspaper: [EasyBatch, les batchs en JAVA tout simplement (in french)](https://blog.netapsys.fr/easybatch-les-batchs-en-java-tout-simplement/)
- :newspaper: [Tutoriel pour développer un batch Java avec Easy Batch en moins de 5 minutes (in french) ](http://benassi.developpez.com/tutoriels/java/developper-batch-easybatch-5-minutes/)
- :memo: [How I reduced my Java batch application's code by 80% using Easy Batch](http://benas.github.io/2014/01/21/how-i-reduced-my-java-app-code-by-80-using-easy-batch.html)
- :memo: [Spring Batch vs Easy Batch: Feature comparison](http://benas.github.io/2014/03/03/spring-batch-vs-easy-batch-feature-comparison.html)
- :memo: [Spring Batch vs Easy Batch: Performance comparison](http://benas.github.io/2015/02/15/spring-batch-vs-easy-batch-performance-comparison.html)

## Current versions

#### Stable:

The current stable version is [v5.3.0](http://search.maven.org/#artifactdetails|org.easybatch|easybatch-core|5.3.0|) | [documentation](https://github.com/j-easy/easy-batch/wiki) | [tutorials](https://github.com/j-easy/easy-batch/tree/master/easybatch-tutorials) | [javadoc](http://javadoc.io/doc/org.easybatch/easybatch-core/5.3.0)

#### Development:

The current development version is 5.3.1-SNAPSHOT [![Build Status](https://travis-ci.org/j-easy/easy-batch.svg?branch=master)](https://travis-ci.org/j-easy/easy-batch) [![Build status](https://ci.appveyor.com/api/projects/status/pwpfbmmew717wtgn/branch/master?svg=true)](https://ci.appveyor.com/project/benas/easy-batch/branch/master)

If you want to import a snapshot version, you need to use the following repository:

```xml
<repository>
    <id>ossrh</id>
    <url>https://oss.sonatype.org/content/repositories/snapshots</url>
</repository>
```

## Contribution

You are welcome to contribute to the project with pull requests on GitHub.

If you find a bug or want to request a feature, please use the [issue tracker](https://github.com/j-easy/easy-batch/issues).

For any further question, you can use the [gitter channel](https://gitter.im/j-easy/easy-batch) of the project.

## Awesome contributors

* [ammachado](https://github.com/ammachado)
* [anandhi](https://github.com/anandhi)
* [AussieGuy0](https://github.com/AussieGuy0)
* [AymanDF](https://github.com/AymanDF)
* [chsfleury](https://github.com/chsfleury)
* [chellan](https://github.com/chellan)
* [DanieleS82](https://github.com/DanieleS82)
* [gs-spadmanabhan](https://github.com/gs-spadmanabhan)
* [imranrajjad](https://github.com/imranrajjad)
* [ipropper](https://github.com/ipropper)
* [IvanAtanasov89](https://github.com/IvanAtanasov89)
* [jawher](https://github.com/jawher)
* [jlcanibe](https://github.com/jlcanibe)
* [MALPI](https://github.com/MALPI)
* [marcosvpcortes](https://github.com/marcosvpcortes)
* [natlantisprog](https://github.com/natlantisprog)
* [nicopatch](https://github.com/nicopatch)
* [nihed](https://github.com/nihed)
* [PascalSchumacher](https://github.com/PascalSchumacher)
* [Toilal](https://github.com/Toilal)
* [xenji](https://github.com/xenji)
* [verdi8](https://github.com/verdi8)

Thank you all for your contributions!

## Acknowledgments

|JetBrains|YourKit|Travis CI|
|:-:|:-:|:-:|
|![IntelliJ IDEA](https://raw.githubusercontent.com/wiki/j-easy/easy-batch/images/logo/intellijidea-logo.png)|![YourKit Java Profiler](https://www.yourkit.com/images/yklogo.png)|![Travis CI](https://travis-ci.com/images/logos/TravisCI-Full-Color.png)|
|Many thanks to [JetBrains](https://www.jetbrains.com/) for providing a free license of [IntelliJ IDEA](https://www.jetbrains.com/idea/) to kindly support the development of Easy Batch|Many thanks to [YourKit, LLC](https://www.yourkit.com/) for providing a free license of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/index.jsp) to kindly support the development of Easy Batch.|Many thanks to [Travis CI](https://travis-ci.org) for providing a free continuous integration service for open source projects.|

## License

Easy Batch is released under the [![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT).

```
The MIT License (MIT)

Copyright (c) 2020 Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)

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
