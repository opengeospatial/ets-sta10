package org.opengis.cite.sta10.util;

/**
 * Created by tania on 15/07/15.
 */
public class EntityRelations {
    public static final String[] THING_RELATIONS = {"Datastreams", "Locations", "HistoricalLocations"};
    public static final String[] LOCATION_RELATIONS = {"Things", "HistoricalLocations"};
    public static final String[] HISTORICAL_LOCATION_RELATIONS = {"Thing", "Locations"};
    public static final String[] DATASTREAM_RELATIONS = {"Thing", "Sensor", "ObservedProperty", "Observations"};
    public static final String[] SENSOR_RELATIONS = {"Datastreams"};
    public static final String[] OBSERVATION_RELATIONS = {"Datastream", "FeatureOfInterest"};
    public static final String[] OBSERVED_PROPERTY_RELATIONS = {"Datastreams"};
    public static final String[] FEATURE_OF_INTEREST_RELATIONS = {"Observations"};

    public static String[] getRelationsListFor(EntityType entityType) {
        switch (entityType)
        {
            case THING:
                return THING_RELATIONS;
            case LOCATION:
                return LOCATION_RELATIONS;
            case FEATURE_OF_INTEREST:
                return FEATURE_OF_INTEREST_RELATIONS;
            case OBSERVED_PROPERTY:
                return OBSERVED_PROPERTY_RELATIONS;
            case HISTORICAL_LOCATION:
                return HISTORICAL_LOCATION_RELATIONS;
            case SENSOR:
                return SENSOR_RELATIONS;
            case DATASTREAM:
                return DATASTREAM_RELATIONS;
            case OBSERVATION:
                return OBSERVATION_RELATIONS;
            default:
                break;
        }
        return null;
    }

    public static String[] getRelationsListFor(String name) {
        switch (name.toLowerCase()) {
            case "thing":
            case "things":
                return THING_RELATIONS;
            case "location":
            case "locations":
                return LOCATION_RELATIONS;
            case "historicallocation":
            case "historicallocations":
                return HISTORICAL_LOCATION_RELATIONS;
            case "datastream":
            case "datastreams":
                return DATASTREAM_RELATIONS;
            case "sensor":
            case "sensors":
                return SENSOR_RELATIONS;
            case "observedproperty":
            case "observedproperties":
                return OBSERVED_PROPERTY_RELATIONS;
            case "observation":
            case "observations":
                return OBSERVATION_RELATIONS;
            case "featureofinterest":
            case "featuresofinterest":
                return FEATURE_OF_INTEREST_RELATIONS;
        }
        return null;
    }

}
