package dev.victorbrugnolo.webcrawler.services;

import java.net.URI;

public interface HttpService {

  String getUrlContent(URI url);

}
