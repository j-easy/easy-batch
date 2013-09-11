*******************************************************************************
*                                                                             *
*            CB4J : CSV Batch processing with Java made easy!                 *
*                                                                             *
*******************************************************************************

***********
* Contact *
***********

Author: md.benhassine@gmail.com
Website: https://github.com/benas/cb4j

***********
* License *
***********

CB4J is released under the MIT License, see the file LICENSE.txt

*************************
* Distribution content  *
*************************

The distribution contains the following directories :

        * src : CB4J source code

        * docs : CB4J documentation (API javadoc and user documentation)

        * dist : CB4J core module jar file

*********************************
* Building and running examples *
*********************************

To build and run CB4J examples, you need to have maven already installed and correctly configured.

The distribution contains a set of ready to use CB4J examples :

    - Hello world : A very simple CB4J batch to process a CSV file containing persons records and generate greetings

    - Customers ETL : An example of how to use CB4J to process customers data from a CSV flat file and generate XML output

    - Products statistics : An example of how to use CB4J to read a fixed length records file containing products data and calculate statistics about prices

    - Book library : A use case of CB4J batch to load books data from a CSV file into a library database using Hibernate

    - Spring tutorial : A tutorial showing how to integrate CB4J with Spring framework

    - Quartz tutorial : A tutorial showing how to schedule a CB4J batch using the Quartz scheduler

    - Bean Validation tutorial : A tutorial showing how to use Bean Validation (JSR303) to validate data in a CB4J batch

    - JMX tutorial : A tutorial showing how to use JMX to monitor CB4J at runtime

1- Build examples:
------------------------

To build CB4J examples, run the following commands from the distribution's root directory:

 $>cd src
 $>mvn install

2- Run examples :
-----------------

2.1 Hello world
---------------

 $>cd cb4j-tutorials
 $>mvn exec:java -PrunHelloWorldTutorial

2.2 Customers ETL
-----------------

 $>cd cb4j-tutorials
 $>mvn exec:java -PrunCustomersTutorial

2.3 Products statistics
-----------------------
 $>cd cb4j-tutorials
 $>mvn exec:java -PrunProductsTutorial

2.4 Book library
-----------------------
 $>cd cb4j-tutorials
 $>mvn exec:java -PrunLibraryTutorial

2.5 Spring tutorial
-----------------------
 $>cd cb4j-tutorials
 $>mvn exec:java -PrunSpringTutorial

2.6 Quartz tutorial
-----------------------
 $>cd cb4j-tutorials
 $>mvn exec:java -PrunQuartzTutorial

2.7 Bean Validation tutorial
-----------------------
 $>cd cb4j-tutorials
 $>mvn exec:java -PrunBeanValidationTutorial

2.8 JMX tutorial
-----------------------
Run the core batch:
 $>cd cb4j-tutorials
 $>mvn exec:exec -PrunJmxTutorial

Launch the command line client in a separate console:
 $>cd cb4j-tutorials
 $>mvn exec:java -PrunJmxCliClient

Launch the graphical client in a separate console:
 $>cd cb4j-tutorials
 $>mvn exec:java -PrunJmxGuiClient

More details about these examples can be found in CB4J documentation.
