***

<div align="center">
    <b><em>Easy Batch</em></b><br>
    The simple, stupid batch framework for Java&trade;
</div>

<div align="center">

[![MIT license](http://img.shields.io/badge/license-MIT-brightgreen.svg?style=flat)](http://opensource.org/licenses/MIT)
[![Coverage](https://coveralls.io/repos/j-easy/easy-batch/badge.svg?style=flat&branch=master&service=github)](https://coveralls.io/github/j-easy/easy-batch?branch=master)
[![Build Status](https://github.com/j-easy/easy-batch/workflows/Java%20CI/badge.svg)](https://github.com/j-easy/easy-batch/actions)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.jeasy/easy-batch-core/badge.svg?style=flat)](http://search.maven.org/#artifactdetails|org.jeasy|easy-batch-core|6.0.0|)
[![Javadoc](https://www.javadoc.io/badge/org.jeasy/easy-batch-core.svg)](http://www.javadoc.io/doc/org.jeasy/easy-batch-core)
[![Gitter](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/j-easy/easy-batch)

</div>

***

## Latest news

* 07/02/2020: Version 6.0.0 is now released! This is definitely the best release ever and comes with a number of significant improvements! See the change log [here](https://github.com/j-easy/easy-batch/releases).
* 14/01/2020: Version 5.3.0 is finally out! See the change log [here](https://github.com/j-easy/easy-batch/releases).

# What is Easy Batch?

Easy Batch is a framework that aims to simplify batch processing with Java. It was **specifically** designed for simple ETL jobs.
Writing batch applications requires a **lot** of boilerplate code: reading, writing, filtering, parsing and validating data, logging, reporting to name a few..
The idea is to free you from these tedious tasks and let you focus on your batch application's logic.

# How does it work?

Easy Batch jobs are simple processing pipelines. Records are read in sequence from a data source, processed in pipeline and written in batches to a data sink:

![batch processing](https://raw.githubusercontent.com/wiki/j-easy/easy-batch/images/batch-processing.png)

The framework provides the `Record` and `Batch` APIs to abstract data format and process records in a consistent way regardless of the data source type.

Let's see a quick example. Suppose you have the following `tweets.csv` file:

```
id,user,message
1,foo,hello
2,bar,@foo hi!
```

and you want to transform these tweets to XML format. Here is how you can do that with Easy Batch:

```java
Path inputFile = Paths.get("tweets.csv");
Path outputFile = Paths.get("tweets.xml");
Job job = new JobBuilder()
         .reader(new FlatFileRecordReader(inputFile))
         .filter(new HeaderRecordFilter())
         .mapper(new DelimitedRecordMapper(Tweet.class, "id", "user", "message"))
         .marshaller(new XmlRecordMarshaller(Tweet.class))
         .writer(new FileRecordWriter(outputFile))
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
 
 ## Quick start
 
Add the following dependency to your project and you are ready to go:
 
 ```xml
 <dependency>
     <groupId>org.jeasy</groupId>
     <artifactId>easy-batch-core</artifactId>
     <version>6.0.0</version>
 </dependency>
```

You can also generate a quick start project with the following command:

```
$>mvn archetype:generate \
      -DarchetypeGroupId=org.jeasy \
      -DarchetypeArtifactId=easy-batch-archetype \
      -DarchetypeVersion=6.0.0
```

For more details, please check the [Getting started](https://github.com/j-easy/easy-batch/wiki/getting-started) guide.

## Presentations, articles & blog posts

- :movie_camera: [Introduction to Easy Batch: the simple, stupid batch processing framework for Java](https://speakerdeck.com/benas/easy-batch)
- :newspaper: [First batch job on Podcastpedia.org using Easy Batch](http://www.codingpedia.org/ama/first-batch-job-on-podcastpedia-org-with-easybatch/)
- :newspaper: [EasyBatch, les batchs en JAVA tout simplement (in french)](https://blog.sodifrance.fr/easybatch-les-batchs-en-java-tout-simplement/)
- :memo: [Spring Batch vs Easy Batch: Feature comparison](https://github.com/benas/easy-batch-vs-spring-batch/issues/1)
- :memo: [Spring Batch vs Easy Batch: Performance comparison](https://github.com/benas/easy-batch-vs-spring-batch/issues/2)

## Current versions

#### Stable:

The current stable version is [v6.0.0](http://search.maven.org/#artifactdetails|org.jeasy|easy-batch-core|6.0.0|) | [documentation](https://github.com/j-easy/easy-batch/wiki) | [tutorials](https://github.com/j-easy/easy-batch/tree/master/easy-batch-tutorials) | [javadoc](http://javadoc.io/doc/org.jeasy/easy-batch-core/6.0.0)

#### Development:

The current development version is 6.1.0-SNAPSHOT: [![Build Status](https://github.com/j-easy/easy-batch/workflows/Java%20CI/badge.svg)](https://github.com/j-easy/easy-batch/actions)

If you want to import a snapshot version, please check the [Getting started](https://github.com/j-easy/easy-batch/wiki/getting-started#use-a-snapshot-version) guide.

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
* [seseso](https://github.com/seseso)
* [Toilal](https://github.com/Toilal)
* [xenji](https://github.com/xenji)
* [verdi8](https://github.com/verdi8)

Thank you all for your contributions!

## Who is using Easy Batch?

Easy Batch has been successfully used in production in a number of companies which I ([@benas](https://github.com/benas)) am not allowed to mention.
That said, here are some companies who publicly mention that they use Easy Batch:

* [Splunk](https://docs.splunk.com/Documentation/DBX/3.2.0/ReleaseNotes/easybatch)
* [DeepData](https://deepdata-ltd.github.io/tenderbase/#/ted-xml-importer?id=implementation)

You can also find some feedback from the community about the project in the [Testimonials](http://www.jeasy.org/#testimonials) section.

## Credits

![YourKit Java Profiler](https://www.yourkit.com/images/yklogo.png)

Many thanks to [YourKit, LLC](https://www.yourkit.com/) for providing a free license of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/index.jsp) to support the development of Easy Rules.

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
