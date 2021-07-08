package org.opengis.cite.sta10.createUpdateDelete;

import de.fraunhofer.iosb.ilt.sta.ServiceFailureException;
import de.fraunhofer.iosb.ilt.sta.dao.ObservationDao;
import de.fraunhofer.iosb.ilt.sta.model.Datastream;
import de.fraunhofer.iosb.ilt.sta.model.Entity;
import de.fraunhofer.iosb.ilt.sta.model.Location;
import de.fraunhofer.iosb.ilt.sta.model.Observation;
import de.fraunhofer.iosb.ilt.sta.model.ObservedProperty;
import de.fraunhofer.iosb.ilt.sta.model.Sensor;
import de.fraunhofer.iosb.ilt.sta.model.Thing;
import de.fraunhofer.iosb.ilt.sta.model.ext.UnitOfMeasurement;
import de.fraunhofer.iosb.ilt.sta.service.SensorThingsService;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geojson.Point;
import org.opengis.cite.sta10.SuiteAttribute;
import org.opengis.cite.sta10.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * Tests date and time functions.
 *
 * @author Hylke van der Schaaf
 */
public class ResultTypesTests {

    /**
     * The logger for this class.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ResultTypesTests.class);
    private String rootUri;
    private SensorThingsService service;
    private final List<Thing> things = new ArrayList<>();
    private final List<Datastream> datastreams = new ArrayList<>();
    private final List<Observation> observations = new ArrayList<>();

    public ResultTypesTests() {
    }

    @BeforeClass()
    public void setUp(ITestContext testContext) {
        LOGGER.info("Setting up class.");
        Object obj = testContext.getSuite().getAttribute(
                SuiteAttribute.LEVEL.getName());
        if ((null != obj)) {
            Integer level = Integer.class.cast(obj);
            Assert.assertTrue(level.intValue() > 1,
                    "Conformance level 2 will not be checked since ics = " + level);
        }

        rootUri = testContext.getSuite().getAttribute(
                SuiteAttribute.TEST_SUBJECT.getName()).toString();
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

    private void createEntities() throws ServiceFailureException, URISyntaxException {
        Thing thing = new Thing("Thing 1", "The first thing.");
        things.add(thing);
        Location location = new Location("Location 1.0", "Location of Thing 1.", "application/vnd.geo+json", new Point(8, 51));
        thing.getLocations().add(location);
        service.create(thing);

        Sensor sensor = new Sensor("Sensor 1", "The first sensor.", "text", "Some metadata.");
        ObservedProperty obsProp = new ObservedProperty("Temperature", new URI("http://ucom.org/temperature"), "The temperature of the thing.");
        Datastream datastream = new Datastream("Datastream 1", "The temperature of thing 1, sensor 1.", "someType", new UnitOfMeasurement("degree celcius", "°C", "ucum:T"));
        datastream.setThing(thing);
        datastream.setSensor(sensor);
        datastream.setObservedProperty(obsProp);
        service.create(datastream);
        datastreams.add(datastream);
    }

    @Test(description = "Test boolean result values.", groups = "level-2")
    public void testBooleanResult() throws ServiceFailureException {
        ObservationDao doa = service.observations();
        Observation b1 = new Observation(Boolean.TRUE, datastreams.get(0));
        doa.create(b1);
        observations.add(b1);

        Observation b2 = new Observation(Boolean.FALSE, datastreams.get(0));
        doa.create(b2);
        observations.add(b2);

        Observation found;
        found = doa.find(b1.getId());
        Assert.assertEquals(found.getResult(), b1.getResult(), "Expected result to be a Boolean.");
        found = doa.find(b2.getId());
        Assert.assertEquals(found.getResult(), b2.getResult(), "Expected result to be a Boolean.");
    }

    @Test(description = "Test string result values.", groups = "level-2")
    public void testStringResult() throws ServiceFailureException {
        ObservationDao doa = service.observations();
        Observation b1 = new Observation("fourty two", datastreams.get(0));
        doa.create(b1);
        observations.add(b1);

        Observation found;
        found = doa.find(b1.getId());
        Assert.assertEquals(found.getResult(), b1.getResult(), "Expected result to be a String.");
    }

    @Test(description = "Test numeric result values.", groups = "level-2")
    public void testNumericResult() throws ServiceFailureException {
        ObservationDao doa = service.observations();
        Observation b1 = new Observation(1, datastreams.get(0));
        doa.create(b1);
        observations.add(b1);

        Observation found;
        found = doa.find(b1.getId());
        Assert.assertEquals(found.getResult(), b1.getResult(), "Expected result to be a Number.");

        Observation b2 = new Observation(1.23, datastreams.get(0));
        doa.create(b2);
        observations.add(b2);

        found = doa.find(b2.getId());
        Assert.assertEquals(found.getResult(), b2.getResult(), "Expected result to be a Number.");

    }

    @Test(description = "Test Object result values.", groups = "level-2")
    public void testObjectResult() throws ServiceFailureException {
        ObservationDao doa = service.observations();
        Map<String, Object> result = new HashMap<>();
        result.put("number", 1.23);
        result.put("string", "One comma twentythree");
        result.put("boolean", Boolean.TRUE);
        Observation o1 = new Observation(result, datastreams.get(0));
        doa.create(o1);
        observations.add(o1);

        Observation found;
        found = doa.find(o1.getId());
        Assert.assertEquals(found.getResult(), o1.getResult(), "Expected result Maps are not equal.");
    }

    @Test(description = "Test Object result values.", groups = "level-2")
    public void testArrayResult() throws ServiceFailureException {
        ObservationDao doa = service.observations();
        List<Object> result = new ArrayList<>();
        result.add(1.23);
        result.add("One comma twentythree");
        result.add(Boolean.TRUE);
        Observation o1 = new Observation(result, datastreams.get(0));
        doa.create(o1);
        observations.add(o1);

        Observation found;
        found = doa.find(o1.getId());
        Assert.assertEquals(found.getResult(), o1.getResult(), "Expected result Arrays are not equal.");
    }

    public static <T extends Entity<T>> List<T> getFromList(List<T> list, int... ids) {
        List<T> result = new ArrayList<>();
        for (int i : ids) {
            result.add(list.get(i));
        }
        return result;
    }
}
