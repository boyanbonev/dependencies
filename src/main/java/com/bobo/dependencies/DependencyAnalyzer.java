package com.bobo.dependencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.lang3.Validate;

/**
 * A class used to analyze graph dependencies.
 *
 * @author boyan
 *
 */
public class DependencyAnalyzer {

    /**
     * Calculates all the dependencies for all nodes of a given graph using DFS.
     * Dependency of a node is all of the node to which it depends.
     *
     * @param <T>
     *
     * @param graph
     *            the graph represented as map of nodes. Each node is
     *            represented by its value and list of direct dependencies
     * @return a map with key the node and value - a set of all of its
     *         dependencies
     */
    public <T> Map<T, Set<T>> calculateDependencies(Map<T, List<T>> graph) {
        Validate.notNull(graph);
        Validate.notEmpty(graph);

        Map<T, Set<T>> result = new HashMap<T, Set<T>>(graph.size());
        for (Map.Entry<T, List<T>> entry : graph.entrySet()) {
            Set<T> dependencies = result.get(entry.getKey());
            if (dependencies == null || dependencies.isEmpty()) {
                // dependencies = calculateNodeDependencies(entry.getKey(),
                // graph, new HashSet<T>(), result);
                dependencies = calculateNodeDependenciesStack(entry.getKey(), graph);
                result.put(entry.getKey(), dependencies);
            }
        }

        return result;
    }

    /**
     * Calculate inverse dependencies for all nodes. Inverse dependencies of a
     * node are all of the nodes pointing to it.
     *
     * @param graph
     *            the graph represented as map of nodes. Each node is
     *            represented by its value and list of direct dependencies
     * @return a map with key the node and value - a set of all of its reverse
     *         dependencies
     */
    public <T> Map<T, List<T>> calculateInverseDependencies(Map<T, List<T>> graph) {
        Map<T, Set<T>> dependencies = calculateDependencies(graph);

        Map<T, List<T>> result = new HashMap<T, List<T>>(dependencies.size());
        for (T node : dependencies.keySet()) {
            List<T> invDeps = new ArrayList<T>();
            for (Map.Entry<T, Set<T>> entry : dependencies.entrySet()) {
                if (node.equals(entry.getKey())) {
                    continue;
                }
                if (entry.getValue().contains(node)) {
                    invDeps.add(entry.getKey());
                }
            }
            result.put(node, invDeps);
        }

        return result;
    }

    /**
     * This implementation is more suited for map-reduce (fork-join).
     *
     * @param node
     * @param graph
     * @param visitedNodes
     * @param result
     * @return
     */
    @SuppressWarnings("unused")
    private <T> Set<T> calculateNodeDependencies(T node, Map<T, List<T>> graph, HashSet<T> visitedNodes,
            Map<T, Set<T>> result) {
        if (visitedNodes.contains(node)) {
            // a cycle was detected, so return the path we have found so far
            return visitedNodes;
        }

        visitedNodes.add(node);
        List<T> directDependencies = graph.get(node);
        if (directDependencies == null) {
            return Collections.emptySet();
        }

        Set<T> allDepenedencies = new HashSet<T>(directDependencies);
        for (T dependency : directDependencies) {
            Set<T> deps = result.get(dependency);
            if (deps == null || deps.isEmpty()) {
                deps = calculateNodeDependencies(dependency, graph, visitedNodes, result);
            }
            allDepenedencies.addAll(deps);
        }

        result.put(node, allDepenedencies);
        return allDepenedencies;
    }

    private <T> Set<T> calculateNodeDependenciesStack(T node, Map<T, List<T>> graph) {
        Stack<T> stack = new Stack<T>();
        stack.push(node);

        Set<T> result = new HashSet<T>();
        Set<T> visitedNodes = new HashSet<T>();
        visitedNodes.add(node);
        while (!stack.isEmpty()) {
            T currentNode = stack.pop();
            List<T> directDependencies = graph.get(currentNode);
            if (directDependencies != null) {
                for (T dep : directDependencies) {
                    // no cycle
                    if (!visitedNodes.contains(dep)) {
                        stack.push(dep);
                        visitedNodes.add(dep);
                    }
                }
                result.addAll(directDependencies);
            }
        }

        return result;
    }
}
