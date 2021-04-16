package com.company;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.shaded.okhttp3.*;
import org.testcontainers.utility.DockerImageName;
import org.testng.annotations.*;

@Slf4j
public class Main {

  static GenericContainer<?> container;

  @BeforeClass
  public static void setup() {
    container =
        new GenericContainer<>(DockerImageName.parse("testcontainers/helloworld:1.1.0"))
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
    String host = container.getContainerIpAddress();
    String port = container.getFirstMappedPort().toString();

    String url = "http://" + host + ":" + port;

    // rest calls to container
    OkHttpClient client = new OkHttpClient();

    Request request = new Request.Builder().url(url).build();

    try (Response response = client.newCall(request).execute()) {
      String x = response.body().string();
      System.out.println(x);
    }
  }
}
