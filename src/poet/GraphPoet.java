/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package poet;

import graph.ConcreteEdgesGraph;
import graph.Graph;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Map;

/**
 * A graph-based poetry generator.
 * 
 * <p>GraphPoet is initialized with a corpus of text, which it uses to derive a
 * word affinity graph.
 * Vertices in the graph are words. Words are defined as non-empty
 * case-insensitive strings of non-space non-newline characters. They are
 * delimited in the corpus by spaces, newlines, or the ends of the file.
 * Edges in the graph count adjacencies: the number of times "w1" is followed by
 * "w2" in the corpus is the weight of the edge from w1 to w2.
 * 
 * <p>For example, given this corpus:
 * <pre>    Hello, HELLO, hello, goodbye!    </pre>
 * <p>the graph would contain two edges:
 * <ul><li> ("hello,") -> ("hello,")   with weight 2
 *     <li> ("hello,") -> ("goodbye!") with weight 1 </ul>
 * <p>where the vertices represent case-insensitive {@code "hello,"} and
 * {@code "goodbye!"}.
 * 
 * <p>Given an input string, GraphPoet generates a poem by attempting to
 * insert a bridge word between every adjacent pair of words in the input.
 * The bridge word between input words "w1" and "w2" will be some "b" such that
 * w1 -> b -> w2 is a two-edge-long path with maximum-weight weight among all
 * the two-edge-long paths from w1 to w2 in the affinity graph.
 * If there are no such paths, no bridge word is inserted.
 * In the output poem, input words retain their original case, while bridge
 * words are lower case. The whitespace between every word in the poem is a
 * single space.
 * 
 * <p>For example, given this corpus:
 * <pre>    This is a test of the Mugar Omni Theater sound system.    </pre>
 * <p>on this input:
 * <pre>    Test the system.    </pre>
 * <p>the output poem would be:
 * <pre>    Test of the system.    </pre>
 * 
 * <p>PS2 instructions: this is a required ADT class, and you MUST NOT weaken
 * the required specifications. However, you MAY strengthen the specifications
 * and you MAY add additional methods.
 * You MUST use Graph in your rep, but otherwise the implementation of this
 * class is up to you.
 */
public class GraphPoet {
    
    private final Graph<String> graph;
    
    // Abstraction function:
    //   AF(graph) = A word affinity graph where each vertex represents a unique, 
    //   case-insensitive word from the corpus, and each directed edge (w1 -> w2) 
    //   has a weight equal to the number of times word w2 follows word w1 in the corpus.
    //
    // Representation invariant:
    //   - All vertices in the graph are non-empty, lowercase strings.
    //   - All edge weights are positive integers.
    //   - The graph accurately reflects the adjacency counts of words in the corpus.
    //
    // Safety from rep exposure:
    //   - The graph is declared as private and final, preventing external modification.
    //   - All methods that expose parts of the graph return copies or immutable views.
    //   - The class does not provide any methods that allow external entities to modify
    //     the internal graph directly.
    
    /**
     * Create a new poet with the graph from corpus (as described above).
     * 
     * @param corpus text file from which to derive the poet's affinity graph
     * @throws IOException if the corpus file cannot be found or read
     */
    public GraphPoet(File corpus) throws IOException {
        // Parse the corpus file and build the affinity graph
        // Separate the words on whitespace
        // Count the number of times each word follows another
        // Add the counts as edge weights to the graph
        // Also convert words to lowercase
        this.graph = new ConcreteEdgesGraph();
        List<String> words = Files.lines(corpus.toPath())
                .flatMap(line -> List.of(line.split("\\s+")).stream())
                .map(String::toLowerCase)
                .toList();
        for (int i = 0; i < words.size() - 1; i++) {
            String source = words.get(i);
            String target = words.get(i + 1);
            // Get previous weight
            int weight = graph.targets(source).getOrDefault(target, 0) + 1;
            // Set the weight
            graph.set(source, target, weight);
        }
        checkRep();
    }
    
    // TODO checkRep
    /**
     * Checks the representation invariant of the class.
     * @throws AssertionError if the representation invariant is violated.
     */
    private void checkRep() {
        // The graph should have only positive edge weights
        for (String vertex : graph.vertices()) {
            for (String target : graph.targets(vertex).keySet()) {
                assert graph.targets(vertex).get(target) > 0;
            }
        }
        // All vertices should be lowercase, and not contain white space(i.e. tab, space or newline)
        // vertices should not be empty
        for (String vertex : graph.vertices()) {
            assert vertex.equals(vertex.toLowerCase());
            assert !vertex.contains(" ");
            assert !vertex.contains("\t");
            assert !vertex.contains("\n");
            assert !vertex.isEmpty();
        }
    }
    
    /**
     * Generate a poem.
     * 
     * @param input string from which to create the poem
     * @return poem (as described above)
     */
    public String poem(String input) {
        // Separate the input into words
        // For each pair of adjacent words, find the bridge word with the highest weight
        // Insert the bridge word between the adjacent words
        // Return the poem
        String[] words = input.split("\\s+");
        StringBuilder poem = new StringBuilder();
        for (int i = 0; i < words.length - 1; i++) {
            String source = words[i].toLowerCase();
            String target = words[i + 1].toLowerCase();
            // Find the bridge word with the maximum combined weight
            String bestBridge = null;
            int maxWeight = 0;

            for (Map.Entry<String, Integer> entry : graph.targets(source).entrySet()) {
                String bridge = entry.getKey();
                int sourceToBridgeWeight = entry.getValue();
                int bridgeToTargetWeight = graph.targets(bridge).getOrDefault(target, 0);

                int combinedWeight = sourceToBridgeWeight + bridgeToTargetWeight;
                if (bridgeToTargetWeight > 0 && combinedWeight > maxWeight) {
                    bestBridge = bridge;
                    maxWeight = combinedWeight;
                }
            }

            // Append the source word and the bridge word (if found) to the poem
            poem.append(words[i]).append(" ");
            if (bestBridge != null) {
                poem.append(bestBridge).append(" ");
            }
        }

        // Append the last word to the poem
        poem.append(words[words.length - 1]);
        return poem.toString().trim();
    }
    
    // TODO toString()
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("GraphPoet Affinity Graph:\n");
        for (String vertex : graph.vertices()) {
            sb.append(vertex).append(" -> ");
            sb.append(graph.targets(vertex).toString());
            sb.append("\n");
        }
        return sb.toString();
    }
}
