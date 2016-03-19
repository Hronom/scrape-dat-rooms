package com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers;

import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.Settings;
import com.machinepublishers.jbrowserdriver.Timezone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class JBrowserDriverGrabber implements Grabber {
    private static final Logger logger = LogManager.getLogger();

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
        Settings settings =
            Settings
                .builder()
                .timezone(Timezone.AMERICA_NEWYORK)
                .build();
        JBrowserDriver driver = new JBrowserDriver(settings);

        // This will block for the page load and any associated AJAX requests.
        driver.get(webpageUrl);

        // You can get status code unlike other Selenium drivers.
        // It blocks for AJAX requests and page loads after clicks and keyboard events.
        logger.info("Status code: " + driver.getStatusCode());

        // Returns the page source in its current state, including any DOM updates that occurred
        // after page load.
        String result = driver.getPageSource();

        // Close the browser. Allows this thread to terminate.
        driver.quit();

        return result;
    }
}
