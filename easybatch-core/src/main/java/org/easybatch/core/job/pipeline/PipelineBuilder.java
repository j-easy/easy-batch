package org.easybatch.core.job.pipeline;

import java.util.ArrayList;
import java.util.List;

public class PipelineBuilder {

    private List<Stage> stages;

    public PipelineBuilder() {
        stages = new ArrayList<>();
    }

    public static PipelineBuilder aNewPipelineBuilder() {
        return new PipelineBuilder();
    }

    public PipelineBuilder stage(Stage stage) {
        stages.add(stage);
        return this;
    }

    public Pipeline build() {
        return new Pipeline(stages);
    }
}
