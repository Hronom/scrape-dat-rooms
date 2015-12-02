package com.github.hronom.scrape.dat.rooms.view.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class ScrapeView extends JPanel {
    private static final Logger logger = LogManager.getLogger();

    private final String progressBarTextPrefix = "Working, please wait...";

    private final JLabel websiteDataUrlLabel;
    private final JTextField websiteDataUrlTextField;

    private final JLabel browserEngineLabel;
    private final JComboBox<String> browserEngineComboBox;

    private final JLabel parserLabel;
    private final JComboBox<String> parserComboBox;

    private final JButton scrapeButton;

    private final JTextArea outputTextArea;

    private final JProgressBar progressBar;

    public ScrapeView() {
        this.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.insets = new Insets(3, 3, 3, 3);
        constraint.weightx = 1;
        constraint.weighty = 0;
        constraint.gridwidth = 1;
        constraint.anchor = GridBagConstraints.CENTER;

        {
            websiteDataUrlLabel = new JLabel("Website data URL:");

            constraint.weightx = 0;
            constraint.weighty = 0;
            constraint.gridx = 0;
            constraint.gridy = 0;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(websiteDataUrlLabel, constraint);
        }

        {
            websiteDataUrlTextField = new JTextField("");
            websiteDataUrlTextField.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    try {
                        String str = e.getDocument().getText(0, e.getDocument().getLength());
                        if (str.contains("motel6.com")) {
                            parserComboBox.setSelectedIndex(0);
                        } else if (str.contains("redroof.com")) {
                            parserComboBox.setSelectedIndex(1);
                        } else if (str.contains("redlion.com")) {
                            parserComboBox.setSelectedIndex(2);
                        }else if (str.contains("ebookers.com")) {
                            parserComboBox.setSelectedIndex(3);
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

            constraint.weightx = 1;
            constraint.weighty = 0;
            constraint.gridx = 1;
            constraint.gridy = 0;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(websiteDataUrlTextField, constraint);
        }

        {
            browserEngineLabel = new JLabel("Browser engine:");

            constraint.weightx = 0;
            constraint.weighty = 0;
            constraint.gridx = 0;
            constraint.gridy = 1;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(browserEngineLabel, constraint);
        }

        {
            String[] values = new String[] {"HtmlUnit", "Ui4j", "JxBrowser"};
            browserEngineComboBox = new JComboBox<>(values);
            browserEngineComboBox.setSelectedIndex(2);

            constraint.weightx = 1;
            constraint.weighty = 0;
            constraint.gridx = 1;
            constraint.gridy = 1;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(browserEngineComboBox, constraint);
        }

        {
            parserLabel = new JLabel("Website parser:");

            constraint.weightx = 0;
            constraint.weighty = 0;
            constraint.gridx = 0;
            constraint.gridy = 2;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(parserLabel, constraint);
        }

        {
            String[] values = new String[] {"Motel6", "RedRoof", "RedLion", "ebookers"};
            parserComboBox = new JComboBox<>(values);
            parserComboBox.setSelectedIndex(0);

            constraint.weightx = 1;
            constraint.weighty = 0;
            constraint.gridx = 1;
            constraint.gridy = 2;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(parserComboBox, constraint);
        }

        {
            scrapeButton = new JButton("Scrape website");

            constraint.weightx = 1;
            constraint.weighty = 0;
            constraint.gridx = 0;
            constraint.gridy = 3;
            constraint.gridwidth = 2;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(scrapeButton, constraint);
        }

        {
            outputTextArea = new JTextArea();
            outputTextArea.setEditable(false);
            outputTextArea.setWrapStyleWord(false);
            outputTextArea.setAutoscrolls(true);

            JScrollPane scrollPane = new JScrollPane(outputTextArea);
            scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

            constraint.weightx = 1;
            constraint.weighty = 1;
            constraint.gridx = 0;
            constraint.gridy = 4;
            constraint.gridwidth = 2;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(scrollPane, constraint);
        }

        {
            progressBar = new JProgressBar();
            progressBar.setString(progressBarTextPrefix);
            progressBar.setStringPainted(true);
            progressBar.setIndeterminate(true);
            progressBar.setVisible(false);

            constraint.weightx = 1;
            constraint.weighty = 0;
            constraint.gridx = 0;
            constraint.gridy = 5;
            constraint.gridwidth = 2;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(progressBar, constraint);
        }
    }

    public String getSelectedBrowserEngine() {
        return (String) browserEngineComboBox.getSelectedItem();
    }

    public String getSelectedParser() {
        return (String) parserComboBox.getSelectedItem();
    }

    public void addScrapeButtonActionListener(ActionListener actionListener) {
        scrapeButton.addActionListener(actionListener);
    }

    public void setWebsiteUrlTextFieldEnabled(boolean enabled) {
        websiteDataUrlTextField.setEnabled(enabled);
    }

    public void setScrapeButtonEnabled(boolean enabled) {
        scrapeButton.setEnabled(enabled);
    }

    public String getWebsiteUrl() {
        return websiteDataUrlTextField.getText();
    }

    public void setOutput(String text) {
        outputTextArea.setText(text);
    }

    public void setWorkInProgress(boolean working) {
        progressBar.setVisible(working);
    }

    public void setProgressBarTaskText(String taskText) {
        progressBar.setString(progressBarTextPrefix + " (" + taskText + ")");
    }
}
