/*
 * The MIT License
 *
 *    Copyright (c) 2013, benas (md.benhassine@gmail.com)
 *
 *    Permission is hereby granted, free of charge, to any person obtaining a copy
 *    of this software and associated documentation files (the "Software"), to deal
 *    in the Software without restriction, including without limitation the rights
 *    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *    copies of the Software, and to permit persons to whom the Software is
 *    furnished to do so, subject to the following conditions:
 *
 *    The above copyright notice and this permission notice shall be included in
 *    all copies or substantial portions of the Software.
 *
 *    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *    THE SOFTWARE.
 */

package io.github.benas.cb4j.monitor.cli;

import io.github.benas.cb4j.monitor.jmx.Client;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * CB4J Command line JMX monitor.
 * @author benas (md.benhassine@gmail.com)
 */
public class Launcher {

    private static Logger logger = Logger.getLogger("cb4j");

    public static void main(String[] args){

        if (args == null || args.length != 2){
            System.err.println("[CB4J] JMX Host and Port are mandatory, usage : ");
            System.err.println("java io.github.benas.cb4j.monitor.cli.Launcher host port");
            System.err.println("Example: java io.github.benas.cb4j.monitor.cli.Launcher localhost 9999");
            System.exit(1);
        }

        String host = args[0];
        String port = args[1];

        Client client = new Client(host, port);
        ReportUpdateNotificationListener reportUpdateNotificationListener = new ReportUpdateNotificationListener();
        ConnectionClosedNotificationListener connectionClosedNotificationListener =
                new ConnectionClosedNotificationListener(reportUpdateNotificationListener);
        try {
            client.init(reportUpdateNotificationListener, connectionClosedNotificationListener);
        } catch (Exception e) {
            logger.severe("Unable to connect to CB4J batch at " + host + ":" + port );
            System.exit(1);
        }

        System.out.println("Press any key to exit");
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
