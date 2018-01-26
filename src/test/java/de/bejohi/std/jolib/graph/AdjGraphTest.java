package de.bejohi.std.jolib.graph;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
public final class AdjGraphTest {

    private final AdjGraph<Integer> undirectedGraph = new AdjGraph<>(false);
    private final AdjGraph<Integer> directedGraph = new AdjGraph<>(true);

    @Test(expected = NullPointerException.class)
    public void addNode_DataNull_ThrowsNullPointerException() {
        // Act and Assert
        this.undirectedGraph.addNode(null);
    }

    @Test
    public void addNode_NodeWithSameData_OneNodeInSet() {
        // Arrange
        this.undirectedGraph.addNode(100);

        // Act
        this.undirectedGraph.addNode(100);

        // Assert
        Assert.assertEquals(this.undirectedGraph.getNodes().size(), 1);
    }

    @Test
    public void addNeighboursToNode_NodeIsNew_AllNeighboursAndNodeAdded() {
        // Arrange
        final Set<Integer> neighbours = new HashSet<Integer>() {{
            add(200);
            add(300);
        }};

        // Act
        this.undirectedGraph.addNeighboursToNode(100, neighbours);

        //Assert
        Assert.assertTrue(this.undirectedGraph.containsNode(100));
        Assert.assertTrue(this.undirectedGraph.containsNode(200));
        Assert.assertTrue(this.undirectedGraph.containsNode(300));
        Assert.assertEquals(this.undirectedGraph.getNeighbours(100).size(), 2);
        Assert.assertEquals(this.undirectedGraph.getNeighbours(200).size(), 1);
        Assert.assertEquals(this.undirectedGraph.getNeighbours(300).size(), 1);
    }

    @Test
    public void addNeighboursToNode_NodeIsOld_NewNeighboursAdded() {
        // Arrange
        this.undirectedGraph.addNeighboursToNode(100, Stream.of(200, 300).collect(Collectors.toSet()));

        // Act
        this.undirectedGraph.addNeighboursToNode(100, Stream.of(400, 500).collect(Collectors.toSet()));

        // Assert
        Assert.assertEquals(this.undirectedGraph.getNeighbours(100).size(), 4);
    }

    @Test
    public void addNeighboursToNode_GraphIsDirected_ActualNodeIsNotAddedToNeighbours() {
        // Act
        this.directedGraph.addNeighboursToNode(100, Stream.of(200, 300).collect(Collectors.toSet()));

        // Assert
        Assert.assertTrue(this.directedGraph.containsNode(100));
        Assert.assertTrue(this.directedGraph.containsNode(200));
        Assert.assertTrue(this.directedGraph.containsNode(300));
        Assert.assertEquals(this.directedGraph.getNeighbours(100).size(), 2);
        Assert.assertEquals(this.directedGraph.getNeighbours(200).size(), 0);
        Assert.assertEquals(this.directedGraph.getNeighbours(300).size(), 0);
    }

    @Test
    public void containsNode_DataNull_ReturnsFalse() {
        // Arrange
        this.directedGraph.addNode(100);

        // Act
        final boolean result = this.directedGraph.containsNode(null);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void containsNode_DataIsInNode_ReturnsTrue() {
        // Arrange
        this.directedGraph.addNode(100);
        this.directedGraph.addNode(100);

        // Act
        final boolean result = this.directedGraph.containsNode(100);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void getNodes_GraphEmpty_ReturnsEmptySet() {
        // Act
        final Set<Integer> result = this.directedGraph.getNodes();

        // Assert
        Assert.assertTrue(result.isEmpty());
    }

    @Test
    public void getNodes_2NodesInGraph_ReturnsSetWithLength2() {
        // Arrange
        this.directedGraph.addNode(100);
        this.directedGraph.addNode(200);

        // Act
        final Set<Integer> result = this.directedGraph.getNodes();

        // Assert
        Assert.assertEquals(result.size(), 2);
    }

    @Test(expected = NullPointerException.class)
    public void getNeighbours_DataNull_ThrowsNullPointerException() {
        // Act and Assert
        this.directedGraph.getNeighbours(null);
    }

    @Test
    public void getNeighbours_NodeHas2Neighbours_ReturnsSetWithLength2() {
        // Arrange
        this.directedGraph.addNeighboursToNode(100, Stream.of(200, 300).collect(Collectors.toSet()));

        // Act
        final Set<Integer> result = this.directedGraph.getNeighbours(100);

        // Assert
        Assert.assertEquals(result.size(), 2);

    }

    @Test(expected = NullPointerException.class)
    public void breadthFirstSearch_StartNodeNull_ThrowsNullPointerException(){
        // Act and Assert
        this.directedGraph.breadthFirstSearch(null,100);
    }

    @Test(expected = NullPointerException.class)
    public void breadthFirstSearch_EndNodeNull_ThrowsNullPointerException(){
        // Act and Assert
        this.directedGraph.breadthFirstSearch(100,null);
    }

    @Test
    public void breadthFirstSearch_GraphEmpty_ReturnsFalse(){
        // Act
        final boolean result = this.directedGraph.breadthFirstSearch(100,200);

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void breadthFirstSearch_NodesAreEqual_ReturnsTrue(){
        // Arrange
        this.directedGraph.addNode(100);
        this.directedGraph.addNode(200);

        // Act
        final boolean result = this.directedGraph.breadthFirstSearch(100,100);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void breadthFirstSearch_NodesAreDirectNeighbours_ReturnsTrue(){
        // Arrange
        this.directedGraph.addNeighboursToNode(100,Stream.of(200).collect(Collectors.toSet()));

        // Act
        final boolean result = this.directedGraph.breadthFirstSearch(100,200);

        // Assert
        Assert.assertTrue(result);

    }

    @Test
    public void breadthFirstSearch_DirectedGraphNodeIsNotNeigbour_ReturnsFalse(){
        // Arrange
        this.directedGraph.addNeighboursToNode(100,Stream.of(200).collect(Collectors.toSet()));

        // Act
        final boolean result = this.directedGraph.breadthFirstSearch(200,100);

        // Assert
        Assert.assertFalse(result);

    }

    @Test
    public void breadthFirstSearch_NodeIsReachableOver2Steps_ReturnsTrue(){
        // Arrange
        this.directedGraph.addNeighboursToNode(100,Stream.of(200,300,400).collect(Collectors.toSet()));
        this.directedGraph.addNeighboursToNode(300,Stream.of(400,500,100).collect(Collectors.toSet()));
        this.directedGraph.addNeighboursToNode(500,Stream.of(600).collect(Collectors.toSet()));

        // Act
        final boolean result = this.directedGraph.breadthFirstSearch(500,600);

        // Assert
        Assert.assertTrue(result);
    }

    @Test(expected = NullPointerException.class)
    public void removeNode_NodeIsNull_ThrowsNullPointerException(){
        // Act and Assert
        this.directedGraph.removeNode(null);
    }

    @Test
    public void removeNode_NodeIsNotInGraph_NoExceptionThrown(){
        // Act and Assert
        this.directedGraph.removeNode(100);
    }

    @Test
    public void removeNode_NodeHasNoNeighbours_NodeIsRemoved(){
        // Arrange
        this.directedGraph.addNode(100);

        // Act
        this.directedGraph.removeNode(100);

        // Assert
        Assert.assertTrue(this.directedGraph.getNodes().isEmpty());
    }

    @Test
    public void removeNode_NodeHasNeighbours_NodeIsAlsoRemovedAsNeighbour(){
        // Arrange
        this.directedGraph.addNeighboursToNode(100,Stream.of(200,300).collect(Collectors.toSet()));

        // Act
        this.directedGraph.removeNode(200);

        // Assert
        Assert.assertEquals(2,this.directedGraph.getNodes().size());
        Assert.assertEquals(1,this.directedGraph.getNeighbours(100).size());
    }

    @Test
    public void cloneGraph_GraphIsEmpty_ReturnsEmptyGraph(){
        // Act
        final AdjGraph<Integer> newGraph = this.directedGraph.cloneGraph();

        // Assert
        Assert.assertTrue(newGraph.getNodes().isEmpty());

    }

    @Test
    public void cloneGraph_GraphWithNeighbourNodes_NeighboursAreAlsoInNewGraph(){
        this.directedGraph.addNeighboursToNode(100,Stream.of(200,300).collect(Collectors.toSet()));
        this.directedGraph.addNeighboursToNode(200,Stream.of(400,500).collect(Collectors.toSet()));

        // Act
        final AdjGraph<Integer> newGraph = this.directedGraph.cloneGraph();

        // Assert
        Assert.assertEquals(5,newGraph.getNodes().size());
        Assert.assertEquals(2,newGraph.getNeighbours(200).size());
        Assert.assertEquals(0,newGraph.getNeighbours(500).size());

    }
}

