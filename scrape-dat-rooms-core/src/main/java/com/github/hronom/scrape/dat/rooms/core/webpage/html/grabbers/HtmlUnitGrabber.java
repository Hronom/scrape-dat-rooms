package com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;

public class HtmlUnitGrabber implements Grabber {
    private static final Logger logger = LogManager.getLogger();

    private final String userAgent =
        "Mozilla/5.0 (compatible; Googlebot/2.1; +http://www.google.com/bot.html)";

    private final ProxyConfig defaultProxyConfig = new ProxyConfig();

    private final WebClient webClient;

    public HtmlUnitGrabber() {
        BrowserVersion browserVersion = BrowserVersion.FIREFOX_38;
        //browserVersion.setUserAgent(userAgent);
        webClient = new WebClient(browserVersion);
        webClient.getOptions().setCssEnabled(true);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.getOptions().setPopupBlockerEnabled(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getOptions().setActiveXNative(true);
        webClient.getOptions().setAppletEnabled(true);
        webClient.getOptions().setUseInsecureSSL(true);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.setAjaxController(new AjaxController() {
            @Override
            public boolean processSynchron(HtmlPage page, WebRequest request, boolean async) {
                return true;
            }
        });
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
        String webpageUrl, String proxyHost, int proxyPort, String proxyUsername, String proxyPassword
    ) {
        try {
            // Set proxy.
            if (proxyHost != null && proxyPort > 0) {
                ProxyConfig proxyConfig = new ProxyConfig(proxyHost, proxyPort);
                webClient.getOptions().setProxyConfig(proxyConfig);
            } else {
                webClient.getOptions().setProxyConfig(defaultProxyConfig);
            }
            CredentialsProvider credentialsProvider = webClient.getCredentialsProvider();
            if (proxyUsername != null && proxyPassword != null) {
                UsernamePasswordCredentials credentials =
                    new UsernamePasswordCredentials(proxyUsername, proxyPassword);
                AuthScope authScope = new AuthScope(proxyHost, proxyPort);
                credentialsProvider.setCredentials(authScope, credentials);
            } else {
                credentialsProvider.clear();
            }

            // Get page.
            URL url = new URL(webpageUrl);
            HtmlPage page = webClient.getPage(url);
            String xml = page.asXml();
            xml = StringEscapeUtils.unescapeHtml4(xml);
            webClient.close();
            return xml;
        } catch (IOException exception) {
            logger.fatal(exception);
            return null;
        }
    }
}
