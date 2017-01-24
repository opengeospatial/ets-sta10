package org.opengis.cite.sta10.util;

import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;

/**
 * Utility methods for comparing results and cleaning the service.
 *
 * @author Hylke van der Schaaf
 */
public class EntityUtils {

    /**
     * Find the expected count value for the given request. Can not determine
     * the count for paths like /Datastreams(xxx)/Thing/Locations since the id
     * of the Thing can not be determined from the path.
     *
     * @param request The request to determine the count for.
     * @param entityCounts The object holding the entity counts.
     * @return The expected count for the given request.
     */
    public static long findCountForRequest(Request request, EntityCounts entityCounts) {
        long parentId = -1;
        long count = -1;
        EntityType parentType = null;
        for (PathElement element : request.getPath()) {
            EntityType elementType = element.getEntityType();
            if (element.getId() != null) {
                parentId = element.getId();
                parentType = elementType;
                count = -1;
            } else if (parentType == null) {
                if (!element.isCollection()) {
                    throw new IllegalArgumentException("Non-collection requested without parent.");
                }
                count = entityCounts.getCount(elementType);
            } else if (element.isCollection()) {
                count = entityCounts.getCount(parentType, parentId, elementType);
                parentType = null;
                parentId = -1;
            } else {
                count = -1;
                // Can not determine the id of this single-entity.
            }
        }

        return count;
    }

    /**
     * Checks the given response against the given request.
     *
     * @param response The response object to check.
     * @param request The request to check the response against.
     * @param entityCounts The object with the expected entity counts.
     */
    public static void checkResponse(JSONObject response, Request request, EntityCounts entityCounts) {
        try {
            if (request.isCollection()) {
                checkCollection(response.getJSONArray("value"), request, entityCounts);

                // check count for request
                Query expandQuery = request.getQuery();
                Boolean count = expandQuery.getCount();
                String countProperty = "@iot.count";
                if (count != null) {
                    if (count) {
                        Assert.assertTrue(response.has(countProperty), "Response should have property " + countProperty + " for request: '" + request.toString() + "'");
                    } else {
                        Assert.assertFalse(response.has(countProperty), "Response should not have property " + countProperty + " for request: '" + request.toString() + "'");
                    }
                }

                long expectedCount = findCountForRequest(request, entityCounts);
                if (response.has(countProperty) && expectedCount != -1) {
                    long foundCount = response.getLong(countProperty);
                    Assert.assertEquals(foundCount, expectedCount, "Incorrect count for collection of " + request.getEntityType() + " for request: '" + request.toString() + "'");
                }
                Long top = expandQuery.getTop();
                if (top != null && expectedCount != -1) {
                    int foundNumber = response.getJSONArray("value").length();
                    long skip = expandQuery.getSkip() == null ? 0 : expandQuery.getSkip();

                    long expectedNumber = Math.max(0, Math.min(expectedCount - skip, top));
                    if (foundNumber != expectedNumber) {
                        Assert.fail("Requested " + top + " of " + expectedCount + ", expected " + expectedNumber + " with skip of " + skip + " but received " + foundNumber + " for request: '" + request.toString() + "'");
                    }

                    String nextLinkProperty = "@iot.nextLink";
                    if (foundNumber + skip < expectedCount) {
                        // should have nextLink
                        Assert.assertTrue(response.has(nextLinkProperty), "Entity should have " + nextLinkProperty + " for request: '" + request.toString() + "'");
                    } else {
                        // should not have nextLink
                        Assert.assertFalse(response.has(nextLinkProperty), "Entity should not have " + nextLinkProperty + " for request: '" + request.toString() + "'");
                    }

                }

            } else {
                checkEntity(response, request, entityCounts);
            }
        } catch (JSONException ex) {
            Assert.fail("Failure when checking response of query '" + request.getLastUrl() + "'", ex);
        }
    }

    /**
     * Check a collection from a response, against the given expand as present
     * in the request.
     *
     * @param collection The collection of items to check.
     * @param expand The expand that led to the collection.
     * @param entityCounts The object with the expected entity counts.
     * @throws JSONException if there is a problem with the json.
     */
    public static void checkCollection(JSONArray collection, Expand expand, EntityCounts entityCounts) throws JSONException {
        // Check entities
        for (int i = 0; i < collection.length(); i++) {
            checkEntity(collection.getJSONObject(i), expand, entityCounts);
        }
        // todo: check orderby
        // todo: check filter
    }

    /**
     * Check the given entity from a response against the given expand.
     *
     * @param entity The entity to check.
     * @param expand The expand that led to the entity.
     * @param entityCounts The object with the expected entity counts.
     * @throws JSONException if there is a problem with the json.
     */
    public static void checkEntity(JSONObject entity, Expand expand, EntityCounts entityCounts) throws JSONException {
        EntityType entityType = expand.getEntityType();
        Query query = expand.getQuery();

        // Check properties & select
        List<String> select = new ArrayList<>(query.getSelect());
        if (select.isEmpty()) {
            select.add("id");
            select.addAll(entityType.getPropertyNames());
            if (expand.isToplevel()) {
                select.addAll(entityType.getRelations());
            }
        }
        if (select.contains("id")) {
            Assert.assertTrue(entity.has("@iot.id"), "Entity should have property @iot.id for request: '" + expand.toString() + "'");
        } else {
            Assert.assertFalse(entity.has("@iot.id"), "Entity should not have property @iot.id for request: '" + expand.toString() + "'");
        }
        for (EntityType.EntityProperty property : entityType.getProperties()) {
            if (select.contains(property.name)) {
                Assert.assertTrue(
                        entity.has(property.name) || property.optional,
                        "Entity should have property " + property.name + " for request: '" + expand.toString() + "'");
            } else {
                Assert.assertFalse(entity.has(property.name), "Entity should not have property " + property.name + " for request: '" + expand.toString() + "'");
            }
        }
        for (String relationName : entityType.getRelations()) {
            String propertyName = relationName + "@iot.navigationLink";
            if (select.contains(relationName)) {
                Assert.assertTrue(entity.has(propertyName), "Entity should have property " + propertyName + " for request: '" + expand.toString() + "'");
            } else {
                Assert.assertFalse(entity.has(propertyName), "Entity should not have property " + propertyName + " for request: '" + expand.toString() + "'");
            }
        }

        // Entity id in case we need to check counts.
        long entityId = entity.optLong("@iot.id", -1);

        // Check expand
        List<String> relations = new ArrayList<>(entityType.getRelations());
        for (Expand subExpand : query.getExpand()) {
            PathElement path = subExpand.getPath().get(0);
            String propertyName = path.getPropertyName();
            if (!entity.has(propertyName)) {
                Assert.fail("Entity should have expanded " + propertyName + " for request: '" + expand.toString() + "'");
            }

            // Check the expanded items
            if (subExpand.isCollection()) {
                checkCollection(entity.getJSONArray(propertyName), subExpand, entityCounts);
            } else {
                checkEntity(entity.getJSONObject(propertyName), subExpand, entityCounts);
            }
            relations.remove(propertyName);

            // For expanded collections, check count, top, skip
            if (subExpand.isCollection()) {
                // Check count
                Query expandQuery = subExpand.getQuery();
                Boolean count = expandQuery.getCount();
                String countProperty = propertyName + "@iot.count";
                boolean hasCountProperty = entity.has(countProperty);
                if (count != null) {
                    if (count) {
                        Assert.assertTrue(hasCountProperty, "Entity should have property " + countProperty + " for request: '" + expand.toString() + "'");
                    } else {
                        Assert.assertFalse(hasCountProperty, "Entity should not have property " + countProperty + " for request: '" + expand.toString() + "'");
                    }
                }

                long expectedCount = entityCounts.getCount(entityType, entityId, EntityType.getForRelation(propertyName));
                if (hasCountProperty && expectedCount != -1) {
                    long foundCount = entity.getLong(countProperty);
                    Assert.assertEquals(foundCount, expectedCount, "Found incorrect count for " + countProperty);
                }

                Long top = expandQuery.getTop();
                if (top != null && expectedCount != -1) {
                    int foundNumber = entity.getJSONArray(propertyName).length();
                    long skip = expandQuery.getSkip() == null ? 0 : expandQuery.getSkip();

                    long expectedNumber = Math.min(expectedCount - skip, top);
                    if (foundNumber != expectedNumber) {
                        Assert.fail("Requested " + top + " of " + expectedCount + ", expected " + expectedNumber + " with skip of " + skip + " but received " + foundNumber);
                    }

                    String nextLinkProperty = propertyName + "@iot.nextLink";
                    if (foundNumber + skip < expectedCount) {
                        // should have nextLink
                        Assert.assertTrue(entity.has(nextLinkProperty), "Entity should have " + nextLinkProperty + " for expand " + subExpand.toString());
                    } else {
                        // should not have nextLink
                        Assert.assertFalse(entity.has(nextLinkProperty), "Entity should have " + nextLinkProperty + " for expand " + subExpand.toString());
                    }

                }

            }
        }
        for (String propertyName : relations) {
            if (entity.has(propertyName)) {
                Assert.fail("Entity should not have expanded " + propertyName + " for request: '" + expand.toString() + "'");
            }
        }
    }

}
