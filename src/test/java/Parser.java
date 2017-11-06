import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;
import java.io.*;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;

/**
 * Created by vadim on 27.10.2017.
 */
public class Parser {

    private ChannelData channelData = new ChannelData();

    //Parameter we specify via -D option while running in CLI
    private String channel = System.getProperty("channel");

    //String for usage in class
    private String channelNameForClass= "";

    private static final String URL = "http://epg.megogo.net/channel";
    private static final String REQUEST = "?external_id";
    private static final String TIMESTAMP_ENDPOINT = "http://epg.megogo.net/time";

    //Variables for First start date and Last start date in JSON - it will help us to filter extra programs in XML file
    private static Date startJsonDateFirst = null;
    private static Date startJsonDateLast= null;



    private void saveXmlProgram(){
        Map<String, String> xmls = null;
        try {
            xmls = channelData.fillXMLSMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String channelName = "";
        String channelId = "";

        for (HashMap.Entry<String, String> map: xmls.entrySet()
             ) {
            String key = map.getKey();

            if (key.contentEquals(channel)){
                channelName = map.getKey();
                channelId = map.getValue();
            }
        }
        File file = new File("src/test/resources/xmlProgramms/"+channelName+".xml");
        try {
            FileUtils.copyURLToFile(new URL("http://www.vsetv.com/export/megogo/epg/"+channelId+".xml"), file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        channelNameForClass = channelName;
    }

    public String getResponseAsString() throws IOException {
        Response response;
        Map<String, String> requestEndpointsSMap = channelData.fillRequestEndpointsSMap();
        String channelId = "";
        for (HashMap.Entry<String, String> map: requestEndpointsSMap.entrySet()
                ) {
            String key = map.getKey();
            String value = map.getValue();

            if (key.contentEquals(channel)){
                channelId = map.getValue();
            }
        }
        response = given()
                .contentType(ContentType.JSON.withCharset("windows-1251"))
                .when()
                .get(URL + REQUEST + "=" + channelId);

        return response.getBody().asString();
    }

    private String xmlToJSon(String channel){

        String XML_PATH = "src/test/resources/xmlProgramms/"+channel+".xml";
        String JSON;
        String x = "";
        try {
            BufferedReader in = new BufferedReader(new FileReader(XML_PATH));
            StringBuffer output = new StringBuffer();
            String st;
            while ((st=in.readLine()) != null) {
                output.append(st);
            }
            x = output.toString();
            in.close();
        }
        catch (Exception fx) {
            System.out.println("Exception " + fx.toString());
        }


        JSON = org.json.XML.toJSONObject(x).toString();

        return JSON;
    }

    //formatting date from XML file for future comparison with JSON dates
    private String formatXMLDates(String date){

        String strDate  = date;
        String reformattedStr=null;
        DateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US); // current format
        DateFormat desirable = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.US); //needed format
        try {

            reformattedStr = desirable.format(inputDate.parse(strDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return reformattedStr;
    }

    private Date formatDateStringsToObject(String dateString){

        Date date = null;
        try {
            date= (new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.US)).parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return date;
    }

    //Get Current Timestamp as Date class object
    private Date getCurrentTimestampAsDateObject(){

        Date date=null;
        String currentTimestamp;
        String reformattedStr ="";
        Response response = get(TIMESTAMP_ENDPOINT);
        String responseAsString = response.getBody().asString();
        JSONObject timeStampResponseJSON = new JSONObject(responseAsString);
        JSONObject data = timeStampResponseJSON.getJSONObject("data");
        currentTimestamp = data.optString("time_local");

        DateFormat inputDate = new SimpleDateFormat("E, MMM dd, yyyy hh:mm:ss", Locale.US); // current format
        DateFormat desirable = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.US); //needed format

        try {
            reformattedStr = desirable.format(inputDate.parse(currentTimestamp));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try {
            date= (new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a", Locale.US)).parse(reformattedStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    //Initializes first and last start dates of JSON programs - to filter XML file within them
    private void initializeStaticFirstLastDatesFromJSONList() throws IOException {
        ArrayList<Program> list =  getJsonObjectsList();
        startJsonDateFirst = getJsonObjectsList().get(0).getStartDate();
        startJsonDateLast = getJsonObjectsList().get(list.size()-1).getStartDate();
    }

    //List with all TV Programs from JSON we have received as response - as Java objects
    public ArrayList<Program> getJsonObjectsList() throws IOException {

        ArrayList<Program> programs = new ArrayList<>();

            saveXmlProgram();

        //Getting primary JSON object from server Response as string
        JSONObject jsonObj = new JSONObject(getResponseAsString());

        //Getting primary response array which consists of TV shows and other data - DATA
        JSONArray ja_data = jsonObj.getJSONArray("data");
        JSONObject jsonObj1 = ja_data.getJSONObject(0);

        //Getting inner array which contains ONLY TV shows  - PROGRAMS
        JSONArray ja = jsonObj1.getJSONArray("programs");

        //Converting existing TV shows to Java objects for future comparison with XML file

        for (int i = 0; i <ja.length() ; i++) {

            String year = ja.getJSONObject(i).optString("year");
            String title = ja.getJSONObject(i).optString("title");
            String description =ja.getJSONObject(i).optString("description");
            String genreTitle = String.valueOf(ja.getJSONObject(i).getJSONObject("genre").get("title"));
            String categoryTitle =String.valueOf(ja.getJSONObject(i).getJSONObject("category").get("title"));
            Date startDate = formatDateStringsToObject(ja.getJSONObject(i).optString("start"));
            Date endDate = formatDateStringsToObject(ja.getJSONObject(i).optString("end"));
            programs.add(new Program(year,title,description,genreTitle,categoryTitle,startDate,endDate));

            startJsonDateFirst = formatDateStringsToObject(ja.getJSONObject(0).optString("start"));
            startJsonDateLast = formatDateStringsToObject(ja.getJSONObject(ja.length()-1).optString("start"));
        }



        return programs;
    }

    //List with all TV Programs from XML we have received as response - as Java objects
    public List<Program> getXmlObjects() throws IOException {
        initializeStaticFirstLastDatesFromJSONList();

        String year, title, description, genreTitle, categoryTitle;
        Date startDate, endDate;
        ArrayList<Program> programsList = new ArrayList<>();
        JSONObject primaryJson = new JSONObject(xmlToJSon(channelNameForClass));
        JSONObject tv = primaryJson.getJSONObject("tv");
        JSONArray programs = tv.getJSONArray("programme");



        for (int i = 0; i <programs.length() ; i++) {

            //If dates are earlier than first JSON object start date
            // and date is later than last JSON object start date
            // we don't add them to List
            if ((formatDateStringsToObject(formatXMLDates( programs.getJSONObject(i).optString("start"))).compareTo(startJsonDateFirst) >=0)
                    &&
                    (formatDateStringsToObject(formatXMLDates( programs.getJSONObject(i).optString("start"))).compareTo(startJsonDateLast) <=0
                    )){
                try {
                    year = programs.getJSONObject(i).optString("production_year");
                }catch (Exception e){
                    year = "";
                }
                try {
                    title = String.valueOf(programs.getJSONObject(i).getJSONObject("title").get("content"));
                }catch (Exception e){
                    title = "";
                }
                try {
                    description =String.valueOf(programs.getJSONObject(i).getJSONObject("desc").get("content"));
                }catch (Exception e){
                    description = "";
                }
                try {
                    genreTitle = String.valueOf(programs.getJSONObject(i).getJSONObject("genre").get("content"));
                }catch (Exception e){
                    genreTitle = "";
                }
                try {
                    categoryTitle =String.valueOf(programs.getJSONObject(i).getJSONObject("category").get("content"));
                }catch (Exception e){
                    categoryTitle = "";
                }
                try {
                    startDate = formatDateStringsToObject(formatXMLDates( programs.getJSONObject(i).optString("start")));

                }catch (Exception e){
                    startDate = null;
                }
                try {
                    endDate = formatDateStringsToObject(formatXMLDates( programs.getJSONObject(i).optString("stop")));
                }catch (Exception e){
                    endDate = null;
                }
                programsList.add(new Program(year,title,description,genreTitle,categoryTitle,startDate,endDate));
            }
        }

        return programsList;
    }

}
