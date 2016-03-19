package com.github.hronom.scrape.dat.rooms.view.views;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;

import javax.swing.*;
import javax.swing.event.DocumentListener;

public class ScrapeView extends JPanel {
    private static final Logger logger = LogManager.getLogger();

    private final String progressBarTextPrefix = "Working, please wait...";

    private final JLabel websiteDataUrlLabel;
    private final JTextField websiteDataUrlTextField;

    private final JLabel browserEngineLabel;
    private final JComboBox<BrowserEngine> browserEngineComboBox;

    private final JLabel parserLabel;
    private final JComboBox<Parser> parserComboBox;

    private final JLabel proxyHostLabel;
    private final JTextField proxyHostTextField;

    private final JLabel proxyPortLabel;
    private final JTextField proxyPortTextField;

    private final JLabel proxyUsernameLabel;
    private final JTextField proxyUsernameTextField;

    private final JLabel proxyPasswordLabel;
    private final JTextField proxyPasswordTextField;

    private final JButton scrapeButton;

    private final JTextArea outputTextArea;

    private final JProgressBar progressBar;

    public enum BrowserEngine {
        HtmlUnit,
        Ui4j,
        JxBrowser,
        Jaunt
    }

    public enum Parser {
        Motel6,
        RedRoof,
        RedLion,
        ebookers,
        windsurfercrs
    }

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
            browserEngineComboBox = new JComboBox<>(BrowserEngine.values());
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
            parserComboBox = new JComboBox<>(Parser.values());
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
            proxyHostLabel = new JLabel("Proxy host:");

            constraint.weightx = 0;
            constraint.weighty = 0;
            constraint.gridx = 0;
            constraint.gridy = 3;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(proxyHostLabel, constraint);
        }

        {
            proxyHostTextField = new JTextField("");

            constraint.weightx = 1;
            constraint.weighty = 0;
            constraint.gridx = 1;
            constraint.gridy = 3;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(proxyHostTextField, constraint);
        }

        {
            proxyPortLabel = new JLabel("Proxy port:");

            constraint.weightx = 0;
            constraint.weighty = 0;
            constraint.gridx = 0;
            constraint.gridy = 4;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(proxyPortLabel, constraint);
        }

        {
            proxyPortTextField = new JTextField("");

            constraint.weightx = 1;
            constraint.weighty = 0;
            constraint.gridx = 1;
            constraint.gridy = 4;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(proxyPortTextField, constraint);
        }

        {
            proxyUsernameLabel = new JLabel("Proxy username:");

            constraint.weightx = 0;
            constraint.weighty = 0;
            constraint.gridx = 0;
            constraint.gridy = 5;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(proxyUsernameLabel, constraint);
        }

        {
            proxyUsernameTextField = new JTextField("");

            constraint.weightx = 1;
            constraint.weighty = 0;
            constraint.gridx = 1;
            constraint.gridy = 5;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(proxyUsernameTextField, constraint);
        }

        {
            proxyPasswordLabel = new JLabel("Proxy password:");

            constraint.weightx = 0;
            constraint.weighty = 0;
            constraint.gridx = 0;
            constraint.gridy = 6;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(proxyPasswordLabel, constraint);
        }

        {
            proxyPasswordTextField = new JTextField("");

            constraint.weightx = 1;
            constraint.weighty = 0;
            constraint.gridx = 1;
            constraint.gridy = 6;
            constraint.gridwidth = 1;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(proxyPasswordTextField, constraint);
        }

        {
            scrapeButton = new JButton("Scrape website");

            constraint.weightx = 1;
            constraint.weighty = 0;
            constraint.gridx = 0;
            constraint.gridy = 7;
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
            constraint.gridy = 8;
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
            constraint.gridy = 9;
            constraint.gridwidth = 2;
            constraint.gridheight = 1;
            constraint.fill = GridBagConstraints.BOTH;
            this.add(progressBar, constraint);
        }
    }

    public String getWebsiteUrl() {
        return websiteDataUrlTextField.getText();
    }

    public void addWebsiteUrlDocumentListener(DocumentListener listener) {
        websiteDataUrlTextField.getDocument().addDocumentListener(listener);
    }

    public BrowserEngine getSelectedBrowserEngine() {
        return (BrowserEngine) browserEngineComboBox.getSelectedItem();
    }

    public void addBrowserEngineItemListener(ItemListener listener) {
        browserEngineComboBox.addItemListener(listener);
    }

    public void selectParser(Parser parser) {
        parserComboBox.setSelectedItem(parser);
    }

    public Parser getSelectedParser() {
        return (Parser) parserComboBox.getSelectedItem();
    }

    public String getProxyUsername() {
        return proxyUsernameTextField.getText();
    }

    public void setProxyUsernameTextFieldEnabled(boolean enabled) {
        proxyUsernameTextField.setEnabled(enabled);
    }

    public String getProxyPassword() {
        return proxyPasswordTextField.getText();
    }

    public void setProxyPasswordTextFieldEnabled(boolean enabled) {
        proxyPasswordTextField.setEnabled(enabled);
    }

    public String getProxyHost() {
        return proxyHostTextField.getText();
    }

    public void setProxyHostTextFieldEnabled(boolean enabled) {
        proxyHostTextField.setEnabled(enabled);
    }

    public String getProxyPort() {
        return proxyPortTextField.getText();
    }

    public void setProxyPortTextFieldEnabled(boolean enabled) {
        proxyPortTextField.setEnabled(enabled);
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
