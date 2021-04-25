package dev.victorbrugnolo.webcrawler.configuration;

import java.util.Objects;

public class EnvironmentAccessor implements Environment {

  private EnvironmentAccessor() {
  }

  private static EnvironmentAccessor instance;

  @Override
  public String getValue(String envName) {
    return System.getenv(envName);
  }

  public static EnvironmentAccessor getInstance() {
    if (Objects.isNull(instance)) {
      instance = new EnvironmentAccessor();
    }

    return instance;
  }
}
