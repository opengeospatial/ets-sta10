package org.opengis.cite.sta10.createUpdateDelete;

import org.json.JSONException;
import org.json.JSONObject;
import org.opengis.cite.sta10.util.EntityProperties;
import org.opengis.cite.sta10.util.EntityType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Includes various tests of capability 2.
 */
public class Capability2Tests{

    public final String rootUri = "http://192.168.1.13:8080/OGCSensorThings/v1.0";

    @Test(description = "POST and DELETE Entities", groups = "level-2")
    public void createAndPutAndDeleteEntities(){
        try {
            String urlParameters = "{\"description\":\"This is a Test Thing From TestNG\"}";
            JSONObject entity = postEntity(EntityType.THING, urlParameters);
            long thingId = entity.getLong("id");
            urlParameters = "{\"description\":\"This is a Updated Test Thing From TestNG\", \"properties\" : {\"test\" : \"update\"}}";
            Map<String,Object> diffs = new HashMap<>();
            diffs.put("description", "This is a Updated Test Thing From TestNG");
            JSONObject updatedEntity = updateEntity(EntityType.THING, urlParameters, thingId);
            checkPut(EntityType.THING,entity,updatedEntity,diffs);


            urlParameters = "{\n" +
                    "  \"description\": \"bow river\",\n" +
                    "  \"encodingType\": \"http://example.org/location_types#GeoJSON\",\n" +
                    "  \"location\": { \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }\n" +
                    "}";
            entity = postEntity(EntityType.LOCATION, urlParameters);
            long locationId = entity.getLong("id");
            urlParameters = "{\"encodingType\":\"UPDATED ENCODING\",\"description\":\"UPDATED DESCRIPTION\", \"location\": { \"type\": \"Point\", \"coordinates\": [-114.05, 50] }}";
            diffs = new HashMap<>();
            diffs.put("encodingType","UPDATED ENCODING");
            diffs.put("description","UPDATED DESCRIPTION");
            diffs.put("location", new JSONObject("{ \"type\": \"Point\", \"coordinates\": [-114.05, 50] }}"));
            updatedEntity = updateEntity(EntityType.LOCATION, urlParameters, locationId);
            checkPut(EntityType.LOCATION,entity,updatedEntity,diffs);


            urlParameters = "{\n" +
                    "  \"description\": \"Fuguro Barometer\",\n" +
                    "  \"encodingType\": \"http://schema.org/description\",\n" +
                    "  \"metadata\": \"Barometer\"\n" +
                    "}";
            entity = postEntity(EntityType.SENSOR, urlParameters);
            long sensorId = entity.getLong("id");
            urlParameters = "{\"description\": \"UPDATED\", \"encodingType\":\"http://schema.org/description\", \"metadata\": \"UPDATED\"}";
            diffs = new HashMap<>();
            diffs.put("description", "UPDATED");
            diffs.put("encodingType", "http://schema.org/description");
            diffs.put("metadata", "UPDATED");
            updatedEntity = updateEntity(EntityType.SENSOR, urlParameters, sensorId);
            checkPut(EntityType.SENSOR,entity,updatedEntity,diffs);


            urlParameters = "{\n" +
                    "  \"name\": \"DewPoint Temperature\",\n" +
                    "  \"definition\": \"http://dbpedia.org/page/Dew_point\",\n" +
                    "  \"description\": \"The dewpoint temperature is the temperature to which the air must be cooled, at constant pressure, for dew to form. As the grass and other objects near the ground cool to the dewpoint, some of the water vapor in the atmosphere condenses into liquid water on the objects.\"\n" +
                    "}";
            entity = postEntity(EntityType.OBSERVED_PROPERTY, urlParameters);
            long obsPropId = entity.getLong("id");
            urlParameters = "{\"name\":\"QWERTY\", \"definition\": \"ZXCVB\", \"description\":\"POIUYTREW\"}";
            diffs = new HashMap<>();
            diffs.put("name","QWERTY");
            diffs.put("definition", "ZXCVB");
            diffs.put("description", "POIUYTREW");
            updatedEntity = updateEntity(EntityType.OBSERVED_PROPERTY, urlParameters, obsPropId);
            checkPut(EntityType.OBSERVED_PROPERTY,entity,updatedEntity,diffs);


            urlParameters = "{\n" +
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
            entity = postEntity(EntityType.FEATURE_OF_INTEREST, urlParameters);
            long foiId = entity.getLong("id");
            urlParameters = "{\"encodingType\":\"SQUARE\",\"feature\":{ \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }, \"description\":\"POIUYTREW\"}";
            diffs = new HashMap<>();
            diffs.put("encodingType","SQUARE");
            diffs.put("feature",new JSONObject("{ \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }"));
            diffs.put("description", "POIUYTREW");
            updatedEntity = updateEntity(EntityType.FEATURE_OF_INTEREST, urlParameters, foiId);
            checkPut(EntityType.FEATURE_OF_INTEREST,entity,updatedEntity,diffs);



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
            entity = postEntity(EntityType.DATASTREAM, urlParameters);
            long datastreamId = entity.getLong("id");
            urlParameters = "{\n" +
                    "  \"description\": \"Data coming from sensor on ISS.\",\n" +
                    "  \"observationType\": \"http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Observation\",\n" +
                    "  \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Entropy\",\n" +
                    "    \"symbol\": \"S\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#Entropy\"\n" +
                    "  }\n" +
                    "}\n";
            diffs = new HashMap<>();
            diffs.put("description", "Data coming from sensor on ISS.");
            diffs.put("observationType", "http://www.opengis.net/def/observationType/OGC-OM/2.0/OM_Observation");
        diffs.put("unitOfMeasurement", new JSONObject("{\"name\": \"Entropy\",\"symbol\": \"S\",\"definition\": \"http://qudt.org/vocab/unit#Entropy\""));
            updatedEntity = updateEntity(EntityType.DATASTREAM, urlParameters, datastreamId);
            checkPut(EntityType.DATASTREAM,entity,updatedEntity,diffs);


            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:40:00Z\",\n" +
                    "  \"result\": 8,\n" +
                    "  \"Datastream\":{\"id\": " + datastreamId + "},\n" +
                    "  \"FeatureOfInterest\": {\"id\": " + foiId + "}  \n" +
                    "}";
            entity = postEntity(EntityType.OBSERVATION, urlParameters);
            long obsId1 = entity.getLong("id");
            urlParameters = "{\"result\": \"99\"}";
            diffs = new HashMap<>();
            diffs.put("result", "99");
            updatedEntity = updateEntity(EntityType.OBSERVATION, urlParameters, obsId1);
            checkPut(EntityType.OBSERVATION, entity, updatedEntity, diffs);


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
            entity = postEntity(EntityType.OBSERVATION, urlParameters);
            long obsId2 = entity.getLong("id");


            urlParameters = "{\n" +
                    "  \"time\": \"2015-03-01T00:40:00Z\",\n" +
                    "  \"Thing\":{\"id\": " + thingId + "},\n" +
                    "  \"Locations\": [{\"id\": " + locationId + "}]  \n" +
                    "}";
            entity = postEntity(EntityType.HISTORICAL_LOCATION, urlParameters);
            long histLocId = entity.getLong("id");
            urlParameters = "{\"time\": \"2015-08-01T00:00:00Z\"}";
            diffs = new HashMap<>();
            diffs.put("time", "2015-08-01T00:00:00Z");
            updatedEntity = updateEntity(EntityType.HISTORICAL_LOCATION, urlParameters, histLocId);
            checkPut(EntityType.HISTORICAL_LOCATION,entity,updatedEntity,diffs);

            deleteEntity(EntityType.OBSERVATION, obsId1);
            deleteEntity(EntityType.OBSERVATION, obsId2);
            deleteEntity(EntityType.FEATURE_OF_INTEREST, foiId);
            deleteEntity(EntityType.DATASTREAM, datastreamId);
            deleteEntity(EntityType.OBSERVED_PROPERTY, obsPropId);
            deleteEntity(EntityType.SENSOR, sensorId);
            deleteEntity(EntityType.HISTORICAL_LOCATION, histLocId);
            deleteEntity(EntityType.LOCATION, locationId);
            deleteEntity(EntityType.THING, thingId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public JSONObject postEntity(EntityType entityType, String urlParameters){
        String urlString =  rootUri;
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
                return null;
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
            conn.setDoOutput(true);

            responseCode = conn.getResponseCode();
            Assert.assertEquals(responseCode, 200, "The POSTed entity is not created.");

            //Get Response
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder responseBuilder = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while ((line = rd.readLine()) != null) {
                responseBuilder.append(line);
                responseBuilder.append('\r');
            }
            rd.close();

            JSONObject result = new JSONObject(responseBuilder.toString());
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
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
            Assert.assertEquals(responseCode, 200, "DELETE does not work properly for " + entityType + " with id " + id + ". Returned with response code " + responseCode + ".");

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

    public JSONObject updateEntity(EntityType entityType, String urlParameters, long id){
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
                return null;
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
            conn.setRequestMethod("PUT");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);
            try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {
                wr.write( postData );
            }

            int responseCode = conn.getResponseCode();

            Assert.assertEquals(responseCode, 200, "Error during updating(PUT) of entity " + entityType.name());

            conn.disconnect();

         //   url = new URL(urlString+"("+id+")");
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type",
                    "application/json");

            conn.setUseCaches(false);
            conn.setDoOutput(true);

            responseCode = conn.getResponseCode();

            //Get Response
            InputStream is = conn.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

            JSONObject result = new JSONObject(response.toString());
            return result;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
    }

    private void checkPatch(EntityType entityType, JSONObject oldEntity, JSONObject newEntity, Map diffs){
        try {
            switch (entityType) {
                case THING:
                    for (String property : EntityProperties.THING_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property), diffs.get(property), "PUT was not applied correctly for "+entityType+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case LOCATION:
                    for (String property : EntityProperties.LOCATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property), diffs.get(property), "PUT was not applied correctly for "+entityType+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case HISTORICAL_LOCATION:
                    for (String property : EntityProperties.HISTORICAL_LOCATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property), diffs.get(property), "PUT was not applied correctly for "+entityType+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case DATASTREAM:
                    for (String property : EntityProperties.DATASTREAM_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property), diffs.get(property), "PUT was not applied correctly for "+entityType+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case SENSOR:
                    for (String property : EntityProperties.SENSOR_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property), diffs.get(property), "PUT was not applied correctly for "+entityType+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case OBSERVATION:
                    for (String property : EntityProperties.OBSERVATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property), diffs.get(property), "PUT was not applied correctly for "+entityType+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case OBSERVED_PROPERTY:
                    for (String property : EntityProperties.OBSERVED_PROPETY_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property), diffs.get(property), "PUT was not applied correctly for "+entityType+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    for (String property : EntityProperties.FEATURE_OF_INTEREST_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property), diffs.get(property), "PUT was not applied correctly for "+entityType+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void checkPut(EntityType entityType, JSONObject oldEntity, JSONObject newEntity, Map diffs){
        try {
            switch (entityType) {
                case THING:
                    for (String property : EntityProperties.THING_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property), diffs.get(property), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case LOCATION:
                    for (String property : EntityProperties.LOCATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case HISTORICAL_LOCATION:
                    for (String property : EntityProperties.HISTORICAL_LOCATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property), diffs.get(property), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case DATASTREAM:
                    for (String property : EntityProperties.DATASTREAM_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case SENSOR:
                    for (String property : EntityProperties.SENSOR_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property), diffs.get(property), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case OBSERVATION:
                    for (String property : EntityProperties.OBSERVATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property), diffs.get(property), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case OBSERVED_PROPERTY:
                    for (String property : EntityProperties.OBSERVED_PROPETY_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property), diffs.get(property), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    for (String property : EntityProperties.FEATURE_OF_INTEREST_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for " + entityType + ".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                default:
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
