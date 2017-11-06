import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.jsonschema.main.JsonValidator;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import java.io.File;
import java.io.IOException;


/**
 * Created by vadim on 01.11.2017.
 */
public class Verificator {

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //                                          ATTENTION!!!!                                                           //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //  WE NEED TO HAVE A PERFECT EXAMPLE OF JSON SCHEMA TO VERIFY RESPONSES. THIS CAN BE USED ONLY AS REFERRAL.        //
    //  I DESIGNED IT REFERRING TO CURRENT SERVER'S RESPONSES AND TRIED TO CUSTOMIZE AS MUCH AS POSSIBLE                //
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

   private Parser parser = new Parser();

    public boolean validateJsonSchema() throws IOException {

        boolean isSuccess = false;
        String text = "";

        try {
            text = Files.toString(new File("src/test/resources/schemas/schema.json"), Charsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonData=parser.getResponseAsString();
        String jsonSchema=text;
        JsonNode data = null;
        try {
            data = JsonLoader.fromString(jsonData);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonNode schema = null;
        try {
            schema = JsonLoader.fromString(jsonSchema);
        } catch (IOException e) {
            e.printStackTrace();
        }

        final JsonSchemaFactory factory = JsonSchemaFactory.byDefault();
        JsonValidator validator = factory.getValidator();

        ProcessingReport report = null;
        try {
            report = validator.validate(schema, data);
            if (report.isSuccess()) isSuccess = true;
            else isSuccess = false;
        } catch (ProcessingException e) {
            e.printStackTrace();
        }
        System.out.println(report.toString());
        return isSuccess;
    }


    }
