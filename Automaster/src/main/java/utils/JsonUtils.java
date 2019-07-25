package utils;

import java.io.IOException;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonUtils {

	public static JsonNode getObjectFromJsonString(String jsonString) throws IOException {
		ObjectMapper objectMapper = new ObjectMapper();	
		JsonNode jsonNode = objectMapper.readTree(jsonString);
		return jsonNode;
	}

	public static JsonNode getJsonObjectFromResourceFile(String resourceFilepath) throws IOException {
		String stringContentsFromResourceFile = FileUtils.getStringContentsFromResourceFile(resourceFilepath);
		JsonNode jsonObject = getObjectFromJsonString(stringContentsFromResourceFile);
		return jsonObject;
	}

	public static JsonNode getJsonObjectFromJsonArray(ArrayNode arrayNode, String key,
			String value) {
		int size = arrayNode.size();
		for(int i=0;i<size;i++) {
			JsonNode jsonNode = arrayNode.get(i);
			String actualValue = jsonNode.get(key).asText().toUpperCase();
			if(value.equals(actualValue)) {
				return jsonNode;
			}
		}
		return null;
	}
	
	
}
