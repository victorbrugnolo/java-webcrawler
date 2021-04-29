package dev.victorbrugnolo.webcrawler.services.impl;

import static java.lang.System.out;

import dev.victorbrugnolo.webcrawler.services.HttpService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Objects;

public class HttpServiceImpl implements HttpService {

  private HttpServiceImpl() {
  }

  private static final String EMPTY = "";
  private static HttpService instance;

  @Override
  public String getUrlContent(final URI uri) {
    try (var bufferedReader = new BufferedReader(
        new InputStreamReader(uri.toURL().openStream()))) {
      String pageContent;
      var stringBuilder = new StringBuilder();

      while ((pageContent = bufferedReader.readLine()) != null) {
        stringBuilder.append(pageContent);
      }

      return stringBuilder.toString();
    } catch (IOException ex) {
      out.printf("[ERROR] Unable to get url content: %s%n", uri);
      return EMPTY;
    }
  }

  public static HttpService getInstance() {
    if (Objects.isNull(instance)) {
      instance = new HttpServiceImpl();
      return instance;
    }

    return instance;
  }
}
