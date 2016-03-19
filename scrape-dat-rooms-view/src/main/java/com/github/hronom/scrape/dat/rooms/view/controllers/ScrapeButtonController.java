package com.github.hronom.scrape.dat.rooms.view.controllers;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.github.hronom.scrape.dat.rooms.core.html.parsers.EbookersHtmlParser;
import com.github.hronom.scrape.dat.rooms.core.html.parsers.Motel6HtmlParser;
import com.github.hronom.scrape.dat.rooms.core.html.parsers.RedLionHtmlParser;
import com.github.hronom.scrape.dat.rooms.core.html.parsers.RedRoofHtmlParser;
import com.github.hronom.scrape.dat.rooms.core.html.parsers.RoomInfo;
import com.github.hronom.scrape.dat.rooms.core.html.parsers.RoomPhotoDownloader;
import com.github.hronom.scrape.dat.rooms.core.html.parsers.WindsurfercrsHtmlParser;
import com.github.hronom.scrape.dat.rooms.core.html.parsers.utils.NetworkUtils;
import com.github.hronom.scrape.dat.rooms.core.html.parsers.utils.PathsUtils;
import com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers.Grabber;
import com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers.HtmlUnitGrabber;
import com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers.JauntGrabber;
import com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers.JxBrowserGrabber;
import com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers.Ui4jGrabber;
import com.github.hronom.scrape.dat.rooms.view.views.ScrapeView;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.concurrent.Executors;

public class ScrapeButtonController {
    private static final Logger logger = LogManager.getLogger();

    private final ScrapeView scrapeView;

    private final HtmlUnitGrabber htmlUnitGrabber = new HtmlUnitGrabber();
    private final Ui4jGrabber ui4jGrabber = new Ui4jGrabber();
    private final JxBrowserGrabber jxBrowserGrabber = new JxBrowserGrabber();
    private final JauntGrabber jauntGrabber = new JauntGrabber();

    private final Path resultsPath = Paths.get("results");

    private final Path motel6ResultsDir = resultsPath.resolve("motel6");
    private final Path motel6ResultsPhotosDir = motel6ResultsDir.resolve("photos");
    private final Motel6HtmlParser motel6HtmlParser = new Motel6HtmlParser();

    private final Path redRoofResultsDir = resultsPath.resolve("redroof");
    private final Path redRoofResultsPhotosDir = redRoofResultsDir.resolve("photos");
    private final RedRoofHtmlParser redRoofHtmlParser = new RedRoofHtmlParser();

    private final Path redLionResultsDir = resultsPath.resolve("redlion");
    private final Path redLionResultsPhotosDir = redLionResultsDir.resolve("photos");
    private final RedLionHtmlParser redLionHtmlParser = new RedLionHtmlParser();

    private final Path ebookersResultsDir = resultsPath.resolve("ebookers");
    private final Path ebookersResultsPhotosDir = ebookersResultsDir.resolve("photos");
    private final EbookersHtmlParser ebookersHtmlParser = new EbookersHtmlParser();

    private final Path windsurfercrsResultsDir = resultsPath.resolve("windsurfercrs");
    private final Path windsurfercrsResultsPhotosDir = windsurfercrsResultsDir.resolve("photos");
    private final WindsurfercrsHtmlParser windsurfercrsHtmlParser = new WindsurfercrsHtmlParser();

    public ScrapeButtonController(ScrapeView scrapeViewArg) {
        scrapeView = scrapeViewArg;
        scrapeView.addScrapeButtonActionListener(createScrapeButtonActionListener());
    }

    public ActionListener createScrapeButtonActionListener() {
        return new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent event) {
                Executors.newSingleThreadExecutor().submit(new Runnable() {
                    public void run() {
                        long beginTime = System.currentTimeMillis();

                        // Disable fields in view.
                        scrapeView.setWebsiteUrlTextFieldEnabled(false);
                        scrapeView.setScrapeButtonEnabled(false);
                        scrapeView.setWorkInProgress(true);
                        scrapeView.setOutput("");

                        scrapeView.setProgressBarTaskText("initializing");
                        logger.info("Start processing...");

                        // Output input parameters.
                        if (!scrapeView.getWebsiteUrl().isEmpty()) {
                            logger.info("Input parameters: \"" + scrapeView.getWebsiteUrl() + "\"");
                        }

                        scrapeView.setProgressBarTaskText("requesting page");
                        logger.info("Requesting page...");

                        String html = null;
                        ScrapeView.BrowserEngine selectedBrowserEngine = scrapeView
                            .getSelectedBrowserEngine();

                        String webpageUrl = scrapeView.getWebsiteUrl();
                        String proxyHost = scrapeView.getProxyHost();
                        String proxyPort = scrapeView.getProxyPort();
                        String proxyUsername = scrapeView.getProxyUsername();
                        String proxyPassword = scrapeView.getProxyPassword();

                        switch (selectedBrowserEngine) {
                            case HtmlUnit:
                                html = getHtml(htmlUnitGrabber,
                                    webpageUrl,
                                    proxyHost,
                                    proxyPort,
                                    proxyUsername,
                                    proxyPassword
                                );
                                break;
                            case Ui4j:
                                html = getHtml(ui4jGrabber,
                                    webpageUrl,
                                    proxyHost,
                                    proxyPort,
                                    proxyUsername,
                                    proxyPassword
                                );
                                break;
                            case JxBrowser:
                                html = getHtml(jxBrowserGrabber,
                                    webpageUrl,
                                    proxyHost,
                                    proxyPort,
                                    proxyUsername,
                                    proxyPassword
                                );
                                break;
                            case Jaunt:
                                html = getHtml(jauntGrabber,
                                    webpageUrl,
                                    proxyHost,
                                    proxyPort,
                                    proxyUsername,
                                    proxyPassword
                                );
                                break;
                            default:
                                logger.error("Unknown browser engine: " + selectedBrowserEngine);
                                break;
                        }

                        if (html != null) {
                            scrapeView.setProgressBarTaskText("parsing HTML");
                            logger.info("Parse HTML");
                            ArrayList<RoomInfo> roomInfos;
                            ScrapeView.Parser selectedParser = scrapeView.getSelectedParser();
                            switch (selectedParser) {
                                case Motel6: {
                                    prepareFolder(motel6ResultsDir, motel6ResultsPhotosDir);
                                    RoomPhotoDownloader downloader = createRoomPhotoDownloader(
                                        motel6ResultsPhotosDir);
                                    roomInfos = motel6HtmlParser.parse(html, downloader);
                                    if (roomInfos != null) {
                                        save(roomInfos, motel6ResultsDir);
                                    }
                                    break;
                                }
                                case RedRoof: {
                                    prepareFolder(redRoofResultsDir, redRoofResultsPhotosDir);
                                    RoomPhotoDownloader downloader = createRoomPhotoDownloader(
                                        redRoofResultsPhotosDir);
                                    roomInfos = redRoofHtmlParser.parse(html, downloader);
                                    if (roomInfos != null) {
                                        save(roomInfos, redRoofResultsDir);
                                    }
                                    break;
                                }
                                case RedLion: {
                                    prepareFolder(redLionResultsDir, redLionResultsPhotosDir);
                                    RoomPhotoDownloader downloader = createRoomPhotoDownloader(
                                        redLionResultsPhotosDir);
                                    roomInfos = redLionHtmlParser.parse(html, downloader);
                                    if (roomInfos != null) {
                                        save(roomInfos, redLionResultsDir);
                                    }
                                    break;
                                }
                                case ebookers: {
                                    prepareFolder(ebookersResultsDir, ebookersResultsPhotosDir);
                                    RoomPhotoDownloader downloader = createRoomPhotoDownloader(
                                        ebookersResultsPhotosDir);
                                    roomInfos = ebookersHtmlParser.parse(html, downloader);
                                    if (roomInfos != null) {
                                        save(roomInfos, ebookersResultsDir);
                                    }
                                    break;
                                }
                                case windsurfercrs: {
                                    prepareFolder(
                                        windsurfercrsResultsDir,
                                        windsurfercrsResultsPhotosDir
                                    );
                                    RoomPhotoDownloader downloader =
                                        createRoomPhotoDownloader(windsurfercrsResultsPhotosDir);
                                    roomInfos = windsurfercrsHtmlParser.parse(html, downloader);
                                    if (roomInfos != null) {
                                        save(roomInfos, windsurfercrsResultsDir);
                                    }
                                    break;
                                }
                                default: {
                                    logger
                                        .error("Unknown browser engine: " + selectedBrowserEngine);
                                    break;
                                }
                            }
                        } else {
                            logger.error("No HTML");
                        }

                        long endTime = System.currentTimeMillis();
                        logger.info("Process time: " + (endTime - beginTime) + " ms.");
                        logger.info("Processing complete.");

                        // Enable fields in view.
                        scrapeView.setWorkInProgress(false);
                        scrapeView.setScrapeButtonEnabled(true);
                        scrapeView.setWebsiteUrlTextFieldEnabled(true);
                    }
                });
            }
        };
    }

    private void prepareFolder(Path resultsPath, Path resultsPhotosDir) {
        try {
            PathsUtils.deletePathIfExists(resultsPath);
            PathsUtils.createDirectoryIfNotExists(resultsPath);
            PathsUtils.createDirectoryIfNotExists(resultsPhotosDir);
        } catch (IOException e) {
            logger.fatal(e);
        }
    }

    private RoomPhotoDownloader createRoomPhotoDownloader(final Path resultsPhotosDir) {
        return new RoomPhotoDownloader() {
            @Override
            public Path download(String url) {
                try {
                    return NetworkUtils.downloadImage(url, resultsPhotosDir).toAbsolutePath();
                } catch (IOException e) {
                    logger.error(e);
                }
                return null;
            }
        };
    }

    private void save(ArrayList<RoomInfo> roomInfos, Path resultsPath) {
        CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);

        CsvSchema schema = mapper.schemaFor(RoomInfo.class).withColumnSeparator(',').withHeader();

        try (BufferedWriter bw = Files.newBufferedWriter(resultsPath.resolve("results.csv"))) {
            mapper.writer(schema).writeValues(bw).writeAll(roomInfos);
            scrapeView.setOutput(mapper.writer(schema).writeValueAsString(roomInfos));
        } catch (IOException exception) {
            logger.error("Fail!", exception);
        }
    }

    private String getHtml(
        Grabber grabber,
        String webpageUrl,
        String proxyHost,
        String proxyPort,
        String proxyUsername,
        String proxyPassword
    ) {
        if (!proxyHost.trim().isEmpty() && !proxyPort.trim().isEmpty() &&
            !proxyUsername.trim().isEmpty() &&
            !proxyPassword.trim().isEmpty()) {
            return grabber.grabHtml(webpageUrl,
                proxyHost,
                Integer.valueOf(proxyPort),
                proxyUsername,
                proxyPassword
            );
        } else if (!proxyHost.trim().isEmpty() && !proxyPort.trim().isEmpty()) {
            return grabber.grabHtml(webpageUrl, proxyHost, Integer.valueOf(proxyPort));
        } else {
            return grabber.grabHtml(webpageUrl);
        }
    }
}