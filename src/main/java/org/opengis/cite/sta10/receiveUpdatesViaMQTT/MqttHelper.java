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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import org.json.JSONObject;
import org.opengis.cite.sta10.util.EntityType;
import org.testng.Assert;

/**
 *
 * @author jab
 */
public class MqttHelper {

    private static final String MQTT_TOPIC_PREFIX = "v1.0/";
    private final String mqttServerUri;
    private final long mqttTimeout;

    public MqttHelper(String mqttServerUri, long mqttTimeout) {
        this.mqttServerUri = mqttServerUri;
        this.mqttTimeout = mqttTimeout;
    }

    public <T> MqttBatchResult<T> executeRequests(Callable<T> action, String... topics) {
        MqttBatchResult<T> result = new MqttBatchResult<>(topics.length);
        Map<String, Future<JSONObject>> tempResult = new HashMap<>(topics.length);
        ExecutorService executor = Executors.newFixedThreadPool(topics.length);
        try {
            for (String topic : topics) {
                MqttListener listener = new MqttListener(mqttServerUri, topic);
                listener.connect();
                tempResult.put(topic, executor.submit(listener));
            }
            try {
                result.setActionResult(action.call());
            } catch (Exception ex) {
                Assert.fail("Error executing : " + ex.getMessage(), ex);
            }
            executor.shutdown();
            if (!executor.awaitTermination(mqttTimeout, TimeUnit.MILLISECONDS)) {
                executor.shutdownNow();
            }
            for (Map.Entry<String, Future<JSONObject>> entry : tempResult.entrySet()) {
                result.addMessage(entry.getKey(), entry.getValue().get());
            }
        } catch (InterruptedException | ExecutionException ex) {
            Assert.fail("Error subcribing to MQTT.", ex);
        } finally {
            executor.shutdownNow();
        }
        return result;
    }

    public static List<String> getRelativeTopicsForEntity(EntityType entityType, Map<EntityType, Long> ids) {
        List<String> result = new ArrayList<>();
        switch (entityType) {
            case THING:
                result.add(getTopic(EntityType.DATASTREAM, ids) + "/Thing");
                result.add(getTopic(EntityType.OBSERVATION, ids) + "/Datastream/Thing");
                result.add(getTopic(EntityType.HISTORICAL_LOCATION, ids) + "/Thing");
                break;
            case LOCATION:
                break;
            case SENSOR:
                result.add(getTopic(EntityType.DATASTREAM, ids) + "/Sensor");
                result.add(getTopic(EntityType.OBSERVATION, ids) + "/Datastream/Sensor");
                break;
            case OBSERVED_PROPERTY:
                result.add(getTopic(EntityType.DATASTREAM, ids) + "/ObservedProperty");
                result.add(getTopic(EntityType.OBSERVATION, ids) + "/Datastream/ObservedProperty");
                break;
            case FEATURE_OF_INTEREST:
                result.add(getTopic(EntityType.OBSERVATION, ids) + "/FeatureOfInterest");
                break;
            case DATASTREAM:
                result.add(getTopic(EntityType.OBSERVATION, ids) + "/Datastream");
                break;
            case OBSERVATION:
                break;
            case HISTORICAL_LOCATION:
                break;
            default:
                throw new IllegalArgumentException("Unknown EntityType '" + entityType.toString() + "'");
        }
        return result;
    }

    public static List<String> getRelativeTopicsForEntitySet(EntityType entityType, Map<EntityType, Long> ids) {
        List<String> result = new ArrayList<>();
        switch (entityType) {
            case THING:
                result.add(getTopic(EntityType.LOCATION, ids) + "/Things");
                break;
            case LOCATION:
                result.add(getTopic(EntityType.THING, ids) + "/Locations");
                result.add(getTopic(EntityType.DATASTREAM, ids) + "/Thing/Locations");
                result.add(getTopic(EntityType.HISTORICAL_LOCATION, ids) + "/Thing/Locations");
                break;
            case SENSOR:
                break;
            case OBSERVED_PROPERTY:
                break;
            case FEATURE_OF_INTEREST:
                break;
            case DATASTREAM:
                result.add(getTopic(EntityType.THING, ids) + "/Datastreams");
                result.add(getTopic(EntityType.HISTORICAL_LOCATION, ids) + "/Thing/Datastreams");
                result.add(getTopic(EntityType.SENSOR, ids) + "/Datastreams");
                result.add(getTopic(EntityType.OBSERVED_PROPERTY, ids) + "/Datastreams");
                break;
            case OBSERVATION:
                result.add(getTopic(EntityType.DATASTREAM, ids) + "/Observations");
                break;
            case HISTORICAL_LOCATION:
                result.add(getTopic(EntityType.THING, ids) + "/HistoricalLocations");
                result.add(getTopic(EntityType.DATASTREAM, ids) + "/Thing/HistoricalLocations");
                result.add(getTopic(EntityType.LOCATION, ids) + "/HistoricalLocations");
                break;
            default:
                throw new IllegalArgumentException("Unknown EntityType '" + entityType.toString() + "'");
        }
        return result;
    }

    public static String getTopic(EntityType entityType, List<String> selectedProperties) {
        return getTopic(entityType) + "?$select=" + selectedProperties.stream().collect(Collectors.joining(","));
    }

    public static String getTopic(EntityType entityType, long id, String property) {
        return getTopic(entityType) + "(" + id + ")/" + property;
    }

    public static String getTopic(EntityType entityType, long id) {
        return getTopic(entityType) + "(" + id + ")";
    }

    public static String getTopic(EntityType entityType) {
        switch (entityType) {
            case THING:
                return MQTT_TOPIC_PREFIX + "Things";
            case LOCATION:
                return MQTT_TOPIC_PREFIX + "Locations";
            case SENSOR:
                return MQTT_TOPIC_PREFIX + "Sensors";
            case OBSERVED_PROPERTY:
                return MQTT_TOPIC_PREFIX + "ObservedProperties";
            case FEATURE_OF_INTEREST:
                return MQTT_TOPIC_PREFIX + "FeaturesOfInterest";
            case DATASTREAM:
                return MQTT_TOPIC_PREFIX + "Datastreams";
            case OBSERVATION:
                return MQTT_TOPIC_PREFIX + "Observations";
            case HISTORICAL_LOCATION:
                return MQTT_TOPIC_PREFIX + "HistoricalLocations";
            default:
                throw new IllegalArgumentException("Unknown EntityType '" + entityType.toString() + "'");
        }
    }

    private static String getTopic(EntityType entityType, Map<EntityType, Long> ids) {
        return getTopic(entityType, ids.get(entityType));
    }

}
