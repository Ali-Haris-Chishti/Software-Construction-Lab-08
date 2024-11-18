/* Copyright (c) 2015-2016 MIT 6.005 course staff, all rights reserved.
* Redistribution of original or derived work requires permission of course staff.
*/
package poet;

import static org.junit.Assert.*;

import org.junit.Test;
import java.io.File;
import java.io.IOException;

/**
 * Tests for GraphPoet.
 */
public class GraphPoetTest {
    
    //   Testing strategy
    //   File does not exist
    //   Empty file
    //   File with one word
    //   File with multiple words
    //   Input with no words
    //   Input with one word
    //   Input with multiple words
    //   Graphs with no edges
    //   Graphs with multiple edges
    //   Graphs with edge of weight > 1  
    //   Graph containing cycles

    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // TODO tests
    // Covers File does not exist
    // Graph with no edges
    @Test public void testGraphPoetFileDoesNotExist() {
        try {
            GraphPoet poet = new GraphPoet(new File("nonexistent.txt"));
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
        }
    }

    // Covers Empty file
    //      Graph with no edges
    @Test public void testGraphPoetEmptyFile() {
        try {
            GraphPoet poet = new GraphPoet(new File("src/poet/empty.txt"));
            assertEquals("This is a test.", poet.poem("This is a test."));
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        } 
    }

    // Covers File with one word
    @Test public void testGraphPoetFileWithOneWord() {
        try {
            GraphPoet poet = new GraphPoet(new File("src/poet/oneWord.txt"));
            assertEquals("This is a test.", poet.poem("This is a test."));
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    // Covers File with multiple words
    //      Input with no words
    //      Input with one word
    //      Input with multiple words
    //      Graph with multiple edges
    @Test public void testGraphPoetFileWithMultipleWords() {
        try {
            GraphPoet poet = new GraphPoet(new File("src/poet/mugar-omni-theater.txt"));
            assertEquals("Test of the system.", poet.poem("Test the system."));
            assertEquals("", poet.poem(""));
            assertEquals("Test.", poet.poem("Test."));
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }

    // Covers Graph with edge of weight > 1
    // graph with cycles
    @Test public void testGraphPoetGraphWithCycles() {
        try {
            GraphPoet poet = new GraphPoet(new File("src/poet/test.txt"));
            assertEquals("This is worth a test.", poet.poem("This is a test."));
        } catch (Exception e) {
            fail("Unexpected exception: " + e);
        }
    }
    	
}
