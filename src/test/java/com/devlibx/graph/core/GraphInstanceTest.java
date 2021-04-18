package com.devlibx.graph.core;

import com.devlibx.graph.domain.Graph;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
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

        ObjectMapper mapperWithParent = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(GraphNode.class, new ItemSerializer.ItemSerializerWithParent());
        mapperWithParent.registerModule(module);

        String toSendOverWire = mapperWithParent.writeValueAsString(graphInstance.endNode);
        GraphNode fromString = objectMapper.readValue(toSendOverWire, GraphNode.class);
        System.out.println("\n");
        System.out.println(toSendOverWire);
        System.out.println("\n");


        ObjectMapper mapperWithChild = new ObjectMapper();
        SimpleModule module1 = new SimpleModule();
        module1.addSerializer(GraphNode.class, new ItemSerializer.ItemSerializerWithChild());
        mapperWithChild.registerModule(module1);

        toSendOverWire = mapperWithChild.writeValueAsString(graphInstance.startNode);
        GraphNode fromString1 = objectMapper.readValue(toSendOverWire, GraphNode.class);
        System.out.println("\n->>");
        System.out.println(objectMapper.writeValueAsString(fromString1));
        System.out.println("\n");

        List<String> result = graphInstance.travers(new AirflowTraversal());
        assertNotNull(result);
        assertTrue(result.size() > 0);
        result.forEach(System.out::println);
        System.out.println();


        List<String> resultNew = graphInstance.travers(fromString, new AirflowTraversal());
        assertNotNull(resultNew);
        assertTrue(resultNew.size() > 0);
        resultNew.forEach(System.out::println);

        assertEquals(result, resultNew);

        System.out.println("\n\n");
        result = graphInstance.traversForward(new AirflowTraversalV2());
        assertNotNull(result);
        assertTrue(result.size() > 0);
        result.forEach(System.out::println);
        System.out.println();


    }
}