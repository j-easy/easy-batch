package org.easybatch.core.flow2;

import org.easybatch.core.job.Job;
import org.easybatch.core.job.JobReport;

import java.util.List;

import static org.easybatch.core.flow2.JobState.FAILURE;
import static org.easybatch.core.flow2.JobState.SUCCESS;

/**
 * Created by Sunand on 5/25/2016.
 */
public class JobVisitor implements Visitor<Job> {
  /**
   * Using the weight of the connection between 2 nodes
   * to determine the path it needs to take in the graph based on the JobReport
   * and marking the unwanted paths as visited
   *
   * @param g - the graph
   * @param v - the node
   */
  @Override
  public void visit(WorkflowGraph<Job> g, Vertex<Job> v) {
    final Job data = v.getData();
    final JobReport report = data.call();
    final boolean b = (report.getStatus() == JobState.SUCCESS.getJobStatus());
    final List<Edge<Job>> outgoingEdges = v.getOutgoingEdges();
    if(b) {
      for (Edge<Job> outgoingEdge : outgoingEdges) {
        final int cost = outgoingEdge.getCost();
        if(cost != SUCCESS.getCost()) {
          outgoingEdge.getTo().mark();
          //g.removeEdge(outgoingEdge.getFrom(), outgoingEdge.getTo());
        }
        else {
          System.out.println("Success : " + outgoingEdge.getFrom().getName());
          outgoingEdge.getTo().clearMark();
        }
      }
    }
    else {
      for (Edge<Job> outgoingEdge : outgoingEdges) {
        final int cost = outgoingEdge.getCost();
        if(cost != FAILURE.getCost()) {
          outgoingEdge.getTo().mark();
          //g.removeEdge(outgoingEdge.getFrom(), outgoingEdge.getTo());
        }
        else {
          System.out.println("Failure : " + outgoingEdge.getFrom().getName());
          outgoingEdge.getTo().clearMark();
        }
      }
    }
  }
}
