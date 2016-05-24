package org.easybatch.core.flow2;

/**
 * A spanning tree visitor callback interface
 *
 * @see WorkflowGraph#dfsSpanningTree(Vertex, DFSVisitor)
 *
 */
interface DFSVisitor<T> {
  /**
   * Called by the graph traversal methods when a vertex is first visited.
   *
   * @param g -
   *          the graph
   * @param v -
   *          the vertex being visited.
   */
  public void visit(WorkflowGraph<T> g, Vertex<T> v);

  /**
   * Used dfsSpanningTree to notify the visitor of each outgoing edge to an
   * unvisited vertex.
   *
   * @param g -
   *          the graph
   * @param v -
   *          the vertex being visited
   * @param e -
   *          the outgoing edge from v
   */
  public void visit(WorkflowGraph<T> g, Vertex<T> v, Edge<T> e);
}