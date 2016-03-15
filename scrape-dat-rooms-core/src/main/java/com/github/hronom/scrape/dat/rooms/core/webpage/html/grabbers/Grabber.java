package com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers;

public interface Grabber {
    void setProxyParameters(String proxyHost, int proxyPort);

    String grabHtml(String webpageUrl);
}
