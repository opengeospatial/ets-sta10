package org.opengis.cite.sta10.multiDatastreamExtension;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.Location;
import de.fraunhofer.iosb.ilt.sta.model.MultiDatastream;
import de.fraunhofer.iosb.ilt.sta.model.Observation;
import de.fraunhofer.iosb.ilt.sta.model.ObservedProperty;
import de.fraunhofer.iosb.ilt.sta.model.Sensor;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import de.fraunhofer.iosb.ilt.sta.model.ext.EntityList;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.geojson.Point;
import org.opengis.cite.sta10.SuiteAttribute;
import org.opengis.cite.sta10.util.EntityUtils;
import org.opengis.cite.sta10.util.HTTPMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.internal.junit.ArrayComparisonFailure;

/**
 * Some odd tests.
 *
 * @author Hylke van der Schaaf
 */
public class MultiDatastreamTests {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MultiDatastreamTests.class);
    private static String rootUri;
    private static SensorThingsService service;
    private static final List<Thing> THINGS = new ArrayList<>();
    private static final List<Location> LOCATIONS = new ArrayList<>();
    private static final List<Sensor> SENSORS = new ArrayList<>();
    private static final List<ObservedProperty> OBSERVED_PROPS = new ArrayList<>();
    private static final List<Datastream> DATASTREAMS = new ArrayList<>();
    private static final List<MultiDatastream> MULTIDATASTREAMS = new ArrayList<>();
    private static final List<Observation> OBSERVATIONS = new ArrayList<>();

    public MultiDatastreamTests() {
    }

    @BeforeClass()
    public void setUp(ITestContext testContext) {
        LOGGER.info("Setting up class.");
        ISuite suite = testContext.getSuite();
        Object obj = suite.getAttribute(SuiteAttribute.LEVEL.getName());
        if ((null != obj)) {
            Integer level = Integer.class.cast(obj);
            Assert.assertTrue(level.intValue() >= 5,
                    "Conformance level 5 will not be checked since ics = " + level);
        }

        boolean hasMultiDatastream = suite.getXmlSuite().getParameter("hasMultiDatastream") != null;
        Assert.assertTrue(hasMultiDatastream, "Conformance level 5 not checked since MultiDatastreams not listed in Service Root.");

        rootUri = suite.getAttribute(SuiteAttribute.TEST_SUBJECT.getName()).toString();
        rootUri = rootUri.trim();
        if (rootUri.lastIndexOf('/') == rootUri.length() - 1) {
            rootUri = rootUri.substring(0, rootUri.length() - 1);
        }
        URL url;
        try {
            url = new URL(rootUri);
            service = new SensorThingsService(url);
            createEntities();
        } catch (MalformedURLException | URISyntaxException ex) {
            LOGGER.error("Failed to create service uri.", ex);
        } catch (ServiceFailureException ex) {
            LOGGER.error("Failed to create entities.", ex);
        } catch (Exception ex) {
            LOGGER.error("Unknown Exception.", ex);
        }
    }

    @AfterClass
    public void tearDown() {
        LOGGER.info("tearing down class.");
        try {
            EntityUtils.deleteAll(service);
        } catch (ServiceFailureException ex) {
            LOGGER.error("Failed to clean database.", ex);
        }
    }

    /**
     * Creates some basic non-MultiDatastream entities.
     *
     * @throws ServiceFailureException
     * @throws URISyntaxException
     */
    private static void createEntities() throws ServiceFailureException, URISyntaxException {
        Location location = new Location("Location 1.0", "Location of Thing 1.", "application/vnd.geo+json", new Point(8, 51));
        service.create(location);
        LOCATIONS.add(location);

        Thing thing = new Thing("Thing 1", "The first thing.");
        thing.getLocations().add(location.withOnlyId());
        service.create(thing);
        THINGS.add(thing);

        thing = new Thing("Thing 2", "The second thing.");
        thing.getLocations().add(location.withOnlyId());
        service.create(thing);
        THINGS.add(thing);

        Sensor sensor = new Sensor("Sensor 1", "The first sensor.", "text", "Some metadata.");
        service.create(sensor);
        SENSORS.add(sensor);

        sensor = new Sensor("Sensor 2", "The second sensor.", "text", "Some metadata.");
        service.create(sensor);
        SENSORS.add(sensor);

        ObservedProperty obsProp = new ObservedProperty("ObservedProperty 1", new URI("http://ucom.org/temperature"), "The temperature of the thing.");
        service.create(obsProp);
        OBSERVED_PROPS.add(obsProp);

        obsProp = new ObservedProperty("ObservedProperty 2", new URI("http://ucom.org/humidity"), "The humidity of the thing.");
        service.create(obsProp);
        OBSERVED_PROPS.add(obsProp);

        Datastream datastream = new Datastream("Datastream 1", "The temperature of thing 1, sensor 1.", "someType", new UnitOfMeasurement("degree celcius", "°C", "ucum:T"));
        DATASTREAMS.add(datastream);
        datastream.setThing(THINGS.get(0).withOnlyId());
        datastream.setSensor(SENSORS.get(0).withOnlyId());
        datastream.setObservedProperty(OBSERVED_PROPS.get(0).withOnlyId());
        service.create(datastream);

        datastream = new Datastream("Datastream 2", "The temperature of thing 2, sensor 2.", "someType", new UnitOfMeasurement("degree celcius", "°C", "ucum:T"));
        DATASTREAMS.add(datastream);
        datastream.setThing(THINGS.get(1).withOnlyId());
        datastream.setSensor(SENSORS.get(1).withOnlyId());
        datastream.setObservedProperty(OBSERVED_PROPS.get(0).withOnlyId());
        service.create(datastream);

        createObservation(DATASTREAMS.get(0).withOnlyId(), -1);
        createObservation(DATASTREAMS.get(1).withOnlyId(), 0);
    }

    private static void createObservation(Datastream ds, double result) throws ServiceFailureException {
        Observation o = new Observation(result, ds);
        service.create(o);
        OBSERVATIONS.add(o);
    }

    private static void createObservation(MultiDatastream ds, double... result) throws ServiceFailureException {
        Observation o = new Observation(result, ds);
        service.create(o);
        OBSERVATIONS.add(o);
    }

    private void updateForException(String test, Entity entity) {
        try {
            service.update(entity);
        } catch (ServiceFailureException ex) {
            return;
        }
        Assert.fail(test + " Update did not respond with 400 Bad Request.");
    }

    private void checkResult(String test, EntityUtils.resultTestResult result) {
        Assert.assertTrue(result.testOk, test + " " + result.message);
    }

    private void checkObservedPropertiesFor(MultiDatastream md, ObservedProperty... expectedObservedProps) throws ArrayComparisonFailure, ServiceFailureException {
        ObservedProperty[] fetchedObservedProps2 = md.observedProperties().query().list().toArray(new ObservedProperty[0]);
        Assert.assertEquals(expectedObservedProps, fetchedObservedProps2, "Incorrect Observed Properties returned.");
    }

    @Test(description = "Test MultiDatastream creation.", groups = "level-5", priority = 0)
    public void testMultiDatastream() throws ServiceFailureException {
        // Create a MultiDatastream with one ObservedProperty.
        MultiDatastream md1 = new MultiDatastream();
        md1.setName("MultiDatastream 1");
        md1.setDescription("The first test MultiDatastream.");
        md1.addUnitOfMeasurement(new UnitOfMeasurement("degree celcius", "°C", "ucum:T"));

        List<String> dataTypes1 = new ArrayList<>();
        dataTypes1.add("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement");
        md1.setMultiObservationDataTypes(dataTypes1);

        md1.setThing(THINGS.get(0).withOnlyId());
        md1.setSensor(SENSORS.get(0).withOnlyId());

        EntityList<ObservedProperty> observedProperties = new EntityList<>(de.fraunhofer.iosb.ilt.sta.model.EntityType.OBSERVED_PROPERTIES);
        observedProperties.add(OBSERVED_PROPS.get(0).withOnlyId());
        md1.setObservedProperties(observedProperties);

        service.create(md1);
        MULTIDATASTREAMS.add(md1);

        // Create a MultiDatastream with two different ObservedProperties.
        MultiDatastream md2 = new MultiDatastream();
        md2.setName("MultiDatastream 2");
        md2.setDescription("The second test MultiDatastream.");
        md2.addUnitOfMeasurement(new UnitOfMeasurement("degree celcius", "°C", "ucum:T"));
        md2.addUnitOfMeasurement(new UnitOfMeasurement("percent", "%", "ucum:%"));

        List<String> dataTypes2 = new ArrayList<>();
        dataTypes2.add("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement");
        dataTypes2.add("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement");
        md2.setMultiObservationDataTypes(dataTypes2);

        md2.setThing(THINGS.get(0).withOnlyId());
        md2.setSensor(SENSORS.get(0).withOnlyId());

        EntityList<ObservedProperty> observedProperties2 = new EntityList<>(de.fraunhofer.iosb.ilt.sta.model.EntityType.OBSERVED_PROPERTIES);
        observedProperties2.add(OBSERVED_PROPS.get(0).withOnlyId());
        observedProperties2.add(OBSERVED_PROPS.get(1).withOnlyId());
        md2.setObservedProperties(observedProperties2);

        service.create(md2);
        MULTIDATASTREAMS.add(md2);

        // Create a MultiDatastream with two different ObservedProperties, in the opposite order.
        MultiDatastream md3 = new MultiDatastream();
        md3.setName("MultiDatastream 3");
        md3.setDescription("The third test MultiDatastream.");
        md3.addUnitOfMeasurement(new UnitOfMeasurement("percent", "%", "ucum:%"));
        md3.addUnitOfMeasurement(new UnitOfMeasurement("degree celcius", "°C", "ucum:T"));

        List<String> dataTypes3 = new ArrayList<>();
        dataTypes3.add("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement");
        dataTypes3.add("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement");
        md3.setMultiObservationDataTypes(dataTypes3);

        md3.setThing(THINGS.get(0).withOnlyId());
        md3.setSensor(SENSORS.get(0).withOnlyId());

        EntityList<ObservedProperty> observedProperties3 = new EntityList<>(de.fraunhofer.iosb.ilt.sta.model.EntityType.OBSERVED_PROPERTIES);
        observedProperties3.add(OBSERVED_PROPS.get(1).withOnlyId());
        observedProperties3.add(OBSERVED_PROPS.get(0).withOnlyId());
        md3.setObservedProperties(observedProperties3);

        service.create(md3);
        MULTIDATASTREAMS.add(md3);

        // Create a MultiDatastream with two of the same ObservedProperties.
        MultiDatastream md4 = new MultiDatastream();
        md4.setName("MultiDatastream 4");
        md4.setDescription("The fourth test MultiDatastream.");
        md4.addUnitOfMeasurement(new UnitOfMeasurement("degree celcius", "°C", "ucum:T"));
        md4.addUnitOfMeasurement(new UnitOfMeasurement("degree celcius", "°C", "ucum:T"));

        List<String> dataTypes4 = new ArrayList<>();
        dataTypes4.add("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement");
        dataTypes4.add("http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement");
        md4.setMultiObservationDataTypes(dataTypes4);

        md4.setThing(THINGS.get(0).withOnlyId());
        md4.setSensor(SENSORS.get(1).withOnlyId());

        EntityList<ObservedProperty> observedProperties4 = new EntityList<>(de.fraunhofer.iosb.ilt.sta.model.EntityType.OBSERVED_PROPERTIES);
        observedProperties4.add(OBSERVED_PROPS.get(0).withOnlyId());
        observedProperties4.add(OBSERVED_PROPS.get(0).withOnlyId());
        md4.setObservedProperties(observedProperties4);

        service.create(md4);
        MULTIDATASTREAMS.add(md4);
    }

    @Test(description = "Test creation of Observations in MultiDatastream.", groups = "level-5", priority = 1)
    public void testObservationInMultiDatastream() throws ServiceFailureException {
        createObservation(MULTIDATASTREAMS.get(0).withOnlyId(), 1);
        createObservation(MULTIDATASTREAMS.get(0).withOnlyId(), 2);
        createObservation(MULTIDATASTREAMS.get(0).withOnlyId(), 3);

        createObservation(MULTIDATASTREAMS.get(1).withOnlyId(), 4, 1);
        createObservation(MULTIDATASTREAMS.get(1).withOnlyId(), 5, 2);
        createObservation(MULTIDATASTREAMS.get(1).withOnlyId(), 6, 3);

        createObservation(MULTIDATASTREAMS.get(2).withOnlyId(), 7, 4);
        createObservation(MULTIDATASTREAMS.get(2).withOnlyId(), 8, 5);
        createObservation(MULTIDATASTREAMS.get(2).withOnlyId(), 9, 6);

        createObservation(MULTIDATASTREAMS.get(3).withOnlyId(), 10, 7);
        createObservation(MULTIDATASTREAMS.get(3).withOnlyId(), 11, 8);
        createObservation(MULTIDATASTREAMS.get(3).withOnlyId(), 12, 9);
    }

    @Test(description = "Test creation of incorrect Observations in MultiDatastream.", groups = "level-5", priority = 2)
    public void testObservationInMultiDatastreamIncorrect() throws ServiceFailureException {
        boolean failed = false;
        try {
            Observation o = new Observation(1, MULTIDATASTREAMS.get(1).withOnlyId());
            service.create(o);
        } catch (ServiceFailureException e) {
            failed = true;
        }
        if (!failed) {
            Assert.fail("Service should have rejected posting non-array result to a multidatastream.");
        }

        failed = false;
        try {
            createObservation(MULTIDATASTREAMS.get(0).withOnlyId(), 1, 2);
        } catch (ServiceFailureException e) {
            failed = true;
        }
        if (!failed) {
            Assert.fail("Service should have rejected posting 2 results to a multidatastream with only 1 observed property.");
        }
        failed = false;
        try {
            createObservation(MULTIDATASTREAMS.get(1).withOnlyId(), 1);
        } catch (ServiceFailureException e) {
            failed = true;
        }
        if (!failed) {
            Assert.fail("Service should have rejected posting 1 result to a multidatastream with 2 observed properties.");
        }
    }

    private JsonNode getJsonObject(String urlString) {
        Map<String, Object> responseMap = HTTPMethods.doGet(urlString);
        String response = responseMap.get("response").toString();
        int responseCode = Integer.parseInt(responseMap.get("response-code").toString());
        Assert.assertEquals(responseCode, 200, "Error getting Observations using Data Array: Code " + responseCode);

        JsonNode json;
        try {
            json = new ObjectMapper().readTree(response);
        } catch (IOException ex) {
            Assert.fail("Server returned malformed JSON for request: " + urlString, ex);
            return null;
        }
        if (!json.isObject()) {
            Assert.fail("Server did not return a JSON object for request: " + urlString);
        }
        return json;
    }

    private JsonNode getJsonValue(String urlString) {
        JsonNode json = getJsonObject(urlString);
        JsonNode value = json.get("value");
        if (value == null || !value.isArray()) {
            Assert.fail("value field is not an array for request: " + urlString);
        }
        return value;
    }

    private void entitiesHaveOneOf(JsonNode value, String EntityName, String... properties) {
        for (JsonNode valueItem : value) {
            if (!valueItem.isObject()) {
                Assert.fail("item in " + EntityName + " array is not an object.");
                return;
            }
            for (String property : properties) {
                if (valueItem.has(property)) {
                    return;
                }
            }
            Assert.fail("item in " + EntityName + " array does not contain any of " + Arrays.toString(properties));
        }
    }

    @Test(description = "Test LowLevel JSON.", groups = "level-5", priority = 2)
    public void testJson() throws ServiceFailureException {
        JsonNode json = getJsonValue(rootUri + "/Things");
        entitiesHaveOneOf(json, "Things", "MultiDatastreams@iot.navigationLink");
        json = getJsonValue(rootUri + "/Sensors");
        entitiesHaveOneOf(json, "Sensors", "MultiDatastreams@iot.navigationLink");
        json = getJsonValue(rootUri + "/ObservedProperties");
        entitiesHaveOneOf(json, "ObservedProperties", "MultiDatastreams@iot.navigationLink");
        json = getJsonValue(rootUri + "/Observations");
        entitiesHaveOneOf(json, "Observations", "MultiDatastream@iot.navigationLink", "Datastream@iot.navigationLink");
        json = getJsonValue(rootUri + "/MultiDatastreams");
        for (String property : EntityTypeMds.MULTI_DATASTREAM.getProperties()) {
            entitiesHaveOneOf(json, "MultiDatastreams", property);
        }
        for (String relation : EntityTypeMds.MULTI_DATASTREAM.getRelations()) {
            entitiesHaveOneOf(json, "MultiDatastreams", relation + "@iot.navigationLink");
        }
    }

    @Test(description = "Test if all Datastreams and MultiDatastreams are linked to Thing 1.", groups = "level-5", priority = 2)
    public void testMultiDatastreamThings() throws ServiceFailureException {
        // Check if all Datastreams and MultiDatastreams are linked to Thing 1.
        Thing fetchedThing = service.things().find(THINGS.get(0).getId());
        EntityList<Datastream> fetchedDatastreams = fetchedThing.datastreams().query().list();
        checkResult("Check Datastreams linked to Thing 1.", EntityUtils.resultContains(fetchedDatastreams, DATASTREAMS.get(0)));
        EntityList<MultiDatastream> fetchedMultiDatastreams = fetchedThing.multiDatastreams().query().list();
        checkResult("Check MultiDatastreams linked to Thing 1.", EntityUtils.resultContains(fetchedMultiDatastreams, new ArrayList<>(MULTIDATASTREAMS)));
    }

    @Test(description = "Test if all Datastreams and MultiDatastreams are linked to Sensor 1.", groups = "level-5", priority = 3)
    public void testMultiDatastreamSensors() throws ServiceFailureException {
        // Check if all Datastreams and MultiDatastreams are linked to Sensor 1.
        Sensor fetchedSensor = service.sensors().find(SENSORS.get(0).getId());
        EntityList<Datastream> fetchedDatastreams = fetchedSensor.datastreams().query().list();
        checkResult("Check Datastreams linked to Sensor 1.", EntityUtils.resultContains(fetchedDatastreams, DATASTREAMS.get(0)));
        EntityList<MultiDatastream> fetchedMultiDatastreams = fetchedSensor.multiDatastreams().query().list();
        checkResult(
                "Check MultiDatastreams linked to Sensor 1.",
                EntityUtils.resultContains(fetchedMultiDatastreams, getFromList(MULTIDATASTREAMS, 0, 1, 2)));
    }

    @Test(description = "Test if all Datastreams and MultiDatastreams are linked to ObservedProperty 1.", groups = "level-5", priority = 4)
    public void testMultiDatastreamObservedProperties1() throws ServiceFailureException {
        // Check if all Datastreams and MultiDatastreams are linked to ObservedProperty 1.
        ObservedProperty fetchedObservedProp = service.observedProperties().find(OBSERVED_PROPS.get(0).getId());
        EntityList<Datastream> fetchedDatastreams = fetchedObservedProp.datastreams().query().list();
        checkResult(
                "Check Datastreams linked to ObservedProperty 1.",
                EntityUtils.resultContains(fetchedDatastreams, getFromList(DATASTREAMS, 0, 1)));
        EntityList<MultiDatastream> fetchedMultiDatastreams = fetchedObservedProp.multiDatastreams().query().list();
        checkResult(
                "Check MultiDatastreams linked to ObservedProperty 1.",
                EntityUtils.resultContains(fetchedMultiDatastreams, new ArrayList<>(MULTIDATASTREAMS)));
    }

    @Test(description = "Test if MultiDatastreams 2 and 3 are linked to ObservedProperty 2.", groups = "level-5", priority = 5)
    public void testMultiDatastreamObservedProperties2() throws ServiceFailureException {
        // Check if MultiDatastreams 2 and 3 are linked to ObservedProperty 2.
        ObservedProperty fetchedObservedProp = service.observedProperties().find(OBSERVED_PROPS.get(1).getId());
        EntityList<Datastream> fetchedDatastreams = fetchedObservedProp.datastreams().query().list();
        checkResult(
                "Check Datastreams linked to ObservedProperty 2.",
                EntityUtils.resultContains(fetchedDatastreams, new ArrayList<>()));
        EntityList<MultiDatastream> fetchedMultiDatastreams = fetchedObservedProp.multiDatastreams().query().list();
        checkResult(
                "Check MultiDatastreams linked to ObservedProperty 2.",
                EntityUtils.resultContains(fetchedMultiDatastreams, getFromList(MULTIDATASTREAMS, 1, 2)));
    }

    @Test(description = "First Observation should have a Datastream but not a MultiDatasteam.", groups = "level-5", priority = 6)
    public void testObservationLinks1() throws ServiceFailureException {
        // First Observation should have a Datastream but not a MultiDatasteam.
        Observation fetchedObservation = service.observations().find(OBSERVATIONS.get(0).getId());
        Datastream fetchedDatastream = fetchedObservation.getDatastream();
        Assert.assertEquals(fetchedDatastream, DATASTREAMS.get(0), "Observation has wrong or no Datastream");
        MultiDatastream fetchedMultiDatastream = fetchedObservation.getMultiDatastream();
        Assert.assertEquals(fetchedMultiDatastream, null, "Observation should not have a MultiDatastream");
    }

    @Test(description = "Second Observation should not have a Datastream but a MultiDatasteam.", groups = "level-5", priority = 7)
    public void testObservationLinks2() throws ServiceFailureException {
        // Second Observation should not have a Datastream but a MultiDatasteam.
        Observation fetchedObservation = service.observations().find(OBSERVATIONS.get(2).getId());
        Datastream fetchedDatastream = fetchedObservation.getDatastream();
        Assert.assertEquals(fetchedDatastream, null, "Observation should not have a Datastream");
        MultiDatastream fetchedMultiDatastream = fetchedObservation.getMultiDatastream();
        Assert.assertEquals(fetchedMultiDatastream, MULTIDATASTREAMS.get(0), "Observation has wrong or no MultiDatastream");
    }

    @Test(description = "Test if the MultiDatastreams have the correct ObservedProperties in the correct order.", groups = "level-5", priority = 8)
    public void testObservedPropertyOrder() throws ServiceFailureException {
        // Check if the MultiDatastreams have the correct ObservedProperties in the correct order.
        checkObservedPropertiesFor(MULTIDATASTREAMS.get(0), OBSERVED_PROPS.get(0));
        checkObservedPropertiesFor(MULTIDATASTREAMS.get(1), OBSERVED_PROPS.get(0), OBSERVED_PROPS.get(1));
        checkObservedPropertiesFor(MULTIDATASTREAMS.get(2), OBSERVED_PROPS.get(1), OBSERVED_PROPS.get(0));
        checkObservedPropertiesFor(MULTIDATASTREAMS.get(3), OBSERVED_PROPS.get(0), OBSERVED_PROPS.get(0));
    }

    @Test(description = "Try to give Observation 1 a MultiDatastream without removing the Datastream. Should give an error.", groups = "level-5", priority = 9)
    public void testIncorrectObservation() throws ServiceFailureException {
        // Try to give Observation 1 a MultiDatastream without removing the Datastream. Should give an error.
        Observation modifiedObservation = OBSERVATIONS.get(0).withOnlyId();
        modifiedObservation.setMultiDatastream(MULTIDATASTREAMS.get(0).withOnlyId());
        updateForException("Linking Observation to Datastream AND MultiDatastream.", modifiedObservation);
    }

    @Test(description = "Try to add a MultiDatastream to an ObservedProperty. Should give an error.", groups = "level-5", priority = 10)
    public void testIncorrectObservedProperty() throws ServiceFailureException {
        // Try to add a MultiDatastream to an ObservedProperty. Should give an error.
        ObservedProperty modifiedObservedProp = OBSERVED_PROPS.get(1).withOnlyId();
        modifiedObservedProp.getMultiDatastreams().add(MULTIDATASTREAMS.get(0).withOnlyId());
        updateForException("Linking MultiDatastream to Observed property.", modifiedObservedProp);
    }

    @Test(description = "Check MultiDatastream(x)/Observations works.", groups = "level-5", priority = 11)
    public void testFetchObservationsByMultiDatastream() throws ServiceFailureException {
        EntityList<Observation> observations = MULTIDATASTREAMS.get(0).observations().query().list();
        checkResult(
                "Looking for all observations",
                EntityUtils.resultContains(observations, getFromList(OBSERVATIONS, 2, 3, 4)));

        observations = MULTIDATASTREAMS.get(1).observations().query().list();
        checkResult(
                "Looking for all observations",
                EntityUtils.resultContains(observations, getFromList(OBSERVATIONS, 5, 6, 7)));

        observations = MULTIDATASTREAMS.get(2).observations().query().list();
        checkResult(
                "Looking for all observations",
                EntityUtils.resultContains(observations, getFromList(OBSERVATIONS, 8, 9, 10)));

        observations = MULTIDATASTREAMS.get(3).observations().query().list();
        checkResult(
                "Looking for all observations",
                EntityUtils.resultContains(observations, getFromList(OBSERVATIONS, 11, 12, 13)));
    }

    @Test(description = "Check if all observations are there.", groups = "level-5", priority = 11)
    public void testObservations() throws ServiceFailureException {
        // Check if all observations are there.
        EntityList<Observation> fetchedObservations = service.observations().query().list();
        checkResult(
                "Looking for all observations",
                EntityUtils.resultContains(fetchedObservations, new ArrayList<>(OBSERVATIONS)));
    }

    @Test(description = "Deleting ObservedProperty 2 should delete MultiDatastream 2 and 3 and their Observations.", groups = "level-5", priority = 12)
    public void testDeleteObservedProperty() throws ServiceFailureException {
        // Deleting ObservedProperty 2 should delete MultiDatastream 2 and 3 and their Observations.
        service.delete(OBSERVED_PROPS.get(1));
        EntityList<MultiDatastream> fetchedMultiDatastreams = service.multiDatastreams().query().list();
        checkResult(
                "Checking if MultiDatastreams are automatically deleted.",
                EntityUtils.resultContains(fetchedMultiDatastreams, getFromList(MULTIDATASTREAMS, 0, 3)));
        EntityList<Observation> fetchedObservations = service.observations().query().list();
        checkResult(
                "Checking if Observations are automatically deleted.",
                EntityUtils.resultContains(fetchedObservations, getFromList(OBSERVATIONS, 0, 1, 2, 3, 4, 11, 12, 13)));
    }

    @Test(description = "Deleting Sensor 2 should delete MultiDatastream 4", groups = "level-5", priority = 13)
    public void testDeleteSensor() throws ServiceFailureException {
        // Deleting Sensor 2 should delete MultiDatastream 4
        service.delete(SENSORS.get(1));
        EntityList<MultiDatastream> fetchedMultiDatastreams = service.multiDatastreams().query().list();
        checkResult(
                "Checking if MultiDatastreams are automatically deleted.",
                EntityUtils.resultContains(fetchedMultiDatastreams, getFromList(MULTIDATASTREAMS, 0)));
    }

    @Test(description = "Deleting Thing 1 should delete the last MultiDatastream.", groups = "level-5", priority = 14)
    public void testDeleteThing() throws ServiceFailureException {
        // Deleting Thing 1 should delete the last MultiDatastream.
        service.delete(THINGS.get(0));
        EntityList<MultiDatastream> fetchedMultiDatastreams = service.multiDatastreams().query().list();
        checkResult(
                "Checking if MultiDatastreams are automatically deleted.",
                EntityUtils.resultContains(fetchedMultiDatastreams, new ArrayList<>()));
    }

    public static <T extends Entity<T>> List<T> getFromList(List<T> list, int... ids) {
        List<T> result = new ArrayList<>();
        for (int i : ids) {
            result.add(list.get(i));
        }
        return result;
    }

}
