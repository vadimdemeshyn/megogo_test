import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

/**
 * Created by vadim on 31.10.2017.
 */
public class Tests {

    @Test
    public void verifyResponseMatchesXML() throws IOException {



        Parser parser = new Parser();
        Verificator verificator = new Verificator();


        int i = 0;

        List<Program> jsonList = parser.getJsonObjectsList();
        List<Program> xmlList = parser.getXmlObjects();

        //Verify that parser done job successfully
        try {
            Assert.assertEquals(jsonList.size(), xmlList.size());
        }
        catch (Exception e){
            System.out.println("JSON list size differs from XML list size");
        }

        //Verify end dates - done for better logging if smth happened wrong
        try {
            for (i = 0; i < jsonList.size(); i++) {
                Assert.assertTrue(jsonList.get(i).equals(xmlList.get(i)));
            }
            System.out.println("Objects are the same");
        }
        catch (Error error){
            System.out.println("Different data in JSON comparing to XML."+"\n"
                    +"IN OBJECT NUMBER: "+i+"\n"
                    +"JSON START DATE: "+jsonList.get(i).getStartDate()+"\n"
                    +"XML START DATE: "+xmlList.get(i).getStartDate()+"\n"
                    +"JSON TITLE: " + jsonList.get(i).getGenreTitle()+"\n"
                    +"XML TITLE: " + xmlList.get(i).getGenreTitle()+"\n"
                    +"JSON END DATE: "+jsonList.get(i).getEndDate()+"\n"
                    +"XML END DATE: "+xmlList.get(i).getEndDate()+"\n"
            );
            throw error;
        }

        //JSON schema verification
        try {
            Assert.assertTrue(verificator.validateJsonSchema());
        }
        catch (Error error){
            System.out.println("JSON in response differs from JSON schema in file");
            throw error;
        }
    }
}

