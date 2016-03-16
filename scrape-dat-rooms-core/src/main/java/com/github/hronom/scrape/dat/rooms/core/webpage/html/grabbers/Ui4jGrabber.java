package com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers;

import com.ui4j.api.browser.BrowserEngine;
import com.ui4j.api.browser.BrowserFactory;
import com.ui4j.api.browser.Page;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class Ui4jGrabber implements Grabber {
    private static final Logger logger = LogManager.getLogger();

    private final Authenticator defaultAuthenticator = new Authenticator() {
        public PasswordAuthentication getPasswordAuthentication() {
            return null;
        }
    };

    private final BrowserEngine browserEngine;

    public Ui4jGrabber() {
        browserEngine = BrowserFactory.getWebKit();
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
        String webpageUrl, String proxyHost, int proxyPort, final String proxyUsername, final String proxyPassword
    ) {
        // Set proxy.
        if (proxyHost != null && proxyPort > 0) {
            System.setProperty("http.proxyHost", proxyHost);
            System.setProperty("http.proxyPort", String.valueOf(proxyPort));
        } else {
            System.clearProperty("http.proxyHost");
            System.clearProperty("http.proxyPort");
        }
        if (proxyUsername != null && proxyPassword != null) {
            System.setProperty("http.proxyUser", proxyUsername);
            System.setProperty("http.proxyPassword", proxyPassword);
            Authenticator.setDefault(
                new Authenticator() {
                    public PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(proxyUsername, proxyPassword.toCharArray());
                    }
                }
            );
        }
        else {
            System.clearProperty("http.proxyUser");
            System.clearProperty("http.proxyPassword");
            Authenticator.setDefault(defaultAuthenticator);
        }

        Page page = browserEngine.navigate(webpageUrl);
        String html = page.getDocument().getBody().getInnerHTML();
        html = StringEscapeUtils.unescapeHtml4(html);
        browserEngine.clearCookies();
        return html;
    }
}
