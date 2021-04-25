package dev.victorbrugnolo.webcrawler.services.impl;

import static java.lang.System.out;

import dev.victorbrugnolo.webcrawler.configuration.Environment;
import dev.victorbrugnolo.webcrawler.services.HttpService;
import dev.victorbrugnolo.webcrawler.services.WebCrawler;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.regex.Pattern;

public class WebCrawlerImpl implements WebCrawler {

  private HttpService httpService;
  private Environment environment;

  private WebCrawlerImpl() {
  }

  private String rootPage;

  public WebCrawlerImpl(final HttpService httpService, final Environment environment) {
    this.httpService = httpService;
    this.environment = environment;
    this.rootPage = environment.getValue("BASE_URL");
  }

  @Override
  public Set<String> crawl() {
    Queue<String> toVisit = new LinkedList<>();
    Set<String> found = new HashSet<>();
    Set<String> marked = new HashSet<>();

    String keyword = environment.getValue("KEYWORD");

    if (!isValidKeyword(keyword)) {
      throw new IllegalArgumentException("[ERROR] Invalid keyword");
    }

    out.printf("[INFO] Search starting with base URL %s and keyword '%s'%n", rootPage, keyword);

    toVisit.add(rootPage);

    executeCrawler(toVisit, found, marked, keyword);

    return found;
  }

  private void executeCrawler(final Queue<String> toVisit, final Set<String> found,
      final Set<String> marked, final String keyword) {

    if (!toVisit.isEmpty() && validateMaxResults(found.size())) {
      String crawledUrl = toVisit.poll();

      try {
        String pageContent = this.httpService.getUrlContent(new URI(crawledUrl));

        out.printf("[INFO] Crawling url: %s%n", crawledUrl);

        if (pageContent.toLowerCase().contains(keyword.toLowerCase())) {
          out.printf("[INFO] Result found on url: %s%n", crawledUrl);
          found.add(crawledUrl);
        }

        getAbsolutPaths(pageContent, toVisit, marked);
        getRelativePaths(pageContent, toVisit, marked);

        executeCrawler(toVisit, found, marked, keyword);
      } catch (URISyntaxException ex) {
        out.printf("[ERROR] Invalid uri: %s%n", crawledUrl);
        executeCrawler(toVisit, found, marked, keyword);
      }
    }

  }

  private boolean isValidKeyword(String keyword) {
    return Objects.nonNull(keyword) && !keyword.isEmpty() && keyword.length() >= 4
        && keyword.length() <= 32 && keyword.matches("^[a-zA-Z0-9]*$");
  }

  private int getMaxResults() {
    try {
      return Integer.parseInt(environment.getValue("MAX_RESULTS"));
    } catch (Exception ex) {
      return -1;
    }
  }

  private boolean validateMaxResults(final int foundSize) {
    if (getMaxResults() <= 0) {
      return true;
    }

    return foundSize < getMaxResults();
  }

  private void getRelativePaths(final String pageContent, final Queue<String> toVisit,
      final Set<String> marked) {
    var hrefRegex = "href=\"(.*?)\"";
    var relativePathPattern = Pattern.compile(hrefRegex);
    var relativePathMatcher = relativePathPattern.matcher(pageContent);

    while (relativePathMatcher.find()) {
      String relativePath = relativePathMatcher.group(1);
      String path = rootPage + relativePath.replace("../", "");

      if (isValidRelativePath(relativePath) && !marked.contains(path)) {
        marked.add(path);
        out.printf("[INFO] Url added for crawling: %s%n", path);
        toVisit.add(path);
      }
    }
  }

  private boolean isValidRelativePath(final String relativePath) {
    var urlRegex = "^(http|https)://.*$";
    return !relativePath.matches(urlRegex) && !relativePath.contains("stylesheet") &&
        !relativePath.toLowerCase().contains("mailto:") && !relativePath.startsWith("ftp:");
  }

  private void getAbsolutPaths(final String pageContent, final Queue<String> toVisit,
      final Set<String> marked) {
    var absolutPathRegex = "http[s]*://(\\w+\\.)*(\\w+)";
    var absolutPathPattern = Pattern.compile(absolutPathRegex);
    var absolutPathMatcher = absolutPathPattern.matcher(pageContent);

    while (absolutPathMatcher.find()) {
      String absolutPath = absolutPathMatcher.group();

      if (absolutPath.startsWith(rootPage) && !marked.contains(absolutPath)) {
        marked.add(absolutPath);
        out.printf("[INFO] Url added for crawling: %s%n", absolutPath);
        toVisit.add(absolutPath);
      }
    }
  }
}
