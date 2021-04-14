package com.devlibx.graph.core;

import com.devlibx.graph.domain.Graph;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

public class GraphInstanceTest extends TestCase {
    private String graphJson;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testReadFile() throws IOException {
        String fileName = "graph.json";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        String content = IOUtils.toString(file.toURI(), Charset.defaultCharset());
        assertNotNull(content);
    }

    public void testGraph() throws Exception {
        // Read file
        String fileName = "graph.json";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        String content = IOUtils.toString(file.toURI(), Charset.defaultCharset());
        assertNotNull(content);

        // Read Graph from file
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        Graph graph = objectMapper.readValue(content, Graph.class);
        assertNotNull(graph);

        // Setup graph
        GraphInstance graphInstance = new GraphInstance();
        graphInstance.initGraph(graph);

        List<String> result = graphInstance.travers(new AirflowTraversal());
        assertNotNull(result);
        assertTrue(result.size() > 0);
        result.forEach(System.out::println);
    }
}