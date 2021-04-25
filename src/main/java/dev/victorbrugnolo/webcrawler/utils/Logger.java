package dev.victorbrugnolo.webcrawler.utils;

import static java.lang.System.out;

import java.util.Collection;
import java.util.Objects;

public class Logger {

  private Logger() {
  }

  private static Logger instance;

  public void logResults(final Collection<String> results) {
    out.println("=============================================");

    for (var result : results) {
      out.printf("Result found: %s%n", result);
    }

    out.printf("Search finished with %s results found%n", results.size());
  }

  public static Logger getInstance() {
    if (Objects.isNull(instance)) {
      instance = new Logger();
    }

    return instance;
  }
}
