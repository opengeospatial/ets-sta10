package org.opengis.cite.sta10.sensingCore;

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
public class Capability1Tests {

    public final String rootUri = "http://chashuhotpot.sensorup.com/OGCSensorThings/v1.0";


    @Test(description = "GET Entities")
    public void readEntitiesAndCheckResponse() {
        String response = getEntities(EntityType.THING);
        checkEntitiesAllAspectsForResponse(EntityType.THING, response);
        response = getEntities(EntityType.LOCATION);
        checkEntitiesAllAspectsForResponse(EntityType.LOCATION, response);
        response = getEntities(EntityType.HISTORICAL_LOCATION);
        checkEntitiesAllAspectsForResponse(EntityType.HISTORICAL_LOCATION, response);
        response = getEntities(EntityType.DATASTREAM);
        checkEntitiesAllAspectsForResponse(EntityType.DATASTREAM, response);
        response = getEntities(EntityType.SENSOR);
        checkEntitiesAllAspectsForResponse(EntityType.SENSOR, response);
        response = getEntities(EntityType.OBSERVATION);
        checkEntitiesAllAspectsForResponse(EntityType.OBSERVATION, response);
        response = getEntities(EntityType.OBSERVED_PROPERTY);
        checkEntitiesAllAspectsForResponse(EntityType.OBSERVED_PROPERTY, response);
        response = getEntities(EntityType.FEATURE_OF_INTEREST);
        checkEntitiesAllAspectsForResponse(EntityType.FEATURE_OF_INTEREST, response);
    }

    @Test(description = "GET not-existed Entity")
    public void readNotExistedEntity() {
        readNotExistedEntityWithEntityType(EntityType.THING);
        readNotExistedEntityWithEntityType(EntityType.LOCATION);
        readNotExistedEntityWithEntityType(EntityType.HISTORICAL_LOCATION);
        readNotExistedEntityWithEntityType(EntityType.DATASTREAM);
        readNotExistedEntityWithEntityType(EntityType.SENSOR);
        readNotExistedEntityWithEntityType(EntityType.OBSERVATION);
        readNotExistedEntityWithEntityType(EntityType.OBSERVED_PROPERTY);
        readNotExistedEntityWithEntityType(EntityType.FEATURE_OF_INTEREST);
    }

    @Test(description = "GET Specific Entity")
    public void readEntityAndCheckResponse() {
        String response = readEntityWithEntityType(EntityType.THING);
        checkEntityAllAspectsForResponse(EntityType.THING, response);
        response = readEntityWithEntityType(EntityType.LOCATION);
        checkEntityAllAspectsForResponse(EntityType.LOCATION, response);
        response = readEntityWithEntityType(EntityType.HISTORICAL_LOCATION);
        checkEntityAllAspectsForResponse(EntityType.HISTORICAL_LOCATION, response);
        response = readEntityWithEntityType(EntityType.DATASTREAM);
        checkEntityAllAspectsForResponse(EntityType.DATASTREAM, response);
        response = readEntityWithEntityType(EntityType.SENSOR);
        checkEntityAllAspectsForResponse(EntityType.SENSOR, response);
        response = readEntityWithEntityType(EntityType.OBSERVATION);
        checkEntityAllAspectsForResponse(EntityType.OBSERVATION, response);
        response = readEntityWithEntityType(EntityType.OBSERVED_PROPERTY);
        checkEntityAllAspectsForResponse(EntityType.OBSERVED_PROPERTY, response);
        response = readEntityWithEntityType(EntityType.FEATURE_OF_INTEREST);
        checkEntityAllAspectsForResponse(EntityType.FEATURE_OF_INTEREST, response);
    }

    @Test(description = "GET Propety of an Entity")
    public void readPropertyOfEntityAndCheckResponse(){
        readPropertyOfEntityWithEntityType(EntityType.THING);
        readPropertyOfEntityWithEntityType(EntityType.LOCATION);
        readPropertyOfEntityWithEntityType(EntityType.HISTORICAL_LOCATION);
        readPropertyOfEntityWithEntityType(EntityType.DATASTREAM);
        readPropertyOfEntityWithEntityType(EntityType.OBSERVED_PROPERTY);
        readPropertyOfEntityWithEntityType(EntityType.SENSOR);
        readPropertyOfEntityWithEntityType(EntityType.OBSERVATION);
        readPropertyOfEntityWithEntityType(EntityType.FEATURE_OF_INTEREST);
    }

    public void readPropertyOfEntityWithEntityType(EntityType entityType) {
        try {
            String response = getEntities(entityType);
            Long id = new JSONObject(response).getJSONArray("value").getJSONObject(0).getLong("id");
            switch (entityType){
                case THING:
                    for (String property : EntityProperties.THING_PROPERTIES) {
                        checkGetPropertyOfEntity(entityType, id, property);
                        checkGetPropertyValueOfEntity(entityType, id, property);
                    }
                    break;
                case LOCATION:
                    for (String property : EntityProperties.LOCATION_PROPERTIES) {
                        checkGetPropertyOfEntity(entityType, id, property);
                        checkGetPropertyValueOfEntity(entityType, id, property);
                    }
                    break;
                case HISTORICAL_LOCATION:
                    for (String property : EntityProperties.HISTORICAL_LOCATION_PROPERTIES) {
                        checkGetPropertyOfEntity(entityType, id, property);
                        checkGetPropertyValueOfEntity(entityType, id, property);
                    }
                    break;
                case DATASTREAM:
                    for (String property : EntityProperties.DATASTREAM_PROPERTIES) {
                        checkGetPropertyOfEntity(entityType, id, property);
                        checkGetPropertyValueOfEntity(entityType, id, property);
                    }
                    break;
                case SENSOR:
                    for (String property : EntityProperties.SENSOR_PROPERTIES) {
                        checkGetPropertyOfEntity(entityType, id, property);
                        checkGetPropertyValueOfEntity(entityType, id, property);
                    }
                    break;
                case OBSERVATION:
                    for (String property : EntityProperties.OBSERVATION_PROPERTIES) {
                        checkGetPropertyOfEntity(entityType, id, property);
                        checkGetPropertyValueOfEntity(entityType, id, property);
                    }
                    break;
                case OBSERVED_PROPERTY:
                    for (String property : EntityProperties.OBSERVED_PROPETY_PROPERTIES) {
                        checkGetPropertyOfEntity(entityType, id, property);
                        checkGetPropertyValueOfEntity(entityType, id, property);
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    for (String property : EntityProperties.FEATURE_OF_INTEREST_PROPERTIES) {
                        checkGetPropertyOfEntity(entityType, id, property);
                        checkGetPropertyValueOfEntity(entityType, id, property);
                    }
                    break;
                default:
                    break;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkGetPropertyOfEntity(EntityType entityType, long id, String property){
        try {
            int responseCode = getEntityResponseCode(entityType, id, property);
            Assert.assertEquals(responseCode, 200, "Reading property \"" + property + "\" of the exitixting " + entityType.name() + " with id " + id + " failed.");
            String response = getEntityResponse(entityType, id, property);
            JSONObject entity = null;
            entity = new JSONObject(response);
            Assert.assertTrue(entity.get(property)!=null, "Reading property \""+ property+"\"of \"" +entityType+"\" fails.");
            Assert.assertEquals(entity.length(), 1, "The response for getting property "+property+" of a "+entityType+" returns more properties!");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkGetPropertyValueOfEntity(EntityType entityType, long id, String property) {
        int responseCode = getEntityResponseCode(entityType, id, property+"/$value");
        Assert.assertEquals(responseCode, 200, "Reading property value of \"" + property + "\" of the exitixting " + entityType.name() + " with id " + id + " failed.");
        String response = getEntityResponse(entityType, id, property+"/$value");
        if(!property.equals("location") && !property.equals("feature") && !property.equals("unitOfMeasurement")) {
            Assert.assertEquals(response.indexOf("{"), -1, "Reading property value of \"" + property + "\"of \"" + entityType + "\" fails.");
        } else {
            Assert.assertEquals(response.indexOf("{"), 0, "Reading property value of \"" + property + "\"of \"" + entityType + "\" fails.");
        }
    }

    public String readEntityWithEntityType(EntityType entityType) {
        try {
            String response = getEntities(entityType);
            Long id = new JSONObject(response).getJSONArray("value").getJSONObject(0).getLong("id");
            int responseCode = getEntityResponseCode(entityType, id, null);
            Assert.assertEquals(responseCode, 200, "Reading exitixting " + entityType.name() + " with id " + id + " failed.");
            response = getEntityResponse(entityType, id, null);
            return response;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void readNotExistedEntityWithEntityType(EntityType entityType) {
        long id = Long.MAX_VALUE;
        int responseCode = getEntityResponseCode(entityType, id, null);
        Assert.assertEquals(responseCode, 404, "Reading non-exitixting " + entityType.name() + " with id " + id + " failed.");
    }

    @Test(description = "Check Service Root UI")
    public void checkServiceRootUri() {
        try {
            String response = getEntities(null);
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            Map<String, Boolean> addedLinks = new HashMap<>();
            addedLinks.put("Things", false);
            addedLinks.put("Locations", false);
            addedLinks.put("HistoricalLocations", false);
            addedLinks.put("Datastreams", false);
            addedLinks.put("Sensors", false);
            addedLinks.put("Observations", false);
            addedLinks.put("ObservedProperties", false);
            addedLinks.put("FeaturesOfInterest", false);
            for (int i = 0; i < entities.length(); i++) {
                JSONObject entity = entities.getJSONObject(i);
                Assert.assertTrue(entity.get("name") != null);
                Assert.assertTrue(entity.get("url") != null);
                String name = entity.getString("name");
                String nameUrl = entity.getString("url");
                switch (name) {
                    case "Things":
                        Assert.assertEquals(nameUrl, rootUri + "/Things", "The URL for Things in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("Things");
                        addedLinks.put(name, true);
                        break;
                    case "Locations":
                        Assert.assertEquals(nameUrl, rootUri + "/Locations", "The URL for Locations in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("Locations");
                        addedLinks.put(name, true);
                        break;
                    case "HistoricalLocations":
                        Assert.assertEquals(nameUrl, rootUri + "/HistoricalLocations", "The URL for HistoricalLocations in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("HistoricalLocations");
                        addedLinks.put(name, true);
                        break;
                    case "Datastreams":
                        Assert.assertEquals(nameUrl, rootUri + "/Datastreams", "The URL for Datastreams in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("Datastreams");
                        addedLinks.put(name, true);
                        break;
                    case "Sensors":
                        Assert.assertEquals(nameUrl, rootUri + "/Sensors", "The URL for Sensors in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("Sensors");
                        addedLinks.put(name, true);
                        break;
                    case "Observations":
                        Assert.assertEquals(nameUrl, rootUri + "/Observations", "The URL for Observations in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("Observations");
                        addedLinks.put(name, true);
                        break;
                    case "ObservedProperties":
                        Assert.assertEquals(nameUrl, rootUri + "/ObservedProperties", "The URL for ObservedProperties in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("ObservedProperties");
                        addedLinks.put(name, true);
                        break;
                    case "FeaturesOfInterest":
                        Assert.assertEquals(nameUrl, rootUri + "/FeaturesOfInterest", "The URL for FeaturesOfInterest in Service Root URI is not compliant to SensorThings API.");
                        addedLinks.remove("FeaturesOfInterest");
                        addedLinks.put(name, true);
                        break;
                    default:
                        Assert.fail("There is a component in Service Root URI response that is not in SensorThings API : " + name);
                        break;
                }
            }
            for (String key : addedLinks.keySet()) {
                Assert.assertTrue(addedLinks.get(key), "The Service Root URI response does not contain " + key);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getEntities(EntityType entityType) {
        String urlString = rootUri;
        if (entityType != null) { // It is not Service Root URI
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
            connection = (HttpURLConnection) url.openConnection();
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
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            if (entityType != null) {
                Assert.assertTrue(response.indexOf("value") != -1, "The GET entities response for entity type \"" + entityType + "\" does not match SensorThings API : missing \"value\" in response.");
            } else { // GET Service Base URI
                Assert.assertTrue(response.indexOf("value") != -1, "The GET entities response for service root URI does not match SensorThings API : missing \"value\" in response.");
            }
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public int getEntityResponseCode(EntityType entityType, long id, String property) {
        String urlString = rootUri;
        if (id == -1) {
            return -1;
        }
        if (entityType != null) { // It is not Service Root URI
            switch (entityType) {
                case THING:
                    urlString += "/Things(" + id + ")";
                    break;
                case LOCATION:
                    urlString += "/Locations(" + id + ")";
                    break;
                case HISTORICAL_LOCATION:
                    urlString += "/HistoricalLocations(" + id + ")";
                    break;
                case DATASTREAM:
                    urlString += "/Datastreams(" + id + ")";
                    break;
                case SENSOR:
                    urlString += "/Sensors(" + id + ")";
                    break;
                case OBSERVATION:
                    urlString += "/Observations(" + id + ")";
                    break;
                case OBSERVED_PROPERTY:
                    urlString += "/ObservedProperties(" + id + ")";
                    break;
                case FEATURE_OF_INTEREST:
                    urlString += "/FeaturesOfInterest(" + id + ")";
                    break;
                default:
                    Assert.fail("Entity type is not recognized in SensorThings API : " + entityType);
                    return -1;
            }
        }
        if(property != null){
            urlString = urlString + "/" + property;
        }
        HttpURLConnection connection = null;
        int result = -1;
        try {
            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.setUseCaches(false);
            connection.setDoOutput(false);

            result = connection.getResponseCode();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public String getEntityResponse(EntityType entityType, long id, String property) {
        String urlString = rootUri;
        if (id == -1) {
            return null;
        }
        if (entityType != null) { // It is not Service Root URI
            switch (entityType) {
                case THING:
                    urlString += "/Things(" + id + ")";
                    break;
                case LOCATION:
                    urlString += "/Locations(" + id + ")";
                    break;
                case HISTORICAL_LOCATION:
                    urlString += "/HistoricalLocations(" + id + ")";
                    break;
                case DATASTREAM:
                    urlString += "/Datastreams(" + id + ")";
                    break;
                case SENSOR:
                    urlString += "/Sensors(" + id + ")";
                    break;
                case OBSERVATION:
                    urlString += "/Observations(" + id + ")";
                    break;
                case OBSERVED_PROPERTY:
                    urlString += "/ObservedProperties(" + id + ")";
                    break;
                case FEATURE_OF_INTEREST:
                    urlString += "/FeaturesOfInterest(" + id + ")";
                    break;
                default:
                    Assert.fail("Entity type is not recognized in SensorThings API : " + entityType);
                    return null;
            }
        }
        if(property != null){
            urlString = urlString + "/" +property;
        }
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
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
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }




    public void checkEntitiesAllAspectsForResponse(EntityType entityType, String response){
        checkEntitiesControlInformation(response);
        checkEntitiesProperties(entityType, response);
        checkEntitiesRelations(entityType, response);
    }

    public void checkEntityAllAspectsForResponse(EntityType entityType, String response){
        checkEntityControlInformation(response);
        checkEntityProperties(entityType, response);
        checkEntityRelations(entityType, response);
    }



    public void checkEntitiesControlInformation(String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            for (int i = 0; i < entities.length(); i++) {
                JSONObject entity = entities.getJSONObject(i);
                checkEntityControlInformation(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkEntityControlInformation(Object response){
        try {
            JSONObject entity = new JSONObject(response);
            Assert.assertTrue(entity.get(ControlInformation.ID)!=null , "The entity does not have mandatory control information : "+ControlInformation.ID);
            Assert.assertTrue(entity.get(ControlInformation.SELF_LINK)!=null , "The entity does not have mandatory control information : "+ControlInformation.SELF_LINK);
            //TODO: This line should be un-commented when the navigationLink is changed to annotation
            // Assert.assertTrue(entity.get(ControlInformation.NAVIGATION_LINK)!=null , "The entity does not have mandatory control information : "+ControlInformation.NAVIGATION_LINK);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkEntitiesProperties(EntityType entityType, String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            for (int i = 0; i < entities.length(); i++) {
                JSONObject entity = entities.getJSONObject(i);
                checkEntityProperties(entityType, entity);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void checkEntityProperties(EntityType entityType, Object response){
        try {
            JSONObject entity = new JSONObject(response);
            switch (entityType){
                case THING:
                        for (String property : EntityProperties.THING_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    break;
                case LOCATION:
                        for (String property : EntityProperties.LOCATION_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    break;
                case HISTORICAL_LOCATION:
                        for (String property : EntityProperties.HISTORICAL_LOCATION_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    break;
                case DATASTREAM:
                        for (String property : EntityProperties.DATASTREAM_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    break;
                case SENSOR:
                        for (String property : EntityProperties.SENSOR_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    break;
                case OBSERVATION:
                        for (String property : EntityProperties.OBSERVATION_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    break;
                case OBSERVED_PROPERTY:
                        for (String property : EntityProperties.OBSERVED_PROPETY_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    break;
                case FEATURE_OF_INTEREST:
                        for (String property : EntityProperties.FEATURE_OF_INTEREST_PROPERTIES) {
                            Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have mandatory property: \""+property+"\".");
                        }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void checkEntitiesRelations(EntityType entityType, String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            for (int i = 0; i < entities.length(); i++) {
                JSONObject entity = entities.getJSONObject(i);
                checkEntityRelations(entityType, entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void checkEntityRelations(EntityType entityType, Object response){
        try {
            JSONObject entity = new JSONObject(response);
            switch (entityType){
                case THING:
                        for (String relation : EntityRelations.THING_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }

                    break;
                case LOCATION:
                        for (String relation : EntityRelations.LOCATION_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }

                    break;
                case HISTORICAL_LOCATION:
                        for (String relation : EntityRelations.HISTORICAL_LOCATION_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }

                    break;
                case DATASTREAM:
                        for (String relation : EntityRelations.DATASTREAM_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }

                    break;
                case SENSOR:
                        for (String relation : EntityRelations.SENSOR_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }
                    break;
                case OBSERVATION:

                        for (String relation : EntityRelations.OBSERVATION_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }

                    break;
                case OBSERVED_PROPERTY:
                        for (String relation : EntityRelations.OBSERVED_PROPERTY_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
                        }
                    break;
                case FEATURE_OF_INTEREST:
                        for (String relation : EntityRelations.FEATURE_OF_INTEREST_RELATIONS) {
                            Assert.assertTrue(entity.get(relation)!=null, "Entity type \""+entityType+"\" does not have mandatory relation: \""+relation+"\".");
                            //TODO: this line must be deleted after adding annotations
                            Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK)!=null);
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
