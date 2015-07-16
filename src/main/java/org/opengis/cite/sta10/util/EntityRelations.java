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


}
