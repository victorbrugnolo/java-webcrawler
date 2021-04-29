package dev.victorbrugnolo.webcrawler;

import dev.victorbrugnolo.webcrawler.configuration.BeanConfiguration;
import dev.victorbrugnolo.webcrawler.configuration.EnvironmentAccessor;
import dev.victorbrugnolo.webcrawler.services.impl.HttpServiceImpl;
import dev.victorbrugnolo.webcrawler.utils.Logger;

public class Main {

  public static void main(String[] args) {
    var httpService = HttpServiceImpl.getInstance();
    var environment = EnvironmentAccessor.getInstance();
    var webCrawler = new BeanConfiguration().getWebCrawler(httpService, environment);

    var logger = Logger.getInstance();
    logger.logResults(webCrawler.crawl());
  }
}
