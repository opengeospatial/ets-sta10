package org.opengis.cite.sta10.createUpdateDelete;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Includes various tests of capability 2.
 */
public class Capability2Tests{

    @Test(description = "Post Thing")
    public void postThing(){
        String urlString = "http://chashuhotpot.sensorup.com/OGCSensorThings/v1.0/Things";
        HttpURLConnection conn= null;
        try {
            //Create connection
            URL url = new URL(urlString);
            String urlParameters  = "{\"description\":\"This is a Test From TestNG\"}";
            byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int    postDataLength = postData.length;
            conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                wr.write( postData );
            }

            int responseCode = conn.getResponseCode();

            Assert.assertTrue(responseCode == 201);

            String response = conn.getHeaderField("location");
            long id = Long.parseLong(response.substring(response.indexOf("(") + 1, response.indexOf(")")));

            conn.disconnect();

            url = new URL(urlString+"("+id+")");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type",
                    "application/json");

            conn.setUseCaches(false);
            conn.setDoOutput(true);

            //Get Response
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder getResponse = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while((line = rd.readLine()) != null) {
                getResponse.append(line);
                getResponse.append('\r');
            }
            rd.close();
            Assert.assertTrue(getResponse.indexOf("This is a Test From TestNG") != -1);
            conn.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
    }
}
