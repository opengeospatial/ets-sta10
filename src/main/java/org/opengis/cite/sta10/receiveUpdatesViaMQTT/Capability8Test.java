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

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.Callable;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opengis.cite.sta10.SuiteAttribute;
import org.opengis.cite.sta10.util.EntityHelper;
import org.opengis.cite.sta10.util.EntityProperties;
import org.opengis.cite.sta10.util.EntityRelations;
import org.opengis.cite.sta10.util.EntityType;
import org.opengis.cite.sta10.util.mqtt.MqttBatchResult;
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
public class Capability8Test {

    private static final List<EntityType> ENTITY_TYPES_FOR_CREATE = Arrays.asList(
            EntityType.THING,
            EntityType.LOCATION,
            EntityType.SENSOR,
            EntityType.OBSERVED_PROPERTY,
            EntityType.FEATURE_OF_INTEREST,
            EntityType.DATASTREAM,
            EntityType.OBSERVATION,
            EntityType.HISTORICAL_LOCATION);
    private static final List<EntityType> ENTITY_TYPES_FOR_DEEP_INSERT = Arrays.asList(
            EntityType.THING,
            EntityType.DATASTREAM,
            EntityType.OBSERVATION);
    private static final List<EntityType> ENTITY_TYPES_FOR_DELETE = Arrays.asList(
            EntityType.OBSERVATION,
            EntityType.FEATURE_OF_INTEREST,
            EntityType.DATASTREAM,
            EntityType.SENSOR,
            EntityType.OBSERVED_PROPERTY,
            EntityType.HISTORICAL_LOCATION,
            EntityType.LOCATION,
            EntityType.THING);

    private EntityHelper entityHelper;
    private final Map<EntityType, Long> ids = new HashMap<>();
    private MqttHelper mqttHelper;
    private String rootUri;

    @Test(description = "Subcribe to EntitySet and insert Entity", groups = "level-8")
    public void checkSubscribeToEntitySetInsert() {
        deleteCreatedEntities();
        ENTITY_TYPES_FOR_CREATE.stream().forEach((entityType) -> {
            MqttBatchResult<Long> result = mqttHelper.executeRequests(getInsertEntityAction(entityType), MqttHelper.getTopic(entityType));
            ids.put(entityType, result.getActionResult());
            Assert.assertTrue(jsonEqualsWithLinkResolving(entityHelper.getEntity(entityType, result.getActionResult()), result.getMessages().values().iterator().next(), MqttHelper.getTopic(entityType)));
        });
    }

    @Test(description = "Subcribe to EntitySet and update (PATCH) Entity", groups = "level-8")
    public void checkSubscribeToEntitySetUpdatePATCH() {
        deleteCreatedEntities();
        createEntities();
        ENTITY_TYPES_FOR_CREATE.stream().forEach((entityType) -> {
            MqttBatchResult<JSONObject> result = mqttHelper.executeRequests(getUpdatePatchEntityAction(entityType), MqttHelper.getTopic(entityType));
            Assert.assertTrue(jsonEqualsWithLinkResolving(result.getActionResult(), result.getMessages().values().iterator().next(), MqttHelper.getTopic(entityType)));
        });
    }

    @Test(description = "Subcribe to EntitySet and update (PUT) Entity", groups = "level-8")
    public void checkSubscribeToEntitySetUpdatePUT() {
        deleteCreatedEntities();
        createEntities();
        ENTITY_TYPES_FOR_CREATE.stream().forEach((entityType) -> {
            MqttBatchResult<JSONObject> result = mqttHelper.executeRequests(getUpdatePutEntityAction(entityType), MqttHelper.getTopic(entityType));
            Assert.assertTrue(jsonEqualsWithLinkResolving(result.getActionResult(), result.getMessages().values().iterator().next(), MqttHelper.getTopic(entityType)));
        });
    }

    @Test(description = "Subcribe to EntitySet with multiple $select and insert Entity", groups = "level-8")
    public void checkSubscribeToEntitySetWithMultipleSelectInsert() {
        deleteCreatedEntities();
        ENTITY_TYPES_FOR_CREATE.stream().forEach((entityType) -> {
            List<String> selectedProperties = getSelectedProperties(entityType);
            MqttBatchResult<Long> result = mqttHelper.executeRequests(getInsertEntityAction(entityType), MqttHelper.getTopic(entityType, selectedProperties));
            ids.put(entityType, result.getActionResult());
            JSONObject entity = entityHelper.getEntity(entityType, result.getActionResult());
            filterEntity(entity, selectedProperties);
            Assert.assertTrue(jsonEqualsWithLinkResolving(entity, result.getMessages().values().iterator().next(), MqttHelper.getTopic(entityType, selectedProperties)));
        });
    }

    @Test(description = "Subcribe to EntitySet with multiple $select and update (PATCH) Entity", groups = "level-8")
    public void checkSubscribeToEntitySetWithMultipleSelectUpdatePATCH() {
        deleteCreatedEntities();
        createEntities();
        ENTITY_TYPES_FOR_CREATE.stream().forEach((entityType) -> {
            List<String> selectedProperties = getSelectedProperties(entityType);

            Map<String, Object> changes = entityHelper.getEntityChanges(entityType, selectedProperties);
            MqttBatchResult<JSONObject> result = mqttHelper.executeRequests(
                    () -> {
                        return entityHelper.patchEntity(entityType, changes, ids.get(entityType));
                    },
                    MqttHelper.getTopic(entityType, selectedProperties));
            Assert.assertTrue(jsonEqualsWithLinkResolving(new JSONObject(changes), result.getMessages().values().iterator().next(), MqttHelper.getTopic(entityType, selectedProperties)));
        });
    }

    @Test(description = "Subcribe to EntitySet with multiple $select and update (PUT) Entity", groups = "level-8")
    public void checkSubscribeToEntitySetWithMultipleSelectUpdatePUT() {
        deleteCreatedEntities();
        createEntities();
        ENTITY_TYPES_FOR_CREATE.stream().forEach((entityType) -> {
            List<String> selectedProperties = getSelectedProperties(entityType);

            Map<String, Object> changes = entityHelper.getEntityChanges(entityType, selectedProperties);
            MqttBatchResult<JSONObject> result = mqttHelper.executeRequests(
                    () -> {
                        return entityHelper.putEntity(entityType, changes, ids.get(entityType));
                    },
                    MqttHelper.getTopic(entityType, selectedProperties));
            Assert.assertTrue(jsonEqualsWithLinkResolving(new JSONObject(changes), result.getMessages().values().iterator().next(), MqttHelper.getTopic(entityType, selectedProperties)));
        });
    }

    @Test(description = "Subcribe to EntitySet via relative topic", groups = "level-8")
    public void checkSubscribeToEntitySetWithRelativeTopicUpdatePUT() {
        deleteCreatedEntities();
        createEntities();
        ENTITY_TYPES_FOR_CREATE.stream().forEach((entityType) -> {
            List<String> relativeTopics = MqttHelper.getRelativeTopicsForEntitySet(entityType, ids);
            if (!(relativeTopics.isEmpty())) {
                MqttBatchResult<JSONObject> result = mqttHelper.executeRequests(
                        getUpdatePutEntityAction(entityType),
                        relativeTopics.toArray(new String[relativeTopics.size()]));
                result.getMessages().entrySet().stream().forEach((entry) -> {
                    try {
                        // coudl return multiple results so make sure we only get the latest
                        JSONObject expectedResult = entityHelper.getEntity(entry.getKey() + "?$orderby=id%20desc&$top=1").getJSONArray("value").getJSONObject(0);
                        Assert.assertTrue(jsonEqualsWithLinkResolving(expectedResult, entry.getValue(), entry.getKey()));
                    } catch (JSONException ex) {
                        Assert.fail("Could not get expected result for MQTT subscription from server", ex);
                    }
                });
            }
        });
    }

    @Test(description = "Subcribe to multiple EntitySets and deep insert multiple entites", groups = "level-8")
    public void checkSubscribeToEntitySetsWithDeepInsert() {
        deleteCreatedEntities();
        ENTITY_TYPES_FOR_DEEP_INSERT.stream().forEach((EntityType entityType) -> {
            DeepInsertInfo deepInsertInfo = entityHelper.getDeepInsertInfo(entityType);
            List<String> topics = new ArrayList<>(deepInsertInfo.getSubEntityTypes().size() + 1);
            topics.add(MqttHelper.getTopic(deepInsertInfo.getEntityType()));
            deepInsertInfo.getSubEntityTypes().stream().forEach((subType) -> {
                topics.add(MqttHelper.getTopic(subType));
            });
            MqttBatchResult<Long> result = mqttHelper.executeRequests(
                    getDeepInsertEntityAction(entityType),
                    topics.toArray(new String[topics.size()]));
            ids.put(entityType, result.getActionResult());
            JSONObject entity = entityHelper.getEntity(deepInsertInfo.getEntityType(), result.getActionResult());
            Optional<JSONObject> rootResult = result.getMessages().entrySet().stream().filter(x -> x.getKey().equals(MqttHelper.getTopic(deepInsertInfo.getEntityType()))).map(x -> x.getValue()).findFirst();
            if (!rootResult.isPresent()) {
                Assert.fail("Deep insert MQTT result is missing root entity");
            }
            Assert.assertTrue(jsonEqualsWithLinkResolving(entity, rootResult.get(), MqttHelper.getTopic(deepInsertInfo.getEntityType())));
            deepInsertInfo.getSubEntityTypes().stream().forEach((subType) -> {
                JSONObject subEntity = getSubEntityByRoot(deepInsertInfo.getEntityType(), result.getActionResult(), subType);
                Optional<JSONObject> subResult = result.getMessages().entrySet().stream().filter(x -> x.getKey().equals(MqttHelper.getTopic(subType))).map(x -> x.getValue()).findFirst();
                if (!subResult.isPresent()) {
                    Assert.fail("Deep insert MQTT result is missing entity " + subEntity.toString());
                }
                Assert.assertTrue(jsonEqualsWithLinkResolving(subEntity, subResult.get(), MqttHelper.getTopic(subType)));
            });
        });
    }

    @Test(description = "Subcribe to Entity and update (PATCH) Entity", groups = "level-8")
    public void checkSubscribeToEntityUpdatePATCH() {
        deleteCreatedEntities();
        createEntities();
        ENTITY_TYPES_FOR_CREATE.stream().forEach((entityType) -> {
            MqttBatchResult<JSONObject> result = mqttHelper.executeRequests(getUpdatePatchEntityAction(entityType), MqttHelper.getTopic(entityType, ids.get(entityType)));
            Assert.assertTrue(jsonEqualsWithLinkResolving(result.getActionResult(), result.getMessages().values().iterator().next(), MqttHelper.getTopic(entityType, ids.get(entityType))));
        });
    }

    @Test(description = "Subcribe to Entity and update (PUT) Entity", groups = "level-8")
    public void checkSubscribeToEntityUpdatePUT() {
        deleteCreatedEntities();
        createEntities();
        ENTITY_TYPES_FOR_CREATE.stream().forEach((entityType) -> {
            MqttBatchResult<JSONObject> result = mqttHelper.executeRequests(getUpdatePutEntityAction(entityType), MqttHelper.getTopic(entityType, ids.get(entityType)));
            Assert.assertTrue(jsonEqualsWithLinkResolving(result.getActionResult(), result.getMessages().values().iterator().next(), MqttHelper.getTopic(entityType, ids.get(entityType))));
        });
    }

    @Test(description = "Subcribe to Entity via relative topic", groups = "level-8")
    public void checkSubscribeToEntityWithRelativeTopicUpdatePUT() {
        deleteCreatedEntities();
        createEntities();
        ENTITY_TYPES_FOR_CREATE.stream().forEach((entityType) -> {
            List<String> relativeTopics = MqttHelper.getRelativeTopicsForEntity(entityType, ids);
            if (!(relativeTopics.isEmpty())) {
                MqttBatchResult<JSONObject> result = mqttHelper.executeRequests(
                        getUpdatePutEntityAction(entityType),
                        relativeTopics.toArray(new String[relativeTopics.size()]));
                result.getMessages().entrySet().stream().forEach((entry) -> {
                    JSONObject expectedResult = entityHelper.getEntity(entry.getKey());
                    Assert.assertTrue(jsonEqualsWithLinkResolving(expectedResult, entry.getValue(), entry.getKey()));
                });
            }
        });
    }

    @Test(description = "Subcribe to Property and update (PATCH) Entity", groups = "level-8")
    public void checkSubscribeToPropertyUpdatePATCH() {
        deleteCreatedEntities();
        createEntities();
        ENTITY_TYPES_FOR_CREATE.stream().forEach((entityType) -> {
            Map<String, Object> changes = entityHelper.getEntityChanges(entityType);
            for (String property : EntityProperties.getPropertiesListFor(entityType)) {
                Map<String, Object> propertyChange = new HashMap<>(0);
                propertyChange.put(property, changes.get(property));
                MqttBatchResult<JSONObject> result = mqttHelper.executeRequests(
                        () -> {
                            return entityHelper.patchEntity(entityType, propertyChange, ids.get(entityType));
                        },
                        MqttHelper.getTopic(entityType, ids.get(entityType), property));
                Assert.assertTrue(jsonEqualsWithLinkResolving(new JSONObject(propertyChange), result.getMessages().values().iterator().next(), MqttHelper.getTopic(entityType, ids.get(entityType), property)));
            }
        });
    }

    @Test(description = "Subcribe to Property and update (PUT) Entity", groups = "level-8")
    public void checkSubscribeToPropertyUpdatePUT() {
        deleteCreatedEntities();
        createEntities();
        ENTITY_TYPES_FOR_CREATE.stream().forEach((entityType) -> {
            Map<String, Object> changes = entityHelper.getEntityChanges(entityType);
            for (String property : EntityProperties.getPropertiesListFor(entityType)) {
                Map<String, Object> propertyChange = new HashMap<>(0);
                propertyChange.put(property, changes.get(property));
                MqttBatchResult<JSONObject> result = mqttHelper.executeRequests(
                        () -> {
                            return entityHelper.putEntity(entityType, propertyChange, ids.get(entityType));
                        },
                        MqttHelper.getTopic(entityType, ids.get(entityType), property));
                Assert.assertTrue(jsonEqualsWithLinkResolving(new JSONObject(propertyChange), result.getMessages().values().iterator().next(), MqttHelper.getTopic(entityType, ids.get(entityType), property)));
            }
        });
    }

    /**
     * This method is run after all the tests of this class is run and clean the
     * database.
     */
    @AfterClass
    public void clearDatabase() {
        entityHelper.deleteEverything();
    }

    /**
     * This method will be run before starting the test for this conformance
     * class. It initializes all objects and connections used within the test.
     *
     * @param testContext The test context to find out whether this class is
     * requested to test or not
     */
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

    private void createEntities() {
        ENTITY_TYPES_FOR_CREATE.stream().forEach((entityType) -> {
            try {
                ids.put(entityType, getInsertEntityAction(entityType).call());
            } catch (Exception ex) {
                Assert.fail("Could not create entities");
            }
        });
    }

    private void deleteCreatedEntities() {

        ENTITY_TYPES_FOR_DELETE.stream().filter((entityType) -> (ids.containsKey(entityType))).map((entityType) -> {
            entityHelper.deleteEntity(entityType, ids.get(entityType));
            return entityType;
        }).forEach((entityType) -> {
            ids.remove(entityType);
        });
    }

    private JSONObject filterEntity(JSONObject entity, List<String> selectedProperties) {
        Iterator iterator = entity.keys();
        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            if (!selectedProperties.contains(key)) {
                iterator.remove();
            }
        }
        return entity;
    }

    private Callable<Long> getDeepInsertEntityAction(EntityType entityType) {
        Callable<Long> trigger = () -> {
            switch (entityType) {
                case THING:
                    return entityHelper.createThingWithDeepInsert();
                case DATASTREAM:
                    return entityHelper.createDatastreamWithDeepInsert(ids.get(EntityType.THING));
                case OBSERVATION:
                    return entityHelper.createObservationWithDeepInsert(ids.get(EntityType.DATASTREAM));
            }
            throw new IllegalArgumentException("Unknown EntityType '" + entityType.toString() + "'");
        };
        return trigger;
    }

    private Callable<Long> getInsertEntityAction(EntityType entityType) {
        Callable<Long> trigger = () -> {
            switch (entityType) {
                case THING:
                    return entityHelper.createThing();
                case DATASTREAM:
                    return entityHelper.createDatastream(ids.get(EntityType.THING), ids.get(EntityType.OBSERVED_PROPERTY), ids.get(EntityType.SENSOR));
                case FEATURE_OF_INTEREST:
                    return entityHelper.createFeatureOfInterest();
                case HISTORICAL_LOCATION:
                    return entityHelper.createHistoricalLocation(ids.get(EntityType.THING), ids.get(EntityType.LOCATION));
                case LOCATION:
                    return entityHelper.createLocation(ids.get(EntityType.THING));
                case OBSERVATION:
                    return entityHelper.createObservation(ids.get(EntityType.DATASTREAM), ids.get(EntityType.FEATURE_OF_INTEREST));
                case OBSERVED_PROPERTY:
                    return entityHelper.createObservedProperty();
                case SENSOR:
                    return entityHelper.createSensor();
            }
            throw new IllegalArgumentException("Unknown EntityType '" + entityType.toString() + "'");
        };
        return trigger;
    }

    private String getPathToRelatedEntity(EntityType sourceEntityType, EntityType destinationEntityType) {
        Queue<BFSStructure> queue = new LinkedList<>();
        queue.offer(new BFSStructure(sourceEntityType, ""));
        while (queue.peek() != null) {
            BFSStructure currentElement = queue.poll();
            List<String> relations = Arrays.asList(EntityRelations.getRelationsListFor(currentElement.entityType));
            for (String relation : relations) {
                EntityType relatedType = EntityRelations.getEntityTypeOfRelation(relation);
                if (relatedType.equals(destinationEntityType)) {
                    return currentElement.path
                            + (currentElement.path.isEmpty()
                                    ? relation
                                    : "/" + relation);
                } else {
                    queue.offer(new BFSStructure(relatedType, currentElement.path + (currentElement.path.isEmpty() ? relation : "/" + relation)));
                }
            }
        }
        return "";
    }

    private List<String> getSelectedProperties(EntityType entityType) {
        String[] allProperties = EntityProperties.getPropertiesListFor(entityType);
        List<String> selectedProperties = new ArrayList<>(allProperties.length / 2);
        for (int i = 0; i < allProperties.length; i += 2) {
            selectedProperties.add(allProperties[i]);
        }
        return selectedProperties;
    }

    private JSONObject getSubEntityByRoot(EntityType rootEntityType, Long rootId, EntityType subtEntityType) {
        try {
            String path = getPathToRelatedEntity(subtEntityType, rootEntityType);
            path = "/" + EntityRelations.getRootEntitySet(subtEntityType) + "?$filter=" + path + "/id%20eq%20" + rootId;
            JSONObject result = entityHelper.getEntity(path);
            if (result.getInt("@iot.count") != 1) {
                Assert.fail("Invalid result with size != 1");
            }
            JSONObject subEntity = result.getJSONArray("value").getJSONObject(0);
            //helper.clearLinks(subEntity);
            return subEntity;
        } catch (JSONException ex) {
            Assert.fail("Invalid JSON", ex);
        }
        throw new IllegalStateException();
    }

    private Callable<JSONObject> getUpdatePatchEntityAction(EntityType entityType) {
        return () -> {
            return entityHelper.updateEntitywithPATCH(entityType, ids.get(entityType));
        };
    }

    private Callable<JSONObject> getUpdatePutEntityAction(EntityType entityType) {
        return () -> {
            return entityHelper.updateEntitywithPUT(entityType, ids.get(entityType));
        };
    }

    private boolean jsonEqualsWithLinkResolving(JSONObject obj1, JSONObject obj2, String topic) {
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
                    if (!jsonEqualsWithLinkResolving((JSONObject) val1, (JSONObject) obj2.getJSONObject(key), topic)) {
                        return false;
                    }
                } else if (val1 instanceof JSONArray) {
                    JSONArray arr1 = (JSONArray) val1;
                    if (!jsonEqualsWithLinkResolving(arr1.toJSONObject(arr1), obj2.getJSONArray(key).toJSONObject(obj2.getJSONArray(key)), topic)) {
                        return false;
                    }
                } else if (key.toLowerCase().endsWith("time")) {
                    if (!checkTimeEquals(val1.toString(), obj2.get(key).toString())) {
                        return false;
                    }
                } else if (topic != null && !topic.isEmpty() && key.endsWith("@iot.navigationLink")) {
                    String version = topic.substring(0, topic.indexOf("/"));

                    String selfLink1 = obj1.getString("@iot.selfLink");
                    URI baseUri1 = URI.create(selfLink1.substring(0, selfLink1.indexOf(version))).resolve(topic);
                    String absoluteUri1 = baseUri1.resolve(obj1.getString(key)).toString();

                    String selfLink2 = obj2.getString("@iot.selfLink");
                    URI baseUri2 = URI.create(selfLink2.substring(0, selfLink2.indexOf(version))).resolve(topic);
                    String absoluteUri2 = baseUri2.resolve(obj2.getString(key)).toString();
                    return absoluteUri1.equals(absoluteUri2);

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
        if (val1.equals(val2)) {
            return true;
        }

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

    private class BFSStructure {

        EntityType entityType;
        String path;

        public BFSStructure(EntityType entityType, String path) {
            this.entityType = entityType;
            this.path = path;
        }
    }
}
