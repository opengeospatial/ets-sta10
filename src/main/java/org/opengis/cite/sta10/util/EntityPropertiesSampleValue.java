package org.opengis.cite.sta10.util;

/**
 * Created by tania on 15/07/15.
 */
public class EntityPropertiesSampleValue {
    public static final String[] THING_PROPERTIES_Values = {"'thing 1'"};
    public static final String[] LOCATION_PROPERTIES_Values = {"'location 2'", "'http://example.org/location_types/GeoJSON'", "location" };
    public static final String[] HISTORICAL_LOCATION_PROPERTIES_Values = {"'2015-10-14T21:30:00.104Z'"};
    public static final String[] DATASTREAM_PROPERTIES_Values = {"'datastream 1'", "unitOfMeasurement", "'http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement'"};
    public static final String[] SENSOR_PROPERTIES_Values = {"'sensor 1'", "'http://schema.org/description'", "'Light flux sensor'"};
    public static final String[] OBSERVED_PROPETY_PROPERTIES_Values = { "'Luminous Flux'", "'http://www.qudt.org/qudt/owl/1.0.0/quantity/Instances.html/LuminousFlux'", "'observedProperty 1'"};
    public static final String[] OBSERVATION_PROPERTIES_Values = {"'2015-03-02T00:00:00.000Z'","'2'","'2015-03-02T00:00:00.000Z'"};
    public static final String[] FEATURE_OF_INTEREST_PROPERTIES_Values = {"'Generated using location details: location 1'", "'http://example.org/location_types/GeoJSON'", "feature" };


    public static String getPropertyValueFor(EntityType entityType, int index) {
        switch (entityType)

        {
            case THING:
                return THING_PROPERTIES_Values[index];
            case LOCATION:
                return LOCATION_PROPERTIES_Values[index];
            case FEATURE_OF_INTEREST:
                return FEATURE_OF_INTEREST_PROPERTIES_Values[index];
            case OBSERVED_PROPERTY:
                return OBSERVED_PROPETY_PROPERTIES_Values[index];
            case HISTORICAL_LOCATION:
                return HISTORICAL_LOCATION_PROPERTIES_Values[index];
            case SENSOR:
                return SENSOR_PROPERTIES_Values[index];
            case DATASTREAM:
                return DATASTREAM_PROPERTIES_Values[index];
            case OBSERVATION:
                return OBSERVATION_PROPERTIES_Values[index];
            default:
                break;
        }
        return null;
    }
}
