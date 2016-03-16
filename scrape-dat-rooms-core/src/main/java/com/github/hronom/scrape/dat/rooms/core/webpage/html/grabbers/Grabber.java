package com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers;

public interface Grabber {
    String grabHtml(String webpageUrl);

    String grabHtml(String webpageUrl, String proxyHost, int proxyPort);

    String grabHtml(
        String webpageUrl,
        String proxyHost,
        int proxyPort,
        String proxyUsername,
        String proxyPassword
    );
}

