package com.github.hronom.scrape.dat.rooms.view.views;

import java.awt.*;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.*;

public class ScrapeMainView {
    public ScrapeMainView(ScrapeView scrapeView) {
        JPanel mainPanel = new JPanel();
        mainPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        GridBagLayout layout = new GridBagLayout();
        mainPanel.setLayout(layout);

        GridBagConstraints constraint = new GridBagConstraints();
        constraint.insets = new Insets(3, 3, 3, 3);
        constraint.weightx = 1;
        constraint.weighty = 1;
        constraint.gridx = 0;
        constraint.gridy = 0;
        constraint.gridwidth = 1;
        constraint.gridheight = 1;
        constraint.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrapeView, constraint);

        ArrayList<Image> images = new ArrayList<>();
        images.add(getImage("1447536874_Application.png"));
        images.add(getImage("1447536961_Application.png"));
        images.add(getImage("1447536965_Application.png"));
        images.add(getImage("1447536969_Application.png"));
        images.add(getImage("1447536973_Application.png"));
        images.add(getImage("1447536976_Application.png"));

        JFrame frame = new JFrame("Scrape dat rooms!!!");
        frame.setIconImages(images);
        frame.setContentPane(mainPanel);
        frame.setPreferredSize(new Dimension(800, 600));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private Image getImage(String fileName) {
        URL url = this.getClass().getResource(fileName);
        ImageIcon imageIcon = new ImageIcon(url);
        return imageIcon.getImage();
    }
}
