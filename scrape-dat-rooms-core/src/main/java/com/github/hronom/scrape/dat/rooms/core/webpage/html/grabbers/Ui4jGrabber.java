package com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;
import com.ui4j.api.browser.PageConfiguration;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Ui4jGrabber implements Grabber {
    private static final Logger logger = LogManager.getLogger();

    private final BrowserEngine browserEngine;

    public Ui4jGrabber() {
        browserEngine = BrowserFactory.getWebKit();
    }

    @Override
    public void setProxyParameters(String proxyHost, int proxyPort) {
        // TODO
    }

    @Override
    public String grabHtml(String webpageUrl) {
        Page page = browserEngine.navigate(webpageUrl);
        String html = page.getDocument().getBody().getInnerHTML();
        html = StringEscapeUtils.unescapeHtml4(html);
        browserEngine.clearCookies();
        return html;
    }
}
