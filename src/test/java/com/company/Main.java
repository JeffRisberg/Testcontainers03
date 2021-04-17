package com.company;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;
import static org.testng.Assert.fail;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;
import org.testng.Assert;
import org.testng.annotations.*;

@Slf4j
public class Main {
  public static final MediaType JSON = okhttp3.MediaType.parse("application/json; charset=utf-8");
  public static final MediaType TEXT = okhttp3.MediaType.parse("text/plain");

  public static final JsonParser jsonParser = new JsonParser();
  public static final OkHttpClient client = new OkHttpClient();

  static GenericContainer<?> container;
  static String host;
  static String port;

  @BeforeClass
  public static void setup() {
    container =
        new GenericContainer<>(DockerImageName.parse("ealen/echo-server")).withExposedPorts(80);

    container.start();

    host = container.getContainerIpAddress();
    port = container.getFirstMappedPort().toString();
  }

  @AfterClass
  public static void shutdown() {
    container.stop();
  }

  @Test
  public void simpleGetTest() {
    String url = "http://" + host + ":" + port;

    Request request = new Request.Builder().url(url).get().build();

    try (Response response = client.newCall(request).execute()) {
      JsonObject result = (JsonObject) jsonParser.parse(response.body().string());
      JsonObject resultRequest = (JsonObject) result.get("request");
      JsonObject resultBody = (JsonObject) resultRequest.get("body");

      Assert.assertNotNull(resultBody);
      Assert.assertEquals(resultBody.size(), 0);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void simplePostTest() {
    final String message = "Attack fortress at dawn";

    String url = "http://" + host + ":" + port;

    RequestBody body = RequestBody.create(message, TEXT);

    Request request =
        new Request.Builder().url(url).header(CONTENT_TYPE, TEXT.toString()).post(body).build();

    try (Response response = client.newCall(request).execute()) {
      JsonObject result = (JsonObject) jsonParser.parse(response.body().string());
      JsonObject resultRequest = (JsonObject) result.get("request");
      JsonElement resultBody = resultRequest.get("body");

      Assert.assertEquals(resultBody.getAsString(), message);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void simpleJsonPostTest() {
    final String APPKEY = "A12345678";
    final String TOKEN = "qwertyuiop";

    String url = "http://" + host + ":" + port;

    JsonObject bodyJson = new JsonObject();
    bodyJson.addProperty("appKey", APPKEY);
    bodyJson.addProperty("token", TOKEN);

    RequestBody body = RequestBody.create(bodyJson.toString(), JSON);

    Request request =
        new Request.Builder().url(url).header(CONTENT_TYPE, JSON.toString()).post(body).build();

    try (Response response = client.newCall(request).execute()) {
      JsonObject result = (JsonObject) jsonParser.parse(response.body().string());
      JsonObject resultRequest = (JsonObject) result.get("request");
      JsonObject resultBody = (JsonObject) resultRequest.get("body");

      Assert.assertNotNull(resultBody);
      Assert.assertEquals(resultBody.get("appKey").getAsString(), APPKEY);
      Assert.assertEquals(resultBody.get("token").getAsString(), TOKEN);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }

  @Test
  public void complexJsonPostTest() {
    final int HEIGHT = 400;
    final int WIDTH = 500;

    String url = "http://" + host + ":" + port;

    JsonObject accessParamMap = new JsonObject();
    accessParamMap.addProperty("buttonElementId", "submit_button");
    accessParamMap.addProperty("colorTheme", "green");
    accessParamMap.addProperty("deployUrl", "http://ncoding.io");
    accessParamMap.addProperty("descriptionElementId", "submit_description");
    accessParamMap.addProperty("formElementId", "submit_form");
    accessParamMap.addProperty("subjectElementId", "submit_subject");
    accessParamMap.addProperty("restServer", "https://api.prod2.company.com/restserver");
    accessParamMap.addProperty("successUrl", "http://ncoding.io/index.html");

    JsonObject bodyJson = new JsonObject();
    bodyJson.add("accessParamMap", accessParamMap);
    bodyJson.addProperty("height", HEIGHT);
    bodyJson.addProperty("width", WIDTH);

    RequestBody body = RequestBody.create(bodyJson.toString(), JSON);

    Request request =
        new Request.Builder().url(url).header(CONTENT_TYPE, JSON.toString()).post(body).build();

    try (Response response = client.newCall(request).execute()) {
      JsonObject result = (JsonObject) jsonParser.parse(response.body().string());
      JsonObject resultRequest = (JsonObject) result.get("request");
      JsonObject resultBody = (JsonObject) resultRequest.get("body");

      Assert.assertNotNull(resultBody);
      Assert.assertEquals(resultBody.get("height").getAsInt(), HEIGHT);
      Assert.assertEquals(resultBody.get("width").getAsInt(), WIDTH);
    } catch (Exception e) {
      e.printStackTrace();
      fail();
    }
  }
}
