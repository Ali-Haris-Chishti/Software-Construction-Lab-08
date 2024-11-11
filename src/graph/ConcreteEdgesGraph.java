package graph;

import java.util.*;
import java.util.stream.Collectors;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteEdgesGraph implements Graph<String> {
    
    private final Set<String> vertices = new HashSet<>();
    private final List<Edge> edges = new ArrayList<>();
    
    // Abstraction function:
    //   Represents a directed graph with vertices and directed, weighted edges.
    // Representation invariant:
    //   vertices != null, edges != null, all edges should have valid vertices from the `vertices` set.
    // Safety from rep exposure:
    //   vertices and edges are private and final. Defensive copying is used in methods that return collections.
    
    public ConcreteEdgesGraph() {
        checkRep();
    }

    // Ensures the representation invariant holds.
    private void checkRep() {
        assert vertices != null;
        assert edges != null;
        for (Edge edge : edges) {
            assert vertices.contains(edge.getSource());
            assert vertices.contains(edge.getTarget());
        }
    }

    @Override
    public boolean add(String vertex) {
        if (vertex == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }
        boolean isAdded = vertices.add(vertex);
        checkRep();
        return isAdded;
    }
    
    @Override
    public int set(String source, String target, int weight) {
        if (source == null || target == null || weight < 0) {
            throw new IllegalArgumentException("Invalid arguments for edge");
        }
        add(source);
        add(target);

        Optional<Edge> existingEdge = edges.stream()
                                           .filter(edge -> edge.getSource().equals(source) && edge.getTarget().equals(target))
                                           .findFirst();
        int previousWeight = existingEdge.map(Edge::getWeight).orElse(0);

        if (weight == 0) {
            existingEdge.ifPresent(edges::remove);
        } else {
            if (existingEdge.isPresent()) {
                edges.remove(existingEdge.get());
            }
            edges.add(new Edge(source, target, weight));
        }

        checkRep();
        return previousWeight;
    }
    
    @Override
    public boolean remove(String vertex) {
        if (!vertices.contains(vertex)) {
            return false;
        }
        vertices.remove(vertex);
        edges.removeIf(edge -> edge.getSource().equals(vertex) || edge.getTarget().equals(vertex));
        checkRep();
        return true;
    }
    
    @Override
    public Set<String> vertices() {
        return Collections.unmodifiableSet(vertices);
    }
    
    @Override
    public Map<String, Integer> sources(String target) {
        Map<String, Integer> sources = edges.stream()
                                            .filter(edge -> edge.getTarget().equals(target))
                                            .collect(Collectors.toMap(Edge::getSource, Edge::getWeight));
        return Collections.unmodifiableMap(sources);
    }
    
    @Override
    public Map<String, Integer> targets(String source) {
        Map<String, Integer> targets = edges.stream()
                                            .filter(edge -> edge.getSource().equals(source))
                                            .collect(Collectors.toMap(Edge::getTarget, Edge::getWeight));
        return Collections.unmodifiableMap(targets);
    }
    
    @Override
    public String toString() {
        return "Vertices: " + vertices + "\nEdges: " + edges;
    }
}

/**
 * Immutable class representing an edge in the graph.
 */
class Edge {
    
    private final String source;
    private final String target;
    private final int weight;
    
    // Abstraction function:
    //   Represents a directed, weighted edge from source to target with the given weight.
    // Representation invariant:
    //   source != null, target != null, weight >= 0.
    // Safety from rep exposure:
    //   Fields are private, final, and immutable.
    
    public Edge(String source, String target, int weight) {
        if (source == null || target == null || weight < 0) {
            throw new IllegalArgumentException("Source and target must be non-null and weight must be non-negative");
        }
        this.source = source;
        this.target = target;
        this.weight = weight;
        checkRep();
    }
    
    // Ensures the representation invariant holds.
    private void checkRep() {
        assert source != null;
        assert target != null;
        assert weight >= 0;
    }
    
    public String getSource() {
        return source;
    }
    
    public String getTarget() {
        return target;
    }
    
    public int getWeight() {
        return weight;
    }
    
    @Override
    public String toString() {
        return source + " -> " + target + " (" + weight + ")";
    }
}


