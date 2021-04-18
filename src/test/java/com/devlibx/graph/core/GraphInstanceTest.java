package com.devlibx.graph.core;

import com.devlibx.graph.domain.Graph;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.module.SimpleModule;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.junit.Ignore;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
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

        // Run traversal
        List<String> result = graphInstance.travers(new AirflowTraversalV2());
        assertNotNull(result);
        assertTrue(result.size() > 0);

        List<String> expected = new ArrayList<>();
        expected.add("sink_2__needs__transformation_2 >> end");
        expected.add("transformation_2__needs_source_read_from_redshift_2_and_3 >> sink_2__needs__transformation_2");
        expected.add("start >> [source_read_from_redshift_1, source_read_from_redshift_2, source_read_from_redshift_3]");
        expected.add("source_read_from_redshift_1 >> transformation_1__needs_source_read_from_redshift_1");
        expected.add("sink_1__needs__transformation_1 >> end");
        expected.add("source_read_from_redshift_3 >> transformation_2__needs_source_read_from_redshift_2_and_3");
        expected.add("source_read_from_redshift_2 >> transformation_2__needs_source_read_from_redshift_2_and_3");
        expected.add("transformation_1__needs_source_read_from_redshift_1 >> sink_1__needs__transformation_1");
        assertEquals(expected, result);
    }

    @Ignore
    public void _testGraphIgnore() throws Exception {
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


        List<String> resultNew = graphInstance.traversDoNotUse(new AirflowTraversal());
        assertNotNull(resultNew);
        assertTrue(resultNew.size() > 0);
        resultNew.forEach(System.out::println);

        assertEquals(result, resultNew);

        System.out.println("\n\n");
        result = graphInstance.travers(new AirflowTraversalV2());
        assertNotNull(result);
        assertTrue(result.size() > 0);
        result.forEach(System.out::println);
        System.out.println();


        System.out.println();
        System.out.println(objectMapper.writeValueAsString(graphInstance.startNode));

    }
}