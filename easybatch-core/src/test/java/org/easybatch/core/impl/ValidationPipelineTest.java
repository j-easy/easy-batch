/*
 * The MIT License
 *
 *  Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
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

package org.easybatch.core.impl;

import org.easybatch.core.api.Record;
import org.easybatch.core.api.RecordValidator;
import org.easybatch.core.api.ValidationError;
import org.easybatch.core.api.event.EventManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.HashSet;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * Test class for {@link ValidationPipeline}.
 *
 * @author Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 */
@RunWith(MockitoJUnitRunner.class)
public class ValidationPipelineTest {

    @Mock
    Record record;

    @Mock
    private RecordValidator validator1, validator2;

    @Mock
    private EventManager eventManager;

    @Mock
    ValidationError error1, error2;

    private ValidationPipeline validationPipeline;

    @Test
    @SuppressWarnings("unchecked")
    public void validateRecord_withNoErrors() throws Exception {
        when(validator1.validateRecord(record)).thenReturn(Collections.emptySet());
        when(validator2.validateRecord(record)).thenReturn(Collections.emptySet());

        validationPipeline = new ValidationPipeline(asList(validator1, validator2), eventManager);

        assertThat(validationPipeline.validateRecord(record)).isEmpty();
    }

    @Test
    @SuppressWarnings("unchecked")
    public void validateRecord_withErrors() throws Exception {
        when(validator1.validateRecord(record)).thenReturn(new HashSet<ValidationError>(Collections.singletonList(error1)));
        when(validator2.validateRecord(record)).thenReturn(new HashSet<ValidationError>(Collections.singletonList(error2)));

        validationPipeline = new ValidationPipeline(asList(validator1, validator2), eventManager);

        assertThat(validationPipeline.validateRecord(record)).containsOnly(error1);
    }
}