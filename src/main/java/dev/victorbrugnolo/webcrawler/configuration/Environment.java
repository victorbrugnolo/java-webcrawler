package dev.victorbrugnolo.webcrawler.configuration;

public interface Environment {
  String getValue(String envVarName);
}
