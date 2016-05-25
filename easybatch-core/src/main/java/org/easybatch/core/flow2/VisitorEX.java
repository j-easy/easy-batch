package org.easybatch.core.flow2;

/**
 * A graph visitor interface that can throw an exception during a visit
 * callback.
 *
 */
interface VisitorEX<T, E extends Exception> {
  /**
   * Called by the graph traversal methods when a vertex is first visited.
   *
   * @param g -
   *          the graph
   * @param v -
   *          the vertex being visited.
   * @throws E
   *           exception for any error
   */
  void visit(WorkflowGraph<T> g, Vertex<T> v) throws E;
}
