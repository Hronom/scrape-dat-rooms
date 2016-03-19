package com.github.hronom.scrape.dat.rooms.view.controllers;

import com.github.hronom.scrape.dat.rooms.view.views.ScrapeView;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class BrowserEngineSelectionController {
    private final ScrapeView scrapeView;

    public BrowserEngineSelectionController(ScrapeView scrapeViewArg) {
        scrapeView = scrapeViewArg;
        if (scrapeView.getSelectedBrowserEngine().equals(ScrapeView.BrowserEngine.HtmlUnit) ||
            scrapeView.getSelectedBrowserEngine().equals(ScrapeView.BrowserEngine.Ui4j) ||
            scrapeView.getSelectedBrowserEngine().equals(ScrapeView.BrowserEngine.jBrowserDriver)) {
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

        scrapeView.addBrowserEngineItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED &&
                    e.getItem().equals(ScrapeView.BrowserEngine.HtmlUnit) ||
                    scrapeView.getSelectedBrowserEngine().equals(ScrapeView.BrowserEngine.Ui4j) ||
                    scrapeView.getSelectedBrowserEngine().equals(ScrapeView.BrowserEngine.jBrowserDriver)) {
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
        });
    }
}
