package org.opengis.cite.sta10.util;

/**
 * Created by tania on 15/07/15.
 */
public class EntityProperties {
    public static final String[] THING_PROPERTIES = {"description"};
    public static final String[] LOCATION_PROPERTIES = {"description", "encodingType", "location" };
    public static final String[] HISTORICAL_LOCATION_PROPERTIES = {"time"};
    public static final String[] DATASTREAM_PROPERTIES = {"description", "unitOfMeasurement", "observationType"};
    public static final String[] SENSOR_PROPERTIES= {"description", "encodingType", "metadata"};
    public static final String[] OBSERVED_PROPETY_PROPERTIES= { "name", "definition", "description"};
    public static final String[] OBSERVATION_PROPERTIES= {"phenomenonTime","result","resultTime"};
    public static final String[] FEATURE_OF_INTEREST_PROPERTIES = {"description", "encodingType", "feature" };

    public static String[] getPropertiesListFor(String name) {
        switch (name.toLowerCase()) {
            case "thing":
            case "things":
                return THING_PROPERTIES;
            case "location":
            case "locations":
                return LOCATION_PROPERTIES;
            case "historicallocation":
            case "historicallocations":
                return HISTORICAL_LOCATION_PROPERTIES;
            case "datastream":
            case "datastreams":
                return DATASTREAM_PROPERTIES;
            case "sensor":
            case "sensors":
                return SENSOR_PROPERTIES;
            case "observedproperty":
            case "observedproperties":
                return OBSERVED_PROPETY_PROPERTIES;
            case "observation":
            case "observations":
                return OBSERVATION_PROPERTIES;
            case "featureofinterest":
            case "featuresofinterest":
                return FEATURE_OF_INTEREST_PROPERTIES;
        }
        return null;
    }

    public static String[] getPropertiesListFor(EntityType entityType) {
        switch (entityType)

        {
            case THING:
                return THING_PROPERTIES;
            case LOCATION:
                return LOCATION_PROPERTIES;
            case FEATURE_OF_INTEREST:
                return FEATURE_OF_INTEREST_PROPERTIES;
            case OBSERVED_PROPERTY:
                return OBSERVED_PROPETY_PROPERTIES;
            case HISTORICAL_LOCATION:
                return HISTORICAL_LOCATION_PROPERTIES;
            case SENSOR:
                return SENSOR_PROPERTIES;
            case DATASTREAM:
                return DATASTREAM_PROPERTIES;
            case OBSERVATION:
                return OBSERVATION_PROPERTIES;
            default:
                break;
        }
        return null;
    }
}
