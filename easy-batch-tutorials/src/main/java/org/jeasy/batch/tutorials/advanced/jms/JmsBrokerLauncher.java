/*
 * The MIT License
 *
 *  Copyright (c) 2021, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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

package org.jeasy.batch.tutorials.advanced.jms;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.activemq.broker.BrokerService;

/**
* Main class to run the JMS Broker.
 *
* @author Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
*/
public class JmsBrokerLauncher {

    public static void main(String[] args) throws Exception {
        BrokerService broker =  new BrokerService();
        broker.addConnector("tcp://localhost:61616");
        broker.start();
        System.out.println("JMS broker started");
        System.out.println("Press any key to stop the broker");
        System.in.read();
        File brokerDataDirectoryFile = broker.getDataDirectoryFile();
        broker.stop();
        Files.walk(brokerDataDirectoryFile.toPath()).map(Path::toFile).forEach(File::delete);
    }

}
