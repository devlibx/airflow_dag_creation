package com.devlibx.graph.core;

import com.devlibx.graph.domain.Node;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
public class GraphNode {
    public static final boolean ADD_PARENTS = false;
    private String id;
    private Node node;
    private Set<GraphNode> vertices;
    private Set<GraphNode> parents;

    GraphNode(Node node) {
        this(node.getId(), node);
    }

    GraphNode(String id, Node node) {
        this.id = id;
        this.node = node;
        this.vertices = new HashSet<>();
        this.parents = new HashSet<>();
    }

    public void addParent(GraphNode parent) {
        if (ADD_PARENTS) {
            parents.add(parent);
        }
    }

    public void addChild(GraphNode child) {
        vertices.add(child);
    }

    public boolean dependsOn() {
        return node != null && node.checkDependsOn();
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        try {
            return "Id=" + id +
                    (ADD_PARENTS ? " parents=" + parents.stream().map(GraphNode::getId).collect((Collectors.toList())) : "") +
                    " child=" + vertices.stream().map(GraphNode::getId).collect((Collectors.toList()));
        } catch (Exception e) {
            return "Id=" + id;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GraphNode graphNode = (GraphNode) o;
        return id.equals(graphNode.id);
    }
}
