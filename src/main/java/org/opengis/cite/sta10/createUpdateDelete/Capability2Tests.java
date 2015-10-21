package org.opengis.cite.sta10.createUpdateDelete;


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

import java.util.*;

/**
 * Includes various tests of capability 2.
 */
public class Capability2Tests {

    public String rootUri;//="http://localhost:8080/OGCSensorThings/v1.0";

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
        deleteEverythings();
    }

    @Test(description = "POST Entities", groups = "level-2", priority = 2)
    public void createEntities() {
        try {
            /** Thing **/
            String urlParameters = "{\"description\":\"This is a Test Thing From TestNG\"}";
            JSONObject entity = postEntity(EntityType.THING, urlParameters);
            long thingId = entity.getLong(ControlInformation.ID);
            thingIds.add(thingId);

            /** Location **/
            urlParameters = "{\n" +
                    "  \"description\": \"bow river\",\n" +
                    "  \"encodingType\": \"http://example.org/location_types#GeoJSON\",\n" +
                    "  \"location\": { \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }\n" +
                    "}";
            entity = postEntity(EntityType.LOCATION, urlParameters);
            long locationId = entity.getLong(ControlInformation.ID);
            locationIds.add(locationId);
            JSONObject locationEntity = entity;

            /** Sensor **/
            urlParameters = "{\n" +
                    "  \"description\": \"Fuguro Barometer\",\n" +
                    "  \"encodingType\": \"http://schema.org/description\",\n" +
                    "  \"metadata\": \"Barometer\"\n" +
                    "}";
            entity = postEntity(EntityType.SENSOR, urlParameters);
            long sensorId = entity.getLong(ControlInformation.ID);
            sensorIds.add(sensorId);

            /** ObservedProperty **/
            urlParameters = "{\n" +
                    "  \"name\": \"DewPoint Temperature\",\n" +
                    "  \"definition\": \"http://dbpedia.org/page/Dew_point\",\n" +
                    "  \"description\": \"The dewpoint temperature is the temperature to which the air must be cooled, at constant pressure, for dew to form. As the grass and other objects near the ground cool to the dewpoint, some of the water vapor in the atmosphere condenses into liquid water on the objects.\"\n" +
                    "}";
            entity = postEntity(EntityType.OBSERVED_PROPERTY, urlParameters);
            long obsPropId = entity.getLong(ControlInformation.ID);
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
            long foiId = entity.getLong(ControlInformation.ID);
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
                    "  \"Thing\": { \"@iot.id\": " + thingId + " },\n" +
                    "  \"ObservedProperty\":{ \"@iot.id\":" + obsPropId + "},\n" +
                    "  \"Sensor\": { \"@iot.id\": " + sensorId + " }\n" +
                    "}";
            entity = postEntity(EntityType.DATASTREAM, urlParameters);
            long datastreamId = entity.getLong(ControlInformation.ID);
            datastreamIds.add(datastreamId);

            /** Observation **/
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:40:00.000Z\",\n" +
                    "  \"result\": 8,\n" +
                    "  \"Datastream\":{\"@iot.id\": " + datastreamId + "},\n" +
                    "  \"FeatureOfInterest\": {\"@iot.id\": " + foiId + "}  \n" +
                    "}";
            entity = postEntity(EntityType.OBSERVATION, urlParameters);
            long obsId1 = entity.getLong(ControlInformation.ID);
            observationIds.add(obsId1);
            //POST Observation without FOI (Automatic creation of FOI)
            //Add location to the Thing
            urlParameters = "{\"Locations\":[{\"@iot.id\":" + locationId + "}]}";
            patchEntity(EntityType.THING, urlParameters, thingId);

            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:00:00.000Z\",\n" +
                    "  \"resultTime\": \"2015-03-01T01:00:00.000Z\",\n" +
                    "  \"result\": 100,\n" +
                    "  \"Datastream\":{\"@iot.id\": " + datastreamId + "}\n" +
                    "}";
            entity = postEntity(EntityType.OBSERVATION, urlParameters);
            checkForObservationResultTime(entity, "2015-03-01T01:00:00.000Z");
            long obsId2 = entity.getLong(ControlInformation.ID);
            observationIds.add(obsId2);
            long automatedFOIId = checkAutomaticInsertionOfFOI(obsId2, locationEntity, -1);
            foiIds.add(automatedFOIId);
            //POST another Observation to make sure it is linked to the previously created FOI
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-05-01T00:00:00.000Z\",\n" +
                    "  \"result\": 105,\n" +
                    "  \"Datastream\":{\"@iot.id\": " + datastreamId + "}\n" +
                    "}";
            entity = postEntity(EntityType.OBSERVATION, urlParameters);
            checkForObservationResultTime(entity, null);
            long obsId3 = entity.getLong(ControlInformation.ID);
            observationIds.add(obsId3);
            checkAutomaticInsertionOfFOI(obsId2, locationEntity, automatedFOIId);

            /** HistoricalLocation **/
            urlParameters = "{\n" +
                    "  \"time\": \"2015-03-01T00:40:00.000Z\",\n" +
                    "  \"Thing\":{\"@iot.id\": " + thingId + "},\n" +
                    "  \"Locations\": [{\"@iot.id\": " + locationId + "}]  \n" +
                    "}";
            entity = postEntity(EntityType.HISTORICAL_LOCATION, urlParameters);
            long histLocId = entity.getLong(ControlInformation.ID);
            historicalLocationIds.add(histLocId);

        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    @Test(description = "POST Entities using Deep Insert", groups = "level-2", priority = 2)
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
            long thingId = entity.getLong(ControlInformation.ID);
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
                    "  \"Thing\": { \"@iot.id\": " + thingId + " },\n" +
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
            datastreamId = entity.getLong(ControlInformation.ID);
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
                    "  \"Datastream\":{\"@iot.id\": "+datastreamId+"}\n" +
                    "}";
            entity = postEntity(EntityType.OBSERVATION, urlParameters);
            long obsId1 = entity.getLong(ControlInformation.ID);
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
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    @Test(description = "POST Invalid Entities using Deep Insert", groups = "level-2", priority = 1)
    public void createInvalidEntitiesWithDeepInsert() {
        try {
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
                    "      }\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
            postInvalidEntity(EntityType.THING, urlParameters);
            List<EntityType> entityTypesToCheck = new ArrayList<>();
            entityTypesToCheck.add(EntityType.THING);
            entityTypesToCheck.add(EntityType.LOCATION);
            entityTypesToCheck.add(EntityType.HISTORICAL_LOCATION);
            entityTypesToCheck.add(EntityType.DATASTREAM);
            entityTypesToCheck.add(EntityType.OBSERVED_PROPERTY);
            checkNotExisting(entityTypesToCheck);

            /** Datastream **/
            urlParameters = "{\"description\": \"Office Building\"}";
            long thingId = postEntity(EntityType.THING, urlParameters).getLong("@iot.id");

            urlParameters = "{\n" +
                    "  \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Celsius\",\n" +
                    "    \"symbol\": \"degC\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n" +
                    "  },\n" +
                    "  \"description\": \"test datastream.\",\n" +
                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "  \"Thing\": { \"@iot.id\": " + thingId + " },\n" +
                    "   \"ObservedProperty\": {\n" +
                    "        \"name\": \"Luminous Flux\",\n" +
                    "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html#LuminousFlux\",\n" +
                    "        \"description\": \"Luminous Flux or Luminous Power is the measure of the perceived power of light.\"\n" +
                    "   },\n" +
                    "      \"Observations\": [\n" +
                    "        {\n" +
                    "          \"phenomenonTime\": \"2015-03-01T00:10:00Z\",\n" +
                    "          \"result\": 10\n" +
                    "        }\n" +
                    "      ]" +
                    "}";
            postInvalidEntity(EntityType.DATASTREAM, urlParameters);

            urlParameters = "{\n" +
                    "  \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Celsius\",\n" +
                    "    \"symbol\": \"degC\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n" +
                    "  },\n" +
                    "  \"description\": \"test datastream.\",\n" +
                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "  \"Thing\": { \"@iot.id\": " + thingId + " },\n" +
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
                    "      ]" +
                    "}";
            postInvalidEntity(EntityType.DATASTREAM, urlParameters);

            urlParameters = "{\n" +
                    "  \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Celsius\",\n" +
                    "    \"symbol\": \"degC\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n" +
                    "  },\n" +
                    "  \"description\": \"test datastream.\",\n" +
                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
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
                    "      ]" +
                    "}";
            postInvalidEntity(EntityType.DATASTREAM, urlParameters);

//            urlParameters = "{\n" +
//                    "  \"unitOfMeasurement\": {\n" +
//                    "    \"name\": \"Celsius\",\n" +
//                    "    \"symbol\": \"degC\",\n" +
//                    "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n" +
//                    "  },\n" +
//                    "  \"description\": \"test datastream.\",\n" +
//                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
//                    "  \"Thing\": { \"@iot.id\": " + thingId + " },\n" +
//                    "   \"ObservedProperty\": {\n" +
//                    "        \"name\": \"Luminous Flux\",\n" +
//                    "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html#LuminousFlux\",\n" +
//                    "        \"description\": \"Luminous Flux or Luminous Power is the measure of the perceived power of light.\"\n" +
//                    "   },\n" +
//                    "   \"Sensor\": {        \n" +
//                    "        \"description\": \"Acme Fluxomatic 1000\",\n" +
//                    "        \"encodingType\": \"http://schema.org/description\",\n" +
//                    "        \"metadata\": \"Light flux sensor\"\n" +
//                    "   },\n" +
//                    "      \"Observations\": [\n" +
//                    "        {\n" +
//                    "        }\n" +
//                    "      ]" +
//                    "}";
//            postInvalidEntity(EntityType.DATASTREAM, urlParameters);

            entityTypesToCheck.clear();
            entityTypesToCheck.add(EntityType.DATASTREAM);
            entityTypesToCheck.add(EntityType.SENSOR);
            entityTypesToCheck.add(EntityType.OBSERVATION);
            entityTypesToCheck.add(EntityType.FEATURE_OF_INTEREST);
            entityTypesToCheck.add(EntityType.OBSERVED_PROPERTY);
            checkNotExisting(entityTypesToCheck);

            /** Observation **/
            urlParameters = "{\n" +
                    "  \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Celsius\",\n" +
                    "    \"symbol\": \"degC\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n" +
                    "  },\n" +
                    "  \"description\": \"test datastream.\",\n" +
                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "  \"Thing\": { \"@iot.id\": " + thingId + " },\n" +
                    "   \"ObservedProperty\": {\n" +
                    "        \"name\": \"Luminous Flux\",\n" +
                    "        \"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html#LuminousFlux\",\n" +
                    "        \"description\": \"Luminous Flux or Luminous Power is the measure of the perceived power of light.\"\n" +
                    "   },\n" +
                    "   \"Sensor\": {        \n" +
                    "        \"description\": \"Acme Fluxomatic 1000\",\n" +
                    "        \"encodingType\": \"http://schema.org/description\",\n" +
                    "        \"metadata\": \"Light flux sensor\"\n" +
                    "   }\n" +
                    "}";
            long datastreamId = postEntity(EntityType.DATASTREAM, urlParameters).getLong("@iot.id");

            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:00:00Z\",\n" +
                    "  \"result\": 100,\n" +
                    "  \"Datastream\":{\"@iot.id\": "+datastreamId+"}\n" +
                    "}";
            postInvalidEntity(EntityType.OBSERVATION, urlParameters);

            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:00:00Z\",\n" +
                    "  \"result\": 100,\n" +
                    "  \"FeatureOfInterest\": {\n" +
                    "  \t\"description\": \"A weather station.\",\n" +
                    "    \"feature\": {\n" +
                    "      \"type\": \"Point\",\n" +
                    "      \"coordinates\": [\n" +
                    "        -114.05,\n" +
                    "        51.05\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  },\n" +
                    "  \"Datastream\":{\"@iot.id\": "+datastreamId+"}\n" +
                    "}";
            postInvalidEntity(EntityType.OBSERVATION, urlParameters);

            entityTypesToCheck.clear();
            entityTypesToCheck.add(EntityType.OBSERVATION);
            entityTypesToCheck.add(EntityType.FEATURE_OF_INTEREST);
            checkNotExisting(entityTypesToCheck);

            deleteEverythings();

        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    @Test(description = "POST Invalid Entities", groups = "level-2", priority = 3)
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
                    "  \"Thing\": { \"@iot.id\": " + thingIds.get(0) + " },\n" +
                    "  \"ObservedProperty\":{ \"@iot.id\":" + obsPropIds.get(0) + "},\n" +
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
                    "  \"Thing\": { \"@iot.id\": " + thingIds.get(0) + " },\n" +
                    "  \"Sensor\": { \"@iot.id\": " + sensorIds.get(0) + " }\n" +
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
                    "  \"ObservedProperty\":{ \"@iot.id\":" + obsPropIds.get(0) + "},\n" +
                    "  \"Sensor\": { \"@iot.id\": " + sensorIds.get(0) + " }\n" +
                    "}";
            postInvalidEntity(EntityType.DATASTREAM, urlParameters);

            /** Observation **/
            //Create Thing and Datastream
            urlParameters = "{\"description\":\"This is a Test Thing From TestNG\"}";
            long thingId = postEntity(EntityType.THING, urlParameters).getLong(ControlInformation.ID);
            thingIds.add(thingId);
            urlParameters = "{\n" +
                    "  \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Celsius\",\n" +
                    "    \"symbol\": \"degC\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n" +
                    "  },\n" +
                    "  \"description\": \"test datastream.\",\n" +
                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                    "  \"Thing\": { \"@iot.id\": " + thingId + " },\n" +
                    "  \"ObservedProperty\":{ \"@iot.id\":" + obsPropIds.get(0) + "},\n" +
                    "  \"Sensor\": { \"@iot.id\": " + sensorIds.get(0) + " }\n" +
                    "}";
            long datastreamId = postEntity(EntityType.DATASTREAM, urlParameters).getLong(ControlInformation.ID);
            datastreamIds.add(datastreamId);
            //Without Datastream
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:40:00.000Z\",\n" +
                    "  \"result\": 8,\n" +
                    "  \"FeatureOfInterest\": {\"@iot.id\": " + foiIds.get(0) + "}  \n" +
                    "}";
            postInvalidEntity(EntityType.OBSERVATION, urlParameters);
            //Without FOI and without Thing's Location
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:00:00.000Z\",\n" +
                    "  \"result\": 100,\n" +
                    "  \"Datastream\":{\"@iot.id\": " + datastreamId + "}\n" +
                    "}";
            postInvalidEntity(EntityType.OBSERVATION, urlParameters);

        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }

    }

    @Test(description = "PATCH Entities", groups = "level-2", priority = 4)
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
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    @Test(description = "PUT Entities", groups = "level-2", priority = 4)
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
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }


    @Test(description = "DELETE Entities", groups = "level-2", priority = 5)
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

        checkDeleteIntegrityConstraint();
    }

    private void checkDeleteIntegrityConstraint(){
        //Thing
        createEntitiesForDelete();
        deleteEntity(EntityType.THING, thingIds.get(0));
        List<EntityType> entityTypes = new ArrayList<>();
        entityTypes.add(EntityType.THING);
        entityTypes.add(EntityType.DATASTREAM);
        entityTypes.add(EntityType.HISTORICAL_LOCATION);
        entityTypes.add(EntityType.OBSERVATION);
        checkNotExisting(entityTypes);
        entityTypes.clear();
        entityTypes.add(EntityType.LOCATION);
        entityTypes.add(EntityType.SENSOR);
        entityTypes.add(EntityType.OBSERVED_PROPERTY);
        entityTypes.add(EntityType.FEATURE_OF_INTEREST);
        checkExisting(entityTypes);

        //Datastream
        createEntitiesForDelete();
        deleteEntity(EntityType.DATASTREAM, datastreamIds.get(0));
        entityTypes.clear();
        entityTypes.add(EntityType.DATASTREAM);
        entityTypes.add(EntityType.OBSERVATION);
        checkNotExisting(entityTypes);
        entityTypes.clear();
        entityTypes.add(EntityType.THING);
        entityTypes.add(EntityType.SENSOR);
        entityTypes.add(EntityType.OBSERVED_PROPERTY);
        entityTypes.add(EntityType.FEATURE_OF_INTEREST);
        entityTypes.add(EntityType.LOCATION);
        entityTypes.add(EntityType.HISTORICAL_LOCATION);
        checkExisting(entityTypes);

        //Loation
        createEntitiesForDelete();
        deleteEntity(EntityType.LOCATION, locationIds.get(0));
        entityTypes.clear();
        entityTypes.add(EntityType.LOCATION);
        entityTypes.add(EntityType.HISTORICAL_LOCATION);
        checkNotExisting(entityTypes);
        entityTypes.clear();
        entityTypes.add(EntityType.THING);
        entityTypes.add(EntityType.SENSOR);
        entityTypes.add(EntityType.OBSERVED_PROPERTY);
        entityTypes.add(EntityType.FEATURE_OF_INTEREST);
        entityTypes.add(EntityType.DATASTREAM);
        entityTypes.add(EntityType.OBSERVATION);
        checkExisting(entityTypes);

        //HistoricalLoation
        createEntitiesForDelete();
        deleteEntity(EntityType.HISTORICAL_LOCATION, historicalLocationIds.get(0));
        entityTypes.clear();
        entityTypes.add(EntityType.HISTORICAL_LOCATION);
        checkNotExisting(entityTypes);
        entityTypes.clear();
        entityTypes.add(EntityType.THING);
        entityTypes.add(EntityType.SENSOR);
        entityTypes.add(EntityType.OBSERVED_PROPERTY);
        entityTypes.add(EntityType.FEATURE_OF_INTEREST);
        entityTypes.add(EntityType.DATASTREAM);
        entityTypes.add(EntityType.OBSERVATION);
        entityTypes.add(EntityType.LOCATION);
        checkExisting(entityTypes);

        //Sensor
        createEntitiesForDelete();
        deleteEntity(EntityType.SENSOR, sensorIds.get(0));
        entityTypes.clear();
        entityTypes.add(EntityType.SENSOR);
        entityTypes.add(EntityType.DATASTREAM);
        entityTypes.add(EntityType.OBSERVATION);
        checkNotExisting(entityTypes);
        entityTypes.clear();
        entityTypes.add(EntityType.THING);
        entityTypes.add(EntityType.OBSERVED_PROPERTY);
        entityTypes.add(EntityType.FEATURE_OF_INTEREST);
        entityTypes.add(EntityType.LOCATION);
        entityTypes.add(EntityType.HISTORICAL_LOCATION);
        checkExisting(entityTypes);

        //ObservedProperty
        createEntitiesForDelete();
        deleteEntity(EntityType.OBSERVED_PROPERTY, obsPropIds.get(0));
        entityTypes.clear();
        entityTypes.add(EntityType.OBSERVED_PROPERTY);
        entityTypes.add(EntityType.DATASTREAM);
        entityTypes.add(EntityType.OBSERVATION);
        checkNotExisting(entityTypes);
        entityTypes.clear();
        entityTypes.add(EntityType.THING);
        entityTypes.add(EntityType.SENSOR);
        entityTypes.add(EntityType.FEATURE_OF_INTEREST);
        entityTypes.add(EntityType.LOCATION);
        entityTypes.add(EntityType.HISTORICAL_LOCATION);
        checkExisting(entityTypes);

        //FeatureOfInterest
        createEntitiesForDelete();
        deleteEntity(EntityType.FEATURE_OF_INTEREST, foiIds.get(0));
        entityTypes.clear();
        entityTypes.add(EntityType.FEATURE_OF_INTEREST);
        entityTypes.add(EntityType.OBSERVATION);
        checkNotExisting(entityTypes);
        entityTypes.clear();
        entityTypes.add(EntityType.THING);
        entityTypes.add(EntityType.SENSOR);
        entityTypes.add(EntityType.OBSERVED_PROPERTY);
        entityTypes.add(EntityType.LOCATION);
        entityTypes.add(EntityType.HISTORICAL_LOCATION);
        entityTypes.add(EntityType.DATASTREAM);
        checkExisting(entityTypes);

        //Observation
        createEntitiesForDelete();
        deleteEntity(EntityType.OBSERVATION, observationIds.get(0));
        entityTypes.clear();
        entityTypes.add(EntityType.OBSERVATION);
        checkNotExisting(entityTypes);
        entityTypes.clear();
        entityTypes.add(EntityType.THING);
        entityTypes.add(EntityType.SENSOR);
        entityTypes.add(EntityType.OBSERVED_PROPERTY);
        entityTypes.add(EntityType.FEATURE_OF_INTEREST);
        entityTypes.add(EntityType.DATASTREAM);
        entityTypes.add(EntityType.HISTORICAL_LOCATION);
        entityTypes.add(EntityType.LOCATION);
        checkExisting(entityTypes);
    }

    //TODO: Add invalid PATCH test for other entities when it is implemented in the service
    @Test(description = "Invalid PATCH Entities", groups = "level-2", priority = 4)
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
                "        ,\"Thing\":{\"@iot.id\":"+thingId+"}"+
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
                "        ,\"Thing\":{\"@iot.id\":"+thingId+"}"+
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

    @Test(description = "DELETE nonexistent Entities", groups = "level-2", priority = 5)
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
        if (id == -1) {
            return null;
        }
        String urlString = ServiceURLBuilder.buildURLString(rootUri,entityType,id,null,null);
        try {
            return new JSONObject(HTTPMethods.doGet(urlString).get("response").toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            return null;
        }
    }

    public JSONObject postEntity(EntityType entityType, String urlParameters) {
        String urlString = ServiceURLBuilder.buildURLString(rootUri,entityType,-1,null,null);
        try {
            Map<String,Object> responseMap = HTTPMethods.doPost(urlString, urlParameters);
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
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            return null;
        }
    }

    public void postInvalidEntity(EntityType entityType, String urlParameters) {
        String urlString = ServiceURLBuilder.buildURLString(rootUri,entityType,-1,null,null);

        Map<String,Object> responseMap = HTTPMethods.doPost(urlString, urlParameters);
        int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
        Assert.assertTrue(responseCode == 400 || responseCode == 409, "The  " + entityType.name() + " should not be created due to integrity constraints.");

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

    private void deleteNonExsistentEntity(EntityType entityType) {
        long id = Long.MAX_VALUE;
        String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, id, null, null);
        Map<String,Object> responseMap = HTTPMethods.doDelete(urlString);
        int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
        Assert.assertEquals(responseCode, 404, "DELETE does not work properly for nonexistent " + entityType + " with id " + id + ". Returned with response code " + responseCode + ".");

    }

    public JSONObject updateEntity(EntityType entityType, String urlParameters, long id) {
        String urlString = ServiceURLBuilder.buildURLString(rootUri,entityType,id,null,null);
        try {
            Map<String,Object> responseMap = HTTPMethods.doPut(urlString, urlParameters);
            int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
            Assert.assertEquals(responseCode, 200, "Error during updating(PUT) of entity " + entityType.name());

            responseMap = HTTPMethods.doGet(urlString);
            JSONObject result = new JSONObject(responseMap.get("response").toString());
            return result;

        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            return null;
        }
    }

    public JSONObject patchEntity(EntityType entityType, String urlParameters, long id) {
        String urlString = ServiceURLBuilder.buildURLString(rootUri,entityType,id,null,null);
        try {

            Map<String,Object> responseMap = HTTPMethods.doPatch(urlString, urlParameters);
            int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
            Assert.assertEquals(responseCode, 200, "Error during updating(PATCH) of entity " + entityType.name());
            responseMap = HTTPMethods.doGet(urlString);
            JSONObject result = new JSONObject(responseMap.get("response").toString());
            return result;

        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            return null;
        }
    }

    public void invalidPatchEntity(EntityType entityType, String urlParameters, long id) {
        String urlString = ServiceURLBuilder.buildURLString(rootUri,entityType,id,null,null);

        Map<String,Object> responseMap = HTTPMethods.doPatch(urlString,urlParameters);
        int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
        Assert.assertEquals(responseCode, 400, "Error: Patching related entities inline must be illegal for entity " + entityType.name());

    }

    private void checkPatch(EntityType entityType, JSONObject oldEntity, JSONObject newEntity, Map diffs){
        try {
            for (String property : EntityProperties.getPropertiesListFor(entityType)) {
                if (diffs.containsKey(property)) {
                    Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                } else{
                    Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    private void checkPut(EntityType entityType, JSONObject oldEntity, JSONObject newEntity, Map diffs){
        try {
            for (String property : EntityProperties.getPropertiesListFor(entityType)) {
                if (diffs.containsKey(property)) {
                    Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
                } else{
//                    Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    private long checkAutomaticInsertionOfFOI(long obsId, JSONObject locationObj, long expectedFOIId){
        String urlString = rootUri+"/Observations("+obsId+")/FeatureOfInterest";
        try {
            Map<String,Object> responseMap = HTTPMethods.doGet(urlString);
            int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
            Assert.assertEquals(responseCode, 200, "ERROR: FeatureOfInterest was not automatically create.");
            JSONObject result = new JSONObject(responseMap.get("response").toString());
            long id = result.getLong(ControlInformation.ID);
            if(expectedFOIId != -1){
                Assert.assertEquals(id, expectedFOIId, "ERROR: the Observation should have linked to FeatureOfInterest with ID: "+expectedFOIId+" , but it is linked for FeatureOfInterest with Id: "+id+".");
            }
            Assert.assertEquals(result.getJSONObject("feature").toString(), locationObj.getJSONObject("location").toString(), "ERROR: Automatic created FeatureOfInterest does not match last Location of that Thing.");
            return id;
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
        return -1;
    }

    private long checkRelatedEntity(EntityType parentEntityType, long parentId, EntityType relationEntityType, JSONObject relationObj){
        boolean isCollection = true;
        String urlString = ServiceURLBuilder.buildURLString(rootUri,parentEntityType,parentId,relationEntityType,null);
        if(urlString.trim().charAt(urlString.trim().length()-1)!='s'){
            isCollection = false;
        }

        try {
            Map<String,Object> responseMap = HTTPMethods.doGet(urlString);
            int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
            Assert.assertEquals(responseCode, 200, "ERROR: Deep inserted " + relationEntityType + " does not created or linked to " + parentEntityType);
            JSONObject result = new JSONObject(responseMap.get("response").toString());
            if(isCollection == true){
                result = result.getJSONArray("value").getJSONObject(0);
            }
            Iterator iterator = relationObj.keys();
            while(iterator.hasNext()){
                String key = iterator.next().toString();
                Assert.assertEquals(result.get(key).toString(), relationObj.get(key).toString(), "ERROR: Deep inserted "+relationEntityType+" is not created correctly.");
            }
            return result.getLong(ControlInformation.ID);
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
        return -1;
    }

    private void checkForObservationResultTime(JSONObject observation, String resultTimeValue){
        try {
            if(resultTimeValue == null){
                    Assert.assertEquals(observation.get("resultTime").toString(),"null","The resultTime of the Observation "+observation.getLong(ControlInformation.ID)+" should have been null but it is now \""+observation.get("resultTime").toString()+"\".");
            } else {
                Assert.assertEquals(observation.get("resultTime").toString(), resultTimeValue, "The resultTime of the Observation " + observation.getLong(ControlInformation.ID) + " should have been \"" + resultTimeValue + "\" but it is now \"" + observation.get("resultTime").toString() + "\".");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }
    }

    private void checkNotExisting(List<EntityType> entityTypes){
        for(EntityType entityType:entityTypes) {
            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, null);
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            try {
                JSONObject result = new JSONObject(responseMap.get("response").toString());
                JSONArray array = result.getJSONArray("value");
                Assert.assertEquals(array.length(), 0, entityType + " is created although it shouldn't.");
            } catch (JSONException e) {
                e.printStackTrace();
                Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            }
        }
    }

    private void checkExisting(List<EntityType> entityTypes){
        for(EntityType entityType:entityTypes) {
            String urlString = ServiceURLBuilder.buildURLString(rootUri, entityType, -1, null, null);
            Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
            try {
                JSONObject result = new JSONObject(responseMap.get("response").toString());
                JSONArray array = result.getJSONArray("value");
                Assert.assertTrue(array.length() > 0, entityType + " is created although it shouldn't.");
            } catch (JSONException e) {
                e.printStackTrace();
                Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
            }
        }
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

    private void createEntitiesForDelete(){
        try {

            deleteEverythings();
            thingIds.clear();
            locationIds.clear();
            historicalLocationIds.clear();
            datastreamIds.clear();
            sensorIds.clear();
            observationIds.clear();
            obsPropIds.clear();
            foiIds.clear();

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
                    "        }\n" +
                    "    ]\n" +
                    "}";
            String urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, -1, null, null);
            Map<String, Object> responseMap = HTTPMethods.doPost(urlString, urlParameters);
            String response = responseMap.get("response").toString();
            thingIds.add(Long.parseLong(response.substring(response.indexOf("(") + 1, response.indexOf(")"))));

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, thingIds.get(0), EntityType.LOCATION, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            JSONArray array = new JSONObject(response).getJSONArray("value");
            locationIds.add(array.getJSONObject(0).getLong(ControlInformation.ID));

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, thingIds.get(0), EntityType.DATASTREAM, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            datastreamIds.add(array.getJSONObject(0).getLong(ControlInformation.ID));

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamIds.get(0), EntityType.SENSOR, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            sensorIds.add(new JSONObject(response).getLong(ControlInformation.ID));
            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamIds.get(0), EntityType.OBSERVED_PROPERTY, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            obsPropIds.add(new JSONObject(response).getLong(ControlInformation.ID));

            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.THING, thingIds.get(0), EntityType.HISTORICAL_LOCATION, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            array = new JSONObject(response).getJSONArray("value");
            historicalLocationIds.add(array.getJSONObject(0).getLong(ControlInformation.ID));

            //Observations
            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.DATASTREAM, datastreamIds.get(0), EntityType.OBSERVATION, null);
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:00:00Z\",\n" +
                    "  \"result\": 1 \n" +
                    "   }";
            responseMap = HTTPMethods.doPost(urlString, urlParameters);
            response = responseMap.get("response").toString();
            observationIds.add(Long.parseLong(response.substring(response.lastIndexOf("(") + 1, response.lastIndexOf(")"))));

            //FeatureOfInterest
            urlString = ServiceURLBuilder.buildURLString(rootUri, EntityType.OBSERVATION, observationIds.get(0), EntityType.FEATURE_OF_INTEREST, null);
            responseMap = HTTPMethods.doGet(urlString);
            response = responseMap.get("response").toString();
            foiIds.add(new JSONObject(response).getLong(ControlInformation.ID));

        } catch (JSONException e) {
            e.printStackTrace();
            Assert.fail("An Exception occurred during testing!:\n" + e.getMessage());
        }


    }
}
