package org.opengis.cite.sta10.readEntities;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opengis.cite.sta10.util.ControlInformation;
import org.opengis.cite.sta10.util.EntityProperties;
import org.opengis.cite.sta10.util.EntityRelations;
import org.opengis.cite.sta10.util.EntityType;
import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * Includes various tests of capability 1.
 */
public class Capability1Tests{


    @Test(description = "Read Things")
    public void readThings(){
        String urlString = "http://chashuhotpot.sensorup.com/OGCSensorThings/v1.0/Things";
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.setUseCaches(false);
            connection.setDoOutput(true);

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            Assert.assertTrue(response.indexOf("count") != -1 && response.indexOf("value") != -1);
            checkControlInformation(response.toString());
            checkEntityPropertie(EntityType.THING, response.toString());
            checkEntityRelations(EntityType.THING, response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }

    public void checkControlInformation(String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            for (int i = 0; i < entities.length(); i++) {
                JSONObject entity = entities.getJSONObject(i);
                Assert.assertTrue(entity.get(ControlInformation.ID)!=null);
                Assert.assertTrue(entity.get(ControlInformation.SELF_LINK)!=null);
                Assert.assertTrue(entity.get(ControlInformation.NAVIGATION_LINK)!=null);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkEntityPropertie(EntityType entityType, String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            switch (entityType){
                case THING:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String property : EntityProperties.THING_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null);
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void checkEntityRelations(EntityType entityType, String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            switch (entityType){
                case THING:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String property : EntityRelations.THING_RELATIONS) {
                            Assert.assertTrue(entity.get(property)!=null);
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(property).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
