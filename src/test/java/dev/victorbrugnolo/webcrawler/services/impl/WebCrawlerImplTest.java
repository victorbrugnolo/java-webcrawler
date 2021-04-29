package dev.victorbrugnolo.webcrawler.services.impl;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import dev.victorbrugnolo.webcrawler.configuration.Environment;
import dev.victorbrugnolo.webcrawler.services.HttpService;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class WebCrawlerImplTest {

  @InjectMocks
  private WebCrawlerImpl webCrawler;

  @Mock
  private Environment environment;

  @Mock
  private HttpService httpService;

  @BeforeEach
  public void setUp() {
    MockitoAnnotations.initMocks(this);
  }

  @ParameterizedTest
  @MethodSource("keywords")
  void mustGetIllegalArgumentExceptionWhenKeywordIsInvalid(final String keyword) {
    when(environment.getValue("KEYWORD")).thenReturn(keyword);
    assertThrows(IllegalArgumentException.class, () -> webCrawler.crawl());
  }

  @Test
  void mustGetIllegalArgumentExceptionWhenKeywordIsSmaller() {
    when(environment.getValue("KEYWORD")).thenReturn(getRandomString(3));
    assertThrows(IllegalArgumentException.class, () -> webCrawler.crawl());
  }

  @Test
  void mustGetIllegalArgumentExceptionWhenKeywordIsBigger() {
    when(environment.getValue("KEYWORD")).thenReturn(getRandomString(33));
    assertThrows(IllegalArgumentException.class, () -> webCrawler.crawl());
  }

  @Test
  void mustCrawlWhenBaseUrlIsInvalid() {
    when(environment.getValue("BASE_URL")).thenReturn("https://");
    when(environment.getValue("KEYWORD")).thenReturn("keyword");
    assertDoesNotThrow(() -> webCrawler.crawl());
  }

  @Test
  void mustCrawlSuccess() {
    String httpContent =
        "<http>KEYWORD<a href=\"/images\"></a><a href=\"https://google.com/search\"></a></http>";
    when(environment.getValue("BASE_URL")).thenReturn("https://google.com");
    when(environment.getValue("KEYWORD")).thenReturn("keyword");
    when(environment.getValue("MAX_RESULTS")).thenReturn("5");
    when(httpService.getUrlContent(any(URI.class))).thenReturn(httpContent);
    assertDoesNotThrow(() -> webCrawler.crawl());
  }

  static Stream<String> keywords() {
    return Stream.of("", "><!a", null);
  }

  private String getRandomString(final int length) {
    byte[] array = new byte[length];
    new Random().nextBytes(array);
    return new String(array, StandardCharsets.UTF_8);
  }
}
