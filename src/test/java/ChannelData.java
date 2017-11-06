import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vadim on 02.11.2017.
 */
public class ChannelData {

    String mappingFile = "src/test/resources/csvMappingFiles/mappings.csv";
    BufferedReader bufferedReader;
    String line = "";
    String cvsSplitBy = ",";

    public Map fillMap(int value1, int value2) throws IOException {
        Map<String,String> map = new HashMap<String, String>();
        bufferedReader = new BufferedReader(new FileReader(mappingFile));
        while ((line = bufferedReader.readLine()) != null){
            String[] params = line.split(cvsSplitBy);
            map.put(params[value1], params[value2]);
        }

        return map;
    }

    public Map fillXMLSMap() throws IOException {
        return fillMap(0,1);
    }

    public Map fillRequestEndpointsSMap() throws IOException {
        return fillMap(0,2);
    }
}
