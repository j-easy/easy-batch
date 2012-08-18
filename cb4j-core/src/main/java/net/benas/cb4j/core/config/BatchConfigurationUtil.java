/*
 * The MIT License
 *
 *  Copyright (c) 2012, benas (md.benhassine@gmail.com)
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

package net.benas.cb4j.core.config;

import net.benas.cb4j.core.util.BatchConstants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * Utility class used by {@link BatchConfiguration} to facilitate configuration process
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class BatchConfigurationUtil {

    private final static Logger logger = Logger.getLogger(BatchConstants.LOGGER_CB4J);

    /**
     * Remove file extension if any.<br/>
     * For instance : if fileName = "myData.dat" , the result returned by this function is "myData".<br/>
     * This will be used when adding default suffixes for ignored and rejected log files.<br/>
     * For this example, default will be "myData-ignored.log" and "myData-rejected.log"
     * @param fileName the file name
     * @return the file name without extension
     */
	public static String removeExtension(final String fileName){
		if(fileName != null && !fileName.isEmpty()){
			if( fileName.indexOf('.') < 0 )
				return fileName;
			else{
				int lastDotIndex = fileName.lastIndexOf('.');
				return fileName.substring(0, lastDotIndex);
			}
		}
		return fileName;//null
	}

    /**
     * Loads properties from a configuration file
     * @param configurationFile the absolute path of the configuration file
     * @return Configuration properties
     * @throws BatchConfigurationException throw if configuration file is not found or cannot be read
     */
	public static Properties loadParametersFromConfigurationFile(final String configurationFile)
			throws BatchConfigurationException {
		Properties properties = new Properties();
		FileInputStream fileInputStream = null;
		try {
			fileInputStream = new FileInputStream(configurationFile);
			properties.load(fileInputStream);
		} catch (FileNotFoundException e) {
			String error = "Configuration failed : configuration file not found : " + configurationFile;
            logger.severe(error);
            throw new BatchConfigurationException(error);
		} catch (IOException e) {
			String error = "Configuration failed : exception in reading configuration file : " + configurationFile;
            logger.severe(error);
            throw new BatchConfigurationException(error);
		}finally{
			try {
				if (fileInputStream!=null)
                    fileInputStream.close();
			} catch (IOException e) {
				logger.warning("Configuration failed : exception in closing configuration file : " + configurationFile);
			}
		}
		return properties;
	}
	
}
