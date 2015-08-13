package org.opengis.cite.sta10.createUpdateDelete;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.json.JSONException;
import org.json.JSONObject;
import org.opengis.cite.sta10.util.EntityProperties;
import org.opengis.cite.sta10.util.EntityType;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Includes various tests of capability 2.
 */
public class Capability2Tests{

    public final String rootUri = "http://192.168.1.13:8080/OGCSensorThings/v1.0";

    List<Long> thingIds = new ArrayList<>();
    List<Long> locationIds = new ArrayList<>();
    List<Long> historicalLocationIds = new ArrayList<>();
    List<Long> datastreamIds = new ArrayList<>();
    List<Long> observationIds = new ArrayList<>();
    List<Long> sensorIds = new ArrayList<>();
    List<Long> obsPropIds = new ArrayList<>();
    List<Long> foiIds = new ArrayList<>();




    @Test(description = "POST Entities", groups = "level-2", priority = 1)
    public void createEntities(){
        try {
            /** Thing **/
            String urlParameters = "{\"description\":\"This is a Test Thing From TestNG\"}";
            JSONObject entity = postEntity(EntityType.THING, urlParameters);
            long thingId = entity.getLong("id");
            thingIds.add(thingId);

            /** Location **/
            urlParameters = "{\n" +
                    "  \"description\": \"bow river\",\n" +
                    "  \"encodingType\": \"http://example.org/location_types#GeoJSON\",\n" +
                    "  \"location\": { \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }\n" +
                    "}";
            entity = postEntity(EntityType.LOCATION, urlParameters);
            long locationId = entity.getLong("id");
            locationIds.add(locationId);
            JSONObject locationEntity = entity;

            /** Sensor **/
            urlParameters = "{\n" +
                    "  \"description\": \"Fuguro Barometer\",\n" +
                    "  \"encodingType\": \"http://schema.org/description\",\n" +
                    "  \"metadata\": \"Barometer\"\n" +
                    "}";
            entity = postEntity(EntityType.SENSOR, urlParameters);
            long sensorId = entity.getLong("id");
            sensorIds.add(sensorId);

            /** ObservedProperty **/
            urlParameters = "{\n" +
                    "  \"name\": \"DewPoint Temperature\",\n" +
                    "  \"definition\": \"http://dbpedia.org/page/Dew_point\",\n" +
                    "  \"description\": \"The dewpoint temperature is the temperature to which the air must be cooled, at constant pressure, for dew to form. As the grass and other objects near the ground cool to the dewpoint, some of the water vapor in the atmosphere condenses into liquid water on the objects.\"\n" +
                    "}";
            entity = postEntity(EntityType.OBSERVED_PROPERTY, urlParameters);
            long obsPropId = entity.getLong("id");
            obsPropIds.add(obsPropId);

            /** FeatureOfInterest **/
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
            foiIds.add(foiId);

            /** Datastream **/
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
            datastreamIds.add(datastreamId);

            /** Observation **/
            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:40:00.000Z\",\n" +
                    "  \"result\": 8,\n" +
                    "  \"Datastream\":{\"id\": " + datastreamId + "},\n" +
                    "  \"FeatureOfInterest\": {\"id\": " + foiId + "}  \n" +
                    "}";
            entity = postEntity(EntityType.OBSERVATION, urlParameters);
            long obsId1 = entity.getLong("id");
            observationIds.add(obsId1);
            //POST Observation without FOI (Automatic creation of FOI)
            //Add location to the Thing
            urlParameters = "{\"Locations\":[{\"id\":"+locationId+"}]}";
            patchEntity(EntityType.THING, urlParameters, thingId);

            urlParameters = "{\n" +
                    "  \"phenomenonTime\": \"2015-03-01T00:00:00.000Z\",\n" +
                    "  \"result\": 100,\n" +
                    "  \"Datastream\":{\"id\": " + datastreamId + "}\n" +
                    "}";
            entity = postEntity(EntityType.OBSERVATION, urlParameters);
            long obsId2 = entity.getLong("id");
            observationIds.add(obsId2);
            checkAutomaticInsertionOfFOI(obsId2, locationEntity);

            /** HistoricalLocation **/
            urlParameters = "{\n" +
                    "  \"time\": \"2015-03-01T00:40:00.000Z\",\n" +
                    "  \"Thing\":{\"id\": " + thingId + "},\n" +
                    "  \"Locations\": [{\"id\": " + locationId + "}]  \n" +
                    "}";
            entity = postEntity(EntityType.HISTORICAL_LOCATION, urlParameters);
            long histLocId = entity.getLong("id");
            historicalLocationIds.add(histLocId);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test(description = "PATCH Entities", groups = "level-2", priority = 2)
    public void patchEntities(){
        try{
            /** Thing **/
            long thingId = thingIds.get(0);
            JSONObject entity = getEntity(EntityType.THING, thingId);
            String urlParameters = "{\"description\":\"This is a PATCHED Test Thing From TestNG\"}";
            Map<String,Object> diffs = new HashMap<>();
            diffs.put("description", "This is a PATCHED Test Thing From TestNG");
            JSONObject updatedEntity = patchEntity(EntityType.THING, urlParameters, thingId);
            checkPatch(EntityType.THING,entity,updatedEntity,diffs);

            /** Location **/
            long locationId = locationIds.get(0);
            entity = getEntity(EntityType.LOCATION, locationId);
            urlParameters = "{\"location\": { \"type\": \"Point\", \"coordinates\": [114.05, -50] }}";
            diffs = new HashMap<>();
            diffs.put("location", new JSONObject("{ \"type\": \"Point\", \"coordinates\": [114.05, -50] }}"));
            updatedEntity = patchEntity(EntityType.LOCATION, urlParameters, locationId);
            checkPatch(EntityType.LOCATION, entity, updatedEntity, diffs);

            /** HistoricalLocation **/
            long histLocId = historicalLocationIds.get(0);
            entity = getEntity(EntityType.HISTORICAL_LOCATION, histLocId);
            urlParameters = "{\"time\": \"2015-07-01T00:00:00.000Z\"}";
            diffs = new HashMap<>();
            diffs.put("time", "2015-07-01T00:00:00.000Z");
            updatedEntity = patchEntity(EntityType.HISTORICAL_LOCATION, urlParameters, histLocId);
            checkPatch(EntityType.HISTORICAL_LOCATION, entity, updatedEntity, diffs);

            /** Sensor **/
            long sensorId = sensorIds.get(0);
            entity = getEntity(EntityType.SENSOR, sensorId);
            urlParameters = "{\"metadata\": \"PATCHED\"}";
            diffs = new HashMap<>();
            diffs.put("metadata", "PATCHED");
            updatedEntity = patchEntity(EntityType.SENSOR, urlParameters, sensorId);
            checkPatch(EntityType.SENSOR, entity, updatedEntity, diffs);

            /** ObserverdProperty **/
            long obsPropId = obsPropIds.get(0);
            entity = getEntity(EntityType.OBSERVED_PROPERTY, obsPropId);
            urlParameters = "{\"description\":\"PATCHED\"}";
            diffs = new HashMap<>();
            diffs.put("description", "PATCHED");
            updatedEntity = patchEntity(EntityType.OBSERVED_PROPERTY, urlParameters, obsPropId);
            checkPatch(EntityType.OBSERVED_PROPERTY,entity,updatedEntity,diffs);

            /** FeatureOfInterest **/
            long foiId = foiIds.get(0);
            entity = getEntity(EntityType.FEATURE_OF_INTEREST, foiId);
            urlParameters = "{\"feature\":{ \"type\": \"Point\", \"coordinates\": [114.05, -51.05] }}";
            diffs = new HashMap<>();
            diffs.put("feature",new JSONObject("{ \"type\": \"Point\", \"coordinates\": [114.05, -51.05] }"));
            updatedEntity = patchEntity(EntityType.FEATURE_OF_INTEREST, urlParameters, foiId);
            checkPatch(EntityType.FEATURE_OF_INTEREST, entity, updatedEntity, diffs);

            /** Datastream **/
            long datastreamId = datastreamIds.get(0);
            entity = getEntity(EntityType.DATASTREAM, datastreamId);
            urlParameters = "{\"description\": \"Patched Description\"}";
            diffs = new HashMap<>();
            diffs.put("description", "Patched Description");
            updatedEntity = patchEntity(EntityType.DATASTREAM, urlParameters, datastreamId);
            checkPatch(EntityType.DATASTREAM, entity, updatedEntity, diffs);
            //Second PATCH for UOM
            entity = updatedEntity;
            urlParameters = "{ \"unitOfMeasurement\": {\n" +
                    "    \"name\": \"Entropy2\",\n" +
                    "    \"symbol\": \"S2\",\n" +
                    "    \"definition\": \"http://qudt.org/vocab/unit#Entropy2\"\n" +
                    "  } }";
            diffs = new HashMap<>();
            diffs.put("unitOfMeasurement", new JSONObject("{\"name\": \"Entropy2\",\"symbol\": \"S2\",\"definition\": \"http://qudt.org/vocab/unit#Entropy2\"}"));
            updatedEntity = patchEntity(EntityType.DATASTREAM, urlParameters, datastreamId);
            checkPatch(EntityType.DATASTREAM, entity, updatedEntity, diffs);

            /** Observation **/
            long obsId1 = observationIds.get(0);
            entity = getEntity(EntityType.OBSERVATION, obsId1);
            urlParameters = "{\"phenomenonTime\": \"2015-07-01T00:40:00.000Z\"}";
            diffs = new HashMap<>();
            diffs.put("phenomenonTime", "2015-07-01T00:40:00.000Z");
            updatedEntity = patchEntity(EntityType.OBSERVATION, urlParameters, obsId1);
            checkPatch(EntityType.OBSERVATION, entity, updatedEntity, diffs);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Test(description = "PUT Entities", groups = "level-2", priority = 2)
    public void putEntities(){
        try{
            /** Thing **/
            long thingId = thingIds.get(0);
            JSONObject entity = getEntity(EntityType.THING, thingId);
            String urlParameters = "{\"description\":\"This is a Updated Test Thing From TestNG\"}";
            Map<String,Object> diffs = new HashMap<>();
            diffs.put("description", "This is a Updated Test Thing From TestNG");
            JSONObject updatedEntity = updateEntity(EntityType.THING, urlParameters, thingId);
            checkPut(EntityType.THING, entity, updatedEntity, diffs);

            /** Location **/
            long locationId = locationIds.get(0);
            entity = getEntity(EntityType.LOCATION, locationId);
            urlParameters = "{\"encodingType\":\"UPDATED ENCODING\",\"description\":\"UPDATED DESCRIPTION\", \"location\": { \"type\": \"Point\", \"coordinates\": [-114.05, 50] }}";
            diffs = new HashMap<>();
            diffs.put("encodingType","UPDATED ENCODING");
            diffs.put("description","UPDATED DESCRIPTION");
            diffs.put("location", new JSONObject("{ \"type\": \"Point\", \"coordinates\": [-114.05, 50] }}"));
            updatedEntity = updateEntity(EntityType.LOCATION, urlParameters, locationId);
            checkPut(EntityType.LOCATION, entity, updatedEntity, diffs);

            /** HistoricalLocation **/
            long histLocId = historicalLocationIds.get(0);
            entity = getEntity(EntityType.HISTORICAL_LOCATION, histLocId);
            urlParameters = "{\"time\": \"2015-08-01T00:00:00.000Z\"}";
            diffs = new HashMap<>();
            diffs.put("time", "2015-08-01T00:00:00.000Z");
            updatedEntity = updateEntity(EntityType.HISTORICAL_LOCATION, urlParameters, histLocId);
            checkPut(EntityType.HISTORICAL_LOCATION, entity, updatedEntity, diffs);

            /** Sensor **/
            long sensorId = sensorIds.get(0);
            entity = getEntity(EntityType.SENSOR, sensorId);
            urlParameters = "{\"description\": \"UPDATED\", \"encodingType\":\"http://schema.org/description\", \"metadata\": \"UPDATED\"}";
            diffs = new HashMap<>();
            diffs.put("description", "UPDATED");
            diffs.put("encodingType", "http://schema.org/description");
            diffs.put("metadata", "UPDATED");
            updatedEntity = updateEntity(EntityType.SENSOR, urlParameters, sensorId);
            checkPut(EntityType.SENSOR, entity, updatedEntity, diffs);

            /** ObserverdProperty **/
            long obsPropId = obsPropIds.get(0);
            urlParameters = "{\"name\":\"QWERTY\", \"definition\": \"ZXCVB\", \"description\":\"POIUYTREW\"}";
            diffs = new HashMap<>();
            diffs.put("name","QWERTY");
            diffs.put("definition", "ZXCVB");
            diffs.put("description", "POIUYTREW");
            updatedEntity = updateEntity(EntityType.OBSERVED_PROPERTY, urlParameters, obsPropId);
            checkPut(EntityType.OBSERVED_PROPERTY, entity, updatedEntity, diffs);

            /** FeatureOfInterest **/
            long foiId = foiIds.get(0);
            entity = getEntity(EntityType.FEATURE_OF_INTEREST, foiId);
            urlParameters = "{\"encodingType\":\"SQUARE\",\"feature\":{ \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }, \"description\":\"POIUYTREW\"}";
            diffs = new HashMap<>();
            diffs.put("encodingType","SQUARE");
            diffs.put("feature",new JSONObject("{ \"type\": \"Point\", \"coordinates\": [-114.05, 51.05] }"));
            diffs.put("description", "POIUYTREW");
            updatedEntity = updateEntity(EntityType.FEATURE_OF_INTEREST, urlParameters, foiId);
            checkPut(EntityType.FEATURE_OF_INTEREST, entity, updatedEntity, diffs);

            /** Datastream **/
            long datastreamId = datastreamIds.get(0);
            entity = getEntity(EntityType.DATASTREAM, datastreamId);
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
            diffs.put("unitOfMeasurement", new JSONObject("{\"name\": \"Entropy\",\"symbol\": \"S\",\"definition\": \"http://qudt.org/vocab/unit#Entropy\"}"));
            updatedEntity = updateEntity(EntityType.DATASTREAM, urlParameters, datastreamId);
            checkPut(EntityType.DATASTREAM, entity, updatedEntity, diffs);

            /** Observation **/
            long obsId1 = observationIds.get(0);
            entity = getEntity(EntityType.OBSERVATION, obsId1);
            urlParameters = "{\"result\": \"99\", \"phenomenonTime\": \"2015-08-01T00:40:00.000Z\"}";
            diffs = new HashMap<>();
            diffs.put("result", "99");
            diffs.put("phenomenonTime", "2015-08-01T00:40:00.000Z");
            updatedEntity = updateEntity(EntityType.OBSERVATION, urlParameters, obsId1);
            checkPut(EntityType.OBSERVATION, entity, updatedEntity, diffs);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Test(description = "DELETE Entities", groups = "level-2", priority = 3)
    public void deleteEntities(){
        for (int i = 0; i < observationIds.size(); i++) {
            deleteEntity(EntityType.OBSERVATION, observationIds.get(i));
        }
        for (int i = 0; i < foiIds.size(); i++) {
            deleteEntity(EntityType.FEATURE_OF_INTEREST, foiIds.get(i));
        }
        for (int i = 0; i < datastreamIds.size(); i++) {
            deleteEntity(EntityType.DATASTREAM, datastreamIds.get(i));
        }
        for (int i = 0; i < obsPropIds.size(); i++) {
            deleteEntity(EntityType.OBSERVED_PROPERTY, obsPropIds.get(i));
        }
        for (int i = 0; i < sensorIds.size(); i++) {
            deleteEntity(EntityType.SENSOR, sensorIds.get(i));
        }
        for (int i = 0; i < historicalLocationIds.size(); i++) {
            deleteEntity(EntityType.HISTORICAL_LOCATION, historicalLocationIds.get(i));
        }
        for (int i = 0; i < locationIds.size(); i++) {
            deleteEntity(EntityType.LOCATION, locationIds.get(i));
        }
        for (int i = 0; i < thingIds.size(); i++) {
            deleteEntity(EntityType.THING, thingIds.get(i));
        }
    }

    public JSONObject getEntity(EntityType entityType, long id) {
        String urlString = rootUri;
        if (id == -1) {
            return null;
        }
        if (entityType != null) { // It is not Service Root URI
            switch (entityType) {
                case THING:
                    urlString += "/Things(" + id + ")";
                    break;
                case LOCATION:
                    urlString += "/Locations(" + id + ")";
                    break;
                case HISTORICAL_LOCATION:
                    urlString += "/HistoricalLocations(" + id + ")";
                    break;
                case DATASTREAM:
                    urlString += "/Datastreams(" + id + ")";
                    break;
                case SENSOR:
                    urlString += "/Sensors(" + id + ")";
                    break;
                case OBSERVATION:
                    urlString += "/Observations(" + id + ")";
                    break;
                case OBSERVED_PROPERTY:
                    urlString += "/ObservedProperties(" + id + ")";
                    break;
                case FEATURE_OF_INTEREST:
                    urlString += "/FeaturesOfInterest(" + id + ")";
                    break;
                default:
                    Assert.fail("Entity type is not recognized in SensorThings API : " + entityType);
                    return null;
            }
        }
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type",
                    "application/json");

            connection.setUseCaches(false);
            connection.setDoOutput(true);
            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder(); // or StringBuffer if not Java 5+
            String line;
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return new JSONObject(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
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

    public JSONObject patchEntity(EntityType entityType, String urlParameters, long id){
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

        HttpURLConnection conn = null;

        try {

            //PATCH
            URI uri = new URI(urlString);
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPatch request = new HttpPatch(uri);
            StringEntity params = new StringEntity(urlParameters, ContentType.APPLICATION_JSON);
            request.setEntity(params);
            CloseableHttpResponse response = httpClient.execute(request);
            int responseCode = response.getStatusLine().getStatusCode();
            Assert.assertEquals(responseCode, 200, "Error during updating(PATCH) of entity " + entityType.name());
            httpClient.close();

            //GET patched entity for return
            URL url = new URL(urlString);
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

    private void checkPatch(EntityType entityType, JSONObject oldEntity, JSONObject newEntity, Map diffs){
        try {
            switch (entityType) {
                case THING:
                    for (String property : EntityProperties.THING_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case LOCATION:
                    for (String property : EntityProperties.LOCATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case HISTORICAL_LOCATION:
                    for (String property : EntityProperties.HISTORICAL_LOCATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case DATASTREAM:
                    for (String property : EntityProperties.DATASTREAM_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case SENSOR:
                    for (String property : EntityProperties.SENSOR_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case OBSERVATION:
                    for (String property : EntityProperties.OBSERVATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case OBSERVED_PROPERTY:
                    for (String property : EntityProperties.OBSERVED_PROPETY_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        }
                    }
                    break;
                case FEATURE_OF_INTEREST:
                    for (String property : EntityProperties.FEATURE_OF_INTEREST_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
                        } else{
                            Assert.assertEquals(newEntity.get(property).toString(), oldEntity.get(property).toString(), "PATCH was not applied correctly for "+entityType+"'s "+property+".");
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
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
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
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
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
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case OBSERVATION:
                    for (String property : EntityProperties.OBSERVATION_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
                        } else{
//                            Assert.assertEquals(newEntity.get(property), oldEntity.get(property), "PUT was not applied correctly for "+entityType+".");
                        }
                    }
                    break;
                case OBSERVED_PROPERTY:
                    for (String property : EntityProperties.OBSERVED_PROPETY_PROPERTIES) {
                        if (diffs.containsKey(property)) {
                            Assert.assertEquals(newEntity.get(property).toString(), diffs.get(property).toString(), "PUT was not applied correctly for "+entityType+".");
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

    private void checkAutomaticInsertionOfFOI(long obsId, JSONObject locationObj){
        String urlString = rootUri+"/Observations("+obsId+")/FeatureOfInterest";
        HttpURLConnection conn = null;
        try {
            URL url = new URL(urlString);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type",
                    "application/json");
            conn.setUseCaches(false);
            conn.setDoOutput(true);
            int responseCode = conn.getResponseCode();
            Assert.assertEquals(responseCode, 200, "ERROR: FeatureOfInterest was not automatically create.");
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
            Assert.assertEquals(result.getJSONObject("feature").toString(), locationObj.getJSONObject("location").toString(), "ERROR: Automatic created FeatureOfInterest does not match last Location of that Thing.");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(conn != null) {
                conn.disconnect();
            }
        }
    }
}
