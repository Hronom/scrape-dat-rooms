package com.github.hronom.scrape.dat.rooms.core.webpage.html.grabbers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.security.cert.CertificateException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class OkHTTPGrabber implements Grabber {
    private static final Logger logger = LogManager.getLogger();

    public OkHTTPGrabber() {
    }

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
        try {
            OkHttpClient client =
                getUnsafeOkHttpClient(proxyHost, proxyPort, proxyUsername, proxyPassword);

            Request request =
                new Request
                    .Builder()
                    .url(webpageUrl)
                    .get()
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        } catch (IOException exception) {
            logger.fatal("Fail!", exception);
            return null;
        }
    }

    private OkHttpClient getUnsafeOkHttpClient(
        String proxyHost, int proxyPort, String proxyUsername, String proxyPassword
    ) {
        try {
            // Create a trust manager that does not validate certificate chains.
            final TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType
                    ) throws CertificateException {
                    }

                    @Override
                    public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain,
                        String authType
                    ) throws CertificateException {
                    }

                    @Override
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return new java.security.cert.X509Certificate[]{};
                    }
                }
            };

            // Install the all-trusting trust manager.
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            // Create an ssl socket factory with our all-trusting manager.
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            if (proxyHost != null && proxyPort != 0) {
                Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort));
                builder.proxy(proxy);
                if (proxyUsername != null && proxyPassword != null) {
                    builder.authenticator(new Authenticator() {
                        @Override
                        public Request authenticate(Route route, Response response)
                            throws IOException {
                            String credential = Credentials.basic(proxyUsername, proxyPassword);
                            return response
                                .request()
                                .newBuilder()
                                .header("Proxy-Authorization", credential)
                                .build();
                        }
                    });
                }
            }

            return builder.build();
        } catch (Exception exception) {
            logger.fatal("Fail!", exception);
            return null;
        }
    }
}
