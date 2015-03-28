## What is Easy Batch?
[![Gitter](https://badges.gitter.im/Join Chat.svg)](https://gitter.im/EasyBatch/easybatch-framework?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

Easy Batch is a framework that aims to simplify batch processing with Java.

It addresses common tedious tasks such as reading, filtering, parsing and validating input data and lets you keep focus on your batch processing business logic.

## Key features

 * Lightweight: The framework's core has no dependencies and hence a small memory footprint : a 80Ko jar file with a lot of features.

 * POJO-oriented development: Map records to your domain objects so you can still work with the Object Oriented aspect of Java.

 * Declarative data validation: Declare data validation constraints on your domain objects and let Easy Batch handle the validation code for you.

 * Easy to learn and use: Easy Batch uses a simple and natural data model and API. You can learn it easily and start using it quickly.

 * Statistics reporting: Easy Batch provides a simple and customizable reporting tools for common statistics including validation errors and processing time.

 * JMX monitoring: Monitor your batch execution and progress using JMX at runtime and in real time.

 * Parallel execution: Run multiple engine instances in parallel to speed up execution and reduce processing time.

## Documentation

### Project Home
[http://www.easybatch.org](http://www.easybatch.org)

### Continuous integration
[Jenkins job @ cloudbees.com](https://buildhive.cloudbees.com/job/benas/job/easy-batch/)

### Agile Board
[Backlog items @ waffle.io](https://waffle.io/easybatch/easybatch-framework)

### Presentation slides
[https://speakerdeck.com/benas/easy-batch](https://speakerdeck.com/benas/easy-batch)

### Articles
- [First batch job on Podcastpedia.org using Easy Batch](http://www.codingpedia.org/ama/first-batch-job-on-podcastpedia-org-with-easybatch/)
- [Develop a Java batch application in less than 5 minutes using Easy Batch (in french) ](http://benassi.developpez.com/tutoriels/java/developper-batch-easybatch-5-minutes/)

### Blog posts
- [How I reduced my Java batch application’s code by 80% using Easy Batch](http://blog.mahmoud-benhassine.fr/2014/01/21/how-i-reduced-my-java-app-code-by-80%25-using-easy-batch.html)
- [Easy Batch vs Spring Batch : a Hello World comparison](http://blog.mahmoud-benhassine.fr/2014/03/03/spring-batch-vs-easy-batch:-a-hello-world-comparison.html)
- [Easy Batch vs Spring Batch : a performance comparison](http://blog.mahmoud-benhassine.fr/2015/02/15/spring-batch-vs-easy-batch:-a-performance-comparison.html)

## Current version

* The current stable version is 3.0.0: [![Build Status](https://buildhive.cloudbees.com/job/benas/job/easy-batch/badge/icon)](https://buildhive.cloudbees.com/job/benas/job/easy-batch/)
* The current development version is 3.0.1-SNAPSHOT

## Contribution

You are welcome to contribute to the project with pull requests on GitHub.

If you believe you found a bug, please use the [issue tracker](https://github.com/benas/easy-batch/issues).

It would be great to attach a JUnit test that fails and it would be awesome to send a pull request with a patch that fixes the bug!

For any further question, you can use the [forum](https://groups.google.com/d/forum/easy-batch) or chat with the team on [Gitter](https://gitter.im/EasyBatch/easybatch-framework).

## Awesome contributors

* [ammachado](https://github.com/ammachado)
* [anandhi](https://github.com/anandhi)
* [chellan](https://github.com/chellan)
* [jawher](https://github.com/jawher)
* [natlantisprog](https://github.com/natlantisprog)
* [nicopatch](https://github.com/nicopatch)
* [nihed](https://github.com/nihed)
* [xenji](https://github.com/xenji)

Thank you all for your contributions!

## Acknowledgments

### JetBrains

Many thanks to [JetBrains](https://www.jetbrains.com/) for providing a free license of [IntelliJ IDEA](https://www.jetbrains.com/idea/) to kindly support the development of Easy Batch.

![IntelliJ IDEA](https://www.jetbrains.com/idea/docs/logo_intellij_idea.png)

### YourKit

Many thanks to [YourKit, LLC](https://www.yourkit.com/) for providing a free license of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/index.jsp) to kindly support the development of Easy Batch.

![YourKit Java Profiler](https://www.yourkit.com/images/yklogo.png)

### CloudBees

Many thanks to [CloudBees]() for providing a free [Jenkins]() service to support continuous integration for open source projects.

![CloudBees](https://www.cloudbees.com/sites/default/files/styles/large/public/Button-Powered-by-CB.png)

## License

Easy Batch is released under the [MIT License](http://opensource.org/licenses/mit-license.php/):

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
