package com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers;

import com.teamdev.jxbrowser.chromium.Browser;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JxBrowserGrabber implements Grabber {
    private static final Logger logger = LogManager.getLogger();

    private final Browser browser;

    public JxBrowserGrabber() {
        browser = new Browser();
    }

    @Override
    public String grabHtml(String webpageUrl) {
        browser.loadURL(webpageUrl);
        // Wait for loading.
        while (browser.isLoading()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        String html = browser.getHTML();
        html = StringEscapeUtils.unescapeHtml4(html);
        browser.stop();
        return html;
    }
}
