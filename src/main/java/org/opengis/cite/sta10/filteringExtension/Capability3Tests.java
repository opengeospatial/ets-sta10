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

    public String rootUri;//="http://localhost:8080/OGCSensorThings-NewQueries/v1.0";

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
        if (rootUri.lastIndexOf('/') == rootUri.length() - 1) {
            rootUri = rootUri.substring(0, rootUri.length() - 1);
        }
        createEntities();
    }


    @Test(description = "Check Query Evaluation Priority.", groups = "level-3")
    public void readEntitiesWithSelectQO() {
        checkSelectForEntityType(EntityType.THING);
        checkSelectForEntityType(EntityType.LOCATION);
        checkSelectForEntityType(EntityType.HISTORICAL_LOCATION);
        checkSelectForEntityType(EntityType.DATASTREAM);
        checkSelectForEntityType(EntityType.SENSOR);
        checkSelectForEntityType(EntityType.OBSERVED_PROPERTY);
        checkSelectForEntityType(EntityType.OBSERVATION);
        checkSelectForEntityType(EntityType.FEATURE_OF_INTEREST);
        checkSelectForEntityTypeRelations(EntityType.THING);
        checkSelectForEntityTypeRelations(EntityType.LOCATION);
        checkSelectForEntityTypeRelations(EntityType.HISTORICAL_LOCATION);
        checkSelectForEntityTypeRelations(EntityType.DATASTREAM);
        checkSelectForEntityTypeRelations(EntityType.SENSOR);
        checkSelectForEntityTypeRelations(EntityType.OBSERVED_PROPERTY);
        checkSelectForEntityTypeRelations(EntityType.OBSERVATION);
        checkSelectForEntityTypeRelations(EntityType.FEATURE_OF_INTEREST);

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
        checkExpandtForEntityTypeRelations(EntityType.THING);
        checkExpandtForEntityTypeRelations(EntityType.LOCATION);
        checkExpandtForEntityTypeRelations(EntityType.HISTORICAL_LOCATION);
        checkExpandtForEntityTypeRelations(EntityType.DATASTREAM);
        checkExpandtForEntityTypeRelations(EntityType.SENSOR);
        checkExpandtForEntityTypeRelations(EntityType.OBSERVED_PROPERTY);
        checkExpandtForEntityTypeRelations(EntityType.OBSERVATION);
        checkExpandtForEntityTypeRelations(EntityType.FEATURE_OF_INTEREST);
        checkExpandtForEntityTypeMultilevel(EntityType.THING);
        checkExpandtForEntityTypeMultilevel(EntityType.LOCATION);
        checkExpandtForEntityTypeMultilevel(EntityType.HISTORICAL_LOCATION);
        checkExpandtForEntityTypeMultilevel(EntityType.DATASTREAM);
        checkExpandtForEntityTypeMultilevel(EntityType.SENSOR);
        checkExpandtForEntityTypeMultilevel(EntityType.OBSERVED_PROPERTY);
        checkExpandtForEntityTypeMultilevel(EntityType.OBSERVATION);
        checkExpandtForEntityTypeMultilevel(EntityType.FEATURE_OF_INTEREST);
        checkExpandtForEntityTypeMultilevelRelations(EntityType.THING);
        checkExpandtForEntityTypeMultilevelRelations(EntityType.LOCATION);
        checkExpandtForEntityTypeMultilevelRelations(EntityType.HISTORICAL_LOCATION);
        checkExpandtForEntityTypeMultilevelRelations(EntityType.DATASTREAM);
        checkExpandtForEntityTypeMultilevelRelations(EntityType.SENSOR);
        checkExpandtForEntityTypeMultilevelRelations(EntityType.OBSERVED_PROPERTY);
        checkExpandtForEntityTypeMultilevelRelations(EntityType.OBSERVATION);
        checkExpandtForEntityTypeMultilevelRelations(EntityType.FEATURE_OF_INTEREST);


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
        checkTopForEntityTypeRelation(EntityType.THING);
        checkTopForEntityTypeRelation(EntityType.LOCATION);
        checkTopForEntityTypeRelation(EntityType.HISTORICAL_LOCATION);
        checkTopForEntityTypeRelation(EntityType.DATASTREAM);
        checkTopForEntityTypeRelation(EntityType.SENSOR);
        checkTopForEntityTypeRelation(EntityType.OBSERVED_PROPERTY);
        checkTopForEntityTypeRelation(EntityType.OBSERVATION);
        checkTopForEntityTypeRelation(EntityType.FEATURE_OF_INTEREST);

    }

    @Test(description = "GET Entities with $skip", groups = "level-3")
    public void readEntitiesWithSkipQO() {
        checkSkipForEntityType(EntityType.THING);
        checkSkipForEntityType(EntityType.LOCATION);
        checkSkipForEntityType(EntityType.HISTORICAL_LOCATION);
        checkSkipForEntityType(EntityType.DATASTREAM);
        checkSkipForEntityType(EntityType.SENSOR);
        checkSkipForEntityType(EntityType.OBSERVED_PROPERTY);
        checkSkipForEntityType(EntityType.OBSERVATION);
        checkSkipForEntityType(EntityType.FEATURE_OF_INTEREST);
        checkSkipForEntityTypeRelation(EntityType.THING);
        checkSkipForEntityTypeRelation(EntityType.LOCATION);
        checkSkipForEntityTypeRelation(EntityType.HISTORICAL_LOCATION);
        checkSkipForEntityTypeRelation(EntityType.DATASTREAM);
        checkSkipForEntityTypeRelation(EntityType.SENSOR);
        checkSkipForEntityTypeRelation(EntityType.OBSERVED_PROPERTY);
        checkSkipForEntityTypeRelation(EntityType.OBSERVATION);
        checkSkipForEntityTypeRelation(EntityType.FEATURE_OF_INTEREST);

    }

    @Test(description = "GET Entities with $orderby", groups = "level-3")
    public void readEntitiesWithOrderbyQO() {
        checkOrderbyForEntityType(EntityType.THING);
        checkOrderbyForEntityType(EntityType.LOCATION);
        checkOrderbyForEntityType(EntityType.HISTORICAL_LOCATION);
        checkOrderbyForEntityType(EntityType.DATASTREAM);
        checkOrderbyForEntityType(EntityType.SENSOR);
        checkOrderbyForEntityType(EntityType.OBSERVED_PROPERTY);
        checkOrderbyForEntityType(EntityType.OBSERVATION);
        checkOrderbyForEntityType(EntityType.FEATURE_OF_INTEREST);
        checkOrderbyForEntityTypeRelations(EntityType.THING);
        checkOrderbyForEntityTypeRelations(EntityType.LOCATION);
        checkOrderbyForEntityTypeRelations(EntityType.HISTORICAL_LOCATION);
        checkOrderbyForEntityTypeRelations(EntityType.DATASTREAM);
        checkOrderbyForEntityTypeRelations(EntityType.SENSOR);
        checkOrderbyForEntityTypeRelations(EntityType.OBSERVED_PROPERTY);
        checkOrderbyForEntityTypeRelations(EntityType.OBSERVATION);
        checkOrderbyForEntityTypeRelations(EntityType.FEATURE_OF_INTEREST);
    }

    @Test(description = "GET Entities with $count", groups = "level-3")
    public void readEntitiesWithCountQO() {
        checkCountForEntityType(EntityType.THING);
        checkCountForEntityType(EntityType.LOCATION);
        checkCountForEntityType(EntityType.HISTORICAL_LOCATION);
        checkCountForEntityType(EntityType.DATASTREAM);
        checkCountForEntityType(EntityType.SENSOR);
        checkCountForEntityType(EntityType.OBSERVED_PROPERTY);
        checkCountForEntityType(EntityType.OBSERVATION);
        checkCountForEntityType(EntityType.FEATURE_OF_INTEREST);
        checkCountForEntityTypeRelations(EntityType.THING);
        checkCountForEntityTypeRelations(EntityType.LOCATION);
        checkCountForEntityTypeRelations(EntityType.HISTORICAL_LOCATION);
        checkCountForEntityTypeRelations(EntityType.DATASTREAM);
        checkCountForEntityTypeRelations(EntityType.SENSOR);
        checkCountForEntityTypeRelations(EntityType.OBSERVED_PROPERTY);
        checkCountForEntityTypeRelations(EntityType.OBSERVATION);
        checkCountForEntityTypeRelations(EntityType.FEATURE_OF_INTEREST);
    }

    @Test(description = "GET Entities with $filter", groups = "level-3")
    public void readEntitiesWithFilterQO() {
        checkFilterForEntityType(EntityType.THING);
        checkFilterForEntityType(EntityType.LOCATION);
        checkFilterForEntityType(EntityType.HISTORICAL_LOCATION);
        checkFilterForEntityType(EntityType.DATASTREAM);
        checkFilterForEntityType(EntityType.SENSOR);
        checkFilterForEntityType(EntityType.OBSERVED_PROPERTY);
        checkFilterForEntityType(EntityType.OBSERVATION);
        checkFilterForEntityType(EntityType.FEATURE_OF_INTEREST);
        checkFilterForEntityTypeRelations(EntityType.THING);
        checkFilterForEntityTypeRelations(EntityType.LOCATION);
        checkFilterForEntityTypeRelations(EntityType.HISTORICAL_LOCATION);
        checkFilterForEntityTypeRelations(EntityType.DATASTREAM);
        checkFilterForEntityTypeRelations(EntityType.SENSOR);
        checkFilterForEntityTypeRelations(EntityType.OBSERVED_PROPERTY);
        checkFilterForEntityTypeRelations(EntityType.OBSERVATION);
        checkFilterForEntityTypeRelations(EntityType.FEATURE_OF_INTEREST);
    }

    @Test(description = "GET Entities with $filter", groups = "level-3")
    public void checkQueriesPriorityOrdering() {
        try{
            String urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.OBSERVATION, -1, null, "?$count=true&$top=1&$skip=2&$orderby=phenomenonTime%20asc&$filter=result%20gt%20'3'");
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            Assert.assertEquals(Integer.parseInt(responseMap.get("response-code").toString()), 200, "There is problem for GET Observations using multiple Query Options! HTTP status code: " + responseMap.get("response-code"));
            String response = responseMap.get("response").toString();
            JSONArray array = new JSONObject(response).getJSONArray("value");
            Assert.assertEquals(new JSONObject(response).getLong("@iot.count"), 6, "The query order of execution is not correct. The expected count is 9, but the service returned "+new JSONObject(response).getLong("@iot.count"));
            Assert.assertEquals(array.length(), 1, "The query asked for top 1, but the service rerurned "+array.length()+" entities.");
            Assert.assertEquals(array.getJSONObject(0).getString("result"),"6", "The query order of execution is not correct. The expected Observation result is 6, but it is "+array.getJSONObject(0).getString("result"));
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    private void checkOrderbyForEntityTypeRelations(EntityType entityType) {
        String[] relations = EntityRelations.getRelationsListFor(entityType);
        try {
            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, null);
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            String response = responseMap.get("response").toString();
            JSONArray array = new JSONObject(response).getJSONArray("value");
            if (array.length() == 0) {
                return;
            }
            long id = array.getJSONObject(0).getLong(ControlInformation.ID);

            for (String relation : relations) {
                if (relation.charAt(relation.length() - 1) != 's' && !relation.equals("FeaturesOfInteret")) {
                    continue;
                }
                String[] properties = EntityProperties.getPropertiesListFor(relation);
                EntityType relationEntityType = getEntityTypeFor(relation);
                //single orderby
                for (String property : properties) {
                    if (property.equals("unitOfMeasurement")) {
                        continue;
                    }
                    urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, "?$orderby=" + property);
                    responseMap = HTTPMethods.doGet(urlString);
                    response = responseMap.get("response").toString();
                    array = new JSONObject(response).getJSONArray("value");
                    for (int i = 1; i < array.length(); i++) {
                        JSONObject obj1 = array.getJSONObject(i - 1);
                        JSONObject obj2 = array.getJSONObject(i);
                        Assert.assertTrue(obj2.get(property).toString().compareTo(obj1.get(property).toString()) >= 0, "The ordering is not correct for EntityType " + entityType + " orderby property " + property);
                    }
                    urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, "?$orderby=" + property + "%20asc");
                    responseMap = HTTPMethods.doGet(urlString);
                    response = responseMap.get("response").toString();
                    array = new JSONObject(response).getJSONArray("value");
                    for (int i = 1; i < array.length(); i++) {
                        JSONObject obj1 = array.getJSONObject(i - 1);
                        JSONObject obj2 = array.getJSONObject(i);
                        Assert.assertTrue(obj2.get(property).toString().compareTo(obj1.get(property).toString()) >= 0, "The ordering is not correct for EntityType " + entityType + " orderby asc property " + property);
                    }
                    urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, "?$orderby=" + property + "%20desc");
                    responseMap = HTTPMethods.doGet(urlString);
                    response = responseMap.get("response").toString();
                    array = new JSONObject(response).getJSONArray("value");
                    for (int i = 1; i < array.length(); i++) {
                        JSONObject obj1 = array.getJSONObject(i - 1);
                        JSONObject obj2 = array.getJSONObject(i);
                        Assert.assertTrue(obj1.get(property).toString().compareTo(obj2.get(property).toString()) >= 0, "The ordering is not correct for EntityType " + entityType + " orderby desc property " + property);
                    }
                }

                //multiple orderby
                List<String> orderbyPropeties = new ArrayList<>();
                String orderby = "?$orderby=";
                String orderbyAsc = "?$orderby=";
                String orderbyDesc = "?$orderby=";
                for (String property : properties) {
                    if (property.equals("unitOfMeasurement")) {
                        continue;
                    }
                    if (orderby.charAt(orderby.length() - 1) != '=') {
                        orderby += ",";
                    }
                    orderby += property;
                    orderbyPropeties.add(property);
                    urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, orderby);
                    responseMap = HTTPMethods.doGet(urlString);
                    response = responseMap.get("response").toString();
                    array = new JSONObject(response).getJSONArray("value");
                    for (int i = 1; i < array.length(); i++) {
                        JSONObject obj1 = array.getJSONObject(i - 1);
                        JSONObject obj2 = array.getJSONObject(i);
                        for (int j = 0; j < orderbyPropeties.size(); j++) {
                            String orderbyProperty = orderbyPropeties.get(j);
                            Assert.assertTrue(obj2.get(orderbyProperty).toString().compareTo(obj1.get(orderbyProperty).toString()) >= 0, "The ordering is not correct for EntityType " + entityType + " orderby property " + orderbyProperty);
                            if (obj2.get(orderbyProperty).toString().compareTo(obj1.get(orderbyProperty).toString()) != 0) {
                                break;
                            }
                        }
                    }
                    if (orderbyAsc.charAt(orderbyAsc.length() - 1) != '=') {
                        orderbyAsc += ",";
                    }
                    orderbyAsc += property + "%20asc";
                    urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, orderbyAsc);
                    responseMap = HTTPMethods.doGet(urlString);
                    response = responseMap.get("response").toString();
                    array = new JSONObject(response).getJSONArray("value");
                    for (int i = 1; i < array.length(); i++) {
                        JSONObject obj1 = array.getJSONObject(i - 1);
                        JSONObject obj2 = array.getJSONObject(i);
                        for (int j = 0; j < orderbyPropeties.size(); j++) {
                            String orderbyProperty = orderbyPropeties.get(j);
                            Assert.assertTrue(obj2.get(orderbyProperty).toString().compareTo(obj1.get(orderbyProperty).toString()) >= 0, "The ordering is not correct for EntityType " + entityType + " orderby asc property " + orderbyProperty);
                            if (obj2.get(orderbyProperty).toString().compareTo(obj1.get(orderbyProperty).toString()) != 0) {
                                break;
                            }
                        }
                    }
                    if (orderbyDesc.charAt(orderbyDesc.length() - 1) != '=') {
                        orderbyDesc += ",";
                    }
                    orderbyDesc += property + "%20desc";
                    urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, orderbyDesc);
                    responseMap = HTTPMethods.doGet(urlString);
                    response = responseMap.get("response").toString();
                    array = new JSONObject(response).getJSONArray("value");
                    for (int i = 1; i < array.length(); i++) {
                        JSONObject obj1 = array.getJSONObject(i - 1);
                        JSONObject obj2 = array.getJSONObject(i);
                        for (int j = 0; j < orderbyPropeties.size(); j++) {
                            String orderbyProperty = orderbyPropeties.get(j);
                            Assert.assertTrue(obj1.get(orderbyProperty).toString().compareTo(obj2.get(orderbyProperty).toString()) >= 0, "The ordering is not correct for EntityType " + entityType + " orderby desc property " + orderbyProperty);
                            if (obj2.get(orderbyProperty).toString().compareTo(obj1.get(orderbyProperty).toString()) != 0) {
                                break;
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n"+e.getMessage());
        }

    }

    private void checkOrderbyForEntityType(EntityType entityType) {
        String[] properties = EntityProperties.getPropertiesListFor(entityType);
        try {
            //single orderby
            for (String property : properties) {
                if (property.equals("unitOfMeasurement")) {
                    continue;
                }
                String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$orderby=" + property);
                Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
                String response = responseMap.get("response").toString();
                JSONArray array = new JSONObject(response).getJSONArray("value");
                for (int i = 1; i < array.length(); i++) {
                    JSONObject obj1 = array.getJSONObject(i - 1);
                    JSONObject obj2 = array.getJSONObject(i);
                    Assert.assertTrue(obj2.get(property).toString().compareTo(obj1.get(property).toString()) >= 0, "The ordering is not correct for EntityType " + entityType + " orderby property " + property);
                }
                urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$orderby=" + property + "%20asc");
                responseMap = HTTPMethods.doGet(urlString);
                response = responseMap.get("response").toString();
                array = new JSONObject(response).getJSONArray("value");
                for (int i = 1; i < array.length(); i++) {
                    JSONObject obj1 = array.getJSONObject(i - 1);
                    JSONObject obj2 = array.getJSONObject(i);
                    Assert.assertTrue(obj2.get(property).toString().compareTo(obj1.get(property).toString()) >= 0, "The ordering is not correct for EntityType " + entityType + " orderby asc property " + property);
                }
                urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$orderby=" + property + "%20desc");
                responseMap = HTTPMethods.doGet(urlString);
                response = responseMap.get("response").toString();
                array = new JSONObject(response).getJSONArray("value");
                for (int i = 1; i < array.length(); i++) {
                    JSONObject obj1 = array.getJSONObject(i - 1);
                    JSONObject obj2 = array.getJSONObject(i);
                    Assert.assertTrue(obj1.get(property).toString().compareTo(obj2.get(property).toString()) >= 0, "The ordering is not correct for EntityType " + entityType + " orderby desc property " + property);
                }
            }

            //multiple orderby
            List<String> orderbyPropeties = new ArrayList<>();
            String orderby = "?$orderby=";
            String orderbyAsc = "?$orderby=";
            String orderbyDesc = "?$orderby=";
            for (String property : properties) {
                if (property.equals("unitOfMeasurement")) {
                    continue;
                }
                if (orderby.charAt(orderby.length() - 1) != '=') {
                    orderby += ",";
                }
                orderby += property;
                orderbyPropeties.add(property);
                String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, orderby);
                Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
                String response = responseMap.get("response").toString();
                JSONArray array = new JSONObject(response).getJSONArray("value");
                for (int i = 1; i < array.length(); i++) {
                    JSONObject obj1 = array.getJSONObject(i - 1);
                    JSONObject obj2 = array.getJSONObject(i);
                    for (int j = 0; j < orderbyPropeties.size(); j++) {
                        String orderbyProperty = orderbyPropeties.get(j);
                        Assert.assertTrue(obj2.get(orderbyProperty).toString().compareTo(obj1.get(orderbyProperty).toString()) >= 0, "The ordering is not correct for EntityType " + entityType + " orderby property " + orderbyProperty);
                        if (obj2.get(orderbyProperty).toString().compareTo(obj1.get(orderbyProperty).toString()) != 0) {
                            break;
                        }
                    }
                }
                if (orderbyAsc.charAt(orderbyAsc.length() - 1) != '=') {
                    orderbyAsc += ",";
                }
                orderbyAsc += property + "%20asc";
                urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, orderbyAsc);
                responseMap = HTTPMethods.doGet(urlString);
                response = responseMap.get("response").toString();
                array = new JSONObject(response).getJSONArray("value");
                for (int i = 1; i < array.length(); i++) {
                    JSONObject obj1 = array.getJSONObject(i - 1);
                    JSONObject obj2 = array.getJSONObject(i);
                    for (int j = 0; j < orderbyPropeties.size(); j++) {
                        String orderbyProperty = orderbyPropeties.get(j);
                        Assert.assertTrue(obj2.get(orderbyProperty).toString().compareTo(obj1.get(orderbyProperty).toString()) >= 0, "The ordering is not correct for EntityType " + entityType + " orderby asc property " + orderbyProperty);
                        if (obj2.get(orderbyProperty).toString().compareTo(obj1.get(orderbyProperty).toString()) != 0) {
                            break;
                        }
                    }
                }
                if (orderbyDesc.charAt(orderbyDesc.length() - 1) != '=') {
                    orderbyDesc += ",";
                }
                orderbyDesc += property + "%20desc";
                urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, orderbyDesc);
                responseMap = HTTPMethods.doGet(urlString);
                response = responseMap.get("response").toString();
                array = new JSONObject(response).getJSONArray("value");
                for (int i = 1; i < array.length(); i++) {
                    JSONObject obj1 = array.getJSONObject(i - 1);
                    JSONObject obj2 = array.getJSONObject(i);
                    for (int j = 0; j < orderbyPropeties.size(); j++) {
                        String orderbyProperty = orderbyPropeties.get(j);
                        Assert.assertTrue(obj1.get(orderbyProperty).toString().compareTo(obj2.get(orderbyProperty).toString()) >= 0, "The ordering is not correct for EntityType " + entityType + " orderby desc property " + orderbyProperty);
                        if (obj2.get(orderbyProperty).toString().compareTo(obj1.get(orderbyProperty).toString()) != 0) {
                            break;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }

    }

    private void checkSkipForEntityType(EntityType entityType) {
        try {

            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$skip=1");
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            String response = responseMap.get("response").toString();
            JSONArray array = new JSONObject(response).getJSONArray("value");
            try {
                Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response should not have nextLink.");
            } catch (JSONException e) {
            }
            switch (entityType) {
                case THING:
                case LOCATION:
                case FEATURE_OF_INTEREST:
                    Assert.assertEquals(array.length(), 1, "Query requested entities skipping 1, result should have contained 1 entity, but it contains " + array.length());
                    break;
                case OBSERVED_PROPERTY:
                    Assert.assertEquals(array.length(), 2, "Query requested entities skipping 1, result should have contained 2 entities, but it contains " + array.length());
                    break;
                case HISTORICAL_LOCATION:
                case SENSOR:
                case DATASTREAM:
                    Assert.assertEquals(array.length(), 3, "Query requested entities skipping 1, result should have contained 3 entities, but it contains " + array.length());
                    break;
                case OBSERVATION:
                    Assert.assertEquals(array.length(), 11, "Query requested entities skipping 1, result should have contained 11 entities, but it contains " + array.length());
                    break;
                default:
                    break;
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$skip=2");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            try {
                Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response should not have nextLink.");
            } catch (JSONException e) {
            }
            switch (entityType) {
                case THING:
                case LOCATION:
                case FEATURE_OF_INTEREST:
                    Assert.assertEquals(array.length(), 0, "Query requested entities skipping 2, result should have contained 0 entity, but it contains " + array.length());
                    break;
                case OBSERVED_PROPERTY:
                    Assert.assertEquals(array.length(), 1, "Query requested entities skipping 2, result should have contained 1 entity, but it contains " + array.length());
                    break;
                case HISTORICAL_LOCATION:
                case SENSOR:
                case DATASTREAM:
                    Assert.assertEquals(array.length(), 2, "Query requested entities skipping 2, result should have contained 2 entities, but it contains " + array.length());
                    break;
                case OBSERVATION:
                    Assert.assertEquals(array.length(), 10, "Query requested entities skipping 2, result should have contained 10 entities, but it contains " + array.length());
                    break;
                default:
                    break;
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$skip=3");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            try {
                Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response should not have nextLink.");
            } catch (JSONException e) {
            }
            switch (entityType) {
                case THING:
                case LOCATION:
                case FEATURE_OF_INTEREST:
                case OBSERVED_PROPERTY:
                    Assert.assertEquals(array.length(), 0, "Query requested entities skipping 3, result should have contained 0 entity, but it contains " + array.length());
                    break;
                case HISTORICAL_LOCATION:
                case SENSOR:
                case DATASTREAM:
                    Assert.assertEquals(array.length(), 1, "Query requested entities skipping 3, result should have contained 1 entity, but it contains " + array.length());
                    break;
                case OBSERVATION:
                    Assert.assertEquals(array.length(), 9, "Query requested entities skipping 3, result should have contained 9 entities, but it contains " + array.length());
                    break;
                default:
                    break;
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$skip=4");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            try {
                Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response should not have nextLink.");
            } catch (JSONException e) {
            }
            switch (entityType) {
                case THING:
                case LOCATION:
                case FEATURE_OF_INTEREST:
                case OBSERVED_PROPERTY:
                case HISTORICAL_LOCATION:
                case SENSOR:
                case DATASTREAM:
                    Assert.assertEquals(array.length(), 0, "Query requested entities skipping 4, result should have contained 0 entity, but it contains " + array.length());
                    break;
                case OBSERVATION:
                    Assert.assertEquals(array.length(), 8, "Query requested entities skipping 4, result should have contained 8 entities, but it contains " + array.length());
                    break;
                default:
                    break;
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$skip=12");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            try {
                Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response should not have nextLink.");
            } catch (JSONException e) {
            }
            Assert.assertEquals(array.length(), 0, "Query requested entities skipping 12, result should have contained 0 entity, but it contains " + array.length());
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    private void checkSkipForEntityTypeRelation(EntityType entityType) {
        try {
            String[] relations = EntityRelations.getRelationsListFor(entityType);
            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, null);
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            String response = responseMap.get("response").toString();
            JSONArray array = new JSONObject(response).getJSONArray("value");
            if (array.length() == 0) {
                return;
            }
            long id = array.getJSONObject(0).getLong(ControlInformation.ID);

            for (String relation : relations) {
                if (relation.charAt(relation.length() - 1) != 's' && !relation.equals("FeaturesOfInterest")) {
                    continue;
                }
                EntityType relationEntityType = getEntityTypeFor(relation);
                urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, "?$skip=1");
                responseMap = HTTPMethods.doGet(urlString);
                response = responseMap.get("response").toString();
                array = new JSONObject(response).getJSONArray("value");
                try {
                    Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response should not have nextLink.");
                } catch (JSONException e) {
                }
                switch (entityType) {
                    case THING:
                        switch (relationEntityType) {
                            case LOCATION:
                                Assert.assertEquals(array.length(), 0, "Query requested entities skipping 1, result should have contained 0 entity, but it contains " + array.length());
                                break;
                            case HISTORICAL_LOCATION:
                                Assert.assertEquals(array.length(), 1, "Query requested entities skipping 1, result should have contained 1 entity, but it contains " + array.length());
                                break;
                            case DATASTREAM:
                                Assert.assertEquals(array.length(), 1, "Query requested entities skipping 1, result should have contained 1 entity, but it contains " + array.length());
                                break;
                        }
                        break;
                    case LOCATION:
                        switch (relationEntityType) {
                            case HISTORICAL_LOCATION:
                                Assert.assertEquals(array.length(), 1, "Query requested entities skipping 1, result should have contained 1 entity, but it contains " + array.length());
                                break;
                            case THING:
                                Assert.assertEquals(array.length(), 0, "Query requested entities skipping 1, result should have contained 0 entity, but it contains " + array.length());
                                break;
                        }
                        break;
                    case FEATURE_OF_INTEREST:
                        Assert.assertEquals(array.length(), 5, "Query requested entities skipping 1, result should have contained 5 entities, but it contains " + array.length());
                        break;
                    case OBSERVED_PROPERTY:
                        Assert.assertTrue(array.length() == 1 || array.length() == 0, "Query requested entities skipping 1, result should have contained 0 or 1 entity, but it contains " + array.length());
                        break;
                    case HISTORICAL_LOCATION:
                        switch (relationEntityType) {
                            case LOCATION:
                                Assert.assertEquals(array.length(), 0, "Query requested entities skipping 1, result should have contained 0 entity, but it contains " + array.length());
                                break;
                        }
                        break;
                    case SENSOR:
                        Assert.assertEquals(array.length(), 0, "Query requested entities skipping 1, result should have contained 0 entity, but it contains " + array.length());
                        break;
                    case DATASTREAM:
                        switch (relationEntityType) {
                            case OBSERVATION:
                                Assert.assertEquals(array.length(), 2, "Query requested entities skipping 1, result should have contained 2 entities, but it contains " + array.length());
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    private void checkTopForEntityType(EntityType entityType) {
        try {
            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=1");
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            String response = responseMap.get("response").toString();
            JSONArray array = new JSONObject(response).getJSONArray("value");
            Assert.assertEquals(array.length(), 1, "Query requested 1 entity but response contains " + array.length());
            try {
                Assert.assertNotNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
            } catch (JSONException e) {
                Assert.fail("The response does not have nextLink");
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=2");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            Assert.assertEquals(array.length(), 2, "Query requested 2 entities but response contains " + array.length());
            switch (entityType) {
                case THING:
                case LOCATION:
                case FEATURE_OF_INTEREST:
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                default:
                    try {
                        Assert.assertNotNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                        Assert.fail("The response does not have nextLink");
                    }
                    break;
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=3");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            switch (entityType) {
                case THING:
                    Assert.assertEquals(array.length(), 2, "Query requested 3 Things, there are only 2 Things,  but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                case LOCATION:
                    Assert.assertEquals(array.length(), 2, "Query requested 3 Locations, there are only 2 Locations,  but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    Assert.assertEquals(array.length(), 2, "Query requested 3 FeaturesOfInterest, there are only 2 FeaturesOfInterest,  but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                case OBSERVED_PROPERTY:
                    Assert.assertEquals(array.length(), 3, "Query requested 3 entities but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                default:
                    Assert.assertEquals(array.length(), 3, "Query requested 3 entities but response contains " + array.length());
                    try {
                        Assert.assertNotNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                        Assert.fail("The response does not have nextLink");
                    }
                    break;
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=4");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            switch (entityType) {
                case THING:
                    Assert.assertEquals(array.length(), 2, "Query requested 4 Things, there are only 2 Things,  but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                case LOCATION:
                    Assert.assertEquals(array.length(), 2, "Query requested 4 Locations, there are only 2 Locations,  but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    Assert.assertEquals(array.length(), 2, "Query requested 4 FeaturesOfInterest, there are only 2 FeaturesOfInterest,  but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                case OBSERVED_PROPERTY:
                    Assert.assertEquals(array.length(), 3, "Query requested 4 ObservedProperties, there are only 3 ObservedProperties,  but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                case SENSOR:
                case HISTORICAL_LOCATION:
                case DATASTREAM:
                    Assert.assertEquals(array.length(), 4, "Query requested 4 entities but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                default:
                    Assert.assertEquals(array.length(), 4, "Query requested 4 entities but response contains " + array.length());
                    try {
                        Assert.assertNotNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                        Assert.fail("The response does not have nextLink");
                    }
                    break;
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=5");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            switch (entityType) {
                case THING:
                    Assert.assertEquals(array.length(), 2, "Query requested 5 Things, there are only 2 Things,  but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                case LOCATION:
                    Assert.assertEquals(array.length(), 2, "Query requested 5 Locations, there are only 2 Locations,  but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    Assert.assertEquals(array.length(), 2, "Query requested 5 FeaturesOfInterest, there are only 2 FeaturesOfInterest,  but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                case OBSERVED_PROPERTY:
                    Assert.assertEquals(array.length(), 3, "Query requested 5 ObservedProperties, there are only 3 ObservedProperties,  but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                case SENSOR:
                    Assert.assertEquals(array.length(), 4, "Query requested 5 Sensors, there are only 4 Sensors,  but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                case HISTORICAL_LOCATION:
                    Assert.assertEquals(array.length(), 4, "Query requested 5 HistoricalLocations, there are only 4 HistoricalLocations,  but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                case DATASTREAM:
                    Assert.assertEquals(array.length(), 4, "Query requested 5 Datastreams, there are only 4 Datastreams, but response contains " + array.length());
                    try {
                        Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                    }
                    break;
                default:
                    Assert.assertEquals(array.length(), 5, "Query requested 5 entities but response contains " + array.length());
                    try {
                        Assert.assertNotNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                    } catch (JSONException e) {
                        Assert.fail("The response does not have nextLink");
                    }
                    break;
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=12");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            try {
                Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
            } catch (JSONException e) {
            }

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$top=13");
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            try {
                Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
            } catch (JSONException e) {
            }
            switch (entityType) {
                case THING:
                    Assert.assertEquals(array.length(), 2, "Query requested 13 Things, there are only 2 Things,  but response contains " + array.length());
                    break;
                case LOCATION:
                    Assert.assertEquals(array.length(), 2, "Query requested 13 Locations, there are only 2 Locations,  but response contains " + array.length());
                    break;
                case FEATURE_OF_INTEREST:
                    Assert.assertEquals(array.length(), 2, "Query requested 13 FeaturesOfInterest, there are only 2 FeaturesOfInterest,  but response contains " + array.length());
                    break;
                case OBSERVED_PROPERTY:
                    Assert.assertEquals(array.length(), 3, "Query requested 13 ObservedProperties, there are only 3 ObservedProperties,  but response contains " + array.length());
                    break;
                case SENSOR:
                    Assert.assertEquals(array.length(), 4, "Query requested 13 Sensors, there are only 4 Sensors,  but response contains " + array.length());
                    break;
                case HISTORICAL_LOCATION:
                    Assert.assertEquals(array.length(), 4, "Query requested 13 HistoricalLocations, there are only 4 HistoricalLocations,  but response contains " + array.length());
                    break;
                case DATASTREAM:
                    Assert.assertEquals(array.length(), 4, "Query requested 13 Datastreams, there are only 4 Datastreams, but response contains " + array.length());
                    break;
                case OBSERVATION:
                    Assert.assertEquals(array.length(), 12, "Query requested 13 Observations, there are only 12 Observations, but response contains " + array.length());
                    break;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    private void checkTopForEntityTypeRelation(EntityType entityType) {
        try {
            String[] relations = EntityRelations.getRelationsListFor(entityType);
            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, null);
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            String response = responseMap.get("response").toString();
            JSONArray array = new JSONObject(response).getJSONArray("value");
            if (array.length() == 0) {
                return;
            }
            long id = array.getJSONObject(0).getLong(ControlInformation.ID);

            for (String relation : relations) {
                if (relation.charAt(relation.length() - 1) != 's' && !relation.equals("FeaturesOfInterest")) {
                    continue;
                }
                EntityType relationEntityType = getEntityTypeFor(relation);
                urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, "?$top=3");
                responseMap = HTTPMethods.doGet(urlString);
                response = responseMap.get("response").toString();
                array = new JSONObject(response).getJSONArray("value");
                switch (entityType) {
                    case THING:
                        try {
                            Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                        } catch (JSONException e) {
                        }
                        switch (relationEntityType) {
                            case LOCATION:
                                Assert.assertEquals(array.length(), 1, "Query requested entities 3 entities, result should have contained 1 entity, but it contains " + array.length());
                                break;
                            case HISTORICAL_LOCATION:
                                Assert.assertEquals(array.length(), 2, "Query requested entities 3 entities, result should have contained 2 entities, but it contains " + array.length());
                                break;
                            case DATASTREAM:
                                Assert.assertEquals(array.length(), 2, "Query requested entities 3 entities, result should have contained 2 entities, but it contains " + array.length());
                                break;
                        }
                        break;
                    case LOCATION:
                        try {
                            Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                        } catch (JSONException e) {
                        }
                        switch (relationEntityType) {
                            case HISTORICAL_LOCATION:
                                Assert.assertEquals(array.length(), 2, "Query requested entities 3 entities, result should have contained 2 entities, but it contains "+ array.length());
                                break;
                            case THING:
                                Assert.assertEquals(array.length(), 1, "Query requested entities 3 entities, result should have contained 1 entity, but it contains" + array.length());
                                break;
                        }
                        break;
                    case FEATURE_OF_INTEREST:
                        Assert.assertEquals(array.length(), 3, "Query requested entities 3 entities, result should have contained 3 entities, but it contains " + array.length());
                        try {
                            Assert.assertNotNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                        } catch (JSONException e) {
                            Assert.fail("The response does not have nextLink");
                        }
                        break;
                    case OBSERVED_PROPERTY:
                        Assert.assertTrue(array.length() == 1 || array.length() == 2, "Query requested entities 3 entities, result should have contained 1 or 2 entities, but it contains " + array.length());
                        try {
                            Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response should not have nextLink");
                        } catch (JSONException e) {
                        }
                        break;
                    case HISTORICAL_LOCATION:
                        try {
                            Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response should not have nextLink");
                        } catch (JSONException e) {
                        }
                        switch (relationEntityType) {
                            case LOCATION:
                                Assert.assertEquals(array.length(), 1, "Query requested entities 3 entities, result should have contained 1 entity, but it contains " + array.length());
                                break;
                        }
                        break;
                    case SENSOR:
                        try {
                            Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response should not have nextLink");
                        } catch (JSONException e) {
                        }
                        Assert.assertEquals(array.length(), 1, "Query requested entities 3 entities, result should have contained 1 entity, but it contains " + array.length());
                        break;
                    case DATASTREAM:
                        try {
                            Assert.assertNull(new JSONObject(response).get("@iot.nextLink"), "The response does not have nextLink");
                        } catch (JSONException e) {
                        }
                        switch (relationEntityType) {
                            case OBSERVATION:
                                Assert.assertEquals(array.length(), 3, "Query requested entities 3 entities, result should have contained 3 entities, but it contains " + array.length());
                                break;
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    private void checkSelectForEntityType(EntityType entityType) {
        List<String> selectedProperties;
        String[] properties = EntityProperties.getPropertiesListFor(entityType);
        for (String property : properties) {
            selectedProperties = new ArrayList<>();
            selectedProperties.add(property);
            String response = getEntities(entityType, -1, null, selectedProperties, null);
            checkEntitiesAllAspectsForSelectResponse(entityType, response, selectedProperties);
        }
        selectedProperties = new ArrayList<>();
        for (String property : properties) {
            selectedProperties.add(property);
            String response = getEntities(entityType, -1, null, selectedProperties, null);
            checkEntitiesAllAspectsForSelectResponse(entityType, response, selectedProperties);
        }
    }

    private void checkSelectForEntityTypeRelations(EntityType entityType) {
        try {
            String[] parentRelations = EntityRelations.getRelationsListFor(entityType);
            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, null);
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            String response = responseMap.get("response").toString();
            JSONArray array = new JSONObject(response).getJSONArray("value");
            if (array.length() == 0) {
                return;
            }
            long id = array.getJSONObject(0).getLong(ControlInformation.ID);

            for (String parentRelation : parentRelations) {
                EntityType relationEntityType = getEntityTypeFor(parentRelation);
                List<String> selectedProperties;
                String[] properties = EntityProperties.getPropertiesListFor(relationEntityType);
                for (String property : properties) {
                    selectedProperties = new ArrayList<>();
                    selectedProperties.add(property);
                    response = getEntities(entityType, id, relationEntityType, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(relationEntityType, response, selectedProperties);
                }
                selectedProperties = new ArrayList<>();
                for (String property : properties) {
                    selectedProperties.add(property);response = getEntities(entityType, id, relationEntityType, selectedProperties, null);
                    checkEntitiesAllAspectsForSelectResponse(relationEntityType, response, selectedProperties);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    public String getEntities(EntityType entityType, long id, EntityType relationEntityType, List<String> selectedProperties, List<String> expandedRelations) {
        String urlString = rootUri;
        String selectString = "";
        if (selectedProperties != null && selectedProperties.size() > 0) {
            selectString = "?$select=";
            for (String select : selectedProperties) {
                if (selectString.charAt(selectString.length() - 1) != '=') {
                    selectString += ',';
                }
                selectString += select;
            }
        }
        String expandString = "";
        if (expandedRelations != null && expandedRelations.size() > 0) {
            expandString = selectString.equals("") ? "?$expand=" : "&$expand=";
            for (String expand : expandedRelations) {
                if (expandString.charAt(expandString.length() - 1) != '=') {
                    expandString += ',';
                }
                expandString += expand;
            }
        }
        if (entityType != null) {
            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, selectString + expandString);
        }
        Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
        String response = responseMap.get("response").toString();
        int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
        Assert.assertEquals(responseCode, 200, "Error during getting entities: " + entityType.name());
        return response;
    }

    public void checkEntitiesAllAspectsForSelectResponse(EntityType entityType, String response, List<String> selectedProperties) {
        checkEntitiesProperties(entityType, response, selectedProperties);
        checkEntitiesRelations(entityType, response, selectedProperties, null);
    }


    public void checkEntitiesProperties(EntityType entityType, String response, List<String> selectedProperties) {
        try {
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray entities = null;
            if(response.contains("value")) {
                entities = jsonResponse.getJSONArray("value");
            }else{
                entities = new JSONArray();
                entities.put(jsonResponse);
            }
            checkPropertiesForEntityArray(entityType, entities, selectedProperties);

        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }

    }

    public void checkPropertiesForEntityArray(EntityType entityType, JSONArray entities, List<String> selectedProperties) {
        int count = 0;
        for (int i = 0; i < entities.length() && count < 2; i++) {
            count++;
            JSONObject entity = null;
            try {
                entity = entities.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
                Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            }
            checkEntityProperties(entityType, entity, selectedProperties);
        }
    }

    public void checkEntityProperties(EntityType entityType, Object response, List<String> selectedProperties) {
        try {
            JSONObject entity = new JSONObject(response.toString());
            String[] properties = EntityProperties.getPropertiesListFor(entityType);
            for (String property : properties) {
                if (selectedProperties.contains(property)) {
                    try {
                        Assert.assertNotNull(entity.get(property), "Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                    } catch (JSONException e) {
                        Assert.fail("Entity type \"" + entityType + "\" does not have selected property: \"" + property + "\".");
                    }
                } else {
                    try {
                        Assert.assertNull(entity.get(property), "Entity type \"" + entityType + "\" contains not-selected property: \"" + property + "\".");
                    } catch (JSONException e) {
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n"+e.getMessage());
        }

    }

    public void checkEntitiesRelations(EntityType entityType, String response, List<String> selectedProperties, List<String> expandedRelations) {
        try {
            JSONObject jsonResponse = new JSONObject(response.toString());
            JSONArray entities = null;
            if(response.contains("value")) {
                entities = jsonResponse.getJSONArray("value");
            }else{
                entities = new JSONArray();
                entities.put(jsonResponse);
            }
            int count = 0;
            for (int i = 0; i < entities.length() && count < 2; i++) {
                count++;
                JSONObject entity = entities.getJSONObject(i);
                checkEntityRelations(entityType, entity, selectedProperties, expandedRelations);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }

    }

    public void checkEntityRelations(EntityType entityType, Object response, List<String> selectedProperties, List<String> expandedRelations) {
        try {
            JSONObject entity = new JSONObject(response.toString());
            String[] relations = EntityRelations.getRelationsListFor(entityType);
            for (String relation : relations) {
                if (selectedProperties == null || selectedProperties.contains(relation)) {
                    if (expandedRelations == null || !listContainsString(expandedRelations, relation)) {
                        try {
                            Assert.assertNotNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                        } catch (JSONException e) {
                            Assert.fail("Entity type \"" + entityType + "\" does not have selected relation: \"" + relation + "\".");
                        }
                    } else {
                            Assert.assertNotNull(entity.get(relation), "Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                            JSONArray expandedEntityArray = null;
                        try {
                            if (relation.charAt(relation.length() - 1) != 's' && !relation.equals("FeaturesOfInterest")) {
                                expandedEntityArray = new JSONArray();
                                expandedEntityArray.put(entity.getJSONObject(relation));
                            } else {
                                expandedEntityArray = entity.getJSONArray(relation);
                            }
                        }catch (JSONException e){
                            Assert.fail("Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "\".");
                        }
                            checkPropertiesForEntityArray(getEntityTypeFor(relation), expandedEntityArray, new ArrayList<String>(Arrays.asList(EntityProperties.getPropertiesListFor(relation))));
                            if(listContainsString(expandedRelations, "/")) {
                                String[] secondLevelRelations = EntityRelations.getRelationsListFor(relation);
                                JSONObject expandedEntity = expandedEntityArray.getJSONObject(0);
                                for (String secondLeveleRelation : secondLevelRelations) {
                                    if (listContainsString(expandedRelations, relation+"/"+secondLeveleRelation)) {

                                        expandedEntityArray = null;
                                        try {
                                            if (secondLeveleRelation.charAt(secondLeveleRelation.length() - 1) != 's' && !secondLeveleRelation.equals("FeaturesOfInterest")) {
                                                expandedEntityArray = new JSONArray();
                                                expandedEntityArray.put(expandedEntity.getJSONObject(secondLeveleRelation));
                                            } else {
                                                expandedEntityArray = expandedEntity.getJSONArray(secondLeveleRelation);
                                            }
                                        } catch (JSONException e){
                                            Assert.fail("Entity type \"" + entityType + "\" does not have expanded relation Correctly: \"" + relation + "/"+secondLeveleRelation+"\".");
                                        }
                                        checkPropertiesForEntityArray(getEntityTypeFor(secondLeveleRelation), expandedEntityArray, new ArrayList<String>(Arrays.asList(EntityProperties.getPropertiesListFor(secondLeveleRelation))));
                                    }
                                }
                            }
                    }
                } else {
                    try {
                        Assert.assertNull(entity.get(relation + ControlInformation.NAVIGATION_LINK), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                    } catch (JSONException e) {
                    }
                    try {
                        Assert.assertNull(entity.get(relation), "Entity type \"" + entityType + "\" contains not-selectd relation: \"" + relation + "\".");
                    } catch (JSONException e) {
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    private void checkExpandtForEntityType(EntityType entityType) {
        List<String> expandedRelations;
        String[] relations = EntityRelations.getRelationsListFor(entityType);
        for (String relation : relations) {
            expandedRelations = new ArrayList<>();
            expandedRelations.add(relation);
            String response = getEntities(entityType, -1, null, null, expandedRelations);
            checkEntitiesAllAspectsForExpandResponse(entityType, response, expandedRelations);
        }
        expandedRelations = new ArrayList<>();
        for (String relation : relations) {
            expandedRelations.add(relation);
            String response = getEntities(entityType, -1, null, null, expandedRelations);
            checkEntitiesAllAspectsForExpandResponse(entityType, response, expandedRelations);
        }
    }

    private void checkExpandtForEntityTypeRelations(EntityType entityType) {
        try {
            String[] parentRelations = EntityRelations.getRelationsListFor(entityType);
            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, null);
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            String response = responseMap.get("response").toString();
            JSONArray array = new JSONObject(response).getJSONArray("value");
            if (array.length() == 0) {
                return;
            }
            long id = array.getJSONObject(0).getLong(ControlInformation.ID);

            for(String parentRelation: parentRelations) {
                EntityType relationEntityType = getEntityTypeFor(parentRelation);
                List<String> expandedRelations;
                String[] relations = EntityRelations.getRelationsListFor(relationEntityType);
                for (String relation : relations) {
                    expandedRelations = new ArrayList<>();
                    expandedRelations.add(relation);
                    response = getEntities(entityType, id, relationEntityType, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(relationEntityType, response, expandedRelations);
                }
                expandedRelations = new ArrayList<>();
                for (String relation : relations) {
                    expandedRelations.add(relation);
                    response = getEntities(entityType, id, relationEntityType, null, expandedRelations);
                    checkEntitiesAllAspectsForExpandResponse(relationEntityType, response, expandedRelations);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    private void checkExpandtForEntityTypeMultilevelRelations(EntityType entityType) {
        try {
            String[] parentRelations = EntityRelations.getRelationsListFor(entityType);
            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, null);
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            String response = responseMap.get("response").toString();
            JSONArray array = new JSONObject(response).getJSONArray("value");
            if (array.length() == 0) {
                return;
            }
            long id = array.getJSONObject(0).getLong(ControlInformation.ID);

            for(String parentRelation: parentRelations) {
                EntityType relationEntityType = getEntityTypeFor(parentRelation);
                List<String> expandedRelations;
                String[] relations = EntityRelations.getRelationsListFor(relationEntityType);
                for (String relation : relations) {
                    String[] secondLevelRelations = EntityRelations.getRelationsListFor(relation);

                    for (String secondLevelRelation: secondLevelRelations) {
                        expandedRelations = new ArrayList<>();
                        expandedRelations.add(relation + "/" + secondLevelRelation);
                        response = getEntities(entityType, id, relationEntityType, null, expandedRelations);
                        checkEntitiesAllAspectsForExpandResponse(relationEntityType, response, expandedRelations);
                    }
                }
                expandedRelations = new ArrayList<>();
                for (String relation : relations) {
                    String[] secondLevelRelations = EntityRelations.getRelationsListFor(relation);
                    for (String secondLevelRelation: secondLevelRelations) {
                        expandedRelations.add(relation+"/"+secondLevelRelation);
                        response = getEntities(entityType, id, relationEntityType, null, expandedRelations);
                        checkEntitiesAllAspectsForExpandResponse(relationEntityType, response, expandedRelations);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    private void checkExpandtForEntityTypeMultilevel(EntityType entityType) {

        List<String> expandedRelations;
        String[] relations = EntityRelations.getRelationsListFor(entityType);
        for (String relation : relations) {
            String[] secondLevelRelations = EntityRelations.getRelationsListFor(relation);

            for (String secondLevelRelation: secondLevelRelations) {
                expandedRelations = new ArrayList<>();
                expandedRelations.add(relation + "/" + secondLevelRelation);
                String response = getEntities(entityType, -1, null, null, expandedRelations);
                checkEntitiesAllAspectsForExpandResponse(entityType, response, expandedRelations);
            }
        }
        expandedRelations = new ArrayList<>();
        for (String relation : relations) {
            String[] secondLevelRelations = EntityRelations.getRelationsListFor(relation);
            for (String secondLevelRelation: secondLevelRelations) {
                expandedRelations.add(relation+"/"+secondLevelRelation);
                String response = getEntities(entityType, -1, null, null, expandedRelations);
                checkEntitiesAllAspectsForExpandResponse(entityType, response, expandedRelations);
            }
        }
    }

    private void checkCountForEntityType(EntityType entityType) {

        String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$count=true");
        Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
        String response = responseMap.get("response").toString();
        int count = -1;
        try {
            count = new JSONObject(response).getInt("@iot.count");
        } catch (JSONException e){
            Assert.fail("the query asked for count but the response does not contain count, for getting collection: "+entityType);
        }
        switch (entityType) {
            case THING:
            case LOCATION:
            case FEATURE_OF_INTEREST:
                Assert.assertEquals(count, 2, "The count for "+entityType+"should be 2, but it is " + count);
                break;
            case OBSERVED_PROPERTY:
                Assert.assertEquals(count, 3, "The count for "+entityType+"should be 3, but it is " + count);
                break;
            case HISTORICAL_LOCATION:
            case SENSOR:
            case DATASTREAM:
                Assert.assertEquals(count, 4, "The count for "+entityType+"should be 4, but it is " + count);
                break;
            case OBSERVATION:
                Assert.assertEquals(count, 12, "The count for "+entityType+"should be 12, but it is " + count);
                break;
            default:
                break;
        }

        urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$count=false");
        responseMap = HTTPMethods.doGet(urlString);
        response = responseMap.get("response").toString();
        try {
            Assert.assertNull(new JSONObject(response).getInt("@iot.count"), "the query asked for not count but the response does contain count, for getting collection: "+entityType);
            Assert.fail("the query asked for not count but the response does contain count, for getting collection: "+entityType);
        } catch (JSONException e){
        }
    }

    private void checkCountForEntityTypeRelations(EntityType entityType) {
        try {
            String[] relations = EntityRelations.getRelationsListFor(entityType);
            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, null);
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            String response = responseMap.get("response").toString();
            JSONArray array = new JSONObject(response).getJSONArray("value");
            if (array.length() == 0) {
                return;
            }
            long id = array.getJSONObject(0).getLong(ControlInformation.ID);

            for (String relation : relations) {
                if(relation.charAt(relation.length()-1)!='s' && !relation.equals("FeatureOfInterest")){
                    return;
                }
                EntityType relationEntityType = getEntityTypeFor(relation);
                urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, "?$count=true");
                responseMap = HTTPMethods.doGet(urlString);
                response = responseMap.get("response").toString();
                int count = -1;
                try {
                    count = new JSONObject(response).getInt("@iot.count");
                } catch (JSONException e) {
                    Assert.fail("the query asked for count but the response does not contain count, for getting collection: " + entityType);
                }
                switch (relationEntityType) {
                    case THING:
                    case LOCATION:
                        Assert.assertEquals(count, 1, "The count for " + entityType + "should be 1, but it is " + count);
                        break;
                    case HISTORICAL_LOCATION:
                    case DATASTREAM:
                        switch (entityType) {
                            case THING:
                                Assert.assertEquals(count, 2, "The count for " + entityType + "should be 2, but it is " + count);
                                break;
                            case SENSOR:
                                Assert.assertEquals(count, 1, "The count for " + entityType + "should be 1, but it is " + count);
                                break;
                            case OBSERVED_PROPERTY:
                                Assert.assertTrue(count == 2 || count == 1, "The count for " + entityType + "should be 1 or 2, but it is " + count);
                                break;
                        }
                        break;
                    case OBSERVATION:
                        if(entityType.equals(EntityType.DATASTREAM)) {
                            Assert.assertEquals(count, 3, "The count for " + entityType + "should be 3, but it is " + count);
                        } else if(entityType.equals(EntityType.FEATURE_OF_INTEREST)) {
                            Assert.assertEquals(count, 6, "The count for " + entityType + "should be 6, but it is " + count);
                        }
                        break;
                    default:
                        break;
                }

                urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, "?$count=false");
                responseMap = HTTPMethods.doGet(urlString);
                response = responseMap.get("response").toString();
                try {
                    Assert.assertNull(new JSONObject(response).getInt("@iot.count"), "the query asked for not count but the response does contain count, for getting collection: " + entityType);
                    Assert.fail("the query asked for not count but the response does contain count, for getting collection: " + entityType);
                } catch (JSONException e) {
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    public void checkEntitiesAllAspectsForExpandResponse(EntityType entityType, String response, List<String> expandedRelations) {
        checkEntitiesRelations(entityType, response, null, expandedRelations);
    }

    private void checkFilterForEntityType(EntityType entityType){
        String[] properties = EntityProperties.getPropertiesListFor(entityType);
        List<String> filteredProperties;
        List<String> samplePropertyValues;
        for (int i = 0; i < properties.length; i++) {
            filteredProperties=new ArrayList<>();
            samplePropertyValues = new ArrayList<>();
            String property = properties[i];
            filteredProperties.add(property);
            if(property.equals("location") || property.equals("feature") || property.equals("unitOfMeasurement")){
                continue;
            }
            String propertyValue = EntityPropertiesSampleValue.getPropertyValueFor(entityType, i);
            samplePropertyValues.add(propertyValue);

            propertyValue = propertyValue.replaceAll(" ","%20");
            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$filter=" + property + "%20lt%20" + propertyValue);
            Map responseMap = HTTPMethods.doGet(urlString);
            String response = responseMap.get("response").toString();
            checkPropertiesForFilter(response, filteredProperties, samplePropertyValues, -2);

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$filter=" + property + "%20le%20" + propertyValue);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            checkPropertiesForFilter(response, filteredProperties, samplePropertyValues, -1);

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$filter=" + property + "%20eq%20" + propertyValue);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            checkPropertiesForFilter(response, filteredProperties, samplePropertyValues, 0);

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$filter=" + property + "%20ge%20" + propertyValue);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            checkPropertiesForFilter(response, filteredProperties, samplePropertyValues, 1);

            urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, "?$filter=" + property + "%20gt%20" + propertyValue);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            checkPropertiesForFilter(response, filteredProperties, samplePropertyValues, 2);
        }
    }

    private void checkFilterForEntityTypeRelations(EntityType entityType){
        String[] relations = EntityRelations.getRelationsListFor(entityType);
        String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, null);
        Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
        String response = responseMap.get("response").toString();
        JSONArray array = null;
        try {
            array = new JSONObject(response).getJSONArray("value");
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
        if (array.length() == 0) {
            return;
        }
        long id = 0;
        try {
            id = array.getJSONObject(0).getLong(ControlInformation.ID);
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }

        for (String relation : relations) {
            if (relation.charAt(relation.length() - 1) != 's' && !relation.equals("FeatureOfInterest")) {
                return;
            }
            EntityType relationEntityType = getEntityTypeFor(relation);

            String[] properties = EntityProperties.getPropertiesListFor(relationEntityType);
            List<String> filteredProperties;
            List<String> samplePropertyValues;
            for (int i = 0; i < properties.length; i++) {
                filteredProperties = new ArrayList<>();
                samplePropertyValues = new ArrayList<>();
                String property = properties[i];
                filteredProperties.add(property);
                if (property.equals("location") || property.equals("feature") || property.equals("unitOfMeasurement")) {
                    continue;
                }
                String propertyValue = EntityPropertiesSampleValue.getPropertyValueFor(relationEntityType, i);
                samplePropertyValues.add(propertyValue);

                propertyValue = propertyValue.replaceAll(" ", "%20");
                urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, "?$filter=" + property + "%20lt%20" + propertyValue);
                responseMap = HTTPMethods.doGet(urlString);
                response = responseMap.get("response").toString();
                checkPropertiesForFilter(response, filteredProperties, samplePropertyValues, -2);

                urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, "?$filter=" + property + "%20le%20" + propertyValue);
                responseMap = HTTPMethods.doGet(urlString);
                response = responseMap.get("response").toString();
                checkPropertiesForFilter(response, filteredProperties, samplePropertyValues, -1);

                urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, "?$filter=" + property + "%20eq%20" + propertyValue);
                responseMap = HTTPMethods.doGet(urlString);
                response = responseMap.get("response").toString();
                checkPropertiesForFilter(response, filteredProperties, samplePropertyValues, 0);

                urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, "?$filter=" + property + "%20ge%20" + propertyValue);
                responseMap = HTTPMethods.doGet(urlString);
                response = responseMap.get("response").toString();
                checkPropertiesForFilter(response, filteredProperties, samplePropertyValues, 1);

                urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, relationEntityType, "?$filter=" + property + "%20gt%20" + propertyValue);
                responseMap = HTTPMethods.doGet(urlString);
                response = responseMap.get("response").toString();
                checkPropertiesForFilter(response, filteredProperties, samplePropertyValues, 2);
            }
        }
    }

    private void checkPropertiesForFilter(String response, List<String> properties, List<String> values, int operator){
        try {
            JSONObject entities = new JSONObject(response);
            JSONArray entityArray = entities.getJSONArray("value");
            for (int i = 0; i < entityArray.length(); i++) {
                JSONObject entity = entityArray.getJSONObject(i);
                for (int j = 0; j < properties.size(); j++) {
                    String propertyValue = null;
                    try {
                        propertyValue = entity.get(properties.get(j)).toString();
                    } catch (JSONException e){
                        Assert.fail("The entity does not have property "+properties.get(j));
                    }
                    String value = values.get(j);
                    if(value.charAt(0)=='\''){
                        value = value.substring(1,value.length()-1);
                    }
                    switch (operator){
                        case -2:
                            Assert.assertTrue(propertyValue.compareTo(value)<0,properties.get(j)+" should be less than "+value+". But the property value is "+propertyValue);
                            break;
                        case -1:
                            Assert.assertTrue(propertyValue.compareTo(value)<=0,properties.get(j)+" should be less than or equal to "+value+". But the property value is "+propertyValue);
                            break;
                        case 0:
                            Assert.assertTrue(propertyValue.compareTo(value)==0,properties.get(j)+" should be equal to than "+value+". But the property value is "+propertyValue);
                            break;
                        case 1:
                            Assert.assertTrue(propertyValue.compareTo(value)>=0,properties.get(j)+" should be greate than or equal to "+value+". But the property value is "+propertyValue);
                            break;
                        case 2:
                            Assert.assertTrue(propertyValue.compareTo(value)>0,properties.get(j)+" should be greater than "+value+". But the property value is "+propertyValue);
                            break;
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    private EntityType getEntityTypeFor(String name) {
        switch (name.toLowerCase()) {
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
                    "            \"encodingType\": \"http://example.org/location_types/GeoJSON\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"Datastreams\": [\n" +
                    "        {\n" +
                    "            \"unitOfMeasurement\": {\n" +
                    "                \"name\": \"Lumen\",\n" +
                    "                \"symbol\": \"lm\",\n" +
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html/Lumen\"\n" +
                    "            },\n" +
                    "            \"description\": \"datastream 1\",\n" +
                    "            \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "            \"ObservedProperty\": {\n" +
                    "                \"name\": \"Luminous Flux\",\n" +
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html/LuminousFlux\",\n" +
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
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html/Lumen\"\n" +
                    "            },\n" +
                    "            \"description\": \"datastream 2\",\n" +
                    "            \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "            \"ObservedProperty\": {\n" +
                    "                \"name\": \"Tempretaure\",\n" +
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html/Tempreture\",\n" +
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
                    "            \"encodingType\": \"http://example.org/location_types/GeoJSON\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"Datastreams\": [\n" +
                    "        {\n" +
                    "            \"unitOfMeasurement\": {\n" +
                    "                \"name\": \"Lumen\",\n" +
                    "                \"symbol\": \"lm\",\n" +
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html/Lumen\"\n" +
                    "            },\n" +
                    "            \"description\": \"datastream 3\",\n" +
                    "            \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "            \"ObservedProperty\": {\n" +
                    "                \"name\": \"Second Luminous Flux\",\n" +
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html/LuminousFlux\",\n" +
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
                    "                \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html/Lumen\"\n" +
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
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }


    }

    private boolean listContainsString(List<String> list, String entity){
        for(String item:list) {
            if (item.toLowerCase().contains(entity.toLowerCase())){
                if(entity.toLowerCase().equals("locations")&&(item.toLowerCase().equals("historicallocations/thing")||item.toLowerCase().equals("historicallocations") || item.toLowerCase().equals("things/historicallocations")  || item.toLowerCase().equals("thing/historicallocations"))){
                    continue;
                }
                if(!entity.contains("/")&&item.contains("/"+entity)){
                    continue;
                }
                return true;

            }
        }
        return false;
    }

    @AfterClass
    private void deleteEverythings(){
        deleteEntityType(EntityType.OBSERVATION);
        deleteEntityType(EntityType.FEATURE_OF_INTEREST);
        deleteEntityType(EntityType.DATASTREAM);
        deleteEntityType(EntityType.SENSOR);
        deleteEntityType(EntityType.OBSERVED_PROPERTY);
        deleteEntityType(EntityType.HISTORICAL_LOCATION);
        deleteEntityType(EntityType.LOCATION);
        deleteEntityType(EntityType.THING);
    }


    private void deleteEntityType(EntityType entityType){
        JSONArray array = null;
        do {
            try {
                String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, null);
                Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
                int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
                JSONObject result = new JSONObject(responseMap.get("response").toString());
                array = result.getJSONArray("value");
                for (int i = 0; i < array.length(); i++) {
                    long id = array.getJSONObject(i).getLong(ControlInformation.ID);
                    deleteEntity(entityType, id);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            }
        } while (array.length() >0);
    }

    private void deleteEntity(EntityType entityType, long id) {
        String urlString = ServiceURLBuilder.buildURLString(rootUri,entityType,id,null,null);
        Map<String,Object> responseMap = HTTPMethods.doDelete(urlString);
        int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
        Assert.assertEquals(responseCode, 200, "DELETE does not work properly for " + entityType + " with id " + id + ". Returned with response code " + responseCode + ".");

        responseMap = HTTPMethods.doGet(urlString);
        responseCode = Integer.parseInt(responseMap.get("response-code").toString());
        Assert.assertEquals(responseCode, 404, "Deleted entity was not actually deleted : " + entityType + "(" + id + ").");
    }

}
