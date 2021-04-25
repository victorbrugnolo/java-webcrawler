package dev.victorbrugnolo.webcrawler.configuration;

import dev.victorbrugnolo.webcrawler.services.HttpService;
import dev.victorbrugnolo.webcrawler.services.WebCrawler;
import dev.victorbrugnolo.webcrawler.services.impl.WebCrawlerImpl;

public class BeanConfiguration {

  public WebCrawler getWebCrawler(final HttpService httpService, final Environment environment) {
    return new WebCrawlerImpl(httpService, environment);
  }
}
