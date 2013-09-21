package io.github.benas.cb4j.core.config;

import io.github.benas.cb4j.core.util.BatchConstants;
import io.github.benas.cb4j.core.util.RecordType;

import java.util.Properties;

/**
 * A {@link BatchConfiguration} instance builder with smart defaults.<br/>
 *
 * This class can be used to programmatically configure CB4J without worrying about configuration parameters names.
 *
 * @author benas (md.benhassine@gmail.com)
 */
public class BatchConfigurationBuilder {

    private Properties properties;

    public BatchConfigurationBuilder() {
        //set default values, will be overridden with called configuration methods
        properties = new Properties();
        properties.setProperty(BatchConstants.INPUT_DATA_ENCODING, BatchConstants.DEFAULT_FILE_ENCODING);
        properties.setProperty(BatchConstants.INPUT_DATA_SKIP_HEADER, String.valueOf(BatchConstants.DEFAULT_SKIP_HEADER));
        properties.setProperty(BatchConstants.INPUT_RECORD_TYPE, BatchConstants.DEFAULT_RECORD_TYPE);
        properties.setProperty(BatchConstants.INPUT_FIELD_DELIMITER, BatchConstants.DEFAULT_FIELD_DELIMITER);
        properties.setProperty(BatchConstants.INPUT_FIELD_TRIM, String.valueOf(BatchConstants.DEFAULT_FIELD_TRIM));
        properties.setProperty(BatchConstants.INPUT_FIELD_QUALIFIER_CHAR, BatchConstants.DEFAULT_FIELD_QUALIFIER_CHAR);
        properties.setProperty(BatchConstants.OUTPUT_DATA_ABORT_ON_FIRST_REJECT, String.valueOf(BatchConstants.DEFAULT_ABORT_ON_FIRST_REJECT));
        properties.setProperty(BatchConstants.OUTPUT_DATA_ABORT_ON_FIRST_ERROR, String.valueOf(BatchConstants.DEFAULT_ABORT_ON_FIRST_ERROR));
        properties.setProperty(BatchConstants.OUTPUT_DATA_ABORT_ON_FIRST_MAPPING_EXCEPTION, String.valueOf(BatchConstants.DEFAULT_ABORT_ON_FIRST_MAPPING_EXCEPTION));
        properties.setProperty(BatchConstants.OUTPUT_DATA_JMX_ENABLED, String.valueOf(BatchConstants.DEFAULT_OUTPUT_DATA_JMX_ENABLED));
    }

    /**
     * Set the absolute path of input data file.
     * @param filePath absolute path of input data file
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder inputDataFile(String filePath) {
        properties.setProperty(BatchConstants.INPUT_DATA_PATH, filePath);
        return this;
    }

    /**
     * Set the input data file encoding.
     * @param encoding input data file encoding. Default to {@link BatchConstants#DEFAULT_FILE_ENCODING}
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder inputDataFileEncoding(String encoding) {
        properties.setProperty(BatchConstants.INPUT_DATA_ENCODING, encoding);
        return this;
    }

    /**
     * Specify if the first record should be skipped. If set to true, the header record is escaped from record numbering.
     * @param skipHeader true if the first record should be skipped, false else. Default to false
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder skipHeader(Boolean skipHeader) {
        properties.setProperty(BatchConstants.INPUT_DATA_SKIP_HEADER, String.valueOf(skipHeader));
        return this;
    }

    /**
     * Specify the record type : Delimiter-Separated Values (DSV) or Fixed-Length Record (FLR).
     * @param recordType Delimiter-Separated Values (DSV) or Fixed-Length Record (FLR). Default to DSV
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder recordType(RecordType recordType) {
        properties.setProperty(BatchConstants.INPUT_RECORD_TYPE, recordType.toString());
        return this;
    }

    /**
     * Set record size (number of fields).
     * @param recordSize record size
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder recordSize(int recordSize) {
        properties.setProperty(BatchConstants.INPUT_RECORD_SIZE, String.valueOf(recordSize));
        return this;
    }

    /**
     * When using DSV record type, this parameter is used to specify the field delimiter.
     * If record type is set to FLR, this parameter is ignored.
     * @param delimiter field delimiter. Default to comma ','
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder delimiter(String delimiter) {
        properties.setProperty(BatchConstants.INPUT_FIELD_DELIMITER, delimiter);
        return this;
    }

    /**
     * When using FRL record type, this parameter is used to specify fields lengths. Example: 3,5,4,10.
     * If record type is set to DSV, this parameter is ignored.
     * @param lengths array of fields lengths
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder fieldsLength(int[] lengths) {
        String lengthsList = "";
        for (int length : lengths) {
            lengthsList += length + ",";
        }
        properties.setProperty(BatchConstants.INPUT_FIELD_LENGTHS, lengthsList);
        return this;
    }

    /**
     * Specify if whitespaces should be trimmed. If record type is set to FLR, this parameter is ignored.
     * @param trim true if whitespaces should be trimmed, false else. Default to true
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder trimWhitespaces(Boolean trim) {
        properties.setProperty(BatchConstants.INPUT_FIELD_TRIM, String.valueOf(trim));
        return this;
    }

    /**
     * Specify data qualifier character. If record type is set to FLR, this parameter is ignored.
     * @param qualifier data qualifier character. Default to empty character ""
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder qualifier(String qualifier) {
        properties.setProperty(BatchConstants.INPUT_FIELD_QUALIFIER_CHAR, qualifier);
        return this;
    }

    /**
     * Set the absolute path to the log file of ignored records. If not set, the default log file will be set to
     * ${input.data.path}-ignored.log. The extension of the input data file (if any) will be removed.
     * @param ignoredRecordsLogFile absolute path to the log file of ignored records
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder ignoredRecordsLogFile(String ignoredRecordsLogFile) {
        properties.setProperty(BatchConstants.OUTPUT_DATA_IGNORED, ignoredRecordsLogFile);
        return this;
    }

    /**
     * Set the absolute path to the log file of rejected records. If not set, the default log file will be set to
     * ${input.data.path}-rejected.log. The extension of the input data file (if any) will be removed
     * @param rejectedRecordsLogFile absolute path to the log file of rejected records
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder rejectedRecordsLogFile(String rejectedRecordsLogFile) {
        properties.setProperty(BatchConstants.OUTPUT_DATA_REJECTED, rejectedRecordsLogFile);
        return this;
    }

    /**
     * Set the absolute path to the log file of error records. If not set, the default log file will be set to
     * ${input.data.path}-errors.log. The extension of the input data file (if any) will be removed
     * @param errorRecordsLogFile absolute path to the log file of error records
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder errorRecordsLogFile(String errorRecordsLogFile) {
        properties.setProperty(BatchConstants.OUTPUT_DATA_ERRORS, errorRecordsLogFile);
        return this;
    }

    /**
     * Specify the fully qualified name of the domain object class. This is mandatory if you use the default record mapper.
     * @param recordClass fully qualified name of the domain object class
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder recordClass(String recordClass) {
        properties.setProperty(BatchConstants.INPUT_RECORD_CLASS, recordClass);
        return this;
    }

    /**
     * Specify headers (comma separated) to be mapped to domain object fields in declaration order.
     * This will be used by default mapper to map record values to domain object fields.
     * This parameter is optional, if not specified, the default mapper will use the header record as basis for field mapping.
     * @param recordHeaders comma separated list of headers names.
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder recordHeaders(String recordHeaders) {
        properties.setProperty(BatchConstants.INPUT_RECORD_HEADERS, recordHeaders);
        return this;
    }

    /**
     * Specify if the engine should abort batch execution on first rejected record.
     * @param abortOnFirstReject true if the engine should abort batch execution on first reject, false else.
     *                           Default to false.
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder abortOnFirstReject(Boolean abortOnFirstReject) {
        properties.setProperty(BatchConstants.OUTPUT_DATA_ABORT_ON_FIRST_REJECT, String.valueOf(abortOnFirstReject));
        return this;
    }

    /**
     * Specify if the engine should abort batch execution on first record processed with error.
     * @param abortOnFirstError true if the engine should abort batch execution on first error, false else.
     *                           Default to false.
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder abortOnFirstError(Boolean abortOnFirstError) {
        properties.setProperty(BatchConstants.OUTPUT_DATA_ABORT_ON_FIRST_ERROR, String.valueOf(abortOnFirstError));
        return this;
    }

    /**
     * Specify if the engine should abort batch execution on first record mapping exception.
     * @param abortOnFirstMappingException true if the engine should abort batch execution on first mapping exception, false else.
     *                           Default to false.
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder abortOnFirstMappingException(Boolean abortOnFirstMappingException) {
        properties.setProperty(BatchConstants.OUTPUT_DATA_ABORT_ON_FIRST_MAPPING_EXCEPTION, String.valueOf(abortOnFirstMappingException));
        return this;
    }

    /**
     * Specify if jmx monitoring feature should be enabled. When enabled, CB4J will register a JMX MBean to get statistics about execution progress in real time.
     * @param enableJmx true if jmx monitoring feature should be enabled, false else. Default to true
     * @return a pre configured batch configuration builder instance with default values for unset optional parameters
     */
    public BatchConfigurationBuilder enableJmx(Boolean enableJmx) {
        properties.setProperty(BatchConstants.OUTPUT_DATA_JMX_ENABLED, String.valueOf(enableJmx));
        return this;
    }

    /**
     * Build a BatchConfiguration instance. Make sure you have set mandatory parameters before calling this method.
     * @return a properly configured (if mandatory parameters set) BatchConfiguration instance
     */
    public BatchConfiguration build() {
        return new BatchConfiguration(properties);
    }

}
