/*
 *  The MIT License
 *
 *   Copyright (c) 2015, Mahmoud Ben Hassine (mahmoud@benhassine.fr)
 *
 *   Permission is hereby granted, free of charge, to any person obtaining a copy
 *   of this software and associated documentation files (the "Software"), to deal
 *   in the Software without restriction, including without limitation the rights
 *   to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *   copies of the Software, and to permit persons to whom the Software is
 *   furnished to do so, subject to the following conditions:
 *
 *   The above copyright notice and this permission notice shall be included in
 *   all copies or substantial portions of the Software.
 *
 *   THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *   IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *   FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *   AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *   LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *   OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *   THE SOFTWARE.
 */

package org.easybatch.extensions.msexcel;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.easybatch.core.job.*;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;

import static org.assertj.core.api.Assertions.assertThat;

public class MsExcelSupportIntegrationTest {

    public static final String SHEET_NAME = "tweets";

    @Test
    public void integrationTest() throws Exception {

        File inputTweets = new File(this.getClass().getResource("/tweets-in.xlsx").toURI());
        File outputTweets = new File(this.getClass().getResource("/tweets-out.xlsx").toURI());

        Job job = JobBuilder.aNewJob()
                .reader(new MsExcelRecordReader(inputTweets))
                .mapper(new MsExcelRecordMapper(Tweet.class, "id", "user", "message"))
                .marshaller(new MsExcelRecordMarshaller<>(Tweet.class, "id", "user", "message"))
                .writer(new MsExcelRecordWriter(outputTweets, SHEET_NAME))
                .build();

        JobReport report = JobExecutor.execute(job);

        assertThat(report).isNotNull();
        assertThat(report.getMetrics().getTotalCount()).isEqualTo(2);
        assertThat(report.getMetrics().getSuccessCount()).isEqualTo(2);
        assertThat(report.getStatus()).isEqualTo(JobStatus.COMPLETED);

        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(outputTweets));
        XSSFSheet sheet = workbook.getSheet(SHEET_NAME);
        XSSFRow row = sheet.getRow(1);
        assertThat(row.getCell(0).getNumericCellValue()).isEqualTo(1.0);
        assertThat(row.getCell(1).getStringCellValue()).isEqualTo("foo");
        assertThat(row.getCell(2).getStringCellValue()).isEqualTo("hi");
        row = sheet.getRow(2);
        assertThat(row.getCell(0).getNumericCellValue()).isEqualTo(2.0);
        assertThat(row.getCell(1).getStringCellValue()).isEqualTo("bar");
        assertThat(row.getCell(2).getStringCellValue()).isEqualTo("hello");
    }

}
