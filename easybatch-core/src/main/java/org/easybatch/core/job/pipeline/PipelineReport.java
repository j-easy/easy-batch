package org.easybatch.core.job.pipeline;

import java.util.ArrayList;
import java.util.List;

public class PipelineReport {

    private List<StageReport> stageReports;

    public PipelineReport() {
        stageReports = new ArrayList<>();
    }

    public PipelineReport(List<StageReport> stageReports) {
        this.stageReports = stageReports;
    }

    public void add(StageReport stageReport) {
        stageReports.add(stageReport);
    }

    public List<StageReport> getStageReports() {
        return stageReports;
    }
}
