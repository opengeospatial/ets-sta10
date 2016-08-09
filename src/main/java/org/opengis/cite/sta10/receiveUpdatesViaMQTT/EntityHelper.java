/*
 * Copyright 2016 Open Geospatial Consortium.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.opengis.cite.sta10.receiveUpdatesViaMQTT;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opengis.cite.sta10.util.ControlInformation;
import org.opengis.cite.sta10.util.EntityType;
import org.opengis.cite.sta10.util.HTTPMethods;
import org.opengis.cite.sta10.util.ServiceURLBuilder;
import org.testng.Assert;

/**
 *
 * @author jab
 */
public class EntityHelper {

    private final String rootUri;

    public EntityHelper(String rootUri) {
        this.rootUri = rootUri;
    }

    private static String concatOverlapping(String s1, String s2) {
        if (!s1.contains(s2.substring(0, 1))) {
            return s1 + s2;
        }
        int idx = s2.length();
        try {
            while (!s1.endsWith(s2.substring(0, idx--))) ;
        } catch (Exception e) {
        }
        return s1 + s2.substring(idx + 1);
    }

    public void deleteEverything() {
        deleteEntityType(EntityType.OBSERVATION);
        deleteEntityType(EntityType.FEATURE_OF_INTEREST);
        deleteEntityType(EntityType.DATASTREAM);
        deleteEntityType(EntityType.SENSOR);
        deleteEntityType(EntityType.OBSERVED_PROPERTY);
        deleteEntityType(EntityType.HISTORICAL_LOCATION);
        deleteEntityType(EntityType.LOCATION);
        deleteEntityType(EntityType.THING);
    }

    /**
     * Delete all the entities of a certain entity type
     *
     * @param entityType The entity type from EntityType enum
     */
    private void deleteEntityType(EntityType entityType) {
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
        } while (array.length() > 0);
    }

    public long createDatastream(long thingId, long observedPropertyId, long sensorId) {
        try {
            String urlParameters = "{\n"
                    + "  \"unitOfMeasurement\": {\n"
                    + "    \"name\": \"Celsius\",\n"
                    + "    \"symbol\": \"degC\",\n"
                    + "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n"
                    + "  },\n"
                    + "  \"name\": \"test datastream.\",\n"
                    + "  \"description\": \"test datastream.\",\n"
                    + "  \"phenomenonTime\": \"2014-03-01T13:00:00Z/2015-05-11T15:30:00Z\",\n"
                    + "  \"resultTime\": \"2014-03-01T13:00:00Z/2015-05-11T15:30:00Z\",\n"
                    + "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n"
                    + "  \"Thing\": { \"@iot.id\": " + thingId + " },\n"
                    + "  \"ObservedProperty\":{ \"@iot.id\":" + observedPropertyId + "},\n"
                    + "  \"Sensor\": { \"@iot.id\": " + sensorId + " }\n"
                    + "}";
            JSONObject entity = postEntity(EntityType.DATASTREAM, urlParameters);
            return entity.getLong(ControlInformation.ID);
        } catch (JSONException e) {
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
        return -1;
    }

    public Long createDatastreamWithDeepInsert(long thingId) {
        try {
            String urlParameters = "{\n"
                    + "  \"unitOfMeasurement\": {\n"
                    + "    \"name\": \"Celsius\",\n"
                    + "    \"symbol\": \"degC\",\n"
                    + "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n"
                    + "  },\n"
                    + "  \"name\": \"test datastream.\",\n"
                    + "  \"description\": \"test datastream.\",\n"
                    + "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n"
                    + "  \"Thing\": { \"@iot.id\": " + thingId + " },\n"
                    + "   \"ObservedProperty\": {\n"
                    + "        \"name\": \"Luminous Flux\",\n"
                    + "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html#LuminousFlux\",\n"
                    + "        \"description\": \"Luminous Flux or Luminous Power is the measure of the perceived power of light.\"\n"
                    + "   },\n"
                    + "   \"Sensor\": {        \n"
                    + "        \"name\": \"Acme Fluxomatic 1000\",\n"
                    + "        \"description\": \"Acme Fluxomatic 1000\",\n"
                    + "        \"encodingType\": \"http://schema.org/description\",\n"
                    + "        \"metadata\": \"Light flux sensor\"\n"
                    + "   },\n"
                    + "      \"Observations\": [\n"
                    + "        {\n"
                    + "          \"phenomenonTime\": \"2015-03-01T00:10:00Z\",\n"
                    + "          \"result\": 10\n"
                    + "        }\n"
                    + "      ]"
                    + "}";
            JSONObject entity = postEntity(EntityType.DATASTREAM, urlParameters);
            return entity.getLong(ControlInformation.ID);
        } catch (JSONException ex) {
            Assert.fail("An Exception occurred during testing!", ex);
        }
        return -1l;
    }

    public long createFeatureOfInterest() {
        try {
            String urlParameters = "{\n"
                    + "  \"name\": \"A weather station.\",\n"
                    + "  \"description\": \"A weather station.\",\n"
                    + "  \"encodingType\": \"application/vnd.geo+json\",\n"
                    + "  \"feature\": {\n"
                    + "    \"type\": \"Point\",\n"
                    + "    \"coordinates\": [\n"
                    + "      10,\n"
                    + "      10\n"
                    + "    ]\n"
                    + "  }\n"
                    + "}";
            JSONObject entity = postEntity(EntityType.FEATURE_OF_INTEREST, urlParameters);
            return entity.getLong(ControlInformation.ID);
        } catch (JSONException e) {
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
        return -1;
    }

    public long createHistoricalLocation(long thingId, long locationId) {
        try {
            String urlParameters = "{\n"
                    + "  \"time\": \"2015-03-01T00:40:00.000Z\",\n"
                    + "  \"Thing\":{\"@iot.id\": " + thingId + "},\n"
                    + "  \"Locations\": [{\"@iot.id\": " + locationId + "}]  \n"
                    + "}";
            JSONObject entity = postEntity(EntityType.HISTORICAL_LOCATION, urlParameters);
            return entity.getLong(ControlInformation.ID);
        } catch (JSONException e) {
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
        return -1;
    }

    public long createLocation(long thingId) {
        try {
            String urlParameters = "{\n"
                    + "  \"name\": \"bow river\",\n"
                    + "  \"description\": \"bow river\",\n"
                    + "  \"encodingType\": \"application/vnd.geo+json\",\n"
                    + "  \"Things\":[{\"@iot.id\": " + thingId + "}],\n"
                    + "  \"location\": { \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }\n"
                    + "}";
            JSONObject entity = postEntity(EntityType.LOCATION, urlParameters);
            return entity.getLong(ControlInformation.ID);
        } catch (JSONException e) {
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
        return -1;
    }

    public long createObservation(long datastreamId, long featureOfInterstId) {
        try {
            String urlParameters = "{\n"
                    + "  \"phenomenonTime\": \"2015-03-01T00:40:00.000Z\",\n"
                    + "  \"validTime\": \"2016-01-01T02:01:01+01:00/2016-01-02T00:59:59+01:00\",\n"
                    + "  \"result\": 8,\n"
                    + "  \"parameters\":{\"param1\": \"some value1\", \"param2\": \"some value2\"},\n"
                    + "  \"Datastream\":{\"@iot.id\": " + datastreamId + "},\n"
                    + "  \"FeatureOfInterest\": {\"@iot.id\": " + featureOfInterstId + "}  \n"
                    + "}";
            JSONObject entity = postEntity(EntityType.OBSERVATION, urlParameters);
            return entity.getLong(ControlInformation.ID);
        } catch (JSONException e) {
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
        return -1;
    }

    public Long createObservationWithDeepInsert(long datastreamId) {
        try {
            String urlParameters = "{\n"
                    + "  \"phenomenonTime\": \"2015-03-01T00:00:00Z\",\n"
                    + "  \"result\": 100,\n"
                    + "  \"FeatureOfInterest\": {\n"
                    + "  \t\"name\": \"A weather station.\",\n"
                    + "  \t\"description\": \"A weather station.\",\n"
                    + "  \t\"encodingType\": \"application/vnd.geo+json\",\n"
                    + "    \"feature\": {\n"
                    + "      \"type\": \"Point\",\n"
                    + "      \"coordinates\": [\n"
                    + "        -114.05,\n"
                    + "        51.05\n"
                    + "      ]\n"
                    + "    }\n"
                    + "  },\n"
                    + "  \"Datastream\":{\"@iot.id\": " + datastreamId + "}\n"
                    + "}";
            JSONObject entity = postEntity(EntityType.OBSERVATION, urlParameters);
            return entity.getLong(ControlInformation.ID);
        } catch (JSONException ex) {
            Assert.fail("An Exception occurred during testing!", ex);
        }
        return -1l;
    }

    public long createObservedProperty() {
        try {
            String urlParameters = "{\n"
                    + "  \"name\": \"DewPoint Temperature\",\n"
                    + "  \"definition\": \"http://dbpedia.org/page/Dew_point\",\n"
                    + "  \"description\": \"The dewpoint temperature is the temperature to which the air must be cooled, at constant pressure, for dew to form. As the grass and other objects near the ground cool to the dewpoint, some of the water vapor in the atmosphere condenses into liquid water on the objects.\"\n"
                    + "}";
            JSONObject entity = postEntity(EntityType.OBSERVED_PROPERTY, urlParameters);
            return entity.getLong(ControlInformation.ID);
        } catch (JSONException e) {
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
        return -1;
    }

    public long createSensor() {
        try {
            String urlParameters = "{\n"
                    + "  \"name\": \"Fuguro Barometer\",\n"
                    + "  \"description\": \"Fuguro Barometer\",\n"
                    + "  \"encodingType\": \"http://schema.org/description\",\n"
                    + "  \"metadata\": \"Barometer\"\n"
                    + "}";
            JSONObject entity = postEntity(EntityType.SENSOR, urlParameters);
            return entity.getLong(ControlInformation.ID);
        } catch (JSONException e) {
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
        return -1;
    }

    public long createThing() {
        try {
            String urlParameters = "{"
                    + "\"name\":\"Test Thing\","
                    + "\"description\":\"This is a Test Thing From TestNG\""
                    + "}";
            JSONObject entity = postEntity(EntityType.THING, urlParameters);
            return entity.getLong(ControlInformation.ID);
        } catch (JSONException e) {
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
        return -1;
    }

    public Long createThingWithDeepInsert() {
        try {
            String urlParameters = "{\n"
                    + "  \"name\": \"Office Building\",\n"
                    + "  \"description\": \"Office Building\",\n"
                    + "  \"properties\": {\n"
                    + "    \"reference\": \"Third Floor\"\n"
                    + "  },\n"
                    + "  \"Locations\": [\n"
                    + "    {\n"
                    + "      \"name\": \"West Roof\",\n"
                    + "      \"description\": \"West Roof\",\n"
                    + "      \"location\": { \"type\": \"Point\", \"coordinates\": [-117.05, 51.05] },\n"
                    + "      \"encodingType\": \"application/vnd.geo+json\"\n"
                    + "    }\n"
                    + "  ],\n"
                    + "  \"Datastreams\": [\n"
                    + "    {\n"
                    + "      \"unitOfMeasurement\": {\n"
                    + "        \"name\": \"Lumen\",\n"
                    + "        \"symbol\": \"lm\",\n"
                    + "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html#Lumen\"\n"
                    + "      },\n"
                    + "      \"name\": \"Light exposure.\",\n"
                    + "      \"description\": \"Light exposure.\",\n"
                    + "      \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n"
                    + "      \"ObservedProperty\": {\n"
                    + "        \"name\": \"Luminous Flux\",\n"
                    + "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html#LuminousFlux\",\n"
                    + "        \"description\": \"Luminous Flux or Luminous Power is the measure of the perceived power of light.\"\n"
                    + "      },\n"
                    + "      \"Sensor\": {        \n"
                    + "        \"name\": \"Acme Fluxomatic 1000\",\n"
                    + "        \"description\": \"Acme Fluxomatic 1000\",\n"
                    + "        \"encodingType\": \"http://schema.org/description\",\n"
                    + "        \"metadata\": \"Light flux sensor\"\n"
                    + "      }\n"
                    + "    }\n"
                    + "  ]\n"
                    + "}";
            JSONObject entity = postEntity(EntityType.THING, urlParameters);
            return entity.getLong(ControlInformation.ID);
        } catch (JSONException ex) {
            Assert.fail("An Exception occurred during testing!", ex);
        }
        return -1l;
    }

    public void deleteEntity(EntityType entityType, long id) {
        String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, null, null);
        Map<String, Object> responseMap = HTTPMethods.doDelete(urlString);
        int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
        Assert.assertEquals(responseCode, 200, "DELETE does not work properly for " + entityType + " with id " + id + ". Returned with response code " + responseCode + ".");

        responseMap = HTTPMethods.doGet(urlString);
        responseCode = Integer.parseInt(responseMap.get("response-code").toString());
        Assert.assertEquals(responseCode, 404, "Deleted entity was not actually deleted : " + entityType + "(" + id + ").");
    }

    public DeepInsertInfo getDeepInsertInfo(EntityType entityType) {
        DeepInsertInfo result = new DeepInsertInfo(entityType);
        switch (entityType) {
            case THING: {
                result.getSubEntityTypes().add(EntityType.LOCATION);
                result.getSubEntityTypes().add(EntityType.DATASTREAM);
                result.getSubEntityTypes().add(EntityType.OBSERVED_PROPERTY);
                result.getSubEntityTypes().add(EntityType.SENSOR);
                break;
            }
            case DATASTREAM: {
                result.getSubEntityTypes().add(EntityType.OBSERVATION);
                result.getSubEntityTypes().add(EntityType.OBSERVED_PROPERTY);
                result.getSubEntityTypes().add(EntityType.SENSOR);
                break;
            }
            case OBSERVATION: {
                result.getSubEntityTypes().add(EntityType.FEATURE_OF_INTEREST);
                break;
            }
            default:
                throw new IllegalStateException();
        }
        return result;
    }

    public JSONObject getEntity(EntityType entityType, long id) {
        if (id == -1) {
            return null;
        }
        String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, null, null);
        try {
            return new JSONObject(HTTPMethods.doGet(urlString).get("response").toString());
        } catch (JSONException e) {
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            return null;
        }
    }

    public JSONObject getEntity(String relativeUrl) {
        String urlString = concatOverlapping(rootUri, relativeUrl);
        try {
            return new JSONObject(HTTPMethods.doGet(urlString).get("response").toString());
        } catch (JSONException e) {
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            return null;
        }
    }

    public Map<String, Object> getEntityChanges(EntityType entityType, List<String> selectedProperties) {
        return getEntityChanges(entityType).entrySet().stream().filter(x -> selectedProperties.contains(x.getKey())).collect(Collectors.toMap(x -> x.getKey(), x -> x.getValue()));
    }

    public Map<String, Object> getEntityChanges(EntityType entityType) {
        switch (entityType) {
            case THING:
                return getThingChanges();
            case DATASTREAM:
                return getDatastreamChanges();
            case FEATURE_OF_INTEREST:
                return getFeatureOfInterestChanges();
            case HISTORICAL_LOCATION:
                return getHistoricalLocationChanges();
            case LOCATION:
                return getLocationChanges();
            case OBSERVATION:
                return getObservationChanges();
            case OBSERVED_PROPERTY:
                return getObservedPropertyChanges();
            case SENSOR:
                return getSensorChanges();
            default:
                throw new IllegalStateException("Unsupported entityType '" + entityType + "'");
        }
    }

    public JSONObject patchEntity(EntityType entityType, Map<String, Object> changes, long id) {
        String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, null, null);
        try {
            Map<String, Object> responseMap = HTTPMethods.doPatch(urlString, new JSONObject(changes).toString());
            int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
            Assert.assertEquals(responseCode, 200, "Error during updating(PATCH) of entity " + entityType.name());

            responseMap = HTTPMethods.doGet(urlString);
            JSONObject result = new JSONObject(responseMap.get("response").toString());
            return result;

        } catch (JSONException e) {
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            return null;
        }
    }

    public JSONObject putEntity(EntityType entityType, Map<String, Object> changes, long id) {
        String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, null, null);
        try {
            JSONObject entity = getEntity(entityType, id);
            clearLinks(entity);
            for (Map.Entry<String, Object> entry : changes.entrySet()) {
                entity.put(entry.getKey(), entry.getValue());
            }
            Map<String, Object> responseMap = HTTPMethods.doPut(urlString, entity.toString());
            int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
            Assert.assertEquals(responseCode, 200, "Error during updating(PUT) of entity " + entityType.name());
            responseMap = HTTPMethods.doGet(urlString);
            JSONObject result = new JSONObject(responseMap.get("response").toString());
            return result;

        } catch (JSONException e) {
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            return null;
        }
    }

    public JSONObject updateEntitywithPATCH(EntityType entityType, long id) {
        return patchEntity(entityType, getEntityChanges(entityType), id);
    }

    public JSONObject updateEntitywithPUT(EntityType entityType, long id) {
        return putEntity(entityType, getEntityChanges(entityType), id);
    }

    private void clearLinks(Object obj) {
        if (!(obj instanceof JSONObject)) {
            return;
        }
        JSONObject entity = (JSONObject) obj;
        Iterator iterator = entity.keys();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            if (key.contains("@")) {
                iterator.remove();
                //entity.remove(key);
            } else {
                try {
                    Object val = entity.get(key);
                    if (val instanceof JSONObject) {
                        clearLinks((JSONObject) val);
                    } else if (val instanceof JSONArray) {
                        JSONArray arr = (JSONArray) val;
                        for (int i = 0; i < arr.length(); i++) {
                            clearLinks(arr.get(i));
                        }
                    }
                } catch (JSONException ex) {
                    Assert.fail();
                }
            }
        }
    }

    private Map<String, Object> getDatastreamChanges() {
        try {
            Map<String, Object> changes = new HashMap<>();
            changes.put("name", "Data coming from sensor on ISS.");
            changes.put("description", "Data coming from sensor on ISS.");
            changes.put("observationType", "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Observation");
            changes.put("unitOfMeasurement", new JSONObject("{\"name\": \"Entropy\",\"symbol\": \"S\",\"definition\": \"http://qudt.org/vocab/unit#Entropy\"}"));
            changes.put("phenomenonTime", "2015-12-12T12:12:12Z/2015-12-12T15:30:00Z");
            changes.put("resultTime", "2015-12-12T12:12:12Z/2015-12-12T15:30:00Z");
            return changes;
        } catch (JSONException ex) {
            Assert.fail("Generating Datastream changes failed", ex);
        }
        throw new IllegalStateException();
    }

    private Map<String, Object> getFeatureOfInterestChanges() {
        try {
            Map<String, Object> changes = new HashMap<>();
            changes.put("encodingType", "SQUARE");
            changes.put("feature", new JSONObject("{ \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }"));
            changes.put("name", "POIUYTREW");
            changes.put("description", "POIUYTREW");
            return changes;
        } catch (JSONException ex) {
            Assert.fail("Generating FeatureOfInterest changes failed", ex);
        }
        throw new IllegalStateException();
    }

    private Map<String, Object> getHistoricalLocationChanges() {
        Map<String, Object> changes = new HashMap<>();
        changes.put("time", "2015-08-01T00:00:00.000Z");
        return changes;
    }

    private Map<String, Object> getLocationChanges() {
        try {
            Map<String, Object> changes = new HashMap<>();
            changes.put("encodingType", "UPDATED ENCODING");
            changes.put("name", "UPDATED NAME");
            changes.put("description", "UPDATED DESCRIPTION");
            changes.put("location", new JSONObject("{ \"type\": \"Point\", \"coordinates\": [-114.05, 50] }}"));
            return changes;
        } catch (JSONException ex) {
            Assert.fail("Generating Location changes failed", ex);
        }
        throw new IllegalStateException();
    }

    private Map<String, Object> getObservationChanges() {
        try {
            Map<String, Object> changes = new HashMap<>();
            changes.put("result", "99");
            changes.put("phenomenonTime", "2015-08-01T00:40:00.000Z");
            changes.put("resultTime", "2015-12-12T12:12:12.000Z");
            changes.put("validTime", "2016-12-12T12:12:12+01:00/2016-12-12T23:59:59+01:00");
            changes.put("parameters", new JSONObject("{\"param1\": \"some updated value1\", \"param2\": \"some updated value2\"}"));

            return changes;
        } catch (JSONException ex) {
            Assert.fail("Generating Observation changes failed", ex);
        }
        throw new IllegalStateException();
    }

    private Map<String, Object> getObservedPropertyChanges() {
        Map<String, Object> changes = new HashMap<>();
        changes.put("name", "QWERTY");
        changes.put("definition", "ZXCVB");
        changes.put("description", "POIUYTREW");
        return changes;
    }

    private Map<String, Object> getSensorChanges() {
        Map<String, Object> changes = new HashMap<>();
        changes.put("name", "UPDATED");
        changes.put("description", "UPDATED");
        changes.put("encodingType", "http://schema.org/newDescription");
        changes.put("metadata", "UPDATED");
        return changes;
    }

    private Map<String, Object> getThingChanges() {
        Map<String, Object> changes = new HashMap<>();
        changes.put("name", "This is a Updated Test Thing From TestNG");
        changes.put("description", "This is a Updated Test Thing From TestNG");
        return changes;
    }

    private JSONObject postEntity(EntityType entityType, String urlParameters) {
        String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, null);
        try {
            Map<String, Object> responseMap = HTTPMethods.doPost(urlString, urlParameters);
            int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
            Assert.assertEquals(responseCode, 201, "Error during creation of entity " + entityType.name());
            String response = responseMap.get("response").toString();
            long id = Long.parseLong(response.substring(response.indexOf("(") + 1, response.indexOf(")")));
            urlString = urlString + "(" + id + ")";
            responseMap = HTTPMethods.doGet(urlString);
            responseCode = Integer.parseInt(responseMap.get("response-code").toString());
            Assert.assertEquals(responseCode, 200, "The POSTed entity is not created.");
            JSONObject result = new JSONObject(responseMap.get("response").toString());
            return result;
        } catch (JSONException e) {
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            return null;
        }
    }

}
