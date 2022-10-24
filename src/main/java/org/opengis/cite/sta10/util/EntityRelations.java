package org.opengis.cite.sta10.util;

/**
 * List of the entity relations for each entity type.
 */
public class EntityRelations {

    /**
     * List of entity relations for Thing entity.
     */
    public static final String[] THING_RELATIONS = {"Datastreams", "Locations", "HistoricalLocations"};
    /**
     * List of entity relations for Location entity.
     */
    public static final String[] LOCATION_RELATIONS = {"Things", "HistoricalLocations"};
    /**
     * List of entity relations for HistoricalLocation entity.
     */
    public static final String[] HISTORICAL_LOCATION_RELATIONS = {"Thing", "Locations"};
    /**
     * List of entity relations for Datastream entity.
     */
    public static final String[] DATASTREAM_RELATIONS = {"Thing", "Sensor", "ObservedProperty", "Observations"};
    /**
     * List of entity relations for Sensor entity.
     */
    public static final String[] SENSOR_RELATIONS = {"Datastreams"};
    /**
     * List of entity relations for Observation entity.
     */
    public static final String[] OBSERVATION_RELATIONS = {"Datastream", "FeatureOfInterest"};
    /**
     * List of entity relations for ObservedProperty entity.
     */
    public static final String[] OBSERVED_PROPERTY_RELATIONS = {"Datastreams"};
    /**
     * List of entity relations for FeatureOfInterest entity.
     */
    public static final String[] FEATURE_OF_INTEREST_RELATIONS = {"Observations"};

    public static EntityType getEntityTypeOfRelation(String relation) {
        switch (relation.toLowerCase()) {
            case "datastream":
            case "datastreams":
                return EntityType.DATASTREAM;
            case "location":
            case "locations":
                return EntityType.LOCATION;
            case "historicallocations":
                return EntityType.HISTORICAL_LOCATION;
            case "thing":
            case "things":
                return EntityType.THING;
            case "sensor":
                return EntityType.SENSOR;
            case "observedproperty":
                return EntityType.OBSERVED_PROPERTY;
            case "observations":
                return EntityType.OBSERVATION;
            case "featureofinterest":
                return EntityType.FEATURE_OF_INTEREST;
        }
        throw new IllegalArgumentException("Unknown relation.");
    }

    public static String getRootEntitySet(EntityType entityType) {
        switch (entityType) {
            case THING:
                return "Things";
            case LOCATION:
                return "Locations";
            case FEATURE_OF_INTEREST:
                return "FeaturesOfInterest";
            case OBSERVED_PROPERTY:
                return "ObservedProperties";
            case HISTORICAL_LOCATION:
                return "HistoricalLocations";
            case SENSOR:
                return "Sensors";
            case DATASTREAM:
                return "Datastreams";
            case OBSERVATION:
                return "Observations";
            default:
                break;
        }
        return null;
    }

    /**
     * Returning the list of entity relations for the given entityType.
     *
     * @param entityType The type of entity from EntityType enum
     * @return List of all entity relations for the given entityType
     */
    public static String[] getRelationsListFor(EntityType entityType) {
        switch (entityType) {
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

    /**
     * Returning the list of entity relations for the given entity name.
     *
     * @param name The type of entity in String format
     * @return List of all entity relations for the given entity
     */
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
