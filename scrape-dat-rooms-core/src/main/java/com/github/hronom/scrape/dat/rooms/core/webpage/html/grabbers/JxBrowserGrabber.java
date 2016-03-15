package com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers;

import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.BrowserContext;
import com.teamdev.jxbrowser.chromium.BrowserContextParams;
import com.teamdev.jxbrowser.chromium.DirectProxyConfig;
import com.teamdev.jxbrowser.chromium.ProxyConfig;

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
    public void setProxyParameters(String proxyHost, int proxyPort) {
        // TODO
    }

    @Override
    public String grabHtml(String webpageUrl) {
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
