package org.opengis.cite.sta10.util;

/**
 * Mandatory properties of each entity.
 */
public class EntityProperties {

    /**
     * List of Mandatory properties for Thing entity.
     */
    public static final String[] THING_PROPERTIES = {"name", "description"};
    /**
     * List of Mandatory properties for Location entity.
     */
    public static final String[] LOCATION_PROPERTIES = {"name", "description", "encodingType", "location"};
    /**
     * List of Mandatory properties for HistoricalLocation entity.
     */
    public static final String[] HISTORICAL_LOCATION_PROPERTIES = {"time"};
    /**
     * List of Mandatory properties for Datastream entity.
     */
    public static final String[] DATASTREAM_PROPERTIES = {"name", "description", "unitOfMeasurement", "observationType"};
    /**
     * List of Mandatory properties for Sensor entity.
     */
    public static final String[] SENSOR_PROPERTIES = {"name", "description", "encodingType", "metadata"};
    /**
     * List of Mandatory properties for ObservedProperty entity.
     */
    public static final String[] OBSERVED_PROPETY_PROPERTIES = {"name", "definition", "description"};
    /**
     * List of Mandatory properties for Observation entity.
     */
    public static final String[] OBSERVATION_PROPERTIES = {"phenomenonTime", "result", "resultTime"};
    /**
     * List of Mandatory properties for FeatureOfInterest entity.
     */
    public static final String[] FEATURE_OF_INTEREST_PROPERTIES = {"name", "description", "encodingType", "feature"};

    /**
     * Returning the list of mandatory properties for the given entity name.
     *
     * @param name The type of entity in String format
     * @return List of all mandatory properties for the given entity
     */
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

    /**
     * Returning the list of mandatory properties for the given entityType.
     *
     * @param entityType The type of entity from EntityType enum
     * @return List of all mandatory properties for the given entityType
     */
    public static String[] getPropertiesListFor(EntityType entityType) {
        switch (entityType) {
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
