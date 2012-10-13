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

    - Products statistics : An example of how to use CB4J to read a CSV file containing products data and calculate statistics about prices

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

More details about these examples can be found in CB4J documentation.
