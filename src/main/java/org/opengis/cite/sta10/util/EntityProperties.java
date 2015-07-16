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
    public static final String[] OBSERVATION_PROPERTIES= {"phenomenonTime","result"/*,"resultTime"*/ };
    public static final String[] FEATURE_OF_INTEREST_PROPERTIES = {"description", "encodingType", "feature" };

}
