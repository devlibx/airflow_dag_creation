package com.devlibx.graph.core;

import com.google.common.base.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Deprecated
public class AirflowTraversal implements ITraversal {

    @Override
    public void travers(GraphNode root, List<String> sb) {
        String text = "";
        List<GraphNode> parents = new ArrayList<>();
        if (root.getParents() != null && root.getParents().size() == 1) {
            parents = new ArrayList<>(root.getParents());
            text = root.getId() + ".set_upstream(" + parents.get(0).getId() + ")";
        } else if (root.getParents() != null && root.getParents().size() > 1) {
            parents = new ArrayList<>(root.getParents());
            text = root.getId() + ".set_upstream([";
            text += parents.stream().map(GraphNode::getId).collect(Collectors.joining(", "));
            text += "])";
        }

        if (!Strings.isNullOrEmpty(text)) {
            sb.add(text);
        }

        parents.forEach(graphNode -> {
            travers(graphNode, sb);
        });
    }
}
