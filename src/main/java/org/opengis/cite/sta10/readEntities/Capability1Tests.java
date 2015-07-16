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


    @Test(description = "GET Entities")
    public void readEntitiesAndCheckResponse(){
        String response = getEntities(EntityType.THING);
        checkAllAspectsForResponse(EntityType.THING,response);
        response = getEntities(EntityType.LOCATION);
        checkAllAspectsForResponse(EntityType.LOCATION,response);
        response = getEntities(EntityType.HISTORICAL_LOCATION);
        checkAllAspectsForResponse(EntityType.HISTORICAL_LOCATION,response);
        response = getEntities(EntityType.DATASTREAM);
        checkAllAspectsForResponse(EntityType.DATASTREAM,response);
        response = getEntities(EntityType.SENSOR);
        checkAllAspectsForResponse(EntityType.SENSOR,response);
        response = getEntities(EntityType.OBSERVATION);
        checkAllAspectsForResponse(EntityType.OBSERVATION,response);
        response = getEntities(EntityType.OBSERVED_PROPERTY);
        checkAllAspectsForResponse(EntityType.OBSERVED_PROPERTY,response);
        response = getEntities(EntityType.FEATURE_OF_INTEREST);
        checkAllAspectsForResponse(EntityType.FEATURE_OF_INTEREST,response);
    }

    @Test(description = "Check Service Root UI")
    public void checkServiceRootUri(){
        try {
            String response = getEntities(null);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            Map<String,Boolean> addedLinks = new HashMap<>();
            addedLinks.put("Things",false);
            addedLinks.put("Locations",false);
            addedLinks.put("HistoricalLocations",false);
            addedLinks.put("Datastreams",false);
            addedLinks.put("Sensors",false);
            addedLinks.put("Observations",false);
            addedLinks.put("ObservedProperties",false);
            addedLinks.put("FeaturesOfInterest",false);
            for (int i = 0; i < entities.length(); i++) {
                JSONObject entity = entities.getJSONObject(i);
                Assert.assertTrue(entity.get("name")!=null);
                Assert.assertTrue(entity.get("url")!=null);
                String name = entity.getString("name");
                String nameUrl = entity.getString("url");
                switch (name){
                    case "Things":
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
            }
            for(String key: addedLinks.keySet()){
                Assert.assertTrue(addedLinks.get(key), "The Service Root URI response does not contain "+key);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEntities(EntityType entityType){
        String urlString = rootUri;
        if(entityType != null) { // It is not Service Root URI
            switch (entityType) {
                case THING:
                    urlString += "/Things";
                    break;
                case LOCATION:
                    urlString += "/Locations";
                    break;
                case HISTORICAL_LOCATION:
                    urlString += "/HistoricalLocations";
                    break;
                case DATASTREAM:
                    urlString += "/Datastreams";
                    break;
                case SENSOR:
                    urlString += "/Sensors";
                    break;
                case OBSERVATION:
                    urlString += "/Observations";
                    break;
                case OBSERVED_PROPERTY:
                    urlString += "/ObservedProperties";
                    break;
                case FEATURE_OF_INTEREST:
                    urlString += "/FeaturesOfInterest";
                    break;
                default:
                    Assert.fail("Entity type is not recognized in SensorThings API : " + entityType);
                    return null;
            }
        }
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
            if(entityType != null) {
                Assert.assertTrue(response.indexOf("value") != -1, "The GET entities response for entity type \"" + entityType + "\" does not match SensorThings API : missing \"value\" in response.");
            } else { // GET Service Base URI
                Assert.assertTrue(response.indexOf("value") != -1, "The GET entities response for service root URI does not match SensorThings API : missing \"value\" in response.");
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }



    public void checkAllAspectsForResponse(EntityType entityType, String response){
        checkControlInformation(response);
        checkEntityProperties(entityType, response);
        checkEntityRelations(entityType, response);
    }



    public void checkControlInformation(String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            for (int i = 0; i < entities.length(); i++) {
                JSONObject entity = entities.getJSONObject(i);
                Assert.assertTrue(entity.get(ControlInformation.ID)!=null , "The entity does not have mandatory control information : "+ControlInformation.ID);
                Assert.assertTrue(entity.get(ControlInformation.SELF_LINK)!=null , "The entity does not have mandatory control information : "+ControlInformation.SELF_LINK);
                //TODO: This line should be un-commented when the navigationLink is changed to annotation
               // Assert.assertTrue(entity.get(ControlInformation.NAVIGATION_LINK)!=null , "The entity does not have mandatory control information : "+ControlInformation.NAVIGATION_LINK);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkEntityProperties(EntityType entityType, String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            switch (entityType){
                case THING:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String property : EntityProperties.THING_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    }
                    break;
                case LOCATION:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String property : EntityProperties.LOCATION_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    }
                    break;
                case HISTORICAL_LOCATION:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String property : EntityProperties.HISTORICAL_LOCATION_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    }
                    break;
                case DATASTREAM:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String property : EntityProperties.DATASTREAM_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    }
                    break;
                case SENSOR:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String property : EntityProperties.SENSOR_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    }
                    break;
                case OBSERVATION:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String property : EntityProperties.OBSERVATION_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    }
                    break;
                case OBSERVED_PROPERTY:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String property : EntityProperties.OBSERVED_PROPETY_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String property : EntityProperties.FEATURE_OF_INTEREST_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
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
                        for (String relation : EntityRelations.THING_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }
                    }
                    break;
                case LOCATION:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String relation : EntityRelations.LOCATION_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }
                    }
                    break;
                case HISTORICAL_LOCATION:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String relation : EntityRelations.HISTORICAL_LOCATION_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }
                    }
                    break;
                case DATASTREAM:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String relation : EntityRelations.DATASTREAM_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }
                    }
                    break;
                case SENSOR:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String relation : EntityRelations.SENSOR_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }
                    }
                    break;
                case OBSERVATION:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String relation : EntityRelations.OBSERVATION_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }
                    }
                    break;
                case OBSERVED_PROPERTY:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String relation : EntityRelations.OBSERVED_PROPERTY_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    for (int i = 0; i < entities.length(); i++) {
                        JSONObject entity = entities.getJSONObject(i);
                        for (String relation : EntityRelations.FEATURE_OF_INTEREST_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
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
