package org.opengis.cite.sta10.util;

import java.util.List;
import org.testng.Assert;

/**
 * Utility class that helps preparing the URL string for the targeted entity.
 */
public class ServiceURLBuilder {

    /**
     * Build the URL String based on the entityType, parent EntityType and id, and the targeted property or query
     *
     * @param rootURI            The base URL of the SensorThings Service
     * @param parentEntityType   The entity type of the parent entity
     * @param parentId           The parent entity id
     * @param relationEntityType The entity type of the targeted entity
     * @param property           The targeted property or the query string
     * @return The URL String created based on the input parameters
     */
    public static String buildURLString(String rootURI, EntityType parentEntityType, Object parentId, EntityType relationEntityType, String property) {
        String urlString = rootURI;
        if (relationEntityType == null) {
            switch (parentEntityType) {
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
                    Assert.fail("Entity type is not recognized in SensorThings API : " + parentEntityType);
                    return null;
            }
            if (parentId != null) {
                urlString += "(" + Utils.quoteIdForUrl(parentId) + ")";
            }
        } else {
            switch (parentEntityType) {
                case THING:
                    urlString += "/Things(" + Utils.quoteIdForUrl(parentId) + ")";
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
                            Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType + " and " + relationEntityType);
                    }
                    break;
                case LOCATION:
                    urlString += "/Locations(" + Utils.quoteIdForUrl(parentId) + ")";
                    switch (relationEntityType) {
                        case THING:
                            urlString += "/Things";
                            break;
                        case HISTORICAL_LOCATION:
                            urlString += "/HistoricalLocations";
                            break;
                        default:
                            Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType + " and " + relationEntityType);
                    }
                    break;
                case HISTORICAL_LOCATION:
                    urlString += "/HistoricalLocations(" + Utils.quoteIdForUrl(parentId) + ")";
                    switch (relationEntityType) {
                        case THING:
                            urlString += "/Thing";
                            break;
                        case LOCATION:
                            urlString += "/Locations";
                            break;
                        default:
                            Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType + " and " + relationEntityType);
                    }
                    break;
                case DATASTREAM:
                    urlString += "/Datastreams(" + Utils.quoteIdForUrl(parentId) + ")";
                    switch (relationEntityType) {
                        case THING:
                            urlString += "/Thing";
                            break;
                        case SENSOR:
                            urlString += "/Sensor";
                            break;
                        case OBSERVATION:
                            urlString += "/Observations";
                            break;
                        case OBSERVED_PROPERTY:
                            urlString += "/ObservedProperty";
                            break;
                        default:
                            Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType + " and " + relationEntityType);
                    }
                    break;
                case SENSOR:
                    urlString += "/Sensors(" + Utils.quoteIdForUrl(parentId) + ")";
                    switch (relationEntityType) {
                        case DATASTREAM:
                            urlString += "/Datastreams";
                            break;
                        default:
                            Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType + " and " + relationEntityType);
                    }
                    break;
                case OBSERVATION:
                    urlString += "/Observations(" + Utils.quoteIdForUrl(parentId) + ")";
                    switch (relationEntityType) {
                        case THING:
                        case DATASTREAM:
                            urlString += "/Datastream";
                            break;
                        case FEATURE_OF_INTEREST:
                            urlString += "/FeatureOfInterest";
                            break;
                        default:
                            Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType + " and " + relationEntityType);
                    }
                    break;
                case OBSERVED_PROPERTY:
                    urlString += "/ObservedProperties(" + Utils.quoteIdForUrl(parentId) + ")";
                    switch (relationEntityType) {
                        case DATASTREAM:
                            urlString += "/Datastreams";
                            break;
                        default:
                            Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType + " and " + relationEntityType);
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    urlString += "/FeaturesOfInterest(" + Utils.quoteIdForUrl(parentId) + ")";
                    switch (relationEntityType) {
                        case OBSERVATION:
                            urlString += "/Observations";
                            break;
                        default:
                            Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType + " and " + relationEntityType);
                    }
                    break;
                default:
                    Assert.fail("Entity type is not recognized in SensorThings API : " + parentEntityType);
            }
        }
        if (property != null) {
            if (property.indexOf('?') >= 0) {
                urlString += property;
            } else {
                urlString += "/" + property;
            }
        }
        return urlString;
    }

    /**
     * Build the URL String based on a chain of entity type hierarchy and ids, and the targeted property or query
     *
     * @param rootURI     The base URL of the SensorThings Service
     * @param entityTypes The list of entity type chain
     * @param ids         The ids for the entity type chain
     * @param property    The targeted property or the query string
     * @return The URL String created based on the input parameters
     */
    public static String buildURLString(String rootURI, List<String> entityTypes, List<Object> ids, String property) {
        String urlString = rootURI;
        if (entityTypes.size() != ids.size() && entityTypes.size() != ids.size() + 1) {
            Assert.fail("There is problem with the path of entities!!!");
        }
        if (urlString.charAt(urlString.length() - 1) != '/') {
            urlString += '/';
        }
        for (int i = 0; i < entityTypes.size(); i++) {
            urlString += entityTypes.get(i);
            if (i < ids.size()) {
                Object id = ids.get(i);
                if (id == null) {
                    urlString += "/";
                } else {
                    urlString += "(" + Utils.quoteIdForUrl(id) + ")/";
                }
            }
        }
        if (urlString.charAt(urlString.length() - 1) == '/') {
            urlString = urlString.substring(0, urlString.length() - 1);
        }
        if (property != null) {
            if (property.indexOf('?') >= 0) {
                urlString += property;
            } else {
                urlString += "/" + property;
            }
        }
        return urlString;
    }
}
