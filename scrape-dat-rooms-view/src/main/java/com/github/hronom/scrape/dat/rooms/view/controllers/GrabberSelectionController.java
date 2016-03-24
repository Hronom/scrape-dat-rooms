package com.github.hronom.scrape.dat.rooms.view.controllers;

import com.github.hronom.scrape.dat.rooms.view.views.ScrapeView;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class GrabberSelectionController {
    private final ScrapeView scrapeView;

    public GrabberSelectionController(ScrapeView scrapeViewArg) {
        scrapeView = scrapeViewArg;
        updateProxyAvailability();

        scrapeView.addGrabberItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                updateProxyAvailability();
            }
        });
    }

    private void updateProxyAvailability() {
        if (scrapeView.getSelectedGrabber().equals(ScrapeView.Grabber.HtmlUnit) ||
            scrapeView.getSelectedGrabber().equals(ScrapeView.Grabber.Ui4j) ||
            scrapeView.getSelectedGrabber().equals(ScrapeView.Grabber.JxBrowser) ||
            scrapeView.getSelectedGrabber().equals(ScrapeView.Grabber.jBrowserDriver) ||
            scrapeView.getSelectedGrabber().equals(ScrapeView.Grabber.OkHTTP)) {
            scrapeView.setProxyUsernameTextFieldEnabled(true);
            scrapeView.setProxyPasswordTextFieldEnabled(true);
            scrapeView.setProxyHostTextFieldEnabled(true);
            scrapeView.setProxyPortTextFieldEnabled(true);
        } else {
            scrapeView.setProxyUsernameTextFieldEnabled(false);
            scrapeView.setProxyPasswordTextFieldEnabled(false);
            scrapeView.setProxyHostTextFieldEnabled(false);
            scrapeView.setProxyPortTextFieldEnabled(false);
        }
    }
}
