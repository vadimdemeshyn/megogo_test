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

        //Verify titles
        try {
            for (i = 0; i <xmlList.size() ; i++) {
                Assert.assertEquals(xmlList.get(i).getTitle(), jsonList.get(i).getTitle());
            }
            System.out.println("Running class "+Thread.currentThread().getStackTrace()[1].getClassName()+
                    " .Running method: "+Thread.currentThread().getStackTrace()[1].getMethodName()+" .Titles are equal");
        }
        catch (Error error) {
            System.out.println("Running class " + Thread.currentThread().getStackTrace()[1].getClassName() +
                    " .Running method: " + Thread.currentThread().getStackTrace()[1].getMethodName() +
                    ". Different data in JSON comparing to XML." + "\n"
                    + "JSON title: " + jsonList.get(i).getTitle() + "\n"
                    + "XML title: " + xmlList.get(i).getTitle());
            throw error;
        }

        //Verify start dates - done for better logging if smth happened wrong
        try {
            for (i = 0; i < jsonList.size(); i++) {
                Assert.assertTrue(jsonList.get(i).getStartDate().compareTo(xmlList.get(i).getStartDate()) ==0);

            }
            System.out.println("Running class "+Thread.currentThread().getStackTrace()[1].getClassName()+
                    " .Running method: "+Thread.currentThread().getStackTrace()[1].getMethodName()+
                    ". Dates in XML list and JSON request are similar");
        }
        catch (Error error){
            System.out.println("Running class "+Thread.currentThread().getStackTrace()[1].getClassName()+
                    " .Running method: "+Thread.currentThread().getStackTrace()[1].getMethodName()+
                    ". Different data in JSON comparing to XML."+"\n"
            +"JSON START DATE: "+jsonList.get(i).getStartDate()+"\n"
            +"XML START DATE: "+xmlList.get(i).getStartDate());
            throw error;
        }

        //Verify end dates - done for better logging if smth happened wrong
        try {
            for (i = 0; i < jsonList.size(); i++) {
                Assert.assertFalse(jsonList.get(i).getEndDate() == xmlList.get(i).getEndDate());
            }
        }
        catch (Error error){
            System.out.println("Different data in JSON comparing to XML."+"\n"
                    +"JSON START DATE: "+jsonList.get(i).getStartDate()+"\n"
                    +"XML START DATE: "+xmlList.get(i).getStartDate());
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

