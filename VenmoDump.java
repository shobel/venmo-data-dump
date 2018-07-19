package venmodump;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VenmoDump {

    private static String URL = "https://venmo.com/api/v5/public?limit=9999";
    public static void main(String[] args) throws MalformedURLException, IOException, ParseException {
        URL url = new URL(URL);
        JSONParser parser = new JSONParser();

        BufferedWriter writer = new BufferedWriter(new FileWriter("C:\\Users\\Sam\\Desktop\\venmo_data_dump2.txt"));
        
        int counter = 1;
        while (url != null) {
            try {
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    writer.append(inputLine);
                    writer.newLine();
                    Object obj = parser.parse(inputLine);
                    JSONObject jo = (JSONObject) obj;
                    JSONObject paging = (JSONObject) jo.get("paging");
                    url = new URL((String) (paging.get("next") + "&limit=9999"));
                    System.out.println("Getting page " + counter + ": " + url.toString());
                }
                counter++;
                in.close();
            } catch (IOException e){
                System.out.println("Problem getting page " + counter + "..trying again in 1 minute");
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(VenmoDump.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        writer.close();
    }

}
