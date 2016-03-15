package com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers;

import com.gargoylesoftware.htmlunit.AjaxController;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;

public class HtmlUnitGrabber implements Grabber {
    private static final Logger logger = LogManager.getLogger();

    private final WebClient webClient;

    public HtmlUnitGrabber() {
        webClient = new WebClient(BrowserVersion.FIREFOX_38);
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
    public void setProxyParameters(String proxyHost, int proxyPort) {
        ProxyConfig proxyConfig = webClient.getOptions().getProxyConfig();
        proxyConfig.setProxyHost(proxyHost);
        proxyConfig.setProxyPort(proxyPort);
    }

    @Override
    public String grabHtml(String webpageUrl) {
        try {
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
