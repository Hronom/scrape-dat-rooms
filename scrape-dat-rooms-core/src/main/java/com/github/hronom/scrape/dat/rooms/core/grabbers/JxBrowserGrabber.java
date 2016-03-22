package com.github.hronom.scrape.dat.rooms.core.grabbers;

import com.teamdev.jxbrowser.chromium.Browser;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JxBrowserGrabber implements Grabber {
    private static final Logger logger = LogManager.getLogger();

    private Browser browser;

    public JxBrowserGrabber() {
        try {
            browser = new Browser();
        } catch (ExceptionInInitializerError exceptionInInitializerError) {
            logger.error(exceptionInInitializerError);
        }
    }

    @Override
    public String grabContent(String url) {
        return grabContent(url, null, 0, null, null);
    }

    @Override
    public String grabContent(String url, String proxyHost, int proxyPort) {
        return grabContent(url, proxyHost, proxyPort, null, null);
    }

    @Override
    public String grabContent(
        String webpageUrl, String proxyHost, int proxyPort, String proxyUsername, String proxyPassword
    ) {
        browser.loadURL(webpageUrl);
        // Wait for loading.
        while (browser.isLoading()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                logger.error(e);
            }
        }
        String html = browser.getHTML();
        html = StringEscapeUtils.unescapeHtml4(html);
        browser.stop();
        return html;
    }
}
