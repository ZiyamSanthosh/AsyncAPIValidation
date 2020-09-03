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

        //AsyncAPI Sample JSON schema
        File jsonData = new File("streetlights.json");
        JSONTokener jsonDataFile = new JSONTokener(new FileInputStream(jsonData));
        JSONObject jsonObject = new JSONObject(jsonDataFile);

        //Validating AsyncAPI documentation using JSON schema validation
        Schema schemaValidator = SchemaLoader.load(jsonSchema);
        try {
            schemaValidator.validate(jsonObject);
        } catch (ValidationException e){
            System.out.println(e.getMessage());
        }

    }
}