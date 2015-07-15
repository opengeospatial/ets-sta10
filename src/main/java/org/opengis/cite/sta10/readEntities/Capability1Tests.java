package org.opengis.cite.sta10.readEntities;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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

    public final String rootUri = "http://chashuhotpot.sensorup.com/OGCSensorThings/v1.0";

    @Test(description = "Read Things")
    public void readThings(){
        String urlString = rootUri + "/Things";
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

    @Test(description = "Check Service Root UI")
    public void checkServiceRootUri(){
        String urlString = rootUri;
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
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            for (int i = 0; i < entities.length(); i++) {
                JSONObject entity = entities.getJSONObject(i);
                Assert.assertTrue(entity.get("name")!=null);
                Assert.assertTrue(entity.get("url")!=null);
                String name = entity.getString("name");
                String nameUrl = entity.getString("url");
                Map<String,Boolean> addedLinks = new HashMap<>();
                addedLinks.put("Things",false);
                addedLinks.put("Locations",false);
                addedLinks.put("HistoricalLocations",false);
                addedLinks.put("Datastreams",false);
                addedLinks.put("Sensors",false);
                addedLinks.put("Observations",false);
                addedLinks.put("ObservedProperties",false);
                addedLinks.put("FeaturesOfInterest",false);
                switch (name){
                    case "Thing":
                        Assert.assertEquals(nameUrl, rootUri + "/Things", "The URL for Things in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("Things");
                        addedLinks.put(name, true);
                        break;
                    case "Locations":
                        Assert.assertEquals(nameUrl,rootUri+"/Locations","The URL for Locations in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("Locations");
                        addedLinks.put(name, true);
                        break;
                    case "HistoricalLocations":
                        Assert.assertEquals(nameUrl, rootUri + "/HistoricalLocations", "The URL for HistoricalLocations in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("HistoricalLocations");
                        addedLinks.put(name, true);
                        break;
                    case "Datastreams":
                        Assert.assertEquals(nameUrl,rootUri+"/Datastreams","The URL for Datastreams in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("Datastreams");
                        addedLinks.put(name, true);
                        break;
                    case "Sensors":
                        Assert.assertEquals(nameUrl,rootUri+"/Sensors","The URL for Sensors in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("Sensors");
                        addedLinks.put(name, true);
                        break;
                    case "Observations":
                        Assert.assertEquals(nameUrl, rootUri + "/Observations", "The URL for Observations in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("Observations");
                        addedLinks.put(name, true);
                        break;
                    case "ObservedProperties":
                        Assert.assertEquals(nameUrl,rootUri+"/ObservedProperties","The URL for ObservedProperties in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("ObservedProperties");
                        addedLinks.put(name, true);
                        break;
                    case "FeaturesOfInterest":
                        Assert.assertEquals(nameUrl,rootUri+"/FeaturesOfInterest","The URL for FeaturesOfInterest in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("FeaturesOfInterest");
                        addedLinks.put(name, true);
                        break;
                    default:
                        Assert.fail("There is a component in Service Root URI response that is not in SensorThings API : " + name);
                        break;
                }
                for(String key: addedLinks.keySet()){
                    Assert.assertTrue(addedLinks.get(key), "The Service Root URI response does not contain "+key);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
