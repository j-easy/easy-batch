package org.easybatch.core.flow2;

import org.easybatch.core.job.Job;

/**
 * Created by Sunand on 5/25/2016.
 */
public class FlowDefinition {

  private final WorkflowGraph<Job> graph;

  public FlowDefinition() {
    graph = new WorkflowGraph<>();
  }

  public FlowDefinition flow(Job from, JobState state, Job to) {

    final Vertex<Job> start = getVertex(from);
    if(graph.getVertices().size() == 0) {
      graph.setRootVertex(start);
    }
    graph.addVertex(start);
    final Vertex<Job> end = getVertex(to);
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

  private Vertex<Job> getVertex(Job job) {
    final Vertex<Job> vertexByName = graph.findVertexByName(job.getName());
    if(vertexByName != null) {
      return vertexByName;
    }
    else {
      return new Vertex<>(job.getName(), job);
    }
  }
}
