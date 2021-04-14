package com.devlibx.graph.core;

import com.devlibx.graph.domain.Graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GraphInstance {
    public Map<String, GraphNode> nodes = new HashMap<>();
    public GraphNode startNode;
    public GraphNode endNode;

    public void initGraph(Graph graph) {
        graph.getSource().forEach(node -> {
            GraphNode graphNode = new GraphNode(node);
            nodes.put(graphNode.getId(), graphNode);
        });

        graph.getTransformation().forEach(node -> {
            GraphNode graphNode = new GraphNode(node);
            nodes.put(graphNode.getId(), graphNode);
        });

        graph.getSink().forEach(node -> {
            GraphNode graphNode = new GraphNode(node);
            nodes.put(graphNode.getId(), graphNode);
        });

        startNode = new GraphNode(graph.getStart());
        nodes.put(startNode.getId(), startNode);

        endNode = new GraphNode(graph.getEnd());
        nodes.put(endNode.getId(), endNode);

        // Setup Graph
        setupGraph();
    }

    public void setupGraph() {
        nodes.forEach((nodeId, graphNode) -> {

            // This does not depend on anything so nothing to be done
            if (!graphNode.dependsOn()) return;

            // Handle all parents
            graphNode.getNode().getDependsOn().stream()
                    .map(parentId -> nodes.get(parentId))
                    .forEach(parentNode -> {
                        parentNode.addChild(graphNode);
                        graphNode.addParent(parentNode);
                    });
        });
    }

    public List<String> travers(ITraversal traversal) {
        List<String> result = new ArrayList<>();
        traversal.travers(endNode, result);
        return result;
    }
}
