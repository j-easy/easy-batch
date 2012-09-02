package net.benas.cb4j.tutorials.orders;

import net.benas.cb4j.core.api.BatchEngine;
import net.benas.cb4j.core.api.FieldValidator;
import net.benas.cb4j.core.config.BatchConfiguration;
import net.benas.cb4j.core.config.BatchConfigurationException;
import net.benas.cb4j.core.validator.FixedLengthNumericFieldValidator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LauncherWithCustomRecordValidator {

    public static void main(String[] args){

        BatchConfiguration batchConfiguration = null;
        try {
//            batchConfiguration = new BatchConfiguration(args[0]);
            batchConfiguration = new BatchConfiguration("D:\\dev\\benassi-projects\\cb4j\\cb4j-usecases\\cb4j-usecase1\\src\\main\\resources\\cb4j.properties");

            /*
             * Registering field validators
             */
            Map<Integer, List<FieldValidator>> fieldValidators = new HashMap<Integer, List<FieldValidator>>();

            List<FieldValidator> l1 = new ArrayList<FieldValidator>();
            l1.add(new FixedLengthNumericFieldValidator(4));
            fieldValidators.put(1, l1);

            List<FieldValidator> l2 = new ArrayList<FieldValidator>();
            l1.add(new FixedLengthNumericFieldValidator(4));
            fieldValidators.put(2, l2);

            MyCustomRecordValidator myCustomRecordValidator = new MyCustomRecordValidator(fieldValidators);
            batchConfiguration.registerRecordValidator(myCustomRecordValidator);

            /*
             * Registering record processor
             */
            batchConfiguration.registerRecordProcessor(new SalesReceiptsProcessor());
            batchConfiguration.configure();

            /*
             * Create a BatchEngine and run the batch
            */
            BatchEngine batchEngine = new SalesReceiptsBatchEngine(batchConfiguration);
            batchEngine.init();
            batchEngine.run();
            batchEngine.shutdown();
        } catch (BatchConfigurationException e) {
            System.err.println(e.getMessage());
        }

    }

}
