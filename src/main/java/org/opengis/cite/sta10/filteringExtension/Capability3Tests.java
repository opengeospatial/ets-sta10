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
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * Includes various tests of capability 3.
 */
public class Capability3Tests {

    public String rootUri;

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
//        checkExpandtForEntityType(EntityType.DATASTREAM);
        checkExpandtForEntityType(EntityType.SENSOR);
        checkExpandtForEntityType(EntityType.OBSERVED_PROPERTY);
        checkExpandtForEntityType(EntityType.OBSERVATION);
//        checkExpandtForEntityType(EntityType.FEATURE_OF_INTEREST);

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
                                Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \""+property+"\".");
                            }else{
                                Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \""+property+"\".");
                            }
                        }
                    break;
                case LOCATION:
                        for (String property : EntityProperties.LOCATION_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \""+property+"\".");
                            }else{
                                Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \""+property+"\".");
                            }
                        }
                    break;
                case HISTORICAL_LOCATION:
                        for (String property : EntityProperties.HISTORICAL_LOCATION_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \""+property+"\".");
                            }else{
                                Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \""+property+"\".");
                            }
                        }
                    break;
                case DATASTREAM:
                        for (String property : EntityProperties.DATASTREAM_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \""+property+"\".");
                            }else{
                                Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \""+property+"\".");
                            }
                        }
                    break;
                case SENSOR:
                        for (String property : EntityProperties.SENSOR_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \""+property+"\".");
                            }else{
                                Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \""+property+"\".");
                            }
                        }
                    break;
                case OBSERVATION:
                        for (String property : EntityProperties.OBSERVATION_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \""+property+"\".");
                            }else{
                                Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \""+property+"\".");
                            }
                        }
                    break;
                case OBSERVED_PROPERTY:
                        for (String property : EntityProperties.OBSERVED_PROPETY_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \""+property+"\".");
                            }else{
                                Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \""+property+"\".");
                            }
                        }
                    break;
                case FEATURE_OF_INTEREST:
                        for (String property : EntityProperties.FEATURE_OF_INTEREST_PROPERTIES) {
                            if(selectedProperties.contains(property)){
                                Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \""+property+"\".");
                            }else{
                                Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \""+property+"\".");
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
                                    Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                }else{
                                    Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    JSONArray expandedEntityArray = entity.getJSONArray(relation);
                                    checkPropertiesForEntityArray(getEntityTypeFor(relation),expandedEntityArray,new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                }
                            } else {
                                Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                            }
                        }

                    break;
                case LOCATION:
                        for (String relation : EntityRelations.LOCATION_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                }else{
                                    Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    JSONArray expandedEntityArray = entity.getJSONArray(relation);
                                    checkPropertiesForEntityArray(getEntityTypeFor(relation),expandedEntityArray,new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                }
                            } else {
                                Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                            }
                        }

                    break;
                case HISTORICAL_LOCATION:
                        for (String relation : EntityRelations.HISTORICAL_LOCATION_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                }else{
                                    Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    JSONArray expandedEntityArray = entity.getJSONArray(relation);
                                    checkPropertiesForEntityArray(getEntityTypeFor(relation),expandedEntityArray,new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                }
                            } else {
                                Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                            }
                        }

                    break;
                case DATASTREAM:
                        for (String relation : EntityRelations.DATASTREAM_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                }else{
                                    Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    JSONArray expandedEntityArray = entity.getJSONArray(relation);
                                    checkPropertiesForEntityArray(getEntityTypeFor(relation),expandedEntityArray,new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                }
                            } else {
                                Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                            }
                        }

                    break;
                case SENSOR:
                        for (String relation : EntityRelations.SENSOR_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                }else{
                                    Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    JSONArray expandedEntityArray = entity.getJSONArray(relation);
                                    checkPropertiesForEntityArray(getEntityTypeFor(relation),expandedEntityArray,new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                }
                            } else {
                                Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                            }
                        }
                    break;
                case OBSERVATION:

                        for (String relation : EntityRelations.OBSERVATION_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                }else{
                                    Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    JSONArray expandedEntityArray = entity.getJSONArray(relation);
                                    checkPropertiesForEntityArray(getEntityTypeFor(relation),expandedEntityArray,new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                }
                            } else {
                                Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                            }
                        }

                    break;
                case OBSERVED_PROPERTY:
                        for (String relation : EntityRelations.OBSERVED_PROPERTY_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                }else{
                                    Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    JSONArray expandedEntityArray = entity.getJSONArray(relation);
                                    checkPropertiesForEntityArray(getEntityTypeFor(relation),expandedEntityArray,new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                }
                            } else {
                                Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                            }
                        }
                    break;
                case FEATURE_OF_INTEREST:
                        for (String relation : EntityRelations.FEATURE_OF_INTEREST_RELATIONS) {
                            if (selectedProperties == null || selectedProperties.contains(relation)) {
                                if(expandedRelations == null || !expandedRelations.contains(relation)) {
                                    Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                                }else{
                                    Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                                    JSONArray expandedEntityArray = entity.getJSONArray(relation);
                                    checkPropertiesForEntityArray(getEntityTypeFor(relation),expandedEntityArray,new ArrayList<String>(Arrays.asList(getPropertiesListFor(relation))));
                                }
                            } else {
                                Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                                Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
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
            case "observedpropery":
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
            case "observedpropery":
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

}
