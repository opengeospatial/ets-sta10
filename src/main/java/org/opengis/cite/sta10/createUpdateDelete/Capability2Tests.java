package org.opengis.cite.sta10.createUpdateDelete;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.opengis.cite.sta10.SuiteAttribute;
import org.opengis.cite.sta10.util.EntityProperties;
import org.opengis.cite.sta10.util.EntityType;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Includes various tests of capability 2.
 */
public class Capability2Tests {

    public String rootUri;

    List<Long> thingIds = new ArrayList<>();
    List<Long> locationIds = new ArrayList<>();
    List<Long> historicalLocationIds = new ArrayList<>();
    List<Long> datastreamIds = new ArrayList<>();
    List<Long> observationIds = new ArrayList<>();
    List<Long> sensorIds = new ArrayList<>();
    List<Long> obsPropIds = new ArrayList<>();
    List<Long> foiIds = new ArrayList<>();


    @BeforeClass
    public void obtainTestSubject(ITestContext testContext) {
        Object obj = testContext.getSuite().getAttribute(
                SuiteAttribute.LEVEL.getName());
        if ((null != obj)) {
            Integer level = Integer.class.cast(obj);
            Assert.assertTrue(level.intValue() > 1,
                    "Conformance level 2 will not be checked since ics = " + level);
        }

        rootUri = testContext.getSuite().getAttribute(
                SuiteAttribute.TEST_SUBJECT.getName()).toString();
        rootUri = rootUri.trim();
        if(rootUri.lastIndexOf('/')==rootUri.length()-1){
            rootUri = rootUri.substring(0,rootUri.length()-1);
        }

    }

    @Test(description = "POST Entities", groups = "level-2", priority = 1)
    public void createEntities() {
        try {
            /** Thing **/
            String urlParameters = "{\"description\":\"This is a Test Thing From TestNG\"}";
            JSONObject entity = postEntity(EntityType.THING, urlParameters);
            long thingId = entity.getLong("id");
            thingIds.add(thingId);

            /** Location **/
            urlParameters = "{\n" +
                    "  \"description\": \"bow river\",\n" +
                    "  \"encodingType\": \"http://example.org/location_types#GeoJSON\",\n" +
                    "  \"location\": { \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }\n" +
                    "}";
            entity = postEntity(EntityType.LOCATION, urlParameters);
            long locationId = entity.getLong("id");
            locationIds.add(locationId);
            JSONObject locationEntity = entity;

            /** Sensor **/
            urlParameters = "{\n" +
                    "  \"description\": \"Fuguro Barometer\",\n" +
                    "  \"encodingType\": \"http://schema.org/description\",\n" +
                    "  \"metadata\": \"Barometer\"\n" +
                    "}";
            entity = postEntity(EntityType.SENSOR, urlParameters);
            long sensorId = entity.getLong("id");
            sensorIds.add(sensorId);

            /** ObservedProperty **/
            urlParameters = "{\n" +
                    "  \"name\": \"DewPoint Temperature\",\n" +
                    "  \"definition\": \"http://dbpedia.org/page/Dew_point\",\n" +
                    "  \"description\": \"The dewpoint temperature is the temperature to which the air must be cooled, at constant pressure, for dew to form. As the grass and other objects near the ground cool to the dewpoint, some of the water vapor in the atmosphere condenses into liquid water on the objects.\"\n" +
                    "}";
            entity = postEntity(EntityType.OBSERVED_PROPERTY, urlParameters);
            long obsPropId = entity.getLong("id");
            obsPropIds.add(obsPropId);

            /** FeatureOfInterest **/
            urlParameters = "{\n" +
                    "  \"description\": \"A weather station.\",\n" +
                    "  \"encodingType\": \"http://example.org/location_types#GeoJSON\",\n" +
                    "  \"feature\": {\n" +
                    "    \"type\": \"Point\",\n" +
                    "    \"coordinates\": [\n" +
                    "      10,\n" +
                    "      10\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}";
            entity = postEntity(EntityType.FEATURE_OF_INTEREST, urlParameters);
            long foiId = entity.getLong("id");
            foiIds.add(foiId);

            /** Datastream **/
            urlParameters = "{\n" +
                    "  \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Celsius\",\n" +
                    "    \"symbol\": \"degC\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n" +
                    "  },\n" +
                    "  \"description\": \"test datastream.\",\n" +
                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "  \"Thing\": { \"id\": " + thingId + " },\n" +
                    "  \"ObservedProperty\":{ \"id\":" + obsPropId + "},\n" +
                    "  \"Sensor\": { \"id\": " + sensorId + " }\n" +
                    "}";
            entity = postEntity(EntityType.DATASTREAM, urlParameters);
            long datastreamId = entity.getLong("id");
            datastreamIds.add(datastreamId);

            /** Observation **/
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:40:00.000Z\",\n" +
                    "  \"result\": 8,\n" +
                    "  \"Datastream\":{\"id\": " + datastreamId + "},\n" +
                    "  \"FeatureOfInterest\": {\"id\": " + foiId + "}  \n" +
                    "}";
            entity = postEntity(EntityType.OBSERVATION, urlParameters);
            long obsId1 = entity.getLong("id");
            observationIds.add(obsId1);
            //POST Observation without FOI (Automatic creation of FOI)
            //Add location to the Thing
            urlParameters = "{\"Locations\":[{\"id\":" + locationId + "}]}";
            patchEntity(EntityType.THING, urlParameters, thingId);

            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:00:00.000Z\",\n" +
                    "  \"result\": 100,\n" +
                    "  \"Datastream\":{\"id\": " + datastreamId + "}\n" +
                    "}";
            entity = postEntity(EntityType.OBSERVATION, urlParameters);
            long obsId2 = entity.getLong("id");
            observationIds.add(obsId2);
            long automatedFOIId = checkAutomaticInsertionOfFOI(obsId2, locationEntity, -1);
            foiIds.add(automatedFOIId);
            //POST another Observation to make sure it is linked to the previously created FOI
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-05-01T00:00:00.000Z\",\n" +
                    "  \"result\": 105,\n" +
                    "  \"Datastream\":{\"id\": " + datastreamId + "}\n" +
                    "}";
            entity = postEntity(EntityType.OBSERVATION, urlParameters);
            long obsId3 = entity.getLong("id");
            observationIds.add(obsId3);
            checkAutomaticInsertionOfFOI(obsId2, locationEntity, automatedFOIId);

            /** HistoricalLocation **/
            urlParameters = "{\n" +
                    "  \"time\": \"2015-03-01T00:40:00.000Z\",\n" +
                    "  \"Thing\":{\"id\": " + thingId + "},\n" +
                    "  \"Locations\": [{\"id\": " + locationId + "}]  \n" +
                    "}";
            entity = postEntity(EntityType.HISTORICAL_LOCATION, urlParameters);
            long histLocId = entity.getLong("id");
            historicalLocationIds.add(histLocId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test(description = "POST Entities using Deep Insert", groups = "level-2", priority = 1)
    public void createEntitiesWithDeepInsert() {
        try {
            /** Thing **/
            String urlParameters = "{\n" +
                    "  \"description\": \"Office Building\",\n" +
                    "  \"properties\": {\n" +
                    "    \"reference\": \"Third Floor\"\n" +
                    "  },\n" +
                    "  \"Locations\": [\n" +
                    "    {\n" +
                    "      \"description\": \"West Roof\",\n" +
                    "      \"location\": { \"type\": \"Point\", \"coordinates\": [-117.05, 51.05] },\n" +
                    "      \"encodingType\": \"http://example.org/location_types#GeoJSON\"\n" +
                    "    }\n" +
                    "  ],\n" +
                    "  \"Datastreams\": [\n" +
                    "    {\n" +
                    "      \"unitOfMeasurement\": {\n" +
                    "        \"name\": \"Lumen\",\n" +
                    "        \"symbol\": \"lm\",\n" +
                    "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html#Lumen\"\n" +
                    "      },\n" +
                    "      \"description\": \"Light exposure.\",\n" +
                    "      \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "      \"ObservedProperty\": {\n" +
                    "        \"name\": \"Luminous Flux\",\n" +
                    "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html#LuminousFlux\",\n" +
                    "        \"description\": \"Luminous Flux or Luminous Power is the measure of the perceived power of light.\"\n" +
                    "      },\n" +
                    "      \"Sensor\": {        \n" +
                    "        \"description\": \"Acme Fluxomatic 1000\",\n" +
                    "        \"encodingType\": \"http://schema.org/description\",\n" +
                    "        \"metadata\": \"Light flux sensor\"\n" +
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
            JSONObject entity = postEntity(EntityType.THING, urlParameters);
            long thingId = entity.getLong("id");
            //Check Datastream
            JSONObject deepInsertedObj = new JSONObject("{\n" +
                    "      \"unitOfMeasurement\": {\n" +
                    "        \"name\": \"Lumen\",\n" +
                    "        \"symbol\": \"lm\",\n" +
                    "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html#Lumen\"\n" +
                    "      },\n" +
                    "      \"description\": \"Light exposure.\",\n" +
                    "      \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\"\n" +
                    "    }\n");
            long datastreamId = checkRelatedEntity(EntityType.THING, thingId, EntityType.DATASTREAM, deepInsertedObj);
            datastreamIds.add(datastreamId);
            //Check Location
            deepInsertedObj = new JSONObject("{\n" +
                    "      \"description\": \"West Roof\",\n" +
                    "      \"location\": { \"type\": \"Point\", \"coordinates\": [-117.05, 51.05] },\n" +
                    "      \"encodingType\": \"http://example.org/location_types#GeoJSON\"\n" +
                    "    }\n");
            locationIds.add(checkRelatedEntity(EntityType.THING, thingId, EntityType.LOCATION, deepInsertedObj));
            //Check Sensor
            deepInsertedObj = new JSONObject( "{\n" +
                    "        \"description\": \"Acme Fluxomatic 1000\",\n" +
                    "        \"encodingType\": \"http://schema.org/description\",\n" +
                    "        \"metadata\": \"Light flux sensor\"\n" +
                    "      }\n");
            sensorIds.add(checkRelatedEntity(EntityType.DATASTREAM, datastreamId, EntityType.SENSOR, deepInsertedObj));
            //Check ObservedProperty
            deepInsertedObj = new JSONObject("{\n" +
                    "        \"name\": \"Luminous Flux\",\n" +
                    "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html#LuminousFlux\",\n" +
                    "        \"description\": \"Luminous Flux or Luminous Power is the measure of the perceived power of light.\"\n" +
                    "      },\n" );
            obsPropIds.add(checkRelatedEntity(EntityType.DATASTREAM, datastreamId, EntityType.OBSERVED_PROPERTY, deepInsertedObj));
            thingIds.add(thingId);

            /** Datastream **/
            urlParameters = "{\n" +
                    "  \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Celsius\",\n" +
                    "    \"symbol\": \"degC\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n" +
                    "  },\n" +
                    "  \"description\": \"test datastream.\",\n" +
                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "  \"Thing\": { \"id\": " + thingId + " },\n" +
                    "   \"ObservedProperty\": {\n" +
                    "        \"name\": \"Luminous Flux\",\n" +
                    "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html#LuminousFlux\",\n" +
                    "        \"description\": \"Luminous Flux or Luminous Power is the measure of the perceived power of light.\"\n" +
                    "   },\n" +
                    "   \"Sensor\": {        \n" +
                    "        \"description\": \"Acme Fluxomatic 1000\",\n" +
                    "        \"encodingType\": \"http://schema.org/description\",\n" +
                    "        \"metadata\": \"Light flux sensor\"\n" +
                    "   },\n" +
                    "      \"Observations\": [\n" +
                    "        {\n" +
                    "          \"phenomenonTime\": \"2015-03-01T00:10:00Z\",\n" +
                    "          \"result\": 10\n" +
                    "        }\n" +
                    "      ]"+
                    "}";
            entity = postEntity(EntityType.DATASTREAM, urlParameters);
            datastreamId = entity.getLong("id");
            //Check Sensor
            deepInsertedObj = new JSONObject( "{\n" +
                    "        \"description\": \"Acme Fluxomatic 1000\",\n" +
                    "        \"encodingType\": \"http://schema.org/description\",\n" +
                    "        \"metadata\": \"Light flux sensor\"\n" +
                    "      }\n");
            sensorIds.add(checkRelatedEntity(EntityType.DATASTREAM, datastreamId, EntityType.SENSOR, deepInsertedObj));
            //Check ObservedProperty
            deepInsertedObj = new JSONObject("{\n" +
                    "        \"name\": \"Luminous Flux\",\n" +
                    "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html#LuminousFlux\",\n" +
                    "        \"description\": \"Luminous Flux or Luminous Power is the measure of the perceived power of light.\"\n" +
                    "      },\n" );
            obsPropIds.add(checkRelatedEntity(EntityType.DATASTREAM, datastreamId, EntityType.OBSERVED_PROPERTY, deepInsertedObj));
            //Check Observation
            deepInsertedObj = new JSONObject(  "{\n" +
                    "          \"phenomenonTime\": \"2015-03-01T00:10:00.000Z\",\n" +
                    "          \"result\": 10\n" +
                    "        }\n");
            observationIds.add(checkRelatedEntity(EntityType.DATASTREAM, datastreamId, EntityType.OBSERVATION, deepInsertedObj));
            datastreamIds.add(datastreamId);

            /** Observation **/
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:00:00Z\",\n" +
                    "  \"result\": 100,\n" +
                    "  \"FeatureOfInterest\": {\n" +
                    "  \t\"description\": \"A weather station.\",\n" +
                    "  \t\"encodingType\": \"http://example.org/location_types#GeoJSON\",\n" +
                    "    \"feature\": {\n" +
                    "      \"type\": \"Point\",\n" +
                    "      \"coordinates\": [\n" +
                    "        -114.05,\n" +
                    "        51.05\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"Datastream\":{\"id\": "+datastreamId+"}\n" +
                    "}";
            entity = postEntity(EntityType.OBSERVATION, urlParameters);
            long obsId1 = entity.getLong("id");
            //Check FeaturOfInterest
            deepInsertedObj = new JSONObject("{\n" +
                    "  \"description\": \"A weather station.\",\n" +
                    "  \"encodingType\": \"http://example.org/location_types#GeoJSON\",\n" +
                    "    \"feature\": {\n" +
                    "      \"type\": \"Point\",\n" +
                    "      \"coordinates\": [\n" +
                    "        -114.05,\n" +
                    "        51.05\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  }\n");
            foiIds.add(checkRelatedEntity(EntityType.OBSERVATION, obsId1, EntityType.FEATURE_OF_INTEREST, deepInsertedObj));
            observationIds.add(obsId1);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test(description = "POST Invalid Entities", groups = "level-2", priority = 2)
    public void createInvalidEntities() {
        try {
            /** Datastream **/
            // Without Sensor
            String urlParameters = "{\n" +
                    "  \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Celsius\",\n" +
                    "    \"symbol\": \"degC\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n" +
                    "  },\n" +
                    "  \"description\": \"test datastream.\",\n" +
                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "  \"Thing\": { \"id\": " + thingIds.get(0) + " },\n" +
                    "  \"ObservedProperty\":{ \"id\":" + obsPropIds.get(0) + "},\n" +
                    "}";
            postInvalidEntity(EntityType.DATASTREAM, urlParameters);
            //Without ObservedProperty
            urlParameters = "{\n" +
                    "  \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Celsius\",\n" +
                    "    \"symbol\": \"degC\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n" +
                    "  },\n" +
                    "  \"description\": \"test datastream.\",\n" +
                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "  \"Thing\": { \"id\": " + thingIds.get(0) + " },\n" +
                    "  \"Sensor\": { \"id\": " + sensorIds.get(0) + " }\n" +
                    "}";
            postInvalidEntity(EntityType.DATASTREAM, urlParameters);
            //Without Things
            urlParameters = "{\n" +
                    "  \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Celsius\",\n" +
                    "    \"symbol\": \"degC\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n" +
                    "  },\n" +
                    "  \"description\": \"test datastream.\",\n" +
                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "  \"ObservedProperty\":{ \"id\":" + obsPropIds.get(0) + "},\n" +
                    "  \"Sensor\": { \"id\": " + sensorIds.get(0) + " }\n" +
                    "}";
            postInvalidEntity(EntityType.DATASTREAM, urlParameters);

            /** Observation **/
            //Create Thing and Datastream
            urlParameters = "{\"description\":\"This is a Test Thing From TestNG\"}";
            long thingId = postEntity(EntityType.THING, urlParameters).getLong("id");
            thingIds.add(thingId);
            urlParameters = "{\n" +
                    "  \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Celsius\",\n" +
                    "    \"symbol\": \"degC\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n" +
                    "  },\n" +
                    "  \"description\": \"test datastream.\",\n" +
                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "  \"Thing\": { \"id\": " + thingId + " },\n" +
                    "  \"ObservedProperty\":{ \"id\":" + obsPropIds.get(0) + "},\n" +
                    "  \"Sensor\": { \"id\": " + sensorIds.get(0) + " }\n" +
                    "}";
            long datastreamId = postEntity(EntityType.DATASTREAM, urlParameters).getLong("id");
            datastreamIds.add(datastreamId);
            //Without Datastream
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:40:00.000Z\",\n" +
                    "  \"result\": 8,\n" +
                    "  \"FeatureOfInterest\": {\"id\": " + foiIds.get(0) + "}  \n" +
                    "}";
            postInvalidEntity(EntityType.OBSERVATION, urlParameters);
            //Without FOI and without Thing's Location
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:00:00.000Z\",\n" +
                    "  \"result\": 100,\n" +
                    "  \"Datastream\":{\"id\": " + datastreamId + "}\n" +
                    "}";
            postInvalidEntity(EntityType.OBSERVATION, urlParameters);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Test(description = "PATCH Entities", groups = "level-2", priority = 3)
    public void patchEntities() {
        try {
            /** Thing **/
            long thingId = thingIds.get(0);
            JSONObject entity = getEntity(EntityType.THING, thingId);
            String urlParameters = "{\"description\":\"This is a PATCHED Test Thing From TestNG\"}";
            Map<String, Object> diffs = new HashMap<>();
            diffs.put("description", "This is a PATCHED Test Thing From TestNG");
            JSONObject updatedEntity = patchEntity(EntityType.THING, urlParameters, thingId);
            checkPatch(EntityType.THING, entity, updatedEntity, diffs);

            /** Location **/
            long locationId = locationIds.get(0);
            entity = getEntity(EntityType.LOCATION, locationId);
            urlParameters = "{\"location\": { \"type\": \"Point\", \"coordinates\": [114.05, -50] }}";
            diffs = new HashMap<>();
            diffs.put("location", new JSONObject("{ \"type\": \"Point\", \"coordinates\": [114.05, -50] }}"));
            updatedEntity = patchEntity(EntityType.LOCATION, urlParameters, locationId);
            checkPatch(EntityType.LOCATION, entity, updatedEntity, diffs);

            /** HistoricalLocation **/
            long histLocId = historicalLocationIds.get(0);
            entity = getEntity(EntityType.HISTORICAL_LOCATION, histLocId);
            urlParameters = "{\"time\": \"2015-07-01T00:00:00.000Z\"}";
            diffs = new HashMap<>();
            diffs.put("time", "2015-07-01T00:00:00.000Z");
            updatedEntity = patchEntity(EntityType.HISTORICAL_LOCATION, urlParameters, histLocId);
            checkPatch(EntityType.HISTORICAL_LOCATION, entity, updatedEntity, diffs);

            /** Sensor **/
            long sensorId = sensorIds.get(0);
            entity = getEntity(EntityType.SENSOR, sensorId);
            urlParameters = "{\"metadata\": \"PATCHED\"}";
            diffs = new HashMap<>();
            diffs.put("metadata", "PATCHED");
            updatedEntity = patchEntity(EntityType.SENSOR, urlParameters, sensorId);
            checkPatch(EntityType.SENSOR, entity, updatedEntity, diffs);

            /** ObserverdProperty **/
            long obsPropId = obsPropIds.get(0);
            entity = getEntity(EntityType.OBSERVED_PROPERTY, obsPropId);
            urlParameters = "{\"description\":\"PATCHED\"}";
            diffs = new HashMap<>();
            diffs.put("description", "PATCHED");
            updatedEntity = patchEntity(EntityType.OBSERVED_PROPERTY, urlParameters, obsPropId);
            checkPatch(EntityType.OBSERVED_PROPERTY, entity, updatedEntity, diffs);

            /** FeatureOfInterest **/
            long foiId = foiIds.get(0);
            entity = getEntity(EntityType.FEATURE_OF_INTEREST, foiId);
            urlParameters = "{\"feature\":{ \"type\": \"Point\", \"coordinates\": [114.05, -51.05] }}";
            diffs = new HashMap<>();
            diffs.put("feature", new JSONObject("{ \"type\": \"Point\", \"coordinates\": [114.05, -51.05] }"));
            updatedEntity = patchEntity(EntityType.FEATURE_OF_INTEREST, urlParameters, foiId);
            checkPatch(EntityType.FEATURE_OF_INTEREST, entity, updatedEntity, diffs);

            /** Datastream **/
            long datastreamId = datastreamIds.get(0);
            entity = getEntity(EntityType.DATASTREAM, datastreamId);
            urlParameters = "{\"description\": \"Patched Description\"}";
            diffs = new HashMap<>();
            diffs.put("description", "Patched Description");
            updatedEntity = patchEntity(EntityType.DATASTREAM, urlParameters, datastreamId);
            checkPatch(EntityType.DATASTREAM, entity, updatedEntity, diffs);
            //Second PATCH for UOM
            entity = updatedEntity;
            urlParameters = "{ \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Entropy2\",\n" +
                    "    \"symbol\": \"S2\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#Entropy2\"\n" +
                    "  } }";
            diffs = new HashMap<>();
            diffs.put("unitOfMeasurement", new JSONObject("{\"name\": \"Entropy2\",\"symbol\": \"S2\",\"definition\": \"http://qudt.org/vocab/unit#Entropy2\"}"));
            updatedEntity = patchEntity(EntityType.DATASTREAM, urlParameters, datastreamId);
            checkPatch(EntityType.DATASTREAM, entity, updatedEntity, diffs);

            /** Observation **/
            long obsId1 = observationIds.get(0);
            entity = getEntity(EntityType.OBSERVATION, obsId1);
            urlParameters = "{\"phenomenonTime\": \"2015-07-01T00:40:00.000Z\"}";
            diffs = new HashMap<>();
            diffs.put("phenomenonTime", "2015-07-01T00:40:00.000Z");
            updatedEntity = patchEntity(EntityType.OBSERVATION, urlParameters, obsId1);
            checkPatch(EntityType.OBSERVATION, entity, updatedEntity, diffs);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test(description = "PUT Entities", groups = "level-2", priority = 3)
    public void putEntities() {
        try {
            /** Thing **/
            long thingId = thingIds.get(0);
            JSONObject entity = getEntity(EntityType.THING, thingId);
            String urlParameters = "{\"description\":\"This is a Updated Test Thing From TestNG\"}";
            Map<String, Object> diffs = new HashMap<>();
            diffs.put("description", "This is a Updated Test Thing From TestNG");
            JSONObject updatedEntity = updateEntity(EntityType.THING, urlParameters, thingId);
            checkPut(EntityType.THING, entity, updatedEntity, diffs);

            /** Location **/
            long locationId = locationIds.get(0);
            entity = getEntity(EntityType.LOCATION, locationId);
            urlParameters = "{\"encodingType\":\"UPDATED ENCODING\",\"description\":\"UPDATED DESCRIPTION\", \"location\": { \"type\": \"Point\", \"coordinates\": [-114.05, 50] }}";
            diffs = new HashMap<>();
            diffs.put("encodingType", "UPDATED ENCODING");
            diffs.put("description", "UPDATED DESCRIPTION");
            diffs.put("location", new JSONObject("{ \"type\": \"Point\", \"coordinates\": [-114.05, 50] }}"));
            updatedEntity = updateEntity(EntityType.LOCATION, urlParameters, locationId);
            checkPut(EntityType.LOCATION, entity, updatedEntity, diffs);

            /** HistoricalLocation **/
            long histLocId = historicalLocationIds.get(0);
            entity = getEntity(EntityType.HISTORICAL_LOCATION, histLocId);
            urlParameters = "{\"time\": \"2015-08-01T00:00:00.000Z\"}";
            diffs = new HashMap<>();
            diffs.put("time", "2015-08-01T00:00:00.000Z");
            updatedEntity = updateEntity(EntityType.HISTORICAL_LOCATION, urlParameters, histLocId);
            checkPut(EntityType.HISTORICAL_LOCATION, entity, updatedEntity, diffs);

            /** Sensor **/
            long sensorId = sensorIds.get(0);
            entity = getEntity(EntityType.SENSOR, sensorId);
            urlParameters = "{\"description\": \"UPDATED\", \"encodingType\":\"http://schema.org/description\", \"metadata\": \"UPDATED\"}";
            diffs = new HashMap<>();
            diffs.put("description", "UPDATED");
            diffs.put("encodingType", "http://schema.org/description");
            diffs.put("metadata", "UPDATED");
            updatedEntity = updateEntity(EntityType.SENSOR, urlParameters, sensorId);
            checkPut(EntityType.SENSOR, entity, updatedEntity, diffs);

            /** ObserverdProperty **/
            long obsPropId = obsPropIds.get(0);
            urlParameters = "{\"name\":\"QWERTY\", \"definition\": \"ZXCVB\", \"description\":\"POIUYTREW\"}";
            diffs = new HashMap<>();
            diffs.put("name", "QWERTY");
            diffs.put("definition", "ZXCVB");
            diffs.put("description", "POIUYTREW");
            updatedEntity = updateEntity(EntityType.OBSERVED_PROPERTY, urlParameters, obsPropId);
            checkPut(EntityType.OBSERVED_PROPERTY, entity, updatedEntity, diffs);

            /** FeatureOfInterest **/
            long foiId = foiIds.get(0);
            entity = getEntity(EntityType.FEATURE_OF_INTEREST, foiId);
            urlParameters = "{\"encodingType\":\"SQUARE\",\"feature\":{ \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }, \"description\":\"POIUYTREW\"}";
            diffs = new HashMap<>();
            diffs.put("encodingType", "SQUARE");
            diffs.put("feature", new JSONObject("{ \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }"));
            diffs.put("description", "POIUYTREW");
            updatedEntity = updateEntity(EntityType.FEATURE_OF_INTEREST, urlParameters, foiId);
            checkPut(EntityType.FEATURE_OF_INTEREST, entity, updatedEntity, diffs);

            /** Datastream **/
            long datastreamId = datastreamIds.get(0);
            entity = getEntity(EntityType.DATASTREAM, datastreamId);
            urlParameters = "{\n" +
                    "  \"description\": \"Data coming from sensor on ISS.\",\n" +
                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Observation\",\n" +
                    "  \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Entropy\",\n" +
                    "    \"symbol\": \"S\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#Entropy\"\n" +
                    "  }\n" +
                    "}\n";
            diffs = new HashMap<>();
            diffs.put("description", "Data coming from sensor on ISS.");
            diffs.put("observationType", "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Observation");
            diffs.put("unitOfMeasurement", new JSONObject("{\"name\": \"Entropy\",\"symbol\": \"S\",\"definition\": \"http://qudt.org/vocab/unit#Entropy\"}"));
            updatedEntity = updateEntity(EntityType.DATASTREAM, urlParameters, datastreamId);
            checkPut(EntityType.DATASTREAM, entity, updatedEntity, diffs);

            /** Observation **/
            long obsId1 = observationIds.get(0);
            entity = getEntity(EntityType.OBSERVATION, obsId1);
            urlParameters = "{\"result\": \"99\", \"phenomenonTime\": \"2015-08-01T00:40:00.000Z\"}";
            diffs = new HashMap<>();
            diffs.put("result", "99");
            diffs.put("phenomenonTime", "2015-08-01T00:40:00.000Z");
            updatedEntity = updateEntity(EntityType.OBSERVATION, urlParameters, obsId1);
            checkPut(EntityType.OBSERVATION, entity, updatedEntity, diffs);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Test(description = "DELETE Entities", groups = "level-2", priority = 4)
    public void deleteEntities() {
        for (int i = 0; i < observationIds.size(); i++) {
            deleteEntity(EntityType.OBSERVATION, observationIds.get(i));
        }
        for (int i = 0; i < foiIds.size(); i++) {
            deleteEntity(EntityType.FEATURE_OF_INTEREST, foiIds.get(i));
        }
        for (int i = 0; i < datastreamIds.size(); i++) {
            deleteEntity(EntityType.DATASTREAM, datastreamIds.get(i));
        }
        for (int i = 0; i < obsPropIds.size(); i++) {
            deleteEntity(EntityType.OBSERVED_PROPERTY, obsPropIds.get(i));
        }
        for (int i = 0; i < sensorIds.size(); i++) {
            deleteEntity(EntityType.SENSOR, sensorIds.get(i));
        }
        for (int i = 0; i < historicalLocationIds.size(); i++) {
            deleteEntity(EntityType.HISTORICAL_LOCATION, historicalLocationIds.get(i));
        }
        for (int i = 0; i < locationIds.size(); i++) {
            deleteEntity(EntityType.LOCATION, locationIds.get(i));
        }
        for (int i = 0; i < thingIds.size(); i++) {
            deleteEntity(EntityType.THING, thingIds.get(i));
        }

        deleteNonExsistentEntity(EntityType.THING);
        deleteNonExsistentEntity(EntityType.LOCATION);
        deleteNonExsistentEntity(EntityType.HISTORICAL_LOCATION);
        deleteNonExsistentEntity(EntityType.SENSOR);
        deleteNonExsistentEntity(EntityType.OBSERVED_PROPERTY);
        deleteNonExsistentEntity(EntityType.DATASTREAM);
        deleteNonExsistentEntity(EntityType.OBSERVATION);
        deleteNonExsistentEntity(EntityType.FEATURE_OF_INTEREST);
    }

    //TODO: Add invalid PATCH test for other entities when it is implemented in the service
    @Test(description = "Invalid PATCH Entities", groups = "level-2", priority = 3)
    public void invalidPatchEntities() {
        /** Thing **/
        long thingId = thingIds.get(0);
        String urlParameters = "{\"Locations\": [\n" +
                "    {\n" +
                "      \"description\": \"West Roof\",\n" +
                "      \"location\": { \"type\": \"Point\", \"coordinates\": [-117.05, 51.05] },\n" +
                "      \"encodingType\": \"http://example.org/location_types#GeoJSON\"\n" +
                "    }\n" +
                "  ]}";
        invalidPatchEntity(EntityType.THING, urlParameters, thingId);
        urlParameters = "{\"Datastreams\": [\n" +
                "    {\n" +
                "      \"unitOfMeasurement\": {\n" +
                "        \"name\": \"Lumen\",\n" +
                "        \"symbol\": \"lm\",\n" +
                "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html#Lumen\"\n" +
                "      }]}";
        invalidPatchEntity(EntityType.THING, urlParameters, thingId);

//        /** Location **/
//        long locationId = locationIds.get(0);
//        urlParameters = "{\"Things\":[{\"description\":\"Orange\"}]}";
//        invalidPatchEntity(EntityType.LOCATION, urlParameters, locationId);
//
//        /** HistoricalLocation **/
//        long histLocId = historicalLocationIds.get(0);
//        urlParameters = "{\"time\": \"2015-07-01T00:00:00.000Z\"}";
//        invalidPatchEntity(EntityType.HISTORICAL_LOCATION, urlParameters, histLocId);
//
        /** Sensor **/
        long sensorId = sensorIds.get(0);
        urlParameters = "{\"Datastreams\": [\n" +
                "    {\n" +
                "      \"unitOfMeasurement\": {\n" +
                "        \"name\": \"Lumen\",\n" +
                "        \"symbol\": \"lm\",\n" +
                "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html#Lumen\"}\n" +
                "        ,\"Thing\":{\"id\":"+thingId+"}"+
                "      }]}";
        invalidPatchEntity(EntityType.SENSOR, urlParameters, sensorId);

        /** ObserverdProperty **/
        long obsPropId = obsPropIds.get(0);
        urlParameters = "{\"Datastreams\": [\n" +
                "    {\n" +
                "      \"unitOfMeasurement\": {\n" +
                "        \"name\": \"Lumen\",\n" +
                "        \"symbol\": \"lm\",\n" +
                "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/unit/Instances.html#Lumen\"}\n" +
                "        ,\"Thing\":{\"id\":"+thingId+"}"+
                "      }]}";
        invalidPatchEntity(EntityType.OBSERVED_PROPERTY, urlParameters, obsPropId);

//        /** FeatureOfInterest **/
//        long foiId = foiIds.get(0);
//        urlParameters = "{\"feature\":{ \"type\": \"Point\", \"coordinates\": [114.05, -51.05] }}";
//        invalidPatchEntity(EntityType.FEATURE_OF_INTEREST, urlParameters, foiId);

        /** Datastream **/
        long datastreamId = datastreamIds.get(0);
        urlParameters = "{\"ObservedProperty\": {\n" +
                "  \t\"name\": \"Count\",\n" +
                "\t\"definition\": \"http://qudt.org/vocab/unit#Dimensionless\",\n" +
                "\t\"description\": \"Count is a dimensionless property.\"\n" +
                "  } }";
        invalidPatchEntity(EntityType.DATASTREAM, urlParameters, datastreamId);
        urlParameters = "{\"Sensor\": {\n" +
                "  \t\"description\": \"Acme Traffic 2000\",  \n" +
                "  \t\"encodingType\": \"http://schema.org/description\",\n" +
                "  \t\"metadata\": \"Traffic counting device\"\n" +
                "  }}";
        invalidPatchEntity(EntityType.DATASTREAM, urlParameters, datastreamId);
        urlParameters = "{\"Thing\": { \"description\": \"test\" }}";
        invalidPatchEntity(EntityType.DATASTREAM, urlParameters, datastreamId);
        urlParameters = "{\"Observations\": [\n" +
                "    {\n" +
                "      \"phenomenonTime\": \"2015-03-01T00:00:00Z\",\n" +
                "      \"result\": 92122,\n" +
                "      \"resultQuality\": \"High\"\n" +
                "    }\n" +
                "  ]}";
        invalidPatchEntity(EntityType.DATASTREAM, urlParameters, datastreamId);

//        /** Observation **/
//        long obsId1 = observationIds.get(0);
//        urlParameters = "{\"phenomenonTime\": \"2015-07-01T00:40:00.000Z\"}";
//        invalidPatchEntity(EntityType.OBSERVATION, urlParameters, obsId1);
    }

    @Test(description = "DELETE nonexistent Entities", groups = "level-2", priority = 4)
    public void deleteNoneexistentEntities() {
        deleteNonExsistentEntity(EntityType.THING);
        deleteNonExsistentEntity(EntityType.LOCATION);
        deleteNonExsistentEntity(EntityType.HISTORICAL_LOCATION);
        deleteNonExsistentEntity(EntityType.SENSOR);
        deleteNonExsistentEntity(EntityType.OBSERVED_PROPERTY);
        deleteNonExsistentEntity(EntityType.DATASTREAM);
        deleteNonExsistentEntity(EntityType.OBSERVATION);
        deleteNonExsistentEntity(EntityType.FEATURE_OF_INTEREST);
    }


    public JSONObject getEntity(EntityType entityType, long id) {
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
            return new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public JSONObject postEntity(EntityType entityType, String urlParameters) {
        String urlString = rootUri;
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
        HttpURLConnection conn = null;
        try {
            //Create connection
            URL url = new URL(urlString);
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }

            int responseCode = conn.getResponseCode();

            Assert.assertEquals(responseCode, 201, "Error during creation of entity " + entityType.name());

            String response = conn.getHeaderField("location");
            long id = Long.parseLong(response.substring(response.indexOf("(") + 1, response.indexOf(")")));

            conn.disconnect();

            url = new URL(urlString + "(" + id + ")");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type",
                    "application/json");

            conn.setUseCaches(false);
            conn.setDoOutput(true);

            responseCode = conn.getResponseCode();
            Assert.assertEquals(responseCode, 200, "The POSTed entity is not created.");

            //Get Response
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder responseBuilder = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while ((line = rd.readLine()) != null) {
                responseBuilder.append(line);
                responseBuilder.append('\r');
            }
            rd.close();

            JSONObject result = new JSONObject(responseBuilder.toString());
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public void postInvalidEntity(EntityType entityType, String urlParameters) {
        String urlString = rootUri;
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
                return;
        }
        HttpURLConnection conn = null;
        try {
            //Create connection
            URL url = new URL(urlString);
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }

            int responseCode = conn.getResponseCode();

            Assert.assertEquals(responseCode, 400, "The  " + entityType.name()+" should not be created due to integrity constraints.");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void deleteEntity(EntityType entityType, long id) {
        String urlString = rootUri;
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
                return;
        }
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("DELETE");
            connection.connect();
            int responseCode = connection.getResponseCode();
            Assert.assertEquals(responseCode, 200, "DELETE does not work properly for " + entityType + " with id " + id + ". Returned with response code " + responseCode + ".");

            connection.disconnect();

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.setUseCaches(false);
            connection.setDoOutput(false);

            responseCode = connection.getResponseCode();
            Assert.assertEquals(responseCode, 404, "Deleted entity was not actually deleted : " + entityType + "(" + id + ").");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private void deleteNonExsistentEntity(EntityType entityType) {
        String urlString = rootUri;
        long id = Long.MAX_VALUE;
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
                return;
        }
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("DELETE");
            connection.connect();
            int responseCode = connection.getResponseCode();
            Assert.assertEquals(responseCode, 404, "DELETE does not work properly for nonexistent " + entityType + " with id " + id + ". Returned with response code " + responseCode + ".");

            connection.disconnect();

        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public JSONObject updateEntity(EntityType entityType, String urlParameters, long id) {
        String urlString = rootUri;
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
        HttpURLConnection conn = null;
        try {
            //Create connection
            URL url = new URL(urlString);
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }

            int responseCode = conn.getResponseCode();

            Assert.assertEquals(responseCode, 200, "Error during updating(PUT) of entity " + entityType.name());

            conn.disconnect();

            //   url = new URL(urlString+"("+id+")");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type",
                    "application/json");

            conn.setUseCaches(false);
            conn.setDoOutput(true);

            responseCode = conn.getResponseCode();

            //Get Response
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            JSONObject result = new JSONObject(response.toString());
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public JSONObject patchEntity(EntityType entityType, String urlParameters, long id) {
        String urlString = rootUri;
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

        HttpURLConnection conn = null;

        try {

            //PATCH
            URI uri = new URI(urlString);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPatch request = new HttpPatch(uri);
            StringEntity params = new StringEntity(urlParameters, ContentType.APPLICATION_JSON);
            request.setEntity(params);
            CloseableHttpResponse response = httpClient.execute(request);
            int responseCode = response.getStatusLine().getStatusCode();
            Assert.assertEquals(responseCode, 200, "Error during updating(PATCH) of entity " + entityType.name());
            httpClient.close();

            //GET patched entity for return
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type",
                    "application/json");

            conn.setUseCaches(false);
            conn.setDoOutput(true);

            responseCode = conn.getResponseCode();

            //Get Response
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder responseBuilder = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while ((line = rd.readLine()) != null) {
                responseBuilder.append(line);
                responseBuilder.append('\r');
            }
            rd.close();

            JSONObject result = new JSONObject(responseBuilder.toString());
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public void invalidPatchEntity(EntityType entityType, String urlParameters, long id) {
        String urlString = rootUri;
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
        }

        HttpURLConnection conn = null;

        try {

            //PATCH
            URI uri = new URI(urlString);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPatch request = new HttpPatch(uri);
            StringEntity params = new StringEntity(urlParameters, ContentType.APPLICATION_JSON);
            request.setEntity(params);
            CloseableHttpResponse response = httpClient.execute(request);
            int responseCode = response.getStatusLine().getStatusCode();
            Assert.assertEquals(responseCode, 400, "Error: Patching related entities inline must be illegal for entity " + entityType.name());
            httpClient.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    private void checkPatch(EntityType entityType, JSONObject oldEntity, JSONObject newEntity, Map diffs){
        try {
            switch (entityType) {
                case THING:
                    for (String property : EntityProperties.THING_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case LOCATION:
                    for (String property : EntityProperties.LOCATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case HISTORICAL_LOCATION:
                    for (String property : EntityProperties.HISTORICAL_LOCATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case DATASTREAM:
                    for (String property : EntityProperties.DATASTREAM_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case SENSOR:
                    for (String property : EntityProperties.SENSOR_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case OBSERVATION:
                    for (String property : EntityProperties.OBSERVATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case OBSERVED_PROPERTY:
                    for (String property : EntityProperties.OBSERVED_PROPETY_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    for (String property : EntityProperties.FEATURE_OF_INTEREST_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
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

    private void checkPut(EntityType entityType, JSONObject oldEntity, JSONObject newEntity, Map diffs){
        try {
            switch (entityType) {
                case THING:
                    for (String property : EntityProperties.THING_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case LOCATION:
                    for (String property : EntityProperties.LOCATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case HISTORICAL_LOCATION:
                    for (String property : EntityProperties.HISTORICAL_LOCATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case DATASTREAM:
                    for (String property : EntityProperties.DATASTREAM_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case SENSOR:
                    for (String property : EntityProperties.SENSOR_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case OBSERVATION:
                    for (String property : EntityProperties.OBSERVATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case OBSERVED_PROPERTY:
                    for (String property : EntityProperties.OBSERVED_PROPETY_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    for (String property : EntityProperties.FEATURE_OF_INTEREST_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for " + entityType + ".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
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

    private long checkAutomaticInsertionOfFOI(long obsId, JSONObject locationObj, long expectedFOIId){
        String urlString = rootUri+"/Observations("+obsId+")/FeatureOfInterest";
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type",
                    "application/json");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();
            Assert.assertEquals(responseCode, 200, "ERROR: FeatureOfInterest was not automatically create.");
            //Get Response
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder responseBuilder = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while ((line = rd.readLine()) != null) {
                responseBuilder.append(line);
                responseBuilder.append('\r');
            }
            rd.close();
            JSONObject result = new JSONObject(responseBuilder.toString());
            long id = result.getLong("id");
            if(expectedFOIId != -1){
                Assert.assertEquals(id, expectedFOIId, "ERROR: the Observation should have linked to FeatureOfInterest with ID: "+expectedFOIId+" , but it is linked for FeatureOfInterest with Id: "+id+".");
            }
            Assert.assertEquals(result.getJSONObject("feature").toString(), locationObj.getJSONObject("location").toString(), "ERROR: Automatic created FeatureOfInterest does not match last Location of that Thing.");
            return id;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return -1;
    }

    private long checkRelatedEntity(EntityType parentEntityType, long parentId, EntityType relationEntityType, JSONObject relationObj){
        String urlString = rootUri;
        boolean isCollection = true;
        switch (parentEntityType) {
            case THING:
                urlString += "/Things(" + parentId + ")";
                switch (relationEntityType) {
                    case LOCATION:
                        urlString += "/Locations";
                        break;
                    case HISTORICAL_LOCATION:
                        urlString += "/HistoricalLocations";
                        break;
                    case DATASTREAM:
                        urlString += "/Datastreams";
                        break;
                    default:
                        Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType+" and "+relationEntityType);
                }
                break;
            case LOCATION:
                urlString += "/Locations(" + parentId + ")";
                switch (relationEntityType) {
                    case THING:
                        urlString += "/Things";
                        break;
                    case HISTORICAL_LOCATION:
                        urlString += "/HistoricalLocations";
                        break;
                    default:
                        Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType+" and "+relationEntityType);
                }
                break;
            case HISTORICAL_LOCATION:
                urlString += "/HistoricalLocations(" + parentId + ")";
                switch (relationEntityType) {
                    case THING:
                        urlString += "/Thing";
                        isCollection = false;
                        break;
                    case LOCATION:
                        urlString += "/Locations";
                        break;
                    default:
                        Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType+" and "+relationEntityType);
                }
                break;
            case DATASTREAM:
                urlString += "/Datastreams(" + parentId + ")";
                switch (relationEntityType) {
                    case THING:
                        urlString += "/Thing";
                        isCollection = false;
                        break;
                    case SENSOR:
                        urlString += "/Sensor";
                        isCollection = false;
                        break;
                    case OBSERVATION:
                        urlString += "/Observations";
                        break;
                    case OBSERVED_PROPERTY:
                        urlString += "/ObservedProperty";
                        isCollection = false;
                        break;
                    default:
                        Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType+" and "+relationEntityType);
                }
                break;
            case SENSOR:
                urlString += "/Sensors(" + parentId + ")";
                switch (relationEntityType) {
                    case DATASTREAM:
                        urlString += "/Datastreams";
                        break;
                    default:
                        Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType+" and "+relationEntityType);
                }
                break;
            case OBSERVATION:
                urlString += "/Observations(" + parentId + ")";
                switch (relationEntityType) {
                    case THING:
                    case DATASTREAM:
                        urlString += "/Datastream";
                        isCollection = false;
                        break;
                    case FEATURE_OF_INTEREST:
                        urlString += "/FeatureOfInterest";
                        isCollection = false;
                        break;
                    default:
                        Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType+" and "+relationEntityType);
                }
                break;
            case OBSERVED_PROPERTY:
                urlString += "/ObservedProperties(" + parentId + ")";
                switch (relationEntityType) {
                    case DATASTREAM:
                        urlString += "/Datastreams";
                        break;
                   default:
                        Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType+" and "+relationEntityType);
                }
                break;
            case FEATURE_OF_INTEREST:
                urlString += "/FeaturesOfInterest(" + parentId + ")";
                switch (relationEntityType) {
                    case OBSERVATION:
                        urlString += "/Observations";
                        break;
                    default:
                        Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType+" and "+relationEntityType);
                }
                break;
            default:
                Assert.fail("Entity type is not recognized in SensorThings API : " + parentEntityType);
        }

        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type",
                    "application/json");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();
            Assert.assertEquals(responseCode, 200, "ERROR: Deep inserted "+relationEntityType+" does not created or linked to "+parentEntityType);
            //Get Response
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder responseBuilder = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while ((line = rd.readLine()) != null) {
                responseBuilder.append(line);
                responseBuilder.append('\r');
            }
            rd.close();
            JSONObject result = new JSONObject(responseBuilder.toString());
            if(isCollection == true){
                result = result.getJSONArray("value").getJSONObject(0);
            }
            Iterator iterator = relationObj.keys();
            while(iterator.hasNext()){
                String key = iterator.next().toString();
                Assert.assertEquals(result.get(key).toString(), relationObj.get(key).toString(), "ERROR: Deep inserted "+relationEntityType+" is not created correctly.");
            }
            return result.getLong("id");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
        return -1;
    }
}
