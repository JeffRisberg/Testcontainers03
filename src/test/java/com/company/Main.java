package com.company;

import java.sql.SQLException;
import lombok.extern.slf4j.Slf4j;
import org.testcontainers.containers.GenericContainer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Slf4j
public class Main {

  static GenericContainer<?> container;

  @BeforeClass
  public static void setup() {
    container = new GenericContainer<>("echo");
  }

  @Test
  public void sampleTestMethod1() throws SQLException {
    // rest calls to container
  }
}
