package org.opengis.cite.sta10.filteringExtension;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opengis.cite.sta10.SuiteAttribute;
import org.opengis.cite.sta10.util.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Includes various tests of capability 3.
 */
public class Capability3Tests {

    public String rootUri="http://192.168.1.13:8080/OGCSensorThings/v1.0";

//    @BeforeClass
//    public void obtainTestSubject(ITestContext testContext) {
//        Object obj = testContext.getSuite().getAttribute(
//                SuiteAttribute.LEVEL.getName());
//        if ((null != obj)) {
//            Integer level = Integer.class.cast(obj);
//            Assert.assertTrue(level.intValue() >= 3,
//                    "Conformance level 3 will not be checked since ics = " + level);
//        }
//
//        rootUri = testContext.getSuite().getAttribute(
//                SuiteAttribute.TEST_SUBJECT.getName()).toString();
//        rootUri = rootUri.trim();
//        if(rootUri.lastIndexOf('/')==rootUri.length()-1) {
//            rootUri = rootUri.substring(0, rootUri.length() - 1);
//        }
//
//    }

    @Test(description = "GET Entities with $select", groups = "level-3")
    public void readEntitiesAndCheckResponse() {
        List<String> selectedProperties = new ArrayList<>();
        selectedProperties.add("description");
        String response = getEntities(EntityType.THING);
        checkEntitiesAllAspectsForResponse(EntityType.THING, response, selectedProperties);
//        response = getEntities(EntityType.LOCATION);
//        checkEntitiesAllAspectsForResponse(EntityType.LOCATION, response, selectedProperties);
//        response = getEntities(EntityType.HISTORICAL_LOCATION);
//        checkEntitiesAllAspectsForResponse(EntityType.HISTORICAL_LOCATION, response, selectedProperties);
//        response = getEntities(EntityType.DATASTREAM);
//        checkEntitiesAllAspectsForResponse(EntityType.DATASTREAM, response, selectedProperties);
//        response = getEntities(EntityType.SENSOR);
//        checkEntitiesAllAspectsForResponse(EntityType.SENSOR, response, selectedProperties);
//        response = getEntities(EntityType.OBSERVATION);
//        checkEntitiesAllAspectsForResponse(EntityType.OBSERVATION, response, selectedProperties);
//        response = getEntities(EntityType.OBSERVED_PROPERTY);
//        checkEntitiesAllAspectsForResponse(EntityType.OBSERVED_PROPERTY, response, selectedProperties);
//        response = getEntities(EntityType.FEATURE_OF_INTEREST);
//        checkEntitiesAllAspectsForResponse(EntityType.FEATURE_OF_INTEREST, response, selectedProperties);
    }

//    @Test(description = "GET nonexistent Entity", groups = "level-1")
//    public void readNonexistentEntity() {
//        readNonexistentEntityWithEntityType(EntityType.THING);
//        readNonexistentEntityWithEntityType(EntityType.LOCATION);
//        readNonexistentEntityWithEntityType(EntityType.HISTORICAL_LOCATION);
//        readNonexistentEntityWithEntityType(EntityType.DATASTREAM);
//        readNonexistentEntityWithEntityType(EntityType.SENSOR);
//        readNonexistentEntityWithEntityType(EntityType.OBSERVATION);
//        readNonexistentEntityWithEntityType(EntityType.OBSERVED_PROPERTY);
//        readNonexistentEntityWithEntityType(EntityType.FEATURE_OF_INTEREST);
//    }
//
//    @Test(description = "GET Specific Entity", groups = "level-1")
//    public void readEntityAndCheckResponse() {
//        List<String> selectedProperties = new ArrayList<>();
//        String response = readEntityWithEntityType(EntityType.THING);
//        checkEntityAllAspectsForResponse(EntityType.THING, response, selectedProperties);
//        response = readEntityWithEntityType(EntityType.LOCATION);
//        checkEntityAllAspectsForResponse(EntityType.LOCATION, response, selectedProperties);
//        response = readEntityWithEntityType(EntityType.HISTORICAL_LOCATION);
//        checkEntityAllAspectsForResponse(EntityType.HISTORICAL_LOCATION, response, selectedProperties);
//        response = readEntityWithEntityType(EntityType.DATASTREAM);
//        checkEntityAllAspectsForResponse(EntityType.DATASTREAM, response, selectedProperties);
//        response = readEntityWithEntityType(EntityType.SENSOR);
//        checkEntityAllAspectsForResponse(EntityType.SENSOR, response, selectedProperties);
//        response = readEntityWithEntityType(EntityType.OBSERVATION);
//        checkEntityAllAspectsForResponse(EntityType.OBSERVATION, response, selectedProperties);
//        response = readEntityWithEntityType(EntityType.OBSERVED_PROPERTY);
//        checkEntityAllAspectsForResponse(EntityType.OBSERVED_PROPERTY, response, selectedProperties);
//        response = readEntityWithEntityType(EntityType.FEATURE_OF_INTEREST);
//        checkEntityAllAspectsForResponse(EntityType.FEATURE_OF_INTEREST, response, selectedProperties);
//    }

//    @Test(description = "GET Propety of an Entity", groups = "level-1")
//    public void readPropertyOfEntityAndCheckResponse(){
//        readPropertyOfEntityWithEntityType(EntityType.THING);
//        readPropertyOfEntityWithEntityType(EntityType.LOCATION);
//        readPropertyOfEntityWithEntityType(EntityType.HISTORICAL_LOCATION);
//        readPropertyOfEntityWithEntityType(EntityType.DATASTREAM);
//        readPropertyOfEntityWithEntityType(EntityType.OBSERVED_PROPERTY);
//        readPropertyOfEntityWithEntityType(EntityType.SENSOR);
//        readPropertyOfEntityWithEntityType(EntityType.OBSERVATION);
//        readPropertyOfEntityWithEntityType(EntityType.FEATURE_OF_INTEREST);
//    }

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
            Map<String,Object> responseMap = getEntity(entityType, id, property);
            int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
            Assert.assertEquals(responseCode, 200, "Reading property \"" + property + "\" of the exitixting " + entityType.name() + " with id " + id + " failed.");
            String response = responseMap.get("response").toString();
            JSONObject entity = null;
            entity = new JSONObject(response);
            Assert.assertTrue(entity.get(property)!=null, "Reading property \""+ property+"\"of \"" +entityType+"\" fails.");
            Assert.assertEquals(entity.length(), 1, "The response for getting property "+property+" of a "+entityType+" returns more properties!");
        } catch (JSONException e) {
            e.printStackTrace();
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
//
//    @Test(description = "GET Related Entity of an Entity", groups = "level-1")
//    public void readRelatedEntityOfEntityAndCheckResponse(){
//        readRelatedEntityOfEntityWithEntityType(EntityType.THING);
//        readRelatedEntityOfEntityWithEntityType(EntityType.LOCATION);
//        readRelatedEntityOfEntityWithEntityType(EntityType.HISTORICAL_LOCATION);
//        readRelatedEntityOfEntityWithEntityType(EntityType.DATASTREAM);
//        readRelatedEntityOfEntityWithEntityType(EntityType.OBSERVED_PROPERTY);
//        readRelatedEntityOfEntityWithEntityType(EntityType.SENSOR);
//        readRelatedEntityOfEntityWithEntityType(EntityType.OBSERVATION);
//        readRelatedEntityOfEntityWithEntityType(EntityType.FEATURE_OF_INTEREST);
//    }

    public void readRelatedEntityOfEntityWithEntityType(EntityType entityType) {
        try {
            String response = getEntities(entityType);
            Long id = new JSONObject(response).getJSONArray("value").getJSONObject(0).getLong("id");
            switch (entityType){
                case THING:
                    for (String relation : EntityRelations.THING_RELATIONS) {
                        checkGetNavigationLinkOfEntity(entityType, id, relation);
                        checkGetAssociationOfEntity(entityType, id, relation);
                    }
                    break;
                case LOCATION:
                    for (String relation : EntityRelations.LOCATION_RELATIONS) {
                        checkGetNavigationLinkOfEntity(entityType, id, relation);
                        checkGetAssociationOfEntity(entityType, id, relation);
                    }
                    break;
                case HISTORICAL_LOCATION:
                    for (String relation : EntityRelations.HISTORICAL_LOCATION_RELATIONS) {
                        checkGetNavigationLinkOfEntity(entityType, id, relation);
                        checkGetAssociationOfEntity(entityType, id, relation);
                    }
                    break;
                case DATASTREAM:
                    for (String relation : EntityRelations.DATASTREAM_RELATIONS) {
                        checkGetNavigationLinkOfEntity(entityType, id, relation);
                        checkGetAssociationOfEntity(entityType, id, relation);
                    }
                    break;
                case SENSOR:
                    for (String relation : EntityRelations.SENSOR_RELATIONS) {
                        checkGetNavigationLinkOfEntity(entityType, id, relation);
                        checkGetAssociationOfEntity(entityType, id, relation);
                    }
                    break;
                case OBSERVATION:
                    for (String relation : EntityRelations.OBSERVATION_RELATIONS) {
                        checkGetNavigationLinkOfEntity(entityType, id, relation);
                        checkGetAssociationOfEntity(entityType, id, relation);
                    }
                    break;
                case OBSERVED_PROPERTY:
                    for (String relation : EntityRelations.OBSERVED_PROPERTY_RELATIONS) {
                        checkGetNavigationLinkOfEntity(entityType, id, relation);
                        checkGetAssociationOfEntity(entityType, id, relation);
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    for (String relation : EntityRelations.FEATURE_OF_INTEREST_RELATIONS) {
                        checkGetNavigationLinkOfEntity(entityType, id, relation);
                        checkGetAssociationOfEntity(entityType, id, relation);
                    }
                    break;
                default:
                    break;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void checkGetNavigationLinkOfEntity(EntityType entityType, long id, String relation){
        int responseCode = Integer.parseInt(getEntity(entityType, id, relation).get("response-code").toString());
        Assert.assertTrue(responseCode==200 /*|| (relation.lastIndexOf("s")!= relation.length()-1 && responseCode==404)*/ , "Reading relation \"" + relation + "\" of the exitixting " + entityType.name() + " with id " + id + " failed.");
    }

    public void checkGetAssociationOfEntity(EntityType entityType, long id, String relation) {
        if(!relation.endsWith("s")){
            return;
        }
        try {
            Map<String,Object> responseMap = getEntity(entityType, id, relation + "/$ref");
            int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
                    Assert.assertEquals(responseCode, 200, "Reading Association Link of \"" + relation + "\" of the exitixting " + entityType.name() + " with id " + id + " failed.");
            String response = responseMap.get("response").toString();
            Assert.assertTrue(response.indexOf("value") != -1, "The GET entities Association Link response for "+entityType+"("+id+")/"+relation+" does not match SensorThings API : missing \"value\" in response.");
            JSONArray value = new JSONObject(response).getJSONArray("value");
            for (int i = 0; i < value.length() ; i++) {
                JSONObject obj = value.getJSONObject(i);
                Assert.assertTrue(obj.get(ControlInformation.SELF_LINK)!=null, "The Association Link for "+entityType+"("+id+")/"+relation+" does not contain self-links.");
                Assert.assertEquals(obj.length(), 1, "The Association Link for "+entityType+"("+id+")/"+relation+" contains properties other than self-link.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String readEntityWithEntityType(EntityType entityType) {
        try {
            String response = getEntities(entityType);
            Long id = new JSONObject(response).getJSONArray("value").getJSONObject(0).getLong("id");
            Map<String,Object> responseMap = getEntity(entityType, id, null);
            int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
            Assert.assertEquals(responseCode, 200, "Reading exitixting " + entityType.name() + " with id " + id + " failed.");
            response = responseMap.get("response").toString();
            return response;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void readNonexistentEntityWithEntityType(EntityType entityType) {
        long id = Long.MAX_VALUE;
        int responseCode = Integer.parseInt(getEntity(entityType, id, null).get("response-code").toString());
        Assert.assertEquals(responseCode, 404, "Reading non-exitixting " + entityType.name() + " with id " + id + " failed.");
    }

//    @Test(description = "Check Service Root UI", groups = "level-1")
//    public void checkServiceRootUri() {
//        try {
//            String response = getEntities(null);
//            JSONObject jsonResponse = new JSONObject(response);
//            JSONArray entities = jsonResponse.getJSONArray("value");
//            Map<String, Boolean> addedLinks = new HashMap<>();
//            addedLinks.put("Things", false);
//            addedLinks.put("Locations", false);
//            addedLinks.put("HistoricalLocations", false);
//            addedLinks.put("Datastreams", false);
//            addedLinks.put("Sensors", false);
//            addedLinks.put("Observations", false);
//            addedLinks.put("ObservedProperties", false);
//            addedLinks.put("FeaturesOfInterest", false);
//            for (int i = 0; i < entities.length(); i++) {
//                JSONObject entity = entities.getJSONObject(i);
//                Assert.assertTrue(entity.get("name") != null);
//                Assert.assertTrue(entity.get("url") != null);
//                String name = entity.getString("name");
//                String nameUrl = entity.getString("url");
//                switch (name) {
//                    case "Things":
//                        Assert.assertEquals(nameUrl, rootUri + "/Things", "The URL for Things in Service Root URI is not compliant to SensorThings API.");
//                        addedLinks.remove("Things");
//                        addedLinks.put(name, true);
//                        break;
//                    case "Locations":
//                        Assert.assertEquals(nameUrl, rootUri + "/Locations", "The URL for Locations in Service Root URI is not compliant to SensorThings API.");
//                        addedLinks.remove("Locations");
//                        addedLinks.put(name, true);
//                        break;
//                    case "HistoricalLocations":
//                        Assert.assertEquals(nameUrl, rootUri + "/HistoricalLocations", "The URL for HistoricalLocations in Service Root URI is not compliant to SensorThings API.");
//                        addedLinks.remove("HistoricalLocations");
//                        addedLinks.put(name, true);
//                        break;
//                    case "Datastreams":
//                        Assert.assertEquals(nameUrl, rootUri + "/Datastreams", "The URL for Datastreams in Service Root URI is not compliant to SensorThings API.");
//                        addedLinks.remove("Datastreams");
//                        addedLinks.put(name, true);
//                        break;
//                    case "Sensors":
//                        Assert.assertEquals(nameUrl, rootUri + "/Sensors", "The URL for Sensors in Service Root URI is not compliant to SensorThings API.");
//                        addedLinks.remove("Sensors");
//                        addedLinks.put(name, true);
//                        break;
//                    case "Observations":
//                        Assert.assertEquals(nameUrl, rootUri + "/Observations", "The URL for Observations in Service Root URI is not compliant to SensorThings API.");
//                        addedLinks.remove("Observations");
//                        addedLinks.put(name, true);
//                        break;
//                    case "ObservedProperties":
//                        Assert.assertEquals(nameUrl, rootUri + "/ObservedProperties", "The URL for ObservedProperties in Service Root URI is not compliant to SensorThings API.");
//                        addedLinks.remove("ObservedProperties");
//                        addedLinks.put(name, true);
//                        break;
//                    case "FeaturesOfInterest":
//                        Assert.assertEquals(nameUrl, rootUri + "/FeaturesOfInterest", "The URL for FeaturesOfInterest in Service Root URI is not compliant to SensorThings API.");
//                        addedLinks.remove("FeaturesOfInterest");
//                        addedLinks.put(name, true);
//                        break;
//                    default:
//                        Assert.fail("There is a component in Service Root URI response that is not in SensorThings API : " + name);
//                        break;
//                }
//            }
//            for (String key : addedLinks.keySet()) {
//                Assert.assertTrue(addedLinks.get(key), "The Service Root URI response does not contain " + key);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    public String getEntities(EntityType entityType) {
        String urlString = rootUri;
        if (entityType != null) {
            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$select=description");
        }
        Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
        String response = responseMap.get("response").toString();
        int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
        Assert.assertEquals(responseCode, 200, "Error during getting entities: " + entityType.name());
        if (entityType != null) {
            Assert.assertTrue(response.indexOf("value") != -1, "The GET entities response for entity type \"" + entityType + "\" does not match SensorThings API : missing \"value\" in response.");
        } else { // GET Service Base URI
            Assert.assertTrue(response.indexOf("value") != -1, "The GET entities response for service root URI does not match SensorThings API : missing \"value\" in response.");
        }
        return response;
    }

    public Map<String,Object> getEntity(EntityType entityType, long id, String property) {
        if (id == -1) {
            return null;
        }
        String urlString = ServiceURLBuilder.buildURLString(rootUri,entityType,id,null,property);
        return HTTPMethods.doGet(urlString);
    }

    public void checkEntitiesAllAspectsForResponse(EntityType entityType, String response, List<String> selectedProperties){
        //checkEntitiesControlInformation(response);
        checkEntitiesProperties(entityType, response, selectedProperties);
        checkEntitiesRelations(entityType, response, selectedProperties);
    }

    public void checkEntityAllAspectsForResponse(EntityType entityType, String response, List<String> selectedProperties){
        checkEntityControlInformation(response);
        checkEntityProperties(entityType, response, selectedProperties);
        checkEntityRelations(entityType, response, selectedProperties);
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

    public void checkEntitiesProperties(EntityType entityType, String response, List<String> selectedProperties){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            for (int i = 0; i < entities.length(); i++) {
                JSONObject entity = entities.getJSONObject(i);
                checkEntityProperties(entityType, entity, selectedProperties);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void checkEntityProperties(EntityType entityType, Object response, List<String> selectedProperties){
        try {
            JSONObject entity = new JSONObject(response.toString());
            switch (entityType){
                case THING:
                        for (String property : EntityProperties.THING_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                Assert.assertTrue(entity.get(property)!=null, "Entity type \""+entityType+"\" does not have selected property: \""+property+"\".");
                            }else{
                                Assert.assertTrue(entity.get(property)==null, "Entity type \""+entityType+"\" contains not-selected property: \""+property+"\".");
                            }
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

    public void checkEntitiesRelations(EntityType entityType, String response, List<String> selectedProperties){
        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONArray entities = jsonResponse.getJSONArray("value");
            for (int i = 0; i < entities.length(); i++) {
                JSONObject entity = entities.getJSONObject(i);
                checkEntityRelations(entityType, entity, selectedProperties);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void checkEntityRelations(EntityType entityType, Object response, List<String> selectedProperties){
        try {
            JSONObject entity = new JSONObject(response);
            switch (entityType){
                case THING:
                        for (String relation : EntityRelations.THING_RELATIONS) {
                            if (selectedProperties.contains(relation)) {
                                Assert.assertTrue(entity.get(relation) != null, "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                //TODO: this line must be deleted after adding annotations
                                Assert.assertTrue(entity.getJSONObject(relation).get(ControlInformation.NAVIGATION_LINK) != null);
                            } else {
                                Assert.assertTrue(entity.get(relation) == null, "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                            }
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
