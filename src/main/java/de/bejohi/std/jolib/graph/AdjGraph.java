package de.bejohi.std.jolib.graph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An implementation of the JoGraph which uses an adjacency-matrix for representing the graph structure.
 *
 * @param <T>
 */
public class AdjGraph<T extends Comparable<T>> implements JoGraph<T> {
    private final Set<Node<T>> nodeSet;
    private final boolean directed;

    public AdjGraph(final boolean directed) {
        this.directed = directed;
        nodeSet = new HashSet<>();
    }

    @Override
    public boolean containsNode(final T data) {
        return this.getNode(data) != null;
    }

    @Override
    public Set<T> getNodes() {
        return nodeSet.stream().map(Node::getData).collect(Collectors.toSet());
    }

    @Override
    public void addNode(final T data) {
        this.addNodeAndReturnIt(data);
    }

    @Override
    public Set<T> getNeighbours(final T nodeData) {
        Objects.requireNonNull(nodeData);

        final Node<T> node = this.getNode(nodeData);
        if (node == null) {
            return Collections.emptySet();
        }

        return node.getNeighbours().stream().map(Node::getData).collect(Collectors.toSet());
    }

    @Override
    public void removeNode(final T nodeData) {
        Objects.requireNonNull(nodeData);

        Node nodeToRemove = this.getNode(nodeData);
        if (nodeToRemove == null) {
            return;
        }
        for (final Node<T> outerNode : this.nodeSet) {
            outerNode.removeNode(nodeToRemove);
        }

        this.nodeSet.remove(nodeToRemove);
    }

    @Override
    public void removeNodeNeighbours(final T nodeData){
        Objects.requireNonNull(nodeData);

        Node node = this.getNode(nodeData);
        if (node == null) {
            return;
        }

        node.removeAllNeighbours();

        if(!this.directed){
            for (final Node<T> outerNode : this.nodeSet) {
                outerNode.removeNode(node);
            }
        }
    }

    @Override
    public void addNeighboursToNode(final T data, final Set<T> neighbours) {
        Objects.requireNonNull(data);
        Objects.requireNonNull(neighbours);

        final Node<T> node = addNodeAndReturnIt(data);

        for (final T neighbourData : neighbours) {
            if (neighbourData == null || neighbourData.equals(data)) {
                continue;
            }
            Node<T> neighbourNode = this.getNode(neighbourData);
            if (neighbourNode == null) {
                neighbourNode = this.addNodeAndReturnIt(neighbourData);
            }
            node.addNeighbour(neighbourNode);
            if (!this.directed) {
                neighbourNode.addNeighbour(node);
            }
        }
    }

    public boolean breadthFirstSearch(final T startNodeData, final T endNodeData) {
        Objects.requireNonNull(startNodeData);
        Objects.requireNonNull(endNodeData);

        Node<T> startNode = this.getNode(startNodeData);
        Node<T> endNode = this.getNode(endNodeData);

        if (startNode == null || endNode == null) {
            return false;
        }

        this.setAllNodesToNotVisited();
        startNode.setVisited(true);

        Queue<Node<T>> queue = new PriorityQueue<>();
        queue.add(startNode);
        while (!queue.isEmpty()) {
            Node<T> currentNode = queue.poll();
            if (currentNode == endNode) {
                return true;
            }
            for (final Node<T> neigbour : currentNode.getNeighbours()) {
                if (!neigbour.isVisited()) {
                    queue.add(neigbour);
                    neigbour.setVisited(true);
                }
            }
        }
        return false;
    }

    @Override
    public AdjGraph<T> cloneGraph() {
        AdjGraph<T> newGraph = new AdjGraph<>(this.directed);

        for(final Node<T> node : this.nodeSet){
            newGraph.addNeighboursToNode(node.data,node.getNeighbours()
                    .stream().map(Node::getData).collect(Collectors.toSet()));
        }

        return newGraph;
    }

    private void setAllNodesToNotVisited() {
        for (final Node<T> node : this.nodeSet) {
            node.setVisited(false);
        }
    }

    private Node<T> addNodeAndReturnIt(final T data) {
        Objects.requireNonNull(data);

        Node<T> node = this.getNode(data);
        if (node != null) {
            return node;
        }
        node = new Node<>(data);
        this.nodeSet.add(node);
        return node;
    }

    private Node<T> getNode(final T data) {
        if (data == null) {
            return null;
        }

        for (final Node<T> node : this.nodeSet) {
            if (node.getData().equals(data)) {
                return node;
            }
        }
        return null;
    }

    private static class Node<T extends Comparable<T>> implements Comparable<T> {
        private final T data;
        private Set<Node<T>> neighbours;
        private boolean visited;

        private Node(final T data) {
            this.data = data;
            this.neighbours = new HashSet<>();
        }

        private boolean addNeighbour(final Node<T> newNeighbour) {
            return this.neighbours.add(newNeighbour);
        }

        private Set<Node<T>> getNeighbours() {
            return neighbours;
        }

        T getData() {
            return data;
        }

        private boolean isVisited() {
            return visited;
        }

        private void setVisited(final boolean visited) {
            this.visited = visited;
        }

        private void removeNode(final Node<T> node) {
            this.neighbours.remove(node);
        }

        private void removeAllNeighbours(){
            this.neighbours.clear();
        }


        @Override
        public int compareTo(T o) {
            return this.data.compareTo(o);
        }
    }
}
