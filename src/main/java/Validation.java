import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Validation{

    public static void main(String[] args) throws IOException {

        //AsyncAPI JSON HyperSchema
        File schemaFile = new File("schema.json");
        JSONTokener schemaData = new JSONTokener(new FileInputStream(schemaFile));
        JSONObject jsonSchema = new JSONObject(schemaData);

        //convert YAML into JSON
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        Object obj = yamlReader.readValue(new File("websocket.yml"), Object.class);
        ObjectMapper jsonWriter = new ObjectMapper();
        String json = jsonWriter.writeValueAsString(obj);
        JSONObject convertedJson = new JSONObject(json);
        //System.out.println(convertedJson.toString());

        /*//AsyncAPI Sample JSON schema
        File jsonData = new File("streetlights.json");
        JSONTokener jsonDataFile = new JSONTokener(new FileInputStream(jsonData));
        JSONObject jsonObject = new JSONObject(jsonDataFile);*/

        //Validating AsyncAPI documentation using JSON schema validation
        Schema schemaValidator = SchemaLoader.load(jsonSchema);
        try {
            schemaValidator.validate(convertedJson);
        } catch (ValidationException e){
            System.out.println(e.getMessage());
        }

        //retrieve protocol type
        String keyInsideServer = convertedJson.getJSONObject("servers").keys().next();
        System.out.println(convertedJson.getJSONObject("servers").getJSONObject(keyInsideServer).get("protocol"));

    }
}