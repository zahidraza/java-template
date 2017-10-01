package com.jazasoft.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.apache.sling.commons.json.JSONArray;
import org.apache.sling.commons.json.JSONException;
import org.apache.sling.commons.json.JSONObject;
import org.apache.sling.commons.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Created by mdzahidraza on 30/09/17.
 */
public class JsonUtils {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtils.class);

    private static final ObjectMapper mapper ;
    private static final ObjectWriter writer;
    private static final ObjectWriter prettyWriter;

    static {
        mapper = new ObjectMapper();
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        writer = mapper.writer();
        prettyWriter = mapper.writerWithDefaultPrettyPrinter();
    }

    /**
     * Get Jackson ObjectMapper Object.
     * @return
     */
    public static ObjectMapper getObjectMapper() {
        return mapper;
    }

    /**
     * Get Jackson Pretty ObjectWriter Object.
     * @return
     */
    public static ObjectWriter getPrettyWriter() {
        return prettyWriter;
    }



    /************ Json Object related Methods *************/

    /**
     * Read Json Objects from file.
     * @param file Json file to read from.
     * @param type Class Object of Bean to be read
     * @param <T> Generic Bean class
     * @return
     * @throws IOException
     */
    public static <T> T readJsonObject(File file, final Class<T> type) throws IOException {
        T jsonObject = mapper.readValue(file, type);
        return jsonObject;
    }

    /**
     * Read Json Object from Json String
     * @param jsonString Json String to read from
     * @param type Class Object of Bean to be read
     * @param <T> Generic Bean class
     * @return
     * @throws JsonProcessingException
     * @throws IOException
     */
    public static <T> T readJsonObject(String jsonString, final Class<T> type) throws IOException {
        T jsonObject = mapper.readValue(jsonString, type);
        return jsonObject;
    }

    /**
     * Convert any Java Object to Json String.
     * @param javaObject Java object to convert
     * @param <T> generic type of java object
     * @return
     * @throws JsonProcessingException
     */
    public static <T> String toString(T javaObject) throws JsonProcessingException {
        String jsonString = writer.writeValueAsString(javaObject);
        return jsonString;
    }

    /**
     * convert Java Object to Formatted Json String
     * @param javaObject Java object to convert
     * @param <T> generic type of java object
     * @return
     * @throws JsonProcessingException
     */
    public static <T> String toFormattedString(T javaObject) throws JsonProcessingException {
        String jsonString = prettyWriter.writeValueAsString(javaObject);
        return jsonString;
    }

    /**
     * Write the JSON content to the file provided in a pretty format
     * @param file File to write the JSON content
     * @param javaObject Java Object
     * @param <T>  Generic Bean type
     * @throws IOException
     */
    public static <T> void writePrettyJsonObject(File file, T javaObject) throws IOException {
        prettyWriter.writeValue(file, javaObject);
    }



    /************ Json Array related Methods *************/

    /**
     * Read Json Array from file.
     * @param file Json file to read from.
     * @param type Class Object of Bean to be read
     * @param <T> Generic Bean class
     * @return List of Java Objects
     * @throws IOException
     */
    public static <T> List<T> readJsonArray(File file, final Class<T> type) throws IOException {
        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, type);
        return mapper.readValue(file, collectionType);
    }

    /**
     * Read Json Array from Json String
     * @param jsonString Json String to read from
     * @param type Class Object of Bean to be read
     * @param <T> Generic Bean class
     * @return List of Java Objects
     * @throws JsonProcessingException
     * @throws IOException
     */
    public static <T> List<T> readJsonArray(String jsonString, final Class<T> type) throws IOException {
        CollectionType collectionType = mapper.getTypeFactory().constructCollectionType(List.class, type);
        return mapper.readValue(jsonString, collectionType);
    }

    /**
     * Convert any Java Object to Json String.
     * @param javaObject Java object to convert
     * @param <T> generic type of java object
     * @return
     * @throws JsonProcessingException
     */
    public static <T> String toString(Collection<T> javaObject) throws JsonProcessingException {
        String jsonString = writer.writeValueAsString(javaObject);
        return jsonString;
    }

    /**
     * convert Java Object to Formatted Json String
     * @param javaObject Java object to convert
     * @param <T> generic type of java object
     * @return
     * @throws JsonProcessingException
     */
    public static <T> String toFormattedString(Collection<T> javaObject) throws JsonProcessingException {
        String jsonString = prettyWriter.writeValueAsString(javaObject);
        return jsonString;
    }

    /**
     * Write the JSON content to the file provided in a pretty format
     * @param file File to write the JSON content
     * @param collection Collection Of Java Objects
     * @param <T>  Generic Bean type
     * @throws IOException
     */
    public static <T> void writePrettyJsonArray(File file, Collection<T> collection) throws IOException {
        prettyWriter.writeValue(file, collection);
    }



    /************* Schema Validation related methods  *****************/

    /**
     * Validate the JSON stream against the schema provided.
     * @param schemaStream json schema stream
     * @param jsonStream input json stream
     * @return a boolean
     * @throws ProcessingException
     * @throws IOException
     */
    public static boolean validateSchema(InputStream schemaStream, InputStream jsonStream)
            throws ProcessingException, IOException {
        JsonSchema schemaNode = _getSchemaNode(JsonLoader.fromReader(new InputStreamReader(schemaStream)));
        JsonNode jsonNode = JsonLoader.fromReader(new InputStreamReader(jsonStream));
        return isJsonValid(schemaNode, jsonNode);
    }

    /**
     * Validate the JSON stream against the schema provided.
     * @param schemaFile json schema stream
     * @param jsonFile input json stream
     * @return a boolean
     * @throws ProcessingException
     * @throws IOException
     */
    public static boolean validateSchema(File schemaFile, File jsonFile) throws IOException, ProcessingException {
        JsonSchema jsonSchema = _getSchemaNode(JsonLoader.fromFile(schemaFile));
        JsonNode jsonNode = JsonLoader.fromFile(jsonFile);
        return isJsonValid(jsonSchema, jsonNode);
    }

    /**
     * Validate the JSON string against the schema provided
     * @param jsonSchema json schema string
     * @param jsonFile input json file
     * @return a boolean
     * @throws ProcessingException
     * @throws IOException
     */
    public static boolean validateSchema(String jsonSchema, File jsonFile)
            throws ProcessingException, IOException {
        JsonSchema schemaNode = _getSchemaNode(JsonLoader.fromString(jsonSchema));
        JsonNode jsonNode = JsonLoader.fromFile(jsonFile);
        return isJsonValid(schemaNode, jsonNode);
    }

    private static boolean isJsonValid(JsonSchema jsonSchemaNode,
                                       JsonNode jsonNode) throws ProcessingException {
        ProcessingReport report = jsonSchemaNode.validate(jsonNode);
        report.forEach(processingMessage -> logger.error(processingMessage.getMessage()));
        return report.isSuccess();
    }

    private static JsonSchema _getSchemaNode(JsonNode jsonNode)
            throws ProcessingException {
        JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        return factory.getJsonSchema(jsonNode);
    }

    /**************** JSON parsing related methods ****************/

    /**
     * Read Json Objects from JSON String.
     * @param jsonStr
     * @return If json string contains single object, It will return array of single JSON object. if json string is json array, It will return array of JSON Objects
     * @throws JSONException
     */
    public static JSONObject[] getJSONObjects(String  jsonStr) throws JSONException {
        JSONObject flattenedObj[] = null;
        // The given String can be either array or a single JSONObject
        JSONTokener jsonTokener = new JSONTokener(jsonStr);
        Object obj = jsonTokener.nextValue();
        if ( obj instanceof JSONObject) {
            // the string holds just one JSON Object
            JSONObject jsonObj = new JSONObject(jsonStr);
            flattenedObj = new JSONObject[]{jsonObj};
        } else  if ( obj instanceof JSONArray){
            // the string represents an array, it is assumed it is an array of JSON objects
            JSONArray arr = new JSONArray(jsonStr);
            flattenedObj = new JSONObject[arr.length()];
            for ( int i = 0; i < arr.length(); i++) {
                flattenedObj[i] = new JSONObject();
                convert("", arr.getJSONObject(i), flattenedObj[i]);
            }
        }
        return flattenedObj;

    }

    /**
     *
     * @param jsonObj
     * @return
     * @throws JSONException
     */
    public static JSONObject flattenJSON(Object jsonObj) throws JSONException {
        JSONObject flattenedObj = new JSONObject();
        convert("", jsonObj, flattenedObj);
        return flattenedObj;

    }

    private static void convert(String path, Object obj, JSONObject flattenedObj) throws JSONException {
        if ( obj instanceof JSONObject) {
            path = "".equals(path) ? path : path+".";
            JSONObject jsonObj = (JSONObject)obj;
            for(Iterator<String> i = jsonObj.keys(); i.hasNext(); ) {
                String key = i.next();
                Object tObj = jsonObj.get(key);
                convert(path+key,tObj,flattenedObj);
            }
        } else if ( obj instanceof JSONArray) {
            JSONArray jsonArr = (JSONArray)obj;
            for ( int i = 0; i < jsonArr.length(); i++ ) {
                convert(path+"[" + i + "]", jsonArr.get(i),flattenedObj);
            }
        } else {
            if(flattenedObj==null){
                flattenedObj = new JSONObject();
            }
            flattenedObj.put(path, obj);
        }
    }
}
