package org.easybatch.core.flow2;

/**
 * A graph visitor interface.
 *
 */
interface Visitor<T> {
  /**
   * Called by the graph traversal methods when a vertex is first visited.
   *
   * @param g -
   *          the graph
   * @param v -
   *          the vertex being visited.
   */
  void visit(WorkflowGraph<T> g, Vertex<T> v);
}
