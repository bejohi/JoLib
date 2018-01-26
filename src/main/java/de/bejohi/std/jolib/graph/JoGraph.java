package de.bejohi.std.jolib.graph;

import java.util.Set;

/**
 * JoGraph represents a simple graph structure. Speed and Performance are not important values for this representation.
 * But the data integrity and the simple usability are important. The graph can be directed or undirected. The
 * internal data structure implementation (e.g. Adjacency-Matrix, Adjacency-List) is not visible for the interface
 * client.
 *
 * @param <T> T can be every object type which implements the interface Comparable.
 */
public interface JoGraph<T extends Comparable<T>> {
    /**
     * @param nodeData the data which the node should store.
     * @return a set of all neighbour-data of the node which stores the given data. In case no node is found an empty
     * set will be returned.
     * @throws NullPointerException in case nodeData == null
     */
    Set<T> getNeighbours(final T nodeData);

    /**
     * @return a set of all node-data stored in the graph. If the graph is empty and empty set will be returned.
     */
    Set<T> getNodes();

    /**
     * @param nodeData the data to search for.
     * @return true if a node was found, false otherwise.
     */
    boolean containsNode(final T nodeData);

    /**
     * Removes the given node from the graph.
     *
     * @param nodeData the node to remove.
     * @throws NullPointerException in case nodeData == null.
     */
    void removeNode(T nodeData);

    /**
     * Adds a node and a list of its neighbours. If the node is not already stored in the graph it will be created.
     * All Neighbours will be stored as neighbours of the node. If one of the neighbours is not already stored
     * it will also be created. If the graph is a undirected graph the given node will also be stored as neighbour
     * in all neighbour nodes.
     *
     * @param nodeData   the node, where neighbours should be added. It must not already be stored in the graph.
     * @param neighbours a list of neighbour nodes. All nodes should be != null but it is not necessary that
     *                   they are already stored in the graph.
     * @throws NullPointerException if nodeData, or neighbours == null.
     */
    void addNeighboursToNode(final T nodeData, final Set<T> neighbours);

    /**
     * Adds a node to the graph. If the node is already stored, nothing will happen.
     *
     * @param nodeData the nodeData to store as a graph node.
     * @throws NullPointerException in case nodeData == null.
     */
    void addNode(final T nodeData);

    /**
     * Runs an breath first search on the graph.
     *
     * @param startNodeData the start node.
     * @param endNodeData   the target node.
     * @return true if there is a connection between the two given nodes, otherwise false.
     * @throws NullPointerException in case startNodeData or endNodeData == null.
     */
    boolean breadthFirstSearch(final T startNodeData, final T endNodeData);
}
