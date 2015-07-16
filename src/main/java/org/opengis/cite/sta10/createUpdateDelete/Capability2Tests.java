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

    @Test(description = "Post Entities")
    public void createEntities(){
        String urlParameters  = "{\"description\":\"This is a Test From TestNG\"}";
        postEntity(EntityType.THING, urlParameters);
        urlParameters  = "{\n" +
                "  \"description\": \"bow river\",\n" +
                "  \"encodingType\": \"http://example.org/location_types#GeoJSON\",\n" +
                "  \"location\": { \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }\n" +
                "}";
        postEntity(EntityType.LOCATION, urlParameters);
        urlParameters  = "{\n" +
                "  \"description\": \"Fuguro Barometer\",\n" +
                "  \"encodingType\": \"http://schema.org/description\",\n" +
                "  \"metadata\": \"Barometer\"\n" +
                "}";
        postEntity(EntityType.SENSOR, urlParameters);
        urlParameters  = "{\n" +
                "  \"name\": \"DewPoint Temperature\",\n" +
                "  \"definition\": \"http://dbpedia.org/page/Dew_point\",\n" +
                "  \"description\": \"The dewpoint temperature is the temperature to which the air must be cooled, at constant pressure, for dew to form. As the grass and other objects near the ground cool to the dewpoint, some of the water vapor in the atmosphere condenses into liquid water on the objects.\"\n" +
                "}";
        postEntity(EntityType.OBSERVED_PROPERTY, urlParameters);
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
        postEntity(EntityType.FEATURE_OF_INTEREST, urlParameters);
    }

    public void postEntity(EntityType entityType, String urlParameters){
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
                return;
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

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
    }
}
