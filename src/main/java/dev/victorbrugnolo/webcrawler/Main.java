package dev.victorbrugnolo.webcrawler;

import dev.victorbrugnolo.webcrawler.configuration.BeanConfiguration;
import dev.victorbrugnolo.webcrawler.configuration.Environment;
import dev.victorbrugnolo.webcrawler.configuration.EnvironmentAccessor;
import dev.victorbrugnolo.webcrawler.services.HttpService;
import dev.victorbrugnolo.webcrawler.services.WebCrawler;
import dev.victorbrugnolo.webcrawler.services.impl.HttpServiceImpl;
import dev.victorbrugnolo.webcrawler.utils.Logger;

public class Main {

  public static void main(String[] args) {
    HttpService httpService = HttpServiceImpl.getInstance();
    Environment environment = EnvironmentAccessor.getInstance();
    WebCrawler webCrawler = new BeanConfiguration().getWebCrawler(httpService, environment);

    Logger logger = Logger.getInstance();
    logger.logResults(webCrawler.crawl());
  }
}
