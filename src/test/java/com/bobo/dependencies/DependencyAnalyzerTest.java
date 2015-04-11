package com.bobo.dependencies;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class DependencyAnalyzerTest {

    public DependencyAnalyzer analyzer = new DependencyAnalyzer();

    @Test
    public void testCalculateDependencies() throws IOException {
        Map<String, List<String>> input = getInput("input.txt");
        Map<String, Set<String>> dependencies = analyzer.calculateDependencies(input);

        Assert.assertNotNull(dependencies);
        System.out.println("\nDependencies:");
        ParserUtil.printData(dependencies);

        Map<String, Set<String>> expectedDependencies = new HashMap<String, Set<String>>();
        expectedDependencies.put("A", new HashSet<String>(Arrays.asList("B", "C", "E", "F", "G", "H")));
        expectedDependencies.put("B", new HashSet<String>(Arrays.asList("C", "E", "F", "G", "H")));
        expectedDependencies.put("C", new HashSet<String>(Arrays.asList("G")));
        expectedDependencies.put("D", new HashSet<String>(Arrays.asList("A", "B", "C", "E", "F", "G", "H")));
        expectedDependencies.put("E", new HashSet<String>(Arrays.asList("F", "H")));
        expectedDependencies.put("F", new HashSet<String>(Arrays.asList("H")));

        Assert.assertEquals(expectedDependencies, dependencies);
    }

    @Test
    public void testCalculateDependencies_CyclicDependencies() throws IOException {
        Map<String, List<String>> input = getInput("input_with_cycle.txt");
        Map<String, Set<String>> dependencies = analyzer.calculateDependencies(input);

        Assert.assertNotNull(dependencies);
        System.out.println("\nDependencies:");
        ParserUtil.printData(dependencies);

        Map<String, Set<String>> expectedDependencies = new HashMap<String, Set<String>>();
        expectedDependencies.put("a", new HashSet<String>(Arrays.asList("a", "b", "c")));
        expectedDependencies.put("b", new HashSet<String>(Arrays.asList("a", "b", "c")));

        Assert.assertEquals(expectedDependencies, dependencies);
    }

    @Test(expected = NullPointerException.class)
    public void testCalculateDependencies_NullInp() throws IOException {
        analyzer.calculateDependencies(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCalculateDependencies_EmptyInp() throws IOException {
        analyzer.calculateDependencies(new HashMap<String, List<String>>());
    }

    private Map<String, List<String>> getInput(String fileName) throws IOException {
        String filePath = ParserUtilTest.getFilePath(fileName);

        Map<String, List<String>> input = ParserUtil.readInput(filePath);
        return input;
    }

    @Test
    public void testCalculateInverseDependencies() throws IOException {
        Map<String, List<String>> input = getInput("input.txt");
        Map<String, List<String>> invDependencies = analyzer.calculateInverseDependencies(input);

        Assert.assertNotNull(invDependencies);

        System.out.println("\nInverse dependencies:");
        ParserUtil.printData(invDependencies);
    }

    @Test
    public void testCalculateInverseDependencies_CyclicDependencies() throws IOException {
        Map<String, List<String>> input = getInput("input_with_cycle.txt");
        Map<String, List<String>> invDependencies = analyzer.calculateInverseDependencies(input);

        Assert.assertNotNull(invDependencies);

        Map<String, List<String>> expectedDependencies = new HashMap<String, List<String>>();
        expectedDependencies.put("a", Arrays.asList("b"));
        expectedDependencies.put("b", Arrays.asList("a"));

        Assert.assertEquals(expectedDependencies, invDependencies);
    }

    @Test(expected = NullPointerException.class)
    public void testCalculateInverseDependencies_NullInp() throws IOException {
        analyzer.calculateInverseDependencies(null);
    }

}
