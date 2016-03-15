package com.github.hronom.scrape.dat.rooms.view.controllers;

import com.github.hronom.scrape.dat.rooms.view.views.ScrapeView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class WebsiteUrlTypingController {
    private static final Logger logger = LogManager.getLogger();

    private final ScrapeView scrapeView;

    public WebsiteUrlTypingController(ScrapeView scrapeViewArg) {
        scrapeView = scrapeViewArg;
        scrapeView.addWebsiteUrlDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                try {
                    String str = e.getDocument().getText(0, e.getDocument().getLength());
                    if (str.contains("motel6.com")) {
                        scrapeView.selectParser(ScrapeView.Parser.Motel6);
                    } else if (str.contains("redroof.com")) {
                        scrapeView.selectParser(ScrapeView.Parser.RedRoof);
                    } else if (str.contains("redlion.com")) {
                        scrapeView.selectParser(ScrapeView.Parser.RedLion);
                    } else if (str.contains("ebookers.com")) {
                        scrapeView.selectParser(ScrapeView.Parser.ebookers);
                    }
                } catch (BadLocationException exception) {
                    logger.error(exception);
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
}
