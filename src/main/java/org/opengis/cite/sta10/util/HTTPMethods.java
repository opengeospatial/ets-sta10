
package org.opengis.cite.sta10.util;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HTTPMethods {

    public static Map<String,Object> doGet(String urlString){
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

            Map<String, Object> result = new HashMap<String,Object>();
            result.put("response-code",connection.getResponseCode());
            if(connection.getResponseCode() == 200){
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
                result.put("response", response.toString());
            } else{
                result.put("response", "");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

        }
    }

    public static Map<String,Object> doPost(String urlString, String postBody){
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(urlString);
            byte[] postData = postBody.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            connection.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(postData);
            }

            Map<String, Object> result = new HashMap<String,Object>();
            result.put("response-code",connection.getResponseCode());
            result.put("response", connection.getHeaderField("location"));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static Map<String,Object> doPut(String urlString, String putBody){
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(urlString);
            byte[] postData = putBody.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setInstanceFollowRedirects(false);
            connection.setRequestMethod("PUT");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            connection.setUseCaches(false);
            try (DataOutputStream wr = new DataOutputStream(connection.getOutputStream())) {
                wr.write(postData);
            }

            Map<String, Object> result = new HashMap<String,Object>();
            result.put("response-code",connection.getResponseCode());
            if(connection.getResponseCode() == 200){
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
                result.put("response", response.toString());
            } else{
                result.put("response", "");
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static Map<String,Object> doDelete(String urlString){
        HttpURLConnection connection = null;
        try {
            //Create connection
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestProperty(
                    "Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestMethod("DELETE");
            connection.connect();

            Map<String, Object> result = new HashMap<String,Object>();
            result.put("response-code",connection.getResponseCode());
            if(connection.getResponseCode()==200){
                result.put("response", "");
            } else{
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
                result.put("response", response.toString());
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public static Map<String,Object> doPatch(String urlString, String patchBody){
        URI uri = null;
        try {
            uri = new URI(urlString);

            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPatch request = new HttpPatch(uri);
            StringEntity params = new StringEntity(patchBody, ContentType.APPLICATION_JSON);
            request.setEntity(params);
            CloseableHttpResponse response = httpClient.execute(request);
            Map<String, Object> result = new HashMap<String,Object>();
            result.put("response-code",response.getStatusLine().getStatusCode());
            result.put("response", EntityUtils.toString(response.getEntity()));
            response.close();
            httpClient.close();
            return result;
        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
