package org.opengis.cite.sta10.createUpdateDelete;

import org.opengis.cite.sta10.util.EntityType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Includes various tests of capability 2.
 */
public class Capability2Tests{

    public final String rootUri = "http://chashuhotpot.sensorup.com/OGCSensorThings/v1.0";

    @Test(description = "POST and DELETE Entities", groups = "level-2")
    public void createAndDeleteEntities(){
        String urlParameters  = "{\"description\":\"This is a Test From TestNG\"}";
        long thingId = postEntity(EntityType.THING, urlParameters);
        urlParameters  = "{\n" +
                "  \"description\": \"bow river\",\n" +
                "  \"encodingType\": \"http://example.org/location_types#GeoJSON\",\n" +
                "  \"location\": { \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }\n" +
                "}";
        long locationId = postEntity(EntityType.LOCATION, urlParameters);
        urlParameters  = "{\n" +
                "  \"description\": \"Fuguro Barometer\",\n" +
                "  \"encodingType\": \"http://schema.org/description\",\n" +
                "  \"metadata\": \"Barometer\"\n" +
                "}";
        long sensorId = postEntity(EntityType.SENSOR, urlParameters);
        urlParameters  = "{\n" +
                "  \"name\": \"DewPoint Temperature\",\n" +
                "  \"definition\": \"http://dbpedia.org/page/Dew_point\",\n" +
                "  \"description\": \"The dewpoint temperature is the temperature to which the air must be cooled, at constant pressure, for dew to form. As the grass and other objects near the ground cool to the dewpoint, some of the water vapor in the atmosphere condenses into liquid water on the objects.\"\n" +
                "}";
        long obsPropId = postEntity(EntityType.OBSERVED_PROPERTY, urlParameters);
        urlParameters  = "{\n" +
                "  \"description\": \"A weather station.\",\n" +
                "  \"encodingType\": \"http://example.org/location_types#GeoJSON\",\n" +
                "  \"feature\": {\n" +
                "    \"type\": \"Point\",\n" +
                "    \"coordinates\": [\n" +
                "      10,\n" +
                "      10\n" +
                "    ]\n" +
                "  }\n" +
                "}";
        long foiId = postEntity(EntityType.FEATURE_OF_INTEREST, urlParameters);
        urlParameters = "{\n" +
                "  \"unitOfMeasurement\": {\n" +
                "    \"name\": \"Celsius\",\n" +
                "    \"symbol\": \"degC\",\n" +
                "    \"definition\": \"http://qudt.org/vocab/unit#DegreeCelsius\"\n" +
                "  },\n" +
                "  \"description\": \"test datastream.\",\n" +
                "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Measurement\",\n" +
                "  \"Thing\": { \"id\": " + thingId + " },\n" +
                "  \"ObservedProperty\":{ \"id\":" + obsPropId + "},\n" +
                "  \"Sensor\": { \"id\": " + sensorId + " }\n" +
                "}";
        long datastreamId = postEntity(EntityType.DATASTREAM, urlParameters);
        urlParameters = "{\n" +
                "  \"phenomenonTime\": \"2015-03-01T00:40:00Z\",\n" +
                "  \"result\": 8,\n" +
                "  \"Datastream\":{\"id\": " + datastreamId + "},\n" +
                "  \"FeatureOfInterest\": {\"id\": " + foiId + "}  \n" +
                "}";
        long obsId1 = postEntity(EntityType.OBSERVATION, urlParameters);
        //POST Observation without FOI (Automatic creation of FOI)
        urlParameters = "{\n" +
                "  \"phenomenonTime\": \"2015-03-01T00:00:00Z\",\n" +
                "  \"result\": 100,\n" +
                "  \"FeatureOfInterest\": {\n" +
                "  \t\"description\": \"A weather station.\",\n" +
                "  \t\"encodingType\": \"http://example.org/location_types#GeoJSON\",\n" +
                "    \"feature\": {\n" +
                "      \"type\": \"Point\",\n" +
                "      \"coordinates\": [\n" +
                "        -114.05,\n" +
                "        51.05\n" +
                "      ]\n" +
                "    }\n" +
                "  },\n" +
                "  \"Datastream\":{\"id\": " + datastreamId + "}\n" +
                "}";
        long obsId2 = postEntity(EntityType.OBSERVATION, urlParameters);


        deleteEntity(EntityType.OBSERVATION, obsId1);
        deleteEntity(EntityType.OBSERVATION, obsId2);
        deleteEntity(EntityType.FEATURE_OF_INTEREST, foiId);
        deleteEntity(EntityType.DATASTREAM, datastreamId);
        deleteEntity(EntityType.OBSERVED_PROPERTY, obsPropId);
        deleteEntity(EntityType.SENSOR, sensorId);
        //TODO: This code must be uncommented when creating historicalLocation is implemented correctly. - STV2-121
//        deleteEntity(EntityType.HISTORICAL_LOCATION, histLocId);
        deleteEntity(EntityType.LOCATION, locationId);
        deleteEntity(EntityType.THING, thingId);
        //TODO: This code must be uncommented when creating historicalLocation is implemented correctly. - STV2-121
//        urlParameters = "{\n" +
//                "  \"time\": \"2015-03-01T00:40:00Z\",\n" +
//                "  \"Thing\":{\"id\": " + thingId + "},\n" +
//                "  \"Locations\": [{\"id\": " + locationId + "}]  \n" +
//                "}";
//        postEntity(EntityType.HISTORICAL_LOCATION, urlParameters);
    }

    public long postEntity(EntityType entityType, String urlParameters){
        String urlString = "http://chashuhotpot.sensorup.com/OGCSensorThings/v1.0";
        switch (entityType) {
            case THING:
                urlString += "/Things";
                break;
            case LOCATION:
                urlString += "/Locations";
                break;
            case HISTORICAL_LOCATION:
                urlString += "/HistoricalLocations";
                break;
            case DATASTREAM:
                urlString += "/Datastreams";
                break;
            case SENSOR:
                urlString += "/Sensors";
                break;
            case OBSERVATION:
                urlString += "/Observations";
                break;
            case OBSERVED_PROPERTY:
                urlString += "/ObservedProperties";
                break;
            case FEATURE_OF_INTEREST:
                urlString += "/FeaturesOfInterest";
                break;
            default:
                Assert.fail("Entity type is not recognized in SensorThings API : " + entityType);
                return -1;
        }
        HttpURLConnection conn= null;
        try {
            //Create connection
            URL url = new URL(urlString);
            byte[] postData       = urlParameters.getBytes( StandardCharsets.UTF_8 );
            int    postDataLength = postData.length;
            conn= (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                wr.write( postData );
            }

            int responseCode = conn.getResponseCode();

            Assert.assertEquals(responseCode, 201, "Error during creation of entity " + entityType.name());

            String response = conn.getHeaderField("location");
            long id = Long.parseLong(response.substring(response.indexOf("(") + 1, response.indexOf(")")));

            conn.disconnect();

            url = new URL(urlString+"("+id+")");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type",
                    "application/json");

            conn.setUseCaches(false);
            conn.setDoOutput(false);

            responseCode = conn.getResponseCode();
            Assert.assertEquals(responseCode, 200, "The POSTed entity is not created.");

            return id;

        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
    }

    private void deleteEntity(EntityType entityType, long id){
        String urlString = rootUri;
        switch (entityType) {
            case THING:
                urlString += "/Things("+id+")";
                break;
            case LOCATION:
                urlString += "/Locations("+id+")";
                break;
            case HISTORICAL_LOCATION:
                urlString += "/HistoricalLocations("+id+")";
                break;
            case DATASTREAM:
                urlString += "/Datastreams("+id+")";
                break;
            case SENSOR:
                urlString += "/Sensors("+id+")";
                break;
            case OBSERVATION:
                urlString += "/Observations("+id+")";
                break;
            case OBSERVED_PROPERTY:
                urlString += "/ObservedProperties("+id+")";
                break;
            case FEATURE_OF_INTEREST:
                urlString += "/FeaturesOfInterest("+id+")";
                break;
            default:
                Assert.fail("Entity type is not recognized in SensorThings API : " + entityType);
                return;
        }
        HttpURLConnection connection= null;
        try {
            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("DELETE");
            connection.connect();
            int responseCode= connection.getResponseCode();
            Assert.assertEquals(responseCode, 200, "DELETE does not work properly for "+entityType+" with id "+id+". Returned with response code "+responseCode+".");

            connection.disconnect();

            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.setUseCaches(false);
            connection.setDoOutput(false);

            responseCode = connection.getResponseCode();
            Assert.assertEquals(responseCode, 404, "Deleted entity was not actually deleted : "+entityType+"("+id+").");

        } catch (Exception e) {
            e.printStackTrace();
            return;
        } finally {
            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
