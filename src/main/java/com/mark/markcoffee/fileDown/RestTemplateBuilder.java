package com.mark.markcoffee.fileDown;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class RestTemplateBuilder {
    private int connectTimeout = 50000;
    private int readTimeout = 50000;
    private boolean enableSslCheck = false;

    public RestTemplateBuilder connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public RestTemplateBuilder readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public RestTemplateBuilder enableSslCheck(boolean enableSslCheck) {
        this.enableSslCheck = enableSslCheck;
        return this;
    }

    public static RestTemplateBuilder builder() {
        return new RestTemplateBuilder();
    }

    public RestTemplate build() {
        final RestTemplate restTemplate = new RestTemplate();

        //sslIgnore
        SimpleClientHttpRequestFactory requestFactory;
        if (!this.enableSslCheck) {
            requestFactory = getUnsafeClientHttpRequestFactory();
        } else {
            requestFactory = new SimpleClientHttpRequestFactory();
        }

        //timeout
        requestFactory.setConnectTimeout(this.connectTimeout);
        requestFactory.setReadTimeout(this.readTimeout);

        restTemplate.setRequestFactory(requestFactory);
        return restTemplate;
    }

    private SimpleClientHttpRequestFactory getUnsafeClientHttpRequestFactory() {
        TrustManager[] trustManagers = {new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        }};
        final SSLContext sslContext;
        try {
            sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagers, new SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }

        return new SimpleClientHttpRequestFactory() {
            @Override
            protected void prepareConnection(HttpURLConnection connection, @NotNull String httpMethod) throws IOException {
                super.prepareConnection(connection, httpMethod);
                if (connection instanceof HttpURLConnection) {
                    ((HttpsURLConnection) connection).setSSLSocketFactory(sslContext.getSocketFactory());
                }
            }
        };
    }
}

