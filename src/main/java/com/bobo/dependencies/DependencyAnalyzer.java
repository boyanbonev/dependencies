package com.bobo.dependencies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
     *            the graph represented as node with list of direct dependencies
     * @return a map with key the node and value - a set of all of its
     *         dependencies
     */
    public <Ò, T> Map<T, Set<T>> calculateDependencies(Map<T, List<T>> graph) {
        Validate.notNull(graph);
        Validate.notEmpty(graph);

        Map<T, Set<T>> result = new HashMap<T, Set<T>>(graph.size());
        for (Map.Entry<T, List<T>> entry : graph.entrySet()) {
            Set<T> dependencies = calculateNodeDependencies(entry.getKey(), graph, new HashSet<T>());
            result.put(entry.getKey(), dependencies);
        }

        return result;
    }

    /**
     * Calculate inverse dependencies for all nodes. Inverse dependencies of a
     * node are all of the nodes pointing to it.
     *
     * @param graph
     *            the graph represented as node with list of direct dependencies
     * @return a map with key the node and value - a set of all of its reverse
     *         dependencies
     */
    public <Ò, T> Map<T, List<T>> calculateInverseDependencies(Map<T, List<T>> graph) {
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

    private <Ò, T> Set<T> calculateNodeDependencies(T node, Map<T, List<T>> graph, HashSet<T> visitedNodes) {
        if (visitedNodes.contains(node)) {
            // a cycle was detected
            return Collections.emptySet();
        }

        visitedNodes.add(node);
        List<T> directDependencies = graph.get(node);
        if (directDependencies == null) {
            return Collections.emptySet();
        }

        Set<T> result = new HashSet<T>(directDependencies);
        for (T dependency : directDependencies) {
            Set<T> deps = calculateNodeDependencies(dependency, graph, visitedNodes);
            result.addAll(deps);
        }

        return result;
    }

}
