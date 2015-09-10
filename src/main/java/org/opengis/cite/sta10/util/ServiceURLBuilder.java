package org.opengis.cite.sta10.util;

import org.testng.Assert;

public class ServiceURLBuilder {
    public static String buildURLString(String rootURI, EntityType parentEntityType, long parentId, EntityType relationEntityType, String property){
        String urlString = rootURI;
        if(relationEntityType == null){
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
            if(parentId !=-1){
                urlString += "("+parentId+")";
            }
        } else{
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
                            Assert.fail("Entity type relation is not recognized in SensorThings API : " + parentEntityType + " and " + relationEntityType);
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
                            break;
                        case FEATURE_OF_INTEREST:
                            urlString += "/FeatureOfInterest";
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
        }
        if(property!=null){
            urlString +="/"+ property;
        }
        return urlString;
    }
}
