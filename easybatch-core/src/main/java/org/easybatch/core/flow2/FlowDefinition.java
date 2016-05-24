package org.easybatch.core.flow2;

import org.easybatch.core.job.Job;

/**
 * Created by Sunand on 5/25/2016.
 */
public class FlowDefinition {

  WorkflowGraph<Job> graph;
  public FlowDefinition() {
    graph = new WorkflowGraph<>();
  }

  public FlowDefinition flow(Job from, JobState state, Job to) {

    final Vertex<Job> start = (Vertex<Job>) new Vertex<>(from.getName(), from);
    if(graph.getVertices().size() == 0) {
      graph.setRootVertex(start);
    }
    graph.addVertex(start);
    final Vertex<Job> end = (Vertex<Job>) new Vertex<>(to.getName(), to);
    graph.addVertex(end);
    graph.addEdge(start, end, state.getCost());
    return this;
  }

  public WorkflowGraph<Job> build() {
    final Edge<Job>[] cycles = graph.findCycles();
    //Need to make sure that this is a DAG
    if (cycles.length > 0) {
      System.out.println("Found cycles in job : " + cycles);
      System.exit(0);
    }
    return graph;
  }


}
