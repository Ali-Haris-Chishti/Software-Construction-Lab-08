package graph;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Map;
import java.util.Set;

/**
 * Tests for the instance methods of the Graph interface.
 */
public abstract class GraphInstanceTest {

    // Provides an empty instance of a graph; this method must be overridden by subclasses
    protected abstract Graph<String> emptyInstance();

    // Testing strategy:
    // - testInitialVerticesEmpty(): test that a new graph has no vertices
    // - testAddVertex(): add vertices and check if they exist in the graph
    // - testAddDuplicateVertex(): add a vertex that already exists and check no duplication
    // - testSetEdge(): add edges and check if they exist with correct weights
    // - testRemoveVertex(): remove a vertex and verify it no longer exists
    // - testSourcesAndTargets(): check sources and targets for directed edges

    @Test
    public void testInitialVerticesEmpty() {
        Graph<String> graph = emptyInstance();
        assertTrue("Expected no vertices in a new graph", graph.vertices().isEmpty());
    }

    @Test
    public void testAddVertex() {
        Graph<String> graph = emptyInstance();
        assertTrue("Expected vertex to be added successfully", graph.add("A"));
        assertTrue("Graph should contain the added vertex", graph.vertices().contains("A"));
    }

    @Test
    public void testAddDuplicateVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        assertFalse("Adding duplicate vertex should return false", graph.add("A"));
        assertEquals("Graph should only have one instance of the vertex", 1, graph.vertices().size());
    }

    @Test
    public void testSetEdge() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        
        assertEquals("Expected previous weight to be 0 when adding new edge", 0, graph.set("A", "B", 5));
        assertEquals("Expected weight of the edge to be updated to 5", 5, (int) graph.targets("A").get("B"));
        
        assertEquals("Expected previous weight to be 5 when updating edge", 5, graph.set("A", "B", 10));
        assertEquals("Expected updated weight of the edge to be 10", 10, (int) graph.targets("A").get("B"));
    }

    @Test
    public void testRemoveVertex() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.set("A", "B", 5);
        
        assertTrue("Expected vertex to be removed successfully", graph.remove("A"));
        assertFalse("Graph should not contain the removed vertex", graph.vertices().contains("A"));
        assertTrue("Graph should have removed edges involving the vertex", graph.targets("A").isEmpty());
    }

    @Test
    public void testSourcesAndTargets() {
        Graph<String> graph = emptyInstance();
        graph.add("A");
        graph.add("B");
        graph.add("C");
        
        graph.set("A", "B", 5);
        graph.set("C", "B", 3);
        graph.set("A", "C", 2);

        Map<String, Integer> sources = graph.sources("B");
        assertTrue("Sources of B should contain A and C", sources.containsKey("A") && sources.containsKey("C"));
        assertEquals("Source A should have weight 5", 5, (int) sources.get("A"));
        assertEquals("Source C should have weight 3", 3, (int) sources.get("C"));

        Map<String, Integer> targets = graph.targets("A");
        assertTrue("Targets of A should contain B and C", targets.containsKey("B") && targets.containsKey("C"));
        assertEquals("Target B should have weight 5", 5, (int) targets.get("B"));
        assertEquals("Target C should have weight 2", 2, (int) targets.get("C"));
    }
}
