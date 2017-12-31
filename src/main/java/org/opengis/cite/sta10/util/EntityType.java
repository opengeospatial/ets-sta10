package org.opengis.cite.sta10.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * List of entity types in SensorThings API.
 */
public enum EntityType {
    THING("Thing", "Things"),
    LOCATION("Location", "Locations"),
    SENSOR("Sensor", "Sensors"),
    OBSERVED_PROPERTY("ObservedProperty", "ObservedProperties"),
    OBSERVATION("Observation", "Observations"),
    DATASTREAM("Datastream", "Datastreams"),
    FEATURE_OF_INTEREST("FeatureOfInterest", "FeaturesOfInterest"),
    HISTORICAL_LOCATION("HistoricalLocation", "HistoricalLocations");

    /**
     * The class representing an EntityProperty.
     */
    public static class EntityProperty {

        public final String name;
        public final boolean optional;
        public final boolean canSort;
        public final String jsonType;

        public EntityProperty(String name, boolean optional, boolean canSort, String jsonType) {
            this.name = name;
            this.optional = optional;
            this.canSort = canSort;
            this.jsonType = jsonType;
        }

        @Override
        public String toString() {
            return name;
        }
    }
    /**
     * The singular name of the entity type.
     */
    public final String singular;
    /**
     * The plural (collection) name of the entity type.
     */
    public final String plural;
    private final List<EntityProperty> properties = new ArrayList<>();
    private final Map<String, EntityProperty> propertiesByName = new HashMap<>();
    private final List<String> relations = new ArrayList<>();

    private static final Map<String, EntityType> NAMES_MAP = new HashMap<>();
    private static final Set<String> NAMES_PLURAL = new HashSet<>();

    static {
        // TODO: Add properties, fix test that break
        THING.addProperty("name", false, true);
        THING.addProperty("description", false, true);
        THING.addProperty("properties", true, false, "object");
        THING.addRelations(DATASTREAM.plural, HISTORICAL_LOCATION.plural, LOCATION.plural);

        LOCATION.addProperty("name", false, true);
        LOCATION.addProperty("description", false, true);
        LOCATION.addProperty("encodingType", false, true);
        LOCATION.addProperty("location", false, false, "object");
        LOCATION.addRelations(HISTORICAL_LOCATION.plural, THING.plural);

        SENSOR.addProperty("name", false, true);
        SENSOR.addProperty("description", false, true);
        SENSOR.addProperty("encodingType", false, true);
        SENSOR.addProperty("metadata", false, true);
        SENSOR.addRelations(DATASTREAM.plural);

        OBSERVED_PROPERTY.addProperty("name", false, true);
        OBSERVED_PROPERTY.addProperty("definition", false, true);
        OBSERVED_PROPERTY.addProperty("description", false, true);
        OBSERVED_PROPERTY.addRelations(DATASTREAM.plural);

        OBSERVATION.addProperty("phenomenonTime", false, true);
        OBSERVATION.addProperty("result", false, true, "any");
        OBSERVATION.addProperty("resultTime", false, true);
        OBSERVATION.addProperty("resultQuality", true, true);
        OBSERVATION.addProperty("validTime", true, true);
        OBSERVATION.addProperty("parameters", true, true, "object");
        OBSERVATION.addRelations(DATASTREAM.singular, FEATURE_OF_INTEREST.singular);

        DATASTREAM.addProperty("name", false, true);
        DATASTREAM.addProperty("description", false, true);
        DATASTREAM.addProperty("unitOfMeasurement", false, false, "object");
        DATASTREAM.addProperty("observationType", false, true);
        DATASTREAM.addProperty("observedArea", true, false, "object");
        DATASTREAM.addProperty("phenomenonTime", true, true);
        DATASTREAM.addProperty("resultTime", true, true);
        DATASTREAM.addRelations(THING.singular, SENSOR.singular, OBSERVED_PROPERTY.singular, OBSERVATION.plural);

        FEATURE_OF_INTEREST.addProperty("name", false, true);
        FEATURE_OF_INTEREST.addProperty("description", false, true);
        FEATURE_OF_INTEREST.addProperty("encodingType", false, true);
        FEATURE_OF_INTEREST.addProperty("feature", false, false, "object");
        FEATURE_OF_INTEREST.addRelations(OBSERVATION.plural);

        HISTORICAL_LOCATION.addProperty("time", false, true);
        HISTORICAL_LOCATION.addRelations(THING.singular, LOCATION.plural);

        for (EntityType entityType : EntityType.values()) {
            NAMES_MAP.put(entityType.singular, entityType);
            NAMES_MAP.put(entityType.plural, entityType);
            NAMES_PLURAL.add(entityType.plural);
        }
    }

    public static EntityType getForRelation(String relation) {
        EntityType entityType = NAMES_MAP.get(relation);
        if (entityType == null) {
            throw new IllegalArgumentException("Unknown relation: " + relation);
        }
        return entityType;
    }

    public static boolean isPlural(String relation) {
        return NAMES_PLURAL.contains(relation);
    }

    private EntityType(String singular, String plural) {
        this.singular = singular;
        this.plural = plural;
    }

    public String getRootEntitySet() {
        return plural;
    }

    public List<String> getRelations() {
        return Collections.unmodifiableList(relations);
    }

    public List<EntityProperty> getProperties() {
        return Collections.unmodifiableList(properties);
    }

    public Set<String> getPropertyNames() {
        return propertiesByName.keySet();
    }

    public EntityProperty getPropertyForName(String property) {
        return propertiesByName.get(property);
    }

    /**
     * Clears and then fills the target list with either the odd or even
     * properties and relations. Always returns "id".
     *
     * @param target the list to fill.
     * @param even if true, the even properties are taken, otherwise the odd.
     */
    public void getHalfPropertiesRelations(List<String> target, final boolean even) {
        target.clear();
        target.add("id");
        boolean isEven = true;
        for (EntityProperty property : properties) {
            if (even == isEven) {
                target.add(property.name);
            }
            isEven = !isEven;
        }
        for (String relation : relations) {
            if (even == isEven) {
                target.add(relation);
            }
            isEven = !isEven;
        }
    }

    private void addProperty(String name, boolean optional, boolean canSort) {
        EntityProperty property = new EntityProperty(name, optional, canSort, "string");
        properties.add(property);
        propertiesByName.put(name, property);
    }

    private void addProperty(String name, boolean optional, boolean canSort, String jsonType) {
        EntityProperty property = new EntityProperty(name, optional, canSort, jsonType);
        properties.add(property);
        propertiesByName.put(name, property);
    }

    private void addRelations(String... relations) {
        this.relations.addAll(Arrays.asList(relations));
    }

}
