package com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers;

import com.jaunt.ResponseException;
import com.jaunt.UserAgent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JauntGrabber implements Grabber {
    private static final Logger logger = LogManager.getLogger();

    private final UserAgent userAgent;

    public JauntGrabber() {
        // Create new userAgent (headless browser).
        userAgent = new UserAgent();
        userAgent.settings.autoRedirect = true;
        userAgent.settings.checkSSLCerts = false;
        userAgent.settings.showHeaders = true;
        userAgent.settings.showTravel = true;
        userAgent.settings.showWarnings = true;
    }

    @Override
    public String grabHtml(String webpageUrl) {
        return grabHtml(webpageUrl, null, 0, null, null);
    }

    @Override
    public String grabHtml(String webpageUrl, String proxyHost, int proxyPort) {
        return grabHtml(webpageUrl, proxyHost, proxyPort, null, null);
    }

    @Override
    public String grabHtml(
        String webpageUrl,
        String proxyHost,
        int proxyPort,
        String proxyUsername,
        String proxyPassword
    ) {
        try {
            userAgent.visit(webpageUrl);
            return userAgent.doc.innerHTML();
        } catch (ResponseException exception) {
            logger.fatal("Fail!", exception);
            return null;
        }
    }
}
