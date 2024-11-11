package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;

/**
 * An implementation of Graph.
 * 
 * <p>PS2 instructions: you MUST use the provided rep.
 */
public class ConcreteVerticesGraph implements Graph<String> {
    
    private final List<Vertex> vertices = new ArrayList<>();
    
    // Abstraction function:
    //   Represents a directed graph with a list of vertices, each containing its outgoing edges.
    // Representation invariant:
    //   No duplicate vertices in the list.
    // Safety from rep exposure:
    //   The vertices list is private and final. Public methods return unmodifiable views or copies.

    // Constructor
    public ConcreteVerticesGraph() {
        checkRep();
    }

    // Check rep invariant
    private void checkRep() {
        Set<String> seenVertices = new HashSet<>();
        for (Vertex v : vertices) {
            assert v != null;
            assert !seenVertices.contains(v.getName());
            seenVertices.add(v.getName());
        }
    }

    @Override public boolean add(String vertex) {
        for (Vertex v : vertices) {
            if (v.getName().equals(vertex)) {
                return false; // Vertex already exists
            }
        }
        vertices.add(new Vertex(vertex));
        checkRep();
        return true;
    }

    @Override public int set(String source, String target, int weight) {
        if (weight < 0) {
            throw new IllegalArgumentException("Weight must be non-negative");
        }

        Vertex sourceVertex = null;
        Vertex targetVertex = null;
        for (Vertex v : vertices) {
            if (v.getName().equals(source)) {
                sourceVertex = v;
            }
            if (v.getName().equals(target)) {
                targetVertex = v;
            }
        }

        if (sourceVertex == null) {
            sourceVertex = new Vertex(source);
            vertices.add(sourceVertex);
        }

        if (targetVertex == null) {
            targetVertex = new Vertex(target);
            vertices.add(targetVertex);
        }

        int previousWeight = sourceVertex.setEdge(target, weight);
        checkRep();
        return previousWeight;
    }

    @Override public boolean remove(String vertex) {
        Vertex toRemove = null;
        for (Vertex v : vertices) {
            if (v.getName().equals(vertex)) {
                toRemove = v;
                break;
            }
        }
        if (toRemove == null) {
            return false; // Vertex not found
        }

        vertices.remove(toRemove);
        // Remove edges in other vertices that point to the removed vertex
        for (Vertex v : vertices) {
            v.removeEdge(vertex);
        }

        checkRep();
        return true;
    }

    @Override public Set<String> vertices() {
        Set<String> vertexNames = new HashSet<>();
        for (Vertex v : vertices) {
            vertexNames.add(v.getName());
        }
        return Set.copyOf(vertexNames);
    }

    @Override public Map<String, Integer> sources(String target) {
        Map<String, Integer> sourceMap = new HashMap<>();
        for (Vertex v : vertices) {
            Integer weight = v.getEdgeWeight(target);
            if (weight != null) {
                sourceMap.put(v.getName(), weight);
            }
        }
        return sourceMap;
    }

    @Override public Map<String, Integer> targets(String source) {
        for (Vertex v : vertices) {
            if (v.getName().equals(source)) {
                return v.getTargets();
            }
        }
        return Map.of(); // Return an empty map if source is not found
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Vertex v : vertices) {
            sb.append(v.toString()).append("\n");
        }
        return sb.toString().trim();
    }
}

/**
 * Mutable.
 * This class represents a vertex in the graph and maintains its outgoing edges.
 */
class Vertex {
    
    private final String name;
    private final Map<String, Integer> edges = new HashMap<>();

    // Abstraction function:
    //   Represents a vertex with a name and a map of outgoing edges with target vertices and weights.
    // Representation invariant:
    //   All weights are positive, and no null keys or values in the edges map.
    // Safety from rep exposure:
    //   The name is final, and edges are encapsulated.

    // Constructor
    public Vertex(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Vertex name cannot be null");
        }
        this.name = name;
        checkRep();
    }

    // Check rep invariant
    private void checkRep() {
        for (Map.Entry<String, Integer> entry : edges.entrySet()) {
            assert entry.getKey() != null;
            assert entry.getValue() > 0;
        }
    }

    public String getName() {
        return name;
    }

    public int setEdge(String target, int weight) {
        if (weight == 0) {
            return edges.remove(target) != null ? edges.getOrDefault(target, 0) : 0;
        } else {
            return edges.put(target, weight) == null ? 0 : edges.get(target);
        }
    }

    public boolean removeEdge(String target) {
        return edges.remove(target) != null;
    }

    public Integer getEdgeWeight(String target) {
        return edges.get(target);
    }

    public Map<String, Integer> getTargets() {
        return new HashMap<>(edges);
    }

    @Override public String toString() {
        StringBuilder sb = new StringBuilder(name).append(" -> ");
        for (Map.Entry<String, Integer> entry : edges.entrySet()) {
            sb.append(entry.getKey()).append(" (").append(entry.getValue()).append("), ");
        }
        if (!edges.isEmpty()) {
            sb.setLength(sb.length() - 2); // Remove trailing ", "
        }
        return sb.toString();
    }
}
