package com.allinone.common.util;

import com.samsung.sse.common.util.StringUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;


public class HttpUtil {

    private static final Logger logger = LoggerFactory.getLogger(com.samsung.sse.common.util.HttpUtil.class);


    public static String getHttpResponse(String strUrl, String strProxyFlag, String strProxyHost, String strProxyPort) {


        StringBuffer strXmlResponse = null;
        Reader reader = null;
        int noOfRetries = 0;

        while (true) //Enclosing in infinite while loop so as to ensure response from MLB with multiple retries
        {

            noOfRetries++;
            try {

                if (strXmlResponse != null && StringUtil.isNotEmpty(strXmlResponse.toString())) {
                    break;
                }

                CloseableHttpClient httpClient;

                if ("true".equals(strProxyFlag)) {
                    httpClient = HttpClients.custom().useSystemProperties().setProxy(new HttpHost(strProxyHost, Integer.parseInt(strProxyPort))).build();
                } else {
                    httpClient = HttpClients.createDefault();
                }

                HttpGet httpGet = new HttpGet(strUrl);
                CloseableHttpResponse response = httpClient.execute(httpGet);

                HttpEntity entity = response.getEntity();


                Header contentEncoding = entity.getContentEncoding();
                String characterSet;
                if (contentEncoding == null || contentEncoding.getValue() == null || StringUtils.isEmpty(contentEncoding.getValue())) {
                    characterSet = "UTF-8";
                    logger.debug("Setting content Encoding to default of UTF-8");
                } else {
                    characterSet = contentEncoding.getValue();
                    logger.debug("Setting content Encoding to: " + characterSet);
                }

                String body = IOUtils.toString(entity.getContent(), characterSet);

                logger.debug("Retrieved Body: " + body);

                if (body != null && StringUtil.isNotEmpty(body)) {
                    strXmlResponse = new StringBuffer();
                    strXmlResponse.append(body);
                }

                try {
                    EntityUtils.consume(entity);
                    response.close();
                    httpClient.close();
                } catch (Exception e1) {
                    logger.error("Exception occured: ", e1);
                }

                if (strXmlResponse != null && StringUtil.isNotEmpty(strXmlResponse.toString())) {
                    break;
                }

            } catch (Exception ex) {
                logger.error("Exception occured: ", ex);
            } finally {
                if (noOfRetries == 10) {
                    break;
                }
            }
        }
        return strXmlResponse.toString();
    }

    public static String getHttpResponseString(String strUrl, String strProxyFlag, String strProxyHost, String strProxyPort) {

        StringBuffer strXmlResponse = null;
        @SuppressWarnings("unused")
        Reader reader = null;
        int noOfRetries = 0;

        while (true) //Enclosing in infinite while loop so as to ensure response from MLB with multiple retries
        {
            noOfRetries++;
            try {
                if ("true".equals(strProxyFlag)) {
                    System.setProperty("proxySet", strProxyFlag);
                    System.setProperty("http.proxyHost", strProxyHost);
                    System.setProperty("http.proxyPort", strProxyPort);
                    System.setProperty("https.proxyHost", strProxyHost);
                    System.setProperty("https.proxyPort", strProxyPort);
                }
                URL url = new URL(strUrl);
                URLConnection conn = url.openConnection();
                InputStream inputStream = conn.getInputStream();
                BufferedReader recv = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                String strFileExtract = "";
                strXmlResponse = new StringBuffer();
                while ((strFileExtract = recv.readLine()) != null) {
                    strXmlResponse.append(new String(strFileExtract.getBytes(), "UTF-8"));
                }

                reader = new StringReader(strXmlResponse.toString());
                if (strXmlResponse != null && StringUtil.isNotEmpty(strXmlResponse.toString())) {
                    break;
                }

            } catch (Exception ex) {
                logger.error("Exception occured: ", ex);
            } finally {
                if (noOfRetries == 10) {
                    break;
                }
            }
        }

        if (strXmlResponse != null) {
            return strXmlResponse.toString();
        } else {
            return null;
        }
    }

    public static Reader getHttpResponseWithBasicAuth(String strUrl, String strProxyFlag, String strProxyHost, String strProxyPort, String authString) {

        StringBuffer strXmlResponse = null;
        Reader reader = null;
        int noOfRetries = 0;

        while (true) //Enclosing in infinite while loop so as to ensure response from MLB with multiple retries
        {
            noOfRetries++;
            try {
                if ("true".equals(strProxyFlag)) {
                    System.setProperty("proxySet", strProxyFlag);
                    System.setProperty("http.proxyHost", strProxyHost);
                    System.setProperty("http.proxyPort", strProxyPort);
                    System.setProperty("https.proxyHost", strProxyHost);
                    System.setProperty("https.proxyPort", strProxyPort);
                }
                URL url = new URL(strUrl);
                URLConnection conn = url.openConnection();

                conn.setRequestProperty("Authorization", "Basic " + authString);
                InputStream inputStream = conn.getInputStream();
                BufferedReader recv = new BufferedReader(new InputStreamReader(inputStream));
                String strFileExtract = "";
                strXmlResponse = new StringBuffer();
                while ((strFileExtract = recv.readLine()) != null) {
                    strXmlResponse.append(new String(strFileExtract.getBytes(), "UTF-8"));
                }

                reader = new StringReader(strXmlResponse.toString());
                if (strXmlResponse != null && StringUtil.isNotEmpty(strXmlResponse.toString())) {
                    break;
                }

            } catch (Exception ex) {
                logger.error("Exception occured: ", ex);
            } finally {
                if (noOfRetries == 10) {
                    break;
                }
            }
        }
        return reader;
    }

    public static String getResponseFromHTTPsUrl(String strUrl, String strProxyFlag) {


        int noOfRetries = 0;
        String strResponse = null;

        while (true) //Enclosing in infinite while loop so as to ensure response from MLB with multiple retries
        {
            noOfRetries++;

            URL url;

            try {
                InetSocketAddress proxyInet = null;
                Proxy proxy = null;
                HttpsURLConnection con = null;

                url = new URL(strUrl);
                if ("true".equals(strProxyFlag)) {
                    System.out.println("strProxyFlag::" + strProxyFlag);
                    proxyInet = new InetSocketAddress("168.219.61.252", 8080); //Proxy is applicable only for Suwon local workstations
                    proxy = new Proxy(Proxy.Type.HTTP, proxyInet);
                    con = (HttpsURLConnection) url.openConnection(proxy);
                }
                con = (HttpsURLConnection) url.openConnection();
                BufferedReader br =
                        new BufferedReader(
                                new InputStreamReader(con.getInputStream()));
                String input;
                StringBuffer stringBuffer = new StringBuffer();
                while ((input = br.readLine()) != null) {

                    stringBuffer.append(input);

                }

                strResponse = stringBuffer.toString();

                br.close();

                break; //There were no issues while getting the feed and hence breaking the loop


            } catch (MalformedURLException ex) {
                logger.error("Exception occured: ", ex);
            } catch (IOException ex) {
                logger.error("Exception occured: ", ex);
            } finally {
                if (noOfRetries == 10) {
                    break;
                }
            }
        }

        return strResponse;
    }
}
