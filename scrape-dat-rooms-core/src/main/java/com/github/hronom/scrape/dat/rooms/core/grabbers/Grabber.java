package com.github.hronom.scrape.dat.rooms.core.grabbers;

public interface Grabber {
    String grabContent(String url);

    String grabContent(String url, String proxyHost, int proxyPort);

    String grabContent(
        String url,
        String proxyHost,
        int proxyPort,
        String proxyUsername,
        String proxyPassword
    );
}

