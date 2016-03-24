package com.github.hronom.scrape.dat.rooms.core.grabbers;

import com.teamdev.jxbrowser.chromium.AuthRequiredParams;
import com.teamdev.jxbrowser.chromium.Browser;
import com.teamdev.jxbrowser.chromium.CustomProxyConfig;
import com.teamdev.jxbrowser.chromium.DirectProxyConfig;
import com.teamdev.jxbrowser.chromium.HostPortPair;
import com.teamdev.jxbrowser.chromium.LoggerProvider;
import com.teamdev.jxbrowser.chromium.javafx.DefaultNetworkDelegate;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;

public class JxBrowserGrabber implements Grabber {
    private static final Logger logger = LogManager.getLogger();

    public JxBrowserGrabber() {
        System.setProperty("teamdev.license.info", "true");
        Handler log4jHandler = createHandler();
        for (Handler handler : LoggerProvider.getBrowserLogger().getHandlers()) {
            LoggerProvider.getBrowserLogger().removeHandler(handler);
        }
        LoggerProvider.getBrowserLogger().addHandler(log4jHandler);

        for (Handler handler : LoggerProvider.getIPCLogger().getHandlers()) {
            LoggerProvider.getIPCLogger().removeHandler(handler);
        }
        LoggerProvider.getIPCLogger().addHandler(log4jHandler);

        for (Handler handler : LoggerProvider.getChromiumProcessLogger().getHandlers()) {
            LoggerProvider.getChromiumProcessLogger().removeHandler(handler);
        }
        LoggerProvider.getChromiumProcessLogger().addHandler(log4jHandler);
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
        String url, String proxyHost, int proxyPort, String proxyUsername, String proxyPassword
    ) {
        try {
            Browser browser;

            // Set proxy.
            if (proxyHost != null && proxyPort > 0) {
                HostPortPair hostPortPair = new HostPortPair(proxyHost, proxyPort);
                CustomProxyConfig customProxyConfig = new CustomProxyConfig(hostPortPair,
                    hostPortPair,
                    hostPortPair
                );
                browser = new Browser(customProxyConfig);
            } else {
                DirectProxyConfig directProxyConfig = new DirectProxyConfig();
                browser = new Browser(directProxyConfig);
            }

            if (proxyUsername != null && proxyPassword != null) {
                browser.getContext().getNetworkService()
                    .setNetworkDelegate(new DefaultNetworkDelegate() {
                        @Override
                        public boolean onAuthRequired(AuthRequiredParams params) {
                            if (params.isProxy()) {
                                params.setUsername(proxyUsername);
                                params.setPassword(proxyPassword);
                                return false;
                            }
                            return true;
                        }
                    });
            } else {
                browser
                    .getContext()
                    .getNetworkService()
                    .setNetworkDelegate(new DefaultNetworkDelegate());
            }

            browser.loadURL(url);

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
            browser.dispose();
            return html;
        } catch (ExceptionInInitializerError exceptionInInitializerError) {
            logger.error(exceptionInInitializerError);
            return null;
        }
    }

    private Handler createHandler() {
        return new Handler() {
            @Override
            public void publish(LogRecord record) {
                if (record.getLevel() == Level.INFO) {
                    logger.info(record.getMessage());
                } else if (record.getLevel() == Level.WARNING) {
                    logger.warn(record.getMessage());
                } else if (record.getLevel() == Level.SEVERE) {
                    logger.fatal(record.getMessage());
                } else {
                    logger.info(record.getMessage());
                }
            }

            @Override
            public void flush() {

            }

            @Override
            public void close() throws SecurityException {

            }
        };
    }
}
