package com.company;

import static com.google.common.net.HttpHeaders.CONTENT_TYPE;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.utility.DockerImageName;
import org.testng.annotations.*;

@Slf4j
public class Main {

  public static final MediaType JSON = okhttp3.MediaType.parse("application/json; charset=utf-8");

  static GenericContainer<?> container;

  @BeforeClass
  public static void setup() {
    container =
        new GenericContainer<>(DockerImageName.parse("ealen/echo-server"))
            .withExposedPorts(8080, 8081)
            .withLogConsumer(new Slf4jLogConsumer(log));

    container.start();
    log.info("created container");
  }

  @AfterClass
  public static void shutdown() {
    container.stop();
    log.info("stopped container");
  }

  @Test
  public void sampleTestMethod1() throws IOException {

    ObjectMapper objectMapper = new ObjectMapper();
    JsonParser jsonParser = new JsonParser();

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
    bodyJson.addProperty("appKey", "12345678");
    bodyJson.addProperty("token", "qwertyuiop");

    RequestBody body = RequestBody.create(JSON, bodyJson.toString());

    String host = container.getContainerIpAddress();
    String port = container.getFirstMappedPort().toString();

    String url = "http://" + host + ":" + port;

    // rest calls to container
    OkHttpClient client = new OkHttpClient();

    Request request =
        new Request.Builder().url(url).header(CONTENT_TYPE, JSON.toString()).post(body).build();

    try (Response response = client.newCall(request).execute()) {
      String x = response.body().string();
      System.out.println(x);
    }
  }
}
