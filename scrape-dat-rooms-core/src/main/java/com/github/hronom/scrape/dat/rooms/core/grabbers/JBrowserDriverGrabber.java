package com.github.hronom.scrape.dat.rooms.core.grabbers;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.ProxyConfig;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;
import com.machinepublishers.jbrowserdriver.UserAgent;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JBrowserDriverGrabber implements Grabber {
    private static final Logger logger = LogManager.getLogger();
    private final UserAgent userAgent;

    public JBrowserDriverGrabber(){
        userAgent = new UserAgent(
            UserAgent.Family.WEBKIT,
            "Google Inc.",
            "Linux",
            "Android 4.1.1",
            "5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Safari/535.19",
            "Mozilla/5.0 (Linux; Android 4.1.1; Nexus 7 Build/JRO03D) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Safari/535.19"
        );
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
        String url,
        String proxyHost,
        int proxyPort,
        String proxyUsername,
        String proxyPassword
    ) {
        // Set proxy.
        ProxyConfig proxyConfig;
        if (proxyHost != null && proxyPort > 0 && proxyUsername != null && proxyPassword != null) {
            proxyConfig = new ProxyConfig(
                ProxyConfig.Type.HTTP,
                proxyHost,
                proxyPort,
                proxyUsername,
                proxyPassword
            );
        } else if(proxyHost != null && proxyPort > 0) {
            proxyConfig = new ProxyConfig(
                ProxyConfig.Type.HTTP,
                proxyHost,
                proxyPort
            );
        } else {
            proxyConfig = new ProxyConfig();
        }

        // Construct jBrowserDriver.
        Settings settings =
            Settings
                .builder()
                .timezone(Timezone.UTC)
                .blockAds(false)
                .headless(true)
                .hostnameVerification(false)
                .ignoreDialogs(false)
                .javascript(true)
                .proxy(proxyConfig)
                .userAgent(userAgent)
                .build();

        JBrowserDriver driver = new JBrowserDriver(settings);

        // Get page.
        // This will block for the page load and any associated AJAX requests.
        driver.get(url);

        // You can get status code unlike other Selenium drivers.
        // It blocks for AJAX requests and page loads after clicks and keyboard events.
        logger.info("Status code: " + driver.getStatusCode());

        // Returns the page source in its current state, including any DOM updates that occurred
        // after page load.
        String html = driver.getPageSource();
        html = StringEscapeUtils.unescapeHtml4(html);

        // Close the browser. Allows this thread to terminate.
        driver.quit();

        return html;
    }
}
