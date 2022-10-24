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
package org.opengis.cite.sta10.createObservationsViaMQTT;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opengis.cite.sta10.SuiteAttribute;
import org.opengis.cite.sta10.util.EntityHelper;
import static org.opengis.cite.sta10.util.EntityRelations.getEntityTypeOfRelation;
import org.opengis.cite.sta10.util.EntityType;
import org.opengis.cite.sta10.util.mqtt.MqttHelper;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 *
 * @author jab
 */
public class Capability7Test {

    private MqttHelper mqttHelper;
    private EntityHelper entityHelper;
    private String rootUri;

    @Test(description = "Create observation via MQTT on observation entity set (topic: [version]/Observations", groups = "level-7")
    public void checkCreateObservationDirect() {
        JSONObject createdObservation = getObservation();
        mqttHelper.publish(MqttHelper.getTopic(EntityType.OBSERVATION), createdObservation.toString());
        JSONObject latestObservation = entityHelper.getLatestEntity(
                EntityType.OBSERVATION,
                "$expand=Datastream($select=id),FeatureOfInterest($select=id)&$select=result,phenomenonTime,validTime,parameters");
        Assert.assertTrue(jsonEquals(latestObservation, createdObservation));
    }

    @Test(description = "Create observation via MQTT using topic [version]/Datastreams([ID])/Observations", groups = "level-7")
    public void checkCreateObservationViaDatastream() {
        JSONObject createdObservation = getObservation();
        long datastreamId = -1;
        try {
            datastreamId = createdObservation.getJSONObject("Datastream").getLong("@iot.id");
        } catch (JSONException ex) {
            Assert.fail("created observation does not contain @iot.id", ex);
        }
        mqttHelper.publish(MqttHelper.getTopic(EntityType.DATASTREAM, datastreamId, "Observations"), createdObservation.toString());
        JSONObject latestObservation = entityHelper.getLatestEntity(
                EntityType.OBSERVATION,
                "$expand=Datastream($select=id),FeatureOfInterest($select=id)&$select=result,phenomenonTime,validTime,parameters");
        Assert.assertTrue(jsonEquals(latestObservation, createdObservation));
    }

    @Test(description = "Create observation via MQTT using topic [version]/FeatureOfInterests([ID])/Observations", groups = "level-7")
    public void checkCreateObservationViaFeatureOfInterest() {
        JSONObject createdObservation = getObservation();
        long featureOfInterestId = -1;
        try {
            featureOfInterestId = createdObservation.getJSONObject("FeatureOfInterest").getLong("@iot.id");
        } catch (JSONException ex) {
            Assert.fail("created observation does not contain @iot.id", ex);
        }
        mqttHelper.publish(MqttHelper.getTopic(EntityType.FEATURE_OF_INTEREST, featureOfInterestId, "Observations"), createdObservation.toString());
        JSONObject latestObservation = entityHelper.getLatestEntity(
                EntityType.OBSERVATION,
                "$expand=Datastream($select=id),FeatureOfInterest($select=id)&$select=result,phenomenonTime,validTime,parameters");
        Assert.assertTrue(jsonEquals(latestObservation, createdObservation));
    }

    @Test(description = "Create observation with deep insert via MQTT on observation entity set (topic: [version]/Observations", groups = "level-7")
    public void checkCreateObservationWithDeepInsert() {
        JSONObject createdObservation = getObservationWithDeepInsert();
        mqttHelper.publish(MqttHelper.getTopic(EntityType.OBSERVATION), createdObservation.toString());
        String rgr = expandQueryFromJsonObjet(createdObservation);
        JSONObject latestObservation = entityHelper.getLatestEntity(
                EntityType.OBSERVATION,
                expandQueryFromJsonObjet(createdObservation));
        Assert.assertTrue(jsonEquals(latestObservation, createdObservation));
    }

    private String expandQueryFromJsonObjet(JSONObject expectedResult) {
        return expandQueryFromJsonObjet(expectedResult, "&");
    }

    private String expandQueryFromJsonObjet(JSONObject expectedResult, String seperator) {
        String result = "";
        List<String> selects = new ArrayList<>();
        List<String> expands = new ArrayList<>();
        Iterator iterator = expectedResult.keys();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            EntityType relationType = null;
            try {
                relationType = getEntityTypeOfRelation(key);
            } catch (IllegalArgumentException ex) {

            }
            // check if navigationLink or simple property
            if (relationType != null) {
                try {
                    expands.add(key + "(" + expandQueryFromJsonObjet(expectedResult.getJSONObject(key), ";") + ")");
                } catch (JSONException ex) {
                    Assert.fail("JSON element addressed by navigationLink is no valid JSON object", ex);
                }
            } else {
                selects.add(key);
            }
        }
        if (!selects.isEmpty()) {
            result += "$select=" + selects.stream().collect(Collectors.joining(","));
        }
        if (!expands.isEmpty()) {
            if (!result.isEmpty()) {
                result += seperator;
            }
            result += "$expand=" + expands.stream().collect(Collectors.joining(","));
        }
        return result;
    }

    @AfterClass
    public void clearDatabase() {
        entityHelper.deleteEverything();
    }

    @BeforeClass
    public void init(ITestContext testContext) {
        Object obj = testContext.getSuite().getAttribute(
                SuiteAttribute.LEVEL.getName());
        if ((null != obj)) {
            Integer level = Integer.class.cast(obj);
            Assert.assertTrue(level > 7,
                    "Conformance level 8 will not be checked since ics = " + level);
        }

        rootUri = testContext.getSuite().getAttribute(
                SuiteAttribute.TEST_SUBJECT.getName()).toString();
        rootUri = rootUri.trim();
        if (rootUri.lastIndexOf('/') == rootUri.length() - 1) {
            rootUri = rootUri.substring(0, rootUri.length() - 1);
        }
        if (testContext.getSuite().getAttribute(SuiteAttribute.MQTT_SERVER.getName()) == null) {
            Assert.fail("Property '" + SuiteAttribute.MQTT_SERVER.getName() + "' not set in configuration");
        }
        String mqttServerUri = testContext.getSuite().getAttribute(SuiteAttribute.MQTT_SERVER.getName()).toString();
        if (testContext.getSuite().getAttribute(SuiteAttribute.MQTT_TIMEOUT.getName()) == null) {
            Assert.fail("Property '" + SuiteAttribute.MQTT_TIMEOUT.getName() + "' not set in configuration");
        }
        long mqttTimeout = Long.parseLong(testContext.getSuite().getAttribute(SuiteAttribute.MQTT_TIMEOUT.getName()).toString());

        this.entityHelper = new EntityHelper(rootUri);
        this.mqttHelper = new MqttHelper(mqttServerUri, mqttTimeout);
    }

    private static boolean jsonEquals(JSONObject obj1, JSONObject obj2) {
        if (obj1 == obj2) {
            return true;
        }
        if (obj1 == null) {
            return false;
        }
        if (obj1.getClass() != obj2.getClass()) {
            return false;
        }
        if (obj1.length() != obj2.length()) {
            return false;
        }
        Iterator iterator = obj1.keys();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            if (!obj2.has(key)) {
                return false;
            }
            try {
                Object val1 = obj1.get(key);
                if (val1 instanceof JSONObject) {
                    if (!jsonEquals((JSONObject) val1, (JSONObject) obj2.getJSONObject(key))) {
                        return false;
                    }
                } else if (val1 instanceof JSONArray) {
                    JSONArray arr1 = (JSONArray) val1;
                    if (!jsonEquals(arr1.toJSONObject(arr1), obj2.getJSONArray(key).toJSONObject(obj2.getJSONArray(key)))) {
                        return false;
                    }
                } // check here for properties ending on 'time"
                else if (key.toLowerCase().endsWith("time")) {
                    if (!checkTimeEquals(val1.toString(), obj2.get(key).toString())) {
                        return false;
                    }
                } else if (!val1.equals(obj2.get(key))) {
                    return false;
                }
            } catch (JSONException ex) {
                return false;
            }
        }
        return true;
    }

    private static boolean checkTimeEquals(String val1, String val2) {
        try {
            DateTime dateTime1 = DateTime.parse(val1);
            DateTime dateTime2 = DateTime.parse(val2);
            return dateTime1.isEqual(dateTime2);
        } catch (Exception ex) {
            // do nothing
        }
        try {
            Interval interval1 = Interval.parse(val1);
            Interval interval2 = Interval.parse(val2);
            return interval1.isEqual(interval2);
        } catch (Exception ex) {
            Assert.fail("time properies could neither be parsed as time nor as interval");
        }
        return false;
    }

    private JSONObject getObservation() {
        long value = new Random().nextLong();
        long thingId = entityHelper.createThing();
        long observedPropertyId = entityHelper.createObservedProperty();
        long sensorId = entityHelper.createSensor();
        long datastreamId = entityHelper.createDatastream(thingId, observedPropertyId, sensorId);
        long featureOfInterestId = entityHelper.createFeatureOfInterest();
        try {
            return new JSONObject("{\n"
                    + "  \"phenomenonTime\": \"2015-03-01T02:40:00+02:00\",\n"
                    + "  \"validTime\": \"2016-01-01T01:01:01.000Z/2016-01-01T23:59:59.000Z\",\n"
                    + "  \"result\": " + value + ",\n"
                    + "  \"parameters\":{\"param1\": \"some value1\", \"param2\": \"some value2\"},\n"
                    + "  \"Datastream\":{\"@iot.id\": " + datastreamId + "},\n"
                    + "  \"FeatureOfInterest\": {\"@iot.id\": " + featureOfInterestId + "}  \n"
                    + "}");
        } catch (JSONException ex) {
            Assert.fail("error converting obsveration to JSON", ex);
        }
        return null;
    }

    private JSONObject getObservationWithDeepInsert() {
        long value = new Random().nextLong();
        try {
            return new JSONObject("{\n"
                    + "	\"phenomenonTime\": \"2015-03-01T00:00:00.000Z\",\n"
                    + "	\"result\": " + value + ",\n"
                    + "	\"FeatureOfInterest\": {\n"
                    + "		\"name\": \"A weather station.\",\n"
                    + "		\"description\": \"A weather station.\",\n"
                    + "		\"encodingType\": \"application/vnd.geo+json\",\n"
                    + "		\"feature\": {\n"
                    + "			\"type\": \"Point\",\n"
                    + "			\"coordinates\": [\n"
                    + "				-114.05,\n"
                    + "				51.05\n"
                    + "			]\n"
                    + "		}\n"
                    + "	},\n"
                    + "	\"Datastream\": {\n"
                    + "		\"unitOfMeasurement\": {\n"
                    + "			\"name\": \"Celsius\",\n"
                    + "			\"symbol\": \"degC\",\n"
                    + "			\"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n"
                    + "		},\n"
                    + "		\"name\": \"test datastream.\",\n"
                    + "		\"description\": \"test datastream.\",\n"
                    + "		\"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n"
                    + "		\"Thing\": {\n"
                    + "			\"name\": \"Test Thing\",\n"
                    + "			\"description\": \"This is a Test Thing From TestNG\"\n"
                    + "		},\n"
                    + "		\"ObservedProperty\": {\n"
                    + "			\"name\": \"Luminous Flux\",\n"
                    + "			\"definition\": \"http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html#LuminousFlux\",\n"
                    + "			\"description\": \"Luminous Flux or Luminous Power is the measure of the perceived power of light.\"\n"
                    + "		},\n"
                    + "		\"Sensor\": {        \n"
                    + "			\"name\": \"Acme Fluxomatic 1000\",\n"
                    + "			\"description\": \"Acme Fluxomatic 1000\",\n"
                    + "			\"encodingType\": \"http://schema.org/description\",\n"
                    + "			\"metadata\": \"Light flux sensor\"\n"
                    + "		}\n"
                    + "	}\n"
                    + "}\n"
                    + "");
        } catch (JSONException ex) {
            Assert.fail("error converting obsveration to JSON", ex);
        }
        return null;
    }
}
