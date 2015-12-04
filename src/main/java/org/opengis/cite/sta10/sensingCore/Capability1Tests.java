package org.opengis.cite.sta10.sensingCore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opengis.cite.sta10.SuiteAttribute;
import org.opengis.cite.sta10.util.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


/**
 * Includes various tests of capability 1.
 */
public class Capability1Tests {

    public String rootUri;//="http://localhost:8080/OGCSensorThings-NewQueries/v1.0";
    private final int resourcePathLevel = 4;

    @BeforeClass
    public void obtainTestSubject(ITestContext testContext) {
        Object obj = testContext.getSuite().getAttribute(
                SuiteAttribute.LEVEL.getName());
        if ((null != obj)) {
            Integer level = Integer.class.cast(obj);
            Assert.assertTrue(level.intValue() > 0,
                    "Conformance level 1 will not be checked since ics = " + level);
        }

        rootUri = testContext.getSuite().getAttribute(
                SuiteAttribute.TEST_SUBJECT.getName()).toString();
        rootUri = rootUri.trim();
        if(rootUri.lastIndexOf('/')==rootUri.length()-1) {
            rootUri = rootUri.substring(0, rootUri.length() - 1);
        }

    }

    @Test(description = "GET Entities", groups = "level-1")
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

    @Test(description = "GET nonexistent Entity", groups = "level-1")
    public void readNonexistentEntity() {
        readNonexistentEntityWithEntityType(EntityType.THING);
        readNonexistentEntityWithEntityType(EntityType.LOCATION);
        readNonexistentEntityWithEntityType(EntityType.HISTORICAL_LOCATION);
        readNonexistentEntityWithEntityType(EntityType.DATASTREAM);
        readNonexistentEntityWithEntityType(EntityType.SENSOR);
        readNonexistentEntityWithEntityType(EntityType.OBSERVATION);
        readNonexistentEntityWithEntityType(EntityType.OBSERVED_PROPERTY);
        readNonexistentEntityWithEntityType(EntityType.FEATURE_OF_INTEREST);
    }

    @Test(description = "GET Specific Entity", groups = "level-1")
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

    @Test(description = "GET Propety of an Entity", groups = "level-1")
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
            Long id = new JSONObject(response).getJSONArray("value").getJSONObject(0).getLong(ControlInformation.ID);
            for (String property : EntityProperties.getPropertiesListFor(entityType)) {
                checkGetPropertyOfEntity(entityType, id, property);
                checkGetPropertyValueOfEntity(entityType, id, property);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    public void checkGetPropertyOfEntity(EntityType entityType, long id, String property){
        try {
            Map<String,Object> responseMap = getEntity(entityType, id, property);
            int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
            Assert.assertEquals(responseCode, 200, "Reading property \"" + property + "\" of the exitixting " + entityType.name() + " with id " + id + " failed.");
            String response = responseMap.get("response").toString();
            JSONObject entity = null;
            entity = new JSONObject(response.toString());
            try {
                Assert.assertNotNull(entity.get(property), "Reading property \"" + property + "\"of \"" + entityType + "\" fails.");
            } catch (JSONException e){
                Assert.fail("Reading property \"" + property + "\"of \"" + entityType + "\" fails.");
            }
            Assert.assertEquals(entity.length(), 1, "The response for getting property "+property+" of a "+entityType+" returns more properties!");
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    public void checkGetPropertyValueOfEntity(EntityType entityType, long id, String property) {
        Map<String,Object> responseMap = getEntity(entityType, id, property+"/$value");
        int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
        Assert.assertEquals(responseCode, 200, "Reading property value of \"" + property + "\" of the exitixting " + entityType.name() + " with id " + id + " failed.");
        String response = responseMap.get("response").toString();
        if(!property.equals("location") && !property.equals("feature") && !property.equals("unitOfMeasurement")) {
            Assert.assertEquals(response.indexOf("{"), -1, "Reading property value of \"" + property + "\"of \"" + entityType + "\" fails.");
        } else {
            Assert.assertEquals(response.indexOf("{"), 0, "Reading property value of \"" + property + "\"of \"" + entityType + "\" fails.");
        }
    }

    @Test(description = "GET Related Entity of an Entity", groups = "level-1")
    public void checkResourcePaths(){
        readRelatedEntityOfEntityWithEntityType(EntityType.THING);
        readRelatedEntityOfEntityWithEntityType(EntityType.LOCATION);
        readRelatedEntityOfEntityWithEntityType(EntityType.HISTORICAL_LOCATION);
        readRelatedEntityOfEntityWithEntityType(EntityType.DATASTREAM);
        readRelatedEntityOfEntityWithEntityType(EntityType.OBSERVED_PROPERTY);
        readRelatedEntityOfEntityWithEntityType(EntityType.SENSOR);
        readRelatedEntityOfEntityWithEntityType(EntityType.OBSERVATION);
        readRelatedEntityOfEntityWithEntityType(EntityType.FEATURE_OF_INTEREST);
    }

    public void readRelatedEntityOfEntityWithEntityType(EntityType entityType) {
        List<String> entityTypes = new ArrayList<>();
        List<Long> ids = new ArrayList<>();
        switch (entityType) {
            case THING:
                entityTypes.add( "Things");
                break;
            case LOCATION:
                entityTypes.add("Locations");
                break;
            case HISTORICAL_LOCATION:
                entityTypes.add( "HistoricalLocations");
                break;
            case DATASTREAM:
                entityTypes.add("Datastreams");
                break;
            case SENSOR:
                entityTypes.add( "Sensors");
                break;
            case OBSERVATION:
                entityTypes.add( "Observations");
                break;
            case OBSERVED_PROPERTY:
                entityTypes.add( "ObservedProperties");
                break;
            case FEATURE_OF_INTEREST:
                entityTypes.add( "FeaturesOfInterest");
                break;
            default:
                Assert.fail("Entity type is not recognized in SensorThings API : " + entityType);
        }
        readRelatedEntity(entityTypes,ids);
    }

    public void readRelatedEntity(List<String> entityTypes, List<Long> ids){
        if(entityTypes.size() > resourcePathLevel){
            return;
        }
        try {
            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityTypes, ids, null);
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            Assert.assertEquals(responseMap.get("response-code"),200, "Reading relation of the entity failed: "+entityTypes.toString());
            String response = responseMap.get("response").toString();
            if(!entityTypes.get(entityTypes.size()-1).toLowerCase().equals("featuresofinterest") && !entityTypes.get(entityTypes.size()-1).endsWith("s")){
                return;
            }
            Long id = new JSONObject(response.toString()).getJSONArray("value").getJSONObject(0).getLong(ControlInformation.ID);

            //check $ref
            urlString = ServiceURLBuilder.buildURLString(rootUri, entityTypes, ids, "$ref");
            responseMap = HTTPMethods.doGet(urlString);
            Assert.assertEquals(responseMap.get("response-code"),200, "Reading relation of the entity failed: "+entityTypes.toString());
            response = responseMap.get("response").toString();
            checkAssociationLinks(response, entityTypes, ids);

            if(entityTypes.size()==resourcePathLevel){
                return;
            }
            ids.add(id);
            for(String relation: EntityRelations.getRelationsListFor(entityTypes.get(entityTypes.size()-1))){
                entityTypes.add(relation);
                readRelatedEntity(entityTypes, ids);
                entityTypes.remove(entityTypes.size()-1);
            }
            ids.remove(ids.size()-1);
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }

    }

    public void checkAssociationLinks(String response, List<String> entityTypes, List<Long> ids) {

        try {
            Assert.assertTrue(response.indexOf("value") != -1, "The GET entities Association Link response does not match SensorThings API : missing \"value\" in response.: "+entityTypes.toString()+ids.toString());
            JSONArray value = new JSONObject(response.toString()).getJSONArray("value");
            int count = 0;
            for (int i = 0; i < value.length()&& count < 2 ; i++) {
                count ++;
                JSONObject obj = value.getJSONObject(i);
                try {
                    Assert.assertNotNull(obj.get(ControlInformation.SELF_LINK), "The Association Link does not contain self-links.: "+entityTypes.toString()+ids.toString());
                }catch (JSONException e){
                    Assert.fail("The Association Link does not contain self-links.: "+entityTypes.toString()+ids.toString());
                }
                Assert.assertEquals(obj.length(), 1, "The Association Link contains properties other than self-link.: "+entityTypes.toString()+ids.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    public String readEntityWithEntityType(EntityType entityType) {
        try {
            String response = getEntities(entityType);
            Long id = new JSONObject(response.toString()).getJSONArray("value").getJSONObject(0).getLong(ControlInformation.ID);
            Map<String,Object> responseMap = getEntity(entityType, id, null);
            int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
            Assert.assertEquals(responseCode, 200, "Reading exitixting " + entityType.name() + " with id " + id + " failed.");
            response = responseMap.get("response").toString();
            return response;
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            return null;
        }
    }

    public void readNonexistentEntityWithEntityType(EntityType entityType) {
        long id = Long.MAX_VALUE;
        int responseCode = Integer.parseInt(getEntity(entityType, id, null).get("response-code").toString());
        Assert.assertEquals(responseCode, 404, "Reading non-exitixting " + entityType.name() + " with id " + id + " failed.");
    }

    @Test(description = "Check Service Root UI", groups = "level-1")
    public void checkServiceRootUri() {
        try {
            String response = getEntities(null);
            JSONObject jsonResponse = new JSONObject(response.toString());
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
                try {
                    Assert.assertNotNull(entity.get("name"));
                    Assert.assertNotNull(entity.get("url"));
                } catch (JSONException e){
                    Assert.fail("Service root URI does not have proper JSON keys: name and value.");
                }
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
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    public String getEntities(EntityType entityType) {
        String urlString = rootUri;
        if(entityType!= null) {
            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, null);
        }
        Map<String,Object> responseMap = HTTPMethods.doGet(urlString);
        String response = responseMap.get("response").toString();
        int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
        Assert.assertEquals(responseCode, 200, "Error during getting entities: " + ((entityType!=null)?entityType.name():"root URI"));
        if (entityType != null) {
            Assert.assertTrue(response.indexOf("value") != -1, "The GET entities response for entity type \"" + entityType + "\" does not match SensorThings API : missing \"value\" in response.");
        } else { // GET Service Base URI
            Assert.assertTrue(response.indexOf("value") != -1, "The GET entities response for service root URI does not match SensorThings API : missing \"value\" in response.");
        }
        return response.toString();
    }

    public Map<String,Object> getEntity(EntityType entityType, long id, String property) {
        if (id == -1) {
            return null;
        }
        String urlString = ServiceURLBuilder.buildURLString(rootUri,entityType,id,null,property);
        return HTTPMethods.doGet(urlString);
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
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray entities = jsonResponse.getJSONArray("value");
            int count = 0;
            for (int i = 0; i < entities.length() && count <2; i++) {
                count ++;
                JSONObject entity = entities.getJSONObject(i);
                checkEntityControlInformation(entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    public void checkEntityControlInformation(Object response){
        try {
            JSONObject entity = new JSONObject(response.toString());
            try {
                Assert.assertNotNull(entity.get(ControlInformation.ID), "The entity does not have mandatory control information : " + ControlInformation.ID);
            } catch (JSONException e){
                Assert.fail("The entity does not have mandatory control information : " + ControlInformation.ID);
            }
            try {
                Assert.assertNotNull(entity.get(ControlInformation.SELF_LINK), "The entity does not have mandatory control information : " + ControlInformation.SELF_LINK);
            }catch (JSONException e){
                Assert.fail("The entity does not have mandatory control information : " + ControlInformation.SELF_LINK);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    public void checkEntitiesProperties(EntityType entityType, String response){
        try {
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray entities = jsonResponse.getJSONArray("value");
            int count = 0;
            for (int i = 0; i < entities.length() && count<2; i++) {
                count++;
                JSONObject entity = entities.getJSONObject(i);
                checkEntityProperties(entityType, entity);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }

    }

    public void checkEntityProperties(EntityType entityType, Object response){
        try {
            JSONObject entity = new JSONObject(response.toString());
            for (String property : EntityProperties.getPropertiesListFor(entityType)) {
                try {
                    Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have mandatory property: \"" + property + "\".");
                }catch (JSONException e){
                    Assert.fail("Entity type \"" + entityType + "\" does not have mandatory property: \"" + property + "\".");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }

    }

    public void checkEntitiesRelations(EntityType entityType, String response){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            int count = 0;
            for (int i = 0; i < entities.length() && count < 2; i++) {
                count ++;
                JSONObject entity = entities.getJSONObject(i);
                checkEntityRelations(entityType, entity);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }

    }

    public void checkEntityRelations(EntityType entityType, Object response){
        try {
            JSONObject entity = new JSONObject(response.toString());
            for (String relation : EntityRelations.getRelationsListFor(entityType)) {
                try {
                    Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have mandatory relation: \"" + relation + "\".");
                } catch (JSONException e){
                    Assert.fail("Entity type \"" + entityType + "\" does not have mandatory relation: \"" + relation + "\".");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

}
