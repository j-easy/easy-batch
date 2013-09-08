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

package net.benas.cb4j.integration.common;

import net.benas.cb4j.core.api.RecordProcessingException;
import net.benas.cb4j.core.impl.DefaultRecordProcessorImpl;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * A processor that pre-validate Greeting POJO using Bean Validation API.
 * @author benas (md.benhassine@gmail.com)
 */
public class GreetingProcessor extends DefaultRecordProcessorImpl<Greeting> {

    private Validator validator ;

    public GreetingProcessor() {
        validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public void preProcessRecord(Greeting greeting) throws RecordProcessingException {
        //pre-validate Greeting POJO using Bean Validation API
        Set<ConstraintViolation<Greeting>> constraintViolations = validator.validate(greeting);
        if (constraintViolations.size() > 0) {
            String error  = "";
            for (ConstraintViolation<Greeting> constraintViolation : constraintViolations) {
                error += constraintViolation.getMessage();
            }
            throw new RecordProcessingException(error);
        }
    }

    @Override
    public void processRecord(Greeting greeting) throws RecordProcessingException {
        System.out.println(greeting.getGreetingMessage());
    }

}
