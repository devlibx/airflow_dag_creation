package com.devlibx.graph.core;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ItemSerializer {

    public static class ItemSerializerWithParent extends StdSerializer<GraphNode> {

        public ItemSerializerWithParent() {
            this(null);
        }

        public ItemSerializerWithParent(Class<GraphNode> t) {
            super(t);
        }

        @Override
        public void serialize(GraphNode value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
            jgen.writeStartObject();
            jgen.writeStringField("id", value.getId());
            jgen.writeObjectField("node", value.getNode());
            jgen.writeObjectField("parents", value.getParents());
            jgen.writeEndObject();
        }
    }

    public static class ItemSerializerWithChild extends StdSerializer<GraphNode> {

        public ItemSerializerWithChild() {
            this(null);
        }

        public ItemSerializerWithChild(Class<GraphNode> t) {
            super(t);
        }

        @Override
        public void serialize(GraphNode value, JsonGenerator jgen, SerializerProvider serializerProvider) throws IOException {
            jgen.writeStartObject();
            jgen.writeStringField("id", value.getId());
            jgen.writeObjectField("node", value.getNode());
            jgen.writeObjectField("vertices", value.getVertices());
            jgen.writeEndObject();
        }
    }
}
