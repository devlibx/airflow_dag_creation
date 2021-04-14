package com.devlibx.graph.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class Node {
    private String id;
    private String type;
    private Map<String, Object> config;
    private List<String> dependsOn;

    public boolean checkDependsOn() {
        return dependsOn != null && !dependsOn.isEmpty();
    }
}
