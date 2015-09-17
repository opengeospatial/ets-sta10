package org.opengis.cite.sta10.filteringExtension;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opengis.cite.sta10.SuiteAttribute;
import org.opengis.cite.sta10.util.*;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Includes various tests of capability 3.
 */
public class Capability3Tests {

    public String rootUri;//="http://localhost:8080/OGCSensorThings/v1.0";

    long thingId1, thingId2,
            datastreamId1, datastreamId2, datastreamId3, datastreamId4,
            locationId1, locationId2, historicalLocationId1,
            historicalLocationId2, historicalLocationId3, historicalLocationId4,
            sensorId1, sensorId2, sensorId3, sensorId4,
            observedPropertyId1, observedPropertyId2, observedPropertyId3,
            observationId1, observationId2, observationId3, observationId4, observationId5, observationId6, observationId7, observationId8, observationId9, observationId10, observationId11, observationId12,
            featureOfInterestId1, featureOfInterestId2;


    @BeforeClass
    public void obtainTestSubject(ITestContext testContext) {
        Object obj = testContext.getSuite().getAttribute(
                SuiteAttribute.LEVEL.getName());
        if ((null != obj)) {
            Integer level = Integer.class.cast(obj);
            Assert.assertTrue(level.intValue() > 2,
                    "Conformance level 3 will not be checked since ics = " + level);
        }

        rootUri = testContext.getSuite().getAttribute(
                SuiteAttribute.TEST_SUBJECT.getName()).toString();
        rootUri = rootUri.trim();
        if(rootUri.lastIndexOf('/')==rootUri.length()-1) {
            rootUri = rootUri.substring(0, rootUri.length() - 1);
        }
        createEntities();
    }


    @Test(description = "GET Entities with $select", groups = "level-3")
    public void readEntitiesWithSelectQO() {
        checkSelectForEntityType(EntityType.THING);
        checkSelectForEntityType(EntityType.LOCATION);
        checkSelectForEntityType(EntityType.HISTORICAL_LOCATION);
        checkSelectForEntityType(EntityType.DATASTREAM);
        checkSelectForEntityType(EntityType.SENSOR);
        checkSelectForEntityType(EntityType.OBSERVED_PROPERTY);
        checkSelectForEntityType(EntityType.OBSERVATION);
        checkSelectForEntityType(EntityType.FEATURE_OF_INTEREST);

    }

    @Test(description = "GET Entities with $expand", groups = "level-3")
    public void readEntitiesWithExpandQO() {
        checkExpandtForEntityType(EntityType.THING);
        checkExpandtForEntityType(EntityType.LOCATION);
        checkExpandtForEntityType(EntityType.HISTORICAL_LOCATION);
        checkExpandtForEntityType(EntityType.DATASTREAM);
        checkExpandtForEntityType(EntityType.SENSOR);
        checkExpandtForEntityType(EntityType.OBSERVED_PROPERTY);
        checkExpandtForEntityType(EntityType.OBSERVATION);
        checkExpandtForEntityType(EntityType.FEATURE_OF_INTEREST);

    }

    @Test(description = "GET Entities with $top", groups = "level-3")
    public void readEntitiesWithTopQO() {
        checkTopForEntityType(EntityType.THING);
        checkTopForEntityType(EntityType.LOCATION);
        checkTopForEntityType(EntityType.HISTORICAL_LOCATION);
        checkTopForEntityType(EntityType.DATASTREAM);
        checkTopForEntityType(EntityType.SENSOR);
        checkTopForEntityType(EntityType.OBSERVED_PROPERTY);
        checkTopForEntityType(EntityType.OBSERVATION);
        checkTopForEntityType(EntityType.FEATURE_OF_INTEREST);

    }

    private void checkTopForEntityType(EntityType entityType){
        try {
            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=1");
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            String response = responseMap.get("response").toString();
            JSONArray array = new JSONObject(response).getJSONArray("value");
            Assert.assertEquals(array.length(), 1, "Query requested 1 entity but response contains "+array.length());
            try {
                Assert.assertNotNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
            } catch (JSONException e){
                Assert.fail("The response does not have nextLink");
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=2");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            Assert.assertEquals(array.length(), 2, "Query requested 2 entities but response contains "+array.length());
            switch (entityType){
                case THING:
                case LOCATION:
                case FEATURE_OF_INTEREST:
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                default:
                    try {
                        Assert.assertNotNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                        Assert.fail("The response does not have nextLink");
                    }
                    break;
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=3");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            switch (entityType){
                case THING:
                    Assert.assertEquals(array.length(), 2, "Query requested 3 Things, there are only 2 Things,  but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                case LOCATION:
                    Assert.assertEquals(array.length(), 2, "Query requested 3 Locations, there are only 2 Locations,  but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    Assert.assertEquals(array.length(), 2, "Query requested 3 FeaturesOfInterest, there are only 2 FeaturesOfInterest,  but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                case OBSERVED_PROPERTY:
                    Assert.assertEquals(array.length(), 3, "Query requested 3 entities but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                default:
                    Assert.assertEquals(array.length(), 3, "Query requested 3 entities but response contains "+array.length());
                    try {
                        Assert.assertNotNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                        Assert.fail("The response does not have nextLink");
                    }
                    break;
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=4");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            switch (entityType){
                case THING:
                    Assert.assertEquals(array.length(), 2, "Query requested 4 Things, there are only 2 Things,  but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                case LOCATION:
                    Assert.assertEquals(array.length(), 2, "Query requested 4 Locations, there are only 2 Locations,  but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    Assert.assertEquals(array.length(), 2, "Query requested 4 FeaturesOfInterest, there are only 2 FeaturesOfInterest,  but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                case OBSERVED_PROPERTY:
                    Assert.assertEquals(array.length(), 3, "Query requested 4 ObservedProperties, there are only 3 ObservedProperties,  but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                case SENSOR:
                case HISTORICAL_LOCATION:
                case DATASTREAM:
                    Assert.assertEquals(array.length(), 4, "Query requested 4 entities but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                default:
                    Assert.assertEquals(array.length(), 4, "Query requested 4 entities but response contains "+array.length());
                    try {
                        Assert.assertNotNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                        Assert.fail("The response does not have nextLink");
                    }
                    break;
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=5");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            switch (entityType){
                case THING:
                    Assert.assertEquals(array.length(), 2, "Query requested 5 Things, there are only 2 Things,  but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                case LOCATION:
                    Assert.assertEquals(array.length(), 2, "Query requested 5 Locations, there are only 2 Locations,  but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    Assert.assertEquals(array.length(), 2, "Query requested 5 FeaturesOfInterest, there are only 2 FeaturesOfInterest,  but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                case OBSERVED_PROPERTY:
                    Assert.assertEquals(array.length(), 3, "Query requested 5 ObservedProperties, there are only 3 ObservedProperties,  but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                case SENSOR:
                    Assert.assertEquals(array.length(), 4, "Query requested 5 Sensors, there are only 4 Sensors,  but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                case HISTORICAL_LOCATION:
                    Assert.assertEquals(array.length(), 4, "Query requested 5 HistoricalLocations, there are only 4 HistoricalLocations,  but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                case DATASTREAM:
                    Assert.assertEquals(array.length(), 4, "Query requested 5 Datastreams, there are only 4 Datastreams, but response contains "+array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                    }
                    break;
                default:
                    Assert.assertEquals(array.length(), 5, "Query requested 5 entities but response contains "+array.length());
                    try {
                        Assert.assertNotNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e){
                        Assert.fail("The response does not have nextLink");
                    }
                    break;
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=12");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            try {
                Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
            } catch (JSONException e){
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=13");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            try {
                Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
            } catch (JSONException e){
            }
            switch (entityType){
                case THING:
                    Assert.assertEquals(array.length(), 2, "Query requested 13 Things, there are only 2 Things,  but response contains "+array.length());
                    break;
                case LOCATION:
                    Assert.assertEquals(array.length(), 2, "Query requested 13 Locations, there are only 2 Locations,  but response contains "+array.length());
                    break;
                case FEATURE_OF_INTEREST:
                    Assert.assertEquals(array.length(), 2, "Query requested 13 FeaturesOfInterest, there are only 2 FeaturesOfInterest,  but response contains "+array.length());
                    break;
                case OBSERVED_PROPERTY:
                    Assert.assertEquals(array.length(), 3, "Query requested 13 ObservedProperties, there are only 3 ObservedProperties,  but response contains "+array.length());
                    break;
                case SENSOR:
                    Assert.assertEquals(array.length(), 4, "Query requested 13 Sensors, there are only 4 Sensors,  but response contains "+array.length());
                    break;
                case HISTORICAL_LOCATION:
                    Assert.assertEquals(array.length(), 4, "Query requested 13 HistoricalLocations, there are only 4 HistoricalLocations,  but response contains "+array.length());
                    break;
                case DATASTREAM:
                    Assert.assertEquals(array.length(), 4, "Query requested 13 Datastreams, there are only 4 Datastreams, but response contains "+array.length());
                    break;
                case OBSERVATION:
                    Assert.assertEquals(array.length(), 12, "Query requested 13 Observations, there are only 12 Observations, but response contains "+array.length());
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkSelectForEntityType(EntityType entityType){
        List<String> selectedProperties;
        switch(entityType){
            case THING:
                for (String property: EntityProperties.THING_PROPERTIES) {
                    selectedProperties = new ArrayList<>();
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.THING, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.THING, response, selectedProperties);
                }
                selectedProperties = new ArrayList<>();
                for (String property: EntityProperties.THING_PROPERTIES) {
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.THING, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.THING, response, selectedProperties);
                }
                break;
            case LOCATION:
                for (String property: EntityProperties.LOCATION_PROPERTIES) {
                    selectedProperties = new ArrayList<>();
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.LOCATION, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.LOCATION, response, selectedProperties);
                }
                selectedProperties = new ArrayList<>();
                for (String property: EntityProperties.LOCATION_PROPERTIES) {
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.LOCATION, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.LOCATION, response, selectedProperties);
                }
                break;
            case HISTORICAL_LOCATION:
                for (String property: EntityProperties.HISTORICAL_LOCATION_PROPERTIES) {
                    selectedProperties = new ArrayList<>();
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.HISTORICAL_LOCATION, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.HISTORICAL_LOCATION, response, selectedProperties);
                }
                selectedProperties = new ArrayList<>();
                for (String property: EntityProperties.HISTORICAL_LOCATION_PROPERTIES) {
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.HISTORICAL_LOCATION, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.HISTORICAL_LOCATION, response, selectedProperties);
                }
                break;
            case DATASTREAM:
                for (String property: EntityProperties.DATASTREAM_PROPERTIES) {
                    selectedProperties = new ArrayList<>();
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.DATASTREAM, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.DATASTREAM, response, selectedProperties);
                }
                selectedProperties = new ArrayList<>();
                for (String property: EntityProperties.DATASTREAM_PROPERTIES) {
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.DATASTREAM, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.DATASTREAM, response, selectedProperties);
                }
                break;
            case SENSOR:
                for (String property: EntityProperties.SENSOR_PROPERTIES) {
                    selectedProperties = new ArrayList<>();
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.SENSOR, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.SENSOR, response, selectedProperties);
                }
                selectedProperties = new ArrayList<>();
                for (String property: EntityProperties.SENSOR_PROPERTIES) {
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.SENSOR, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.SENSOR, response, selectedProperties);
                }
                break;
            case OBSERVED_PROPERTY:
                for (String property: EntityProperties.OBSERVED_PROPETY_PROPERTIES) {
                    selectedProperties = new ArrayList<>();
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.OBSERVED_PROPERTY, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.OBSERVED_PROPERTY, response, selectedProperties);
                }
                selectedProperties = new ArrayList<>();
                for (String property: EntityProperties.OBSERVED_PROPETY_PROPERTIES) {
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.OBSERVED_PROPERTY, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.OBSERVED_PROPERTY, response, selectedProperties);
                }
                break;
            case OBSERVATION:
                for (String property: EntityProperties.OBSERVATION_PROPERTIES) {
                    selectedProperties = new ArrayList<>();
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.OBSERVATION, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.OBSERVATION, response, selectedProperties);
                }
                selectedProperties = new ArrayList<>();
                for (String property: EntityProperties.OBSERVATION_PROPERTIES) {
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.OBSERVATION, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.OBSERVATION, response, selectedProperties);
                }
                break;
            case FEATURE_OF_INTEREST:
                for (String property: EntityProperties.FEATURE_OF_INTEREST_PROPERTIES) {
                    selectedProperties = new ArrayList<>();
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.FEATURE_OF_INTEREST, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.FEATURE_OF_INTEREST, response, selectedProperties);
                }
                selectedProperties = new ArrayList<>();
                for (String property: EntityProperties.FEATURE_OF_INTEREST_PROPERTIES) {
                    selectedProperties.add(property);
                    String response = getEntities(EntityType.FEATURE_OF_INTEREST, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(EntityType.FEATURE_OF_INTEREST, response, selectedProperties);
                }
                break;
        }
    }

    public String getEntities(EntityType entityType, List<String> selectedProperties, List<String> expandedRelations) {
        String urlString = rootUri;
        String selectString="";
        if(selectedProperties!=null && selectedProperties.size()>0) {
            selectString = "?$select=";
            for (String select : selectedProperties) {
                if (selectString.charAt(selectString.length() - 1) != '=') {
                    selectString += ',';
                }
                selectString += select;
            }
        }
        String expandString="";
        if(expandedRelations!=null && expandedRelations.size()>0) {
            expandString = selectString.equals("") ? "?$expand=" : "&$expand=";
            for (String expand : expandedRelations) {
                if (expandString.charAt(expandString.length() - 1) != '=') {
                    expandString += ',';
                }
                expandString += expand;
            }
        }
        if (entityType != null) {
            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, selectString+expandString);
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

    public void checkEntitiesAllAspectsForSelectResponse(EntityType entityType, String response, List<String> selectedProperties){
        checkEntitiesProperties(entityType, response, selectedProperties);
        checkEntitiesRelations(entityType, response, selectedProperties, null);
    }


    public void checkEntitiesProperties(EntityType entityType, String response, List<String> selectedProperties){
        try {
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray entities = jsonResponse.getJSONArray("value");
            checkPropertiesForEntityArray(entityType, entities, selectedProperties);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void checkPropertiesForEntityArray(EntityType entityType, JSONArray entities, List<String> selectedProperties){
        int count = 0;
        for (int i = 0; i < entities.length() && count<2; i++) {
            count ++;
            JSONObject entity = null;
            try {
                entity = entities.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            checkEntityProperties(entityType, entity, selectedProperties);
        }
    }

    public void checkEntityProperties(EntityType entityType, Object response, List<String> selectedProperties){
        try {
            JSONObject entity = new JSONObject(response.toString());
            switch (entityType){
                case THING:
                        for (String property : EntityProperties.THING_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                try {
                                    Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }catch (JSONException e){
                                    Assert.fail("Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }
                            }else{
                                try {
                                    Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \"" + property + "\".");
                                }catch(JSONException e){}
                            }
                        }
                    break;
                case LOCATION:
                        for (String property : EntityProperties.LOCATION_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                try {
                                    Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }catch (JSONException e){
                                    Assert.fail("Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }
                            }else{
                                try {
                                    Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \"" + property + "\".");
                                }catch(JSONException e){}
                            }
                        }
                    break;
                case HISTORICAL_LOCATION:
                        for (String property : EntityProperties.HISTORICAL_LOCATION_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                try {
                                    Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }catch (JSONException e){
                                    Assert.fail("Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }
                            }else{
                                try {
                                    Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \"" + property + "\".");
                                }catch(JSONException e){}
                            }
                        }
                    break;
                case DATASTREAM:
                        for (String property : EntityProperties.DATASTREAM_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                try {
                                    Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }catch (JSONException e){
                                    Assert.fail("Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }
                            }else{
                                try {
                                    Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \"" + property + "\".");
                                }catch(JSONException e){}
                            }
                        }
                    break;
                case SENSOR:
                        for (String property : EntityProperties.SENSOR_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                try {
                                    Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }catch (JSONException e){
                                    Assert.fail("Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }
                            }else{
                                try {
                                    Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \"" + property + "\".");
                                }catch(JSONException e){}
                            }
                        }
                    break;
                case OBSERVATION:
                        for (String property : EntityProperties.OBSERVATION_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                try {
                                    Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }catch (JSONException e){
                                    Assert.fail("Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }
                            }else{
                                try {
                                    Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \"" + property + "\".");
                                }catch(JSONException e){}
                            }
                        }
                    break;
                case OBSERVED_PROPERTY:
                        for (String property : EntityProperties.OBSERVED_PROPETY_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                try {
                                    Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }catch (JSONException e){
                                    Assert.fail("Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }
                            }else{
                                try {
                                    Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \"" + property + "\".");
                                }catch(JSONException e){}
                            }
                        }
                    break;
                case FEATURE_OF_INTEREST:
                        for (String property : EntityProperties.FEATURE_OF_INTEREST_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                try {
                                    Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }catch (JSONException e){
                                    Assert.fail("Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                                }
                            }else{
                                try {
                                    Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \"" + property + "\".");
                                }catch(JSONException e){}
                            }
                        }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            //The program reachs here in normal state, because it tries to check the non-existense of some navigation properties.
        }

    }

    public void checkEntitiesRelations(EntityType entityType, String response, List<String> selectedProperties , List<String> expandedRelations){
        try {
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray entities = jsonResponse.getJSONArray("value");
            int count = 0;
            for (int i = 0; i < entities.length()&& count <2; i++) {
                count ++;
                JSONObject entity = entities.getJSONObject(i);
                checkEntityRelations(entityType, entity, selectedProperties, expandedRelations);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void checkEntityRelations(EntityType entityType, Object response, List<String> selectedProperties, List<String> expandedRelations){
        try {
            JSONObject entity = new JSONObject(response.toString());
            switch (entityType){
                case THING:
                        for (String relation : EntityRelations.THING_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    try {
                                        Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }catch (JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }
                                }else{
                                    try {
                                        Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                        JSONArray expandedEntityArray = entity.getJSONArray(relation);
                                        checkPropertiesForEntityArray(getEntityTypeFor(relation), expandedEntityArray, new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                    } catch(JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    }
                                }
                            } else {
                                try {
                                    Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch(JSONException e){}
                                try {
                                    Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch (JSONException e){}
                            }
                        }

                    break;
                case LOCATION:
                        for (String relation : EntityRelations.LOCATION_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    try {
                                        Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }catch (JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }
                                }else{
                                    try {
                                        Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                        JSONArray expandedEntityArray = entity.getJSONArray(relation);
                                        checkPropertiesForEntityArray(getEntityTypeFor(relation), expandedEntityArray, new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                    } catch(JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    }
                                }
                            } else {
                                try {
                                    Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch(JSONException e){}
                                try {
                                    Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch (JSONException e){}
                            }
                        }

                    break;
                case HISTORICAL_LOCATION:
                        for (String relation : EntityRelations.HISTORICAL_LOCATION_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    try {
                                        Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }catch (JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }
                                }else{
                                    try {
                                        Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                        JSONArray expandedEntityArray ;
                                        if(!relation.equals("Thing")) {
                                            expandedEntityArray = entity.getJSONArray(relation);
                                        }else{
                                            expandedEntityArray = new JSONArray();
                                            expandedEntityArray.put(entity.getJSONObject(relation));
                                        }
                                        checkPropertiesForEntityArray(getEntityTypeFor(relation), expandedEntityArray, new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                    } catch(JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    }
                                }
                            } else {
                                try {
                                    Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch(JSONException e){}
                                try {
                                    Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch (JSONException e){}
                            }
                        }

                    break;
                case DATASTREAM:
                        for (String relation : EntityRelations.DATASTREAM_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    try {
                                        Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }catch (JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }
                                }else{
                                    try {
                                        Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                        JSONArray expandedEntityArray ;
                                        if(!relation.equals("Observations")) {
                                            expandedEntityArray = new JSONArray();
                                            expandedEntityArray.put(entity.getJSONObject(relation));
                                        }else{
                                            expandedEntityArray = entity.getJSONArray(relation);
                                        }
                                        checkPropertiesForEntityArray(getEntityTypeFor(relation), expandedEntityArray, new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                    } catch(JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    }
                                }
                            } else {
                                try {
                                    Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch(JSONException e){}
                                try {
                                    Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch (JSONException e){}
                            }
                        }

                    break;
                case SENSOR:
                        for (String relation : EntityRelations.SENSOR_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    try {
                                        Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }catch (JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }
                                }else{
                                    try {
                                        Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                        JSONArray expandedEntityArray = entity.getJSONArray(relation);
                                        checkPropertiesForEntityArray(getEntityTypeFor(relation), expandedEntityArray, new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                    } catch(JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    }
                                }
                            } else {
                                try {
                                    Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch(JSONException e){}
                                try {
                                    Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch (JSONException e){}
                            }
                        }
                    break;
                case OBSERVATION:

                        for (String relation : EntityRelations.OBSERVATION_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    try {
                                        Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }catch (JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }
                                }else{
                                    try {
                                        Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                        JSONArray expandedEntityArray = new JSONArray();
                                        expandedEntityArray.put(entity.getJSONObject(relation));
                                        checkPropertiesForEntityArray(getEntityTypeFor(relation), expandedEntityArray, new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                    } catch(JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    }
                                }
                            } else {
                                try {
                                    Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch(JSONException e){}
                                try {
                                    Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch (JSONException e){}
                            }
                        }

                    break;
                case OBSERVED_PROPERTY:
                        for (String relation : EntityRelations.OBSERVED_PROPERTY_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    try {
                                        Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }catch (JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }
                                }else{
                                    try {
                                        Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                        JSONArray expandedEntityArray = entity.getJSONArray(relation);
                                        checkPropertiesForEntityArray(getEntityTypeFor(relation), expandedEntityArray, new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                    } catch(JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    }
                                }
                            } else {
                                try {
                                    Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch(JSONException e){}
                                try {
                                    Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch (JSONException e){}
                            }
                        }
                    break;
                case FEATURE_OF_INTEREST:
                        for (String relation : EntityRelations.FEATURE_OF_INTEREST_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    try {
                                        Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }catch (JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                    }
                                }else{
                                    try {
                                        Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                        JSONArray expandedEntityArray = entity.getJSONArray(relation);
                                        checkPropertiesForEntityArray(getEntityTypeFor(relation), expandedEntityArray, new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                    } catch(JSONException e){
                                        Assert.fail("Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    }
                                }
                            } else {
                                try {
                                    Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch(JSONException e){}
                                try {
                                    Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                }catch (JSONException e){}
                            }
                        }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            //The program reachs here in normal state, because it tries to check the non-existense of some navigation properties.
        }
    }

    private void checkExpandtForEntityType(EntityType entityType){
        List<String> expandedRelations;
        switch(entityType){
            case THING:
                for (String relation: EntityRelations.THING_RELATIONS) {
                    expandedRelations = new ArrayList<>();
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.THING, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.THING, response, expandedRelations);
                }
                expandedRelations = new ArrayList<>();
                for (String relation: EntityRelations.THING_RELATIONS) {
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.THING, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.THING, response, expandedRelations);
                }
                break;
            case LOCATION:
                for (String relation: EntityRelations.LOCATION_RELATIONS) {
                    expandedRelations = new ArrayList<>();
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.LOCATION, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.LOCATION, response, expandedRelations);
                }
                expandedRelations = new ArrayList<>();
                for (String relation: EntityRelations.LOCATION_RELATIONS) {
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.LOCATION, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.LOCATION, response, expandedRelations);
                }
                break;
            case HISTORICAL_LOCATION:
                for (String relation: EntityRelations.HISTORICAL_LOCATION_RELATIONS) {
                    expandedRelations = new ArrayList<>();
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.HISTORICAL_LOCATION, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.HISTORICAL_LOCATION, response, expandedRelations);
                }
                expandedRelations = new ArrayList<>();
                for (String relation: EntityRelations.HISTORICAL_LOCATION_RELATIONS) {
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.HISTORICAL_LOCATION, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.HISTORICAL_LOCATION, response, expandedRelations);
                }
                break;
            case DATASTREAM:
                for (String relation: EntityRelations.DATASTREAM_RELATIONS) {
                    expandedRelations = new ArrayList<>();
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.DATASTREAM, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.DATASTREAM, response, expandedRelations);
                }
                expandedRelations = new ArrayList<>();
                for (String relation: EntityRelations.DATASTREAM_RELATIONS) {
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.DATASTREAM, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.DATASTREAM, response, expandedRelations);
                }
                break;
            case SENSOR:
                for (String relation: EntityRelations.SENSOR_RELATIONS) {
                    expandedRelations = new ArrayList<>();
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.SENSOR, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.SENSOR, response, expandedRelations);
                }
                expandedRelations = new ArrayList<>();
                for (String relation: EntityRelations.SENSOR_RELATIONS) {
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.SENSOR, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.SENSOR, response, expandedRelations);
                }
                break;
            case OBSERVED_PROPERTY:
                for (String relation: EntityRelations.OBSERVED_PROPERTY_RELATIONS) {
                    expandedRelations = new ArrayList<>();
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.OBSERVED_PROPERTY, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.OBSERVED_PROPERTY, response, expandedRelations);
                }
                expandedRelations = new ArrayList<>();
                for (String relation: EntityRelations.OBSERVED_PROPERTY_RELATIONS) {
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.OBSERVED_PROPERTY, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.OBSERVED_PROPERTY, response, expandedRelations);
                }
                break;
            case OBSERVATION:
                for (String relation: EntityRelations.OBSERVATION_RELATIONS) {
                    expandedRelations = new ArrayList<>();
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.OBSERVATION, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.OBSERVATION, response, expandedRelations);
                }
                expandedRelations = new ArrayList<>();
                for (String relation: EntityRelations.OBSERVATION_RELATIONS) {
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.OBSERVATION, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.OBSERVATION, response, expandedRelations);
                }
                break;
            case FEATURE_OF_INTEREST:
                for (String relation: EntityRelations.FEATURE_OF_INTEREST_RELATIONS) {
                    expandedRelations = new ArrayList<>();
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.FEATURE_OF_INTEREST, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.FEATURE_OF_INTEREST, response, expandedRelations);
                }
                expandedRelations = new ArrayList<>();
                for (String relation: EntityRelations.FEATURE_OF_INTEREST_RELATIONS) {
                    expandedRelations.add(relation);
                    String response = getEntities(EntityType.FEATURE_OF_INTEREST, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(EntityType.FEATURE_OF_INTEREST, response, expandedRelations);
                }
                break;
        }
    }

    public void checkEntitiesAllAspectsForExpandResponse(EntityType entityType, String response, List<String> expandedRelations){
        checkEntitiesRelations(entityType, response, null, expandedRelations);
    }

    private EntityType getEntityTypeFor(String name){
        switch (name.toLowerCase()){
            case "thing":
            case "things":
                return EntityType.THING;
            case "location":
            case "locations":
                return EntityType.LOCATION;
            case "historicallocation":
            case "historicallocations":
                return EntityType.HISTORICAL_LOCATION;
            case "datastream":
            case "datastreams":
                return EntityType.DATASTREAM;
            case "sensor":
            case "sensors":
                return EntityType.SENSOR;
            case "observedproperty":
            case "observedproperties":
                return EntityType.OBSERVED_PROPERTY;
            case "observation":
            case "observations":
                return EntityType.OBSERVATION;
            case "featureofinterest":
            case "featuresofinterest":
                return EntityType.FEATURE_OF_INTEREST;
        }
        return null;
    }

    private String[] getPropertiesListFor(String name){
        switch (name.toLowerCase()){
            case "thing":
            case "things":
                return EntityProperties.THING_PROPERTIES;
            case "location":
            case "locations":
                return EntityProperties.LOCATION_PROPERTIES;
            case "historicallocation":
            case "historicallocations":
                return EntityProperties.HISTORICAL_LOCATION_PROPERTIES;
            case "datastream":
            case "datastreams":
                return EntityProperties.DATASTREAM_PROPERTIES;
            case "sensor":
            case "sensors":
                return EntityProperties.SENSOR_PROPERTIES;
            case "observedproperty":
            case "observedproperties":
                return EntityProperties.OBSERVED_PROPETY_PROPERTIES;
            case "observation":
            case "observations":
                return EntityProperties.OBSERVATION_PROPERTIES;
            case "featureofinterest":
            case "featuresofinterest":
                return EntityProperties.FEATURE_OF_INTEREST_PROPERTIES;
        }
        return null;
    }

    private void createEntities(){
        try {
            //First Thing
            String urlParameters = "{\n" +
                    "    \"description\": \"thing 1\",\n" +
                    "    \"properties\": {\n" +
                    "        \"reference\": \"first\"\n" +
                    "    },\n" +
                    "    \"Locations\": [\n" +
                    "        {\n" +
                    "            \"description\": \"location 1\",\n" +
                    "            \"location\": {\n" +
                    "                \"type\": \"Point\",\n" +
                    "                \"coordinates\": [\n" +
                    "                    -117.05,\n" +
                    "                    51.05\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            \"encodingType\": \"http://example.org/location_types#GeoJSON\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"Datastreams\": [\n" +
                    "        {\n" +
                    "            \"unitOfMeasurement\": {\n" +
                    "                \"name\": \"Lumen\",\n" +
                    "                \"symbol\": \"lm\",\n" +
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html#Lumen\"\n" +
                    "            },\n" +
                    "            \"description\": \"datastream 1\",\n" +
                    "            \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "            \"ObservedProperty\": {\n" +
                    "                \"name\": \"Luminous Flux\",\n" +
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html#LuminousFlux\",\n" +
                    "                \"description\": \"observedProperty 1\"\n" +
                    "            },\n" +
                    "            \"Sensor\": {\n" +
                    "                \"description\": \"sensor 1\",\n" +
                    "                \"encodingType\": \"http://schema.org/description\",\n" +
                    "                \"metadata\": \"Light flux sensor\"\n" +
                    "            }\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"unitOfMeasurement\": {\n" +
                    "                \"name\": \"Centigrade\",\n" +
                    "                \"symbol\": \"C\",\n" +
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html#Lumen\"\n" +
                    "            },\n" +
                    "            \"description\": \"datastream 2\",\n" +
                    "            \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "            \"ObservedProperty\": {\n" +
                    "                \"name\": \"Tempretaure\",\n" +
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html#Tempreture\",\n" +
                    "                \"description\": \"observedProperty 2\"\n" +
                    "            },\n" +
                    "            \"Sensor\": {\n" +
                    "                \"description\": \"sensor 2\",\n" +
                    "                \"encodingType\": \"http://schema.org/description\",\n" +
                    "                \"metadata\": \"Tempreture sensor\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
            String urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, -1, null, null);
            Map<String, Object> responseMap = HTTPMethods.doPost(urlString, urlParameters);
            String response = responseMap.get("response").toString();
            thingId1 = Long.parseLong(response.substring(response.indexOf("(") + 1, response.indexOf(")")));

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, thingId1, EntityType.LOCATION, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            JSONArray array = new JSONObject(response).getJSONArray("value");
            locationId1 = array.getJSONObject(0).getLong(ControlInformation.ID);

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, thingId1, EntityType.DATASTREAM, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            datastreamId1 = array.getJSONObject(0).getLong(ControlInformation.ID);
            datastreamId2 = array.getJSONObject(1).getLong(ControlInformation.ID);

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamId1, EntityType.SENSOR, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            sensorId1 = new JSONObject(response).getLong(ControlInformation.ID);
            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamId1, EntityType.OBSERVED_PROPERTY, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            observedPropertyId1 = new JSONObject(response).getLong(ControlInformation.ID);

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamId2, EntityType.SENSOR, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            sensorId2 = new JSONObject(response).getLong(ControlInformation.ID);
            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamId1, EntityType.OBSERVED_PROPERTY, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            observedPropertyId2 = new JSONObject(response).getLong(ControlInformation.ID);


            //Second Thing
            urlParameters = "{\n" +
                    "    \"description\": \"thing 2\",\n" +
                    "    \"properties\": {\n" +
                    "        \"reference\": \"second\"\n" +
                    "    },\n" +
                    "    \"Locations\": [\n" +
                    "        {\n" +
                    "            \"description\": \"location 2\",\n" +
                    "            \"location\": {\n" +
                    "                \"type\": \"Point\",\n" +
                    "                \"coordinates\": [\n" +
                    "                    -100.05,\n" +
                    "                    50.05\n" +
                    "                ]\n" +
                    "            },\n" +
                    "            \"encodingType\": \"http://example.org/location_types#GeoJSON\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"Datastreams\": [\n" +
                    "        {\n" +
                    "            \"unitOfMeasurement\": {\n" +
                    "                \"name\": \"Lumen\",\n" +
                    "                \"symbol\": \"lm\",\n" +
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html#Lumen\"\n" +
                    "            },\n" +
                    "            \"description\": \"datastream 3\",\n" +
                    "            \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "            \"ObservedProperty\": {\n" +
                    "                \"name\": \"Second Luminous Flux\",\n" +
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html#LuminousFlux\",\n" +
                    "                \"description\": \"observedProperty 3\"\n" +
                    "            },\n" +
                    "            \"Sensor\": {\n" +
                    "                \"description\": \"sensor 3\",\n" +
                    "                \"encodingType\": \"http://schema.org/description\",\n" +
                    "                \"metadata\": \"Second Light flux sensor\"\n" +
                    "            }\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"unitOfMeasurement\": {\n" +
                    "                \"name\": \"Centigrade\",\n" +
                    "                \"symbol\": \"C\",\n" +
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html#Lumen\"\n" +
                    "            },\n" +
                    "            \"description\": \"datastream 2\",\n" +
                    "            \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "            \"ObservedProperty\": {\n" +
                    "                \"@iot.id\": "+observedPropertyId2+"\n" +
                    "            },\n" +
                    "            \"Sensor\": {\n" +
                    "                \"description\": \"sensor 4 \",\n" +
                    "                \"encodingType\": \"http://schema.org/description\",\n" +
                    "                \"metadata\": \"Second Tempreture sensor\"\n" +
                    "            }\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";
            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, -1, null, null);
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            thingId2 = Long.parseLong(response.substring(response.indexOf("(") + 1, response.indexOf(")")));

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, thingId2, EntityType.LOCATION, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            locationId2 = array.getJSONObject(0).getLong(ControlInformation.ID);

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, thingId2, EntityType.DATASTREAM, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            datastreamId3 = array.getJSONObject(0).getLong(ControlInformation.ID);
            datastreamId4 = array.getJSONObject(1).getLong(ControlInformation.ID);

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamId3, EntityType.SENSOR, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            sensorId3 = new JSONObject(response).getLong(ControlInformation.ID);
            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamId3, EntityType.OBSERVED_PROPERTY, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            observedPropertyId3 = new JSONObject(response).getLong(ControlInformation.ID);

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamId4, EntityType.SENSOR, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            sensorId4 = new JSONObject(response).getLong(ControlInformation.ID);

            //HistoricalLocations
            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, thingId1, null, null);
            urlParameters = "{\"Locations\": [\n" +
                    "    {\n" +
                    "      \"@iot.id\": "+locationId2+"\n" +
                    "    }\n" +
                    "  ]}";
            HTTPMethods.doPatch(urlString, urlParameters);

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, thingId2, null, null);
            urlParameters = "{\"Locations\": [\n" +
                    "    {\n" +
                    "      \"@iot.id\": "+locationId1+"\n" +
                    "    }\n" +
                    "  ]}";
            HTTPMethods.doPatch(urlString, urlParameters);

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, thingId1, EntityType.HISTORICAL_LOCATION, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            historicalLocationId1 = array.getJSONObject(0).getLong(ControlInformation.ID);
            historicalLocationId2 = array.getJSONObject(1).getLong(ControlInformation.ID);

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, thingId2, EntityType.HISTORICAL_LOCATION, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            historicalLocationId3 = array.getJSONObject(0).getLong(ControlInformation.ID);
            historicalLocationId4 = array.getJSONObject(1).getLong(ControlInformation.ID);

            //Observations
            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamId1, EntityType.OBSERVATION, null);
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:00:00Z\",\n" +
                    "  \"result\": 1 \n" +
                    "   }";
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            observationId1 = Long.parseLong(response.substring(response.lastIndexOf("(") + 1, response.lastIndexOf(")")));
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-02T00:00:00Z\",\n" +
                    "  \"result\": 2 \n" +
                    "   }";
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            observationId2 = Long.parseLong(response.substring(response.lastIndexOf("(") + 1, response.lastIndexOf(")")));
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-03T00:00:00Z\",\n" +
                    "  \"result\": 3 \n" +
                    "   }";
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            observationId3 = Long.parseLong(response.substring(response.lastIndexOf("(") + 1, response.lastIndexOf(")")));

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamId2, EntityType.OBSERVATION, null);
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-04T00:00:00Z\",\n" +
                    "  \"result\": 4 \n" +
                    "   }";
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            observationId4 = Long.parseLong(response.substring(response.lastIndexOf("(") + 1, response.lastIndexOf(")")));
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-05T00:00:00Z\",\n" +
                    "  \"result\": 5 \n" +
                    "   }";
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            observationId5 = Long.parseLong(response.substring(response.lastIndexOf("(") + 1, response.lastIndexOf(")")));
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-06T00:00:00Z\",\n" +
                    "  \"result\": 6 \n" +
                    "   }";
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            observationId6 = Long.parseLong(response.substring(response.lastIndexOf("(") + 1, response.lastIndexOf(")")));

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamId3, EntityType.OBSERVATION, null);
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-07T00:00:00Z\",\n" +
                    "  \"result\": 7 \n" +
                    "   }";
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            observationId7 = Long.parseLong(response.substring(response.lastIndexOf("(") + 1, response.lastIndexOf(")")));
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-08T00:00:00Z\",\n" +
                    "  \"result\": 8 \n" +
                    "   }";
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            observationId8 = Long.parseLong(response.substring(response.lastIndexOf("(") + 1, response.lastIndexOf(")")));
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-09T00:00:00Z\",\n" +
                    "  \"result\": 9 \n" +
                    "   }";
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            observationId9 = Long.parseLong(response.substring(response.lastIndexOf("(") + 1, response.lastIndexOf(")")));

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamId4, EntityType.OBSERVATION, null);
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-10T00:00:00Z\",\n" +
                    "  \"result\": 10 \n" +
                    "   }";
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            observationId10 = Long.parseLong(response.substring(response.lastIndexOf("(") + 1, response.lastIndexOf(")")));
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-11T00:00:00Z\",\n" +
                    "  \"result\": 11 \n" +
                    "   }";
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            observationId11 = Long.parseLong(response.substring(response.lastIndexOf("(") + 1, response.lastIndexOf(")")));
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-12T00:00:00Z\",\n" +
                    "  \"result\": 12 \n" +
                    "   }";
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            observationId12 = Long.parseLong(response.substring(response.lastIndexOf("(") + 1, response.lastIndexOf(")")));

            //FeatureOfInterest
            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.OBSERVATION, observationId1, EntityType.FEATURE_OF_INTEREST, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            featureOfInterestId1 = new JSONObject(response).getLong(ControlInformation.ID);

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.OBSERVATION, observationId7, EntityType.FEATURE_OF_INTEREST, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            featureOfInterestId2 = new JSONObject(response).getLong(ControlInformation.ID);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

}
