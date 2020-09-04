import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.maven.shared.utils.io.FileUtils;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class Validation{

    public static void main(String[] args) throws IOException {

        /*//AsyncAPI JSON HyperSchema
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

        //AsyncAPI Sample JSON schema
        File jsonData = new File("streetlights.json");
        JSONTokener jsonDataFile = new JSONTokener(new FileInputStream(jsonData));
        JSONObject jsonObject = new JSONObject(jsonDataFile);

        //get YAML file from URL
        URL url = new URL("https://raw.githubusercontent.com/asyncapi/asyncapi/master/examples/2.0.0/streetlights.yml");
        //System.out.println(url.getFile());
        //File yamlFileFromURL = new File(url.getFile());
        File file = new File("new.yaml");
        if (file.createNewFile()){
            FileUtils.copyURLToFile(url, file);
            System.out.println("Success");
        }*/

        Validation validation = new Validation();

        //Validating AsyncAPI documentation using JSON schema validation
        Schema schemaValidator = SchemaLoader.load(validation.importHyperSchema("schema.json"));
        try {

            //schemaValidator.validate(validation.importSampleSchema("streetlights.json"));

            schemaValidator.validate(validation.convertYamlFileToJSONObject("websocket.yml"));

            /*schemaValidator.validate(
                    validation.convertYamlFileToJSONObject(
                            validation.convertURLtoYAML(
                                    "https://raw.githubusercontent.com/asyncapi/asyncapi/master/examples/2.0.0/streetlights.yml"
                            )
                    )
            );*/

            System.out.println("Validation successful!");

            //retrieve data of the API
            validation.getDataOfAPI(validation.convertYamlFileToJSONObject("websocket.yml"));

        } catch (ValidationException e){
            for (String errors: e.getAllMessages()){
                System.out.println(errors);
            }
        }

        //retrieve protocol type
        //String keyInsideServer = validation.convertYamlFileToJSONObject("websocket.yml").getJSONObject("servers").keys().next();
        //System.out.println(validation.convertYamlFileToJSONObject("websocket.yml").getJSONObject("servers").getJSONObject(keyInsideServer).get("protocol"));

    }

    public JSONObject importHyperSchema(String jsonHyperSchema) throws FileNotFoundException {
        File schemaFile = new File(jsonHyperSchema);  //schema.json
        JSONTokener schemaData = new JSONTokener(new FileInputStream(schemaFile));
        return new JSONObject(schemaData);
    }

    public JSONObject importSampleSchema(String jsonSampleSchema) throws FileNotFoundException {
        File jsonData = new File(jsonSampleSchema);  //streetlights.json
        JSONTokener jsonDataFile = new JSONTokener(new FileInputStream(jsonData));
        return new JSONObject(jsonDataFile);
    }

    public JSONObject convertYamlFileToJSONObject(String yamlFile) throws IOException {
        ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
        Object obj = yamlReader.readValue(new File(yamlFile), Object.class); //websocket.yml
        ObjectMapper jsonWriter = new ObjectMapper();
        String json = jsonWriter.writeValueAsString(obj);
        return new JSONObject(json);
    }

    public String convertURLtoYAML(String fileURL) throws IOException {
        URL url = new URL(fileURL);
        File file = new File("new.yaml");
        if (file.exists()){
            file.delete();
        }
        if (file.createNewFile()){
            FileUtils.copyURLToFile(url, file);
        }
        System.out.println("File created!");
        return file.getName();
    }

    public void getDataOfAPI(JSONObject api){

        //get title
        System.out.println("Title: " + api.getJSONObject("info").getString("title"));

        //get version
        System.out.println("Version: " + api.getJSONObject("info").getString("version"));

        //get protocol
        String keyInsideServer = api.getJSONObject("servers").keys().next();
        System.out.println("Protocol: " + api.getJSONObject("servers").getJSONObject(keyInsideServer).getString("protocol"));
    }
}