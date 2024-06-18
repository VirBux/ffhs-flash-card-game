package util;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

// Generated with ChatGPT
public class JsonTest {
    @Test
    public void testParseValidJson() throws IOException {
        String json = "{\"name\":\"John\", \"age\":30}";
        JsonNode node = Json.parse(json);
        Assert.assertNotNull("The parsed JsonNode should not be null.", node);
        Assert.assertEquals("The name should be John.", "John", node.get("name").asText());
        Assert.assertEquals("The age should be 30.", 30, node.get("age").asInt());
    }

    @Test(expected = IOException.class)
    public void testParseInvalidJson() throws IOException {
        String json = "{name:\"John\", age:30}";
        Json.parse(json);
    }
}