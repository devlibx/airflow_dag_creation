package com.devlibx.graph.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AirflowTraversalV2 implements ITraversal {

    @Override
    public void travers(GraphNode root, List<String> sb) {
        internalTraversal(root, sb);
        Set<String> finalResult = new HashSet<>(sb);
        sb.clear();
        sb.addAll(finalResult);
    }

    private void internalTraversal(GraphNode root, List<String> sb) {
        if (root.getVertices() != null && !root.getVertices().isEmpty()) {

            StringBuilder builder = new StringBuilder();
            List<GraphNode> vertices = new ArrayList<>(root.getVertices());
            if (vertices.size() == 1) {
                builder.append(root.getId()).append(" >> ").append(vertices.get(0).getId());
            } else if (vertices.size() > 1) {
                builder.append(root.getId()).append(" >> ").append("[");
                builder.append(vertices.stream().map(GraphNode::getId).collect(Collectors.joining(", ")));
                builder.append("]");
            }
            sb.add(builder.toString());

            for (GraphNode gn : root.getVertices()) {
                internalTraversal(gn, sb);
            }
        }
    }
}
