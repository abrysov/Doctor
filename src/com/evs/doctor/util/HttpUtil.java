package com.evs.doctor.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import com.evs.doctor.service.ApplicationException;

public class HttpUtil {
    public static String makeGetRequest(String url, String token) throws ClientProtocolException, IOException,
            IllegalStateException, ApplicationException {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("Token", token);
        DefaultHttpClient client = getHttpClient();
        client.getParams().setParameter("Token", token);
        HttpResponse response = client.execute(httpGet);
        return handleResponse(response);
    }

    public static String makePostRequest(String url, Map<String, String> paramsValues) throws IllegalStateException,
            ClientProtocolException, IOException, ApplicationException {
        return makePostRequest(url, paramsValues, null);
    }

    public static String makePostRequest(String url, Map<String, String> paramsValues, String token)
            throws IllegalStateException, ClientProtocolException, IOException, ApplicationException {
        Logger.i(HttpUtil.class, "Make post request for url " + url);
        HttpPost httpPost = new HttpPost(url);
        DefaultHttpClient client = getHttpClient();

        if (token != null) {
            httpPost.addHeader("Token", token);
            client.getParams().setParameter("Token", token);
            httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        }

        if (paramsValues != null) {
            List<NameValuePair> parametres = new ArrayList<NameValuePair>();
            for (String s : paramsValues.keySet()) {
                parametres.add(new BasicNameValuePair(s, paramsValues.get(s)));
            }
            httpPost.setEntity(new UrlEncodedFormEntity(parametres, HTTP.UTF_8));
        }
        HttpResponse response = client.execute(httpPost);
        return handleResponse(response);
    }

    /** Handle HTTP response.
     * @param response {@link HttpResponse}
     * @return  content as string   if ok
     * @throws ApplicationException in case of problems
     */
    private static String handleResponse(HttpResponse response) throws IllegalStateException, IOException,
            ApplicationException {
        StatusLine statusLine = response.getStatusLine();
        String content = FileUtil.getStreamAsString(response.getEntity().getContent());
        switch (statusLine.getStatusCode()) {
        case 200:
            return content;
        case 404:// HTTP/1.1 404 Not Found
        case 500:// 500: HTTP/1.1 500 Internal Server Error
            throw new ApplicationException("Server response:" + statusLine + "and content " + content);
        default:
            throw new IllegalStateException("Something wrong on server" + statusLine);
        }
    }

    public static DefaultHttpClient getHttpClient() {
        return getHttpClient(6000);
    }

    /**
     * Gets HttpClient 
     *
     * @return httpClient
     */
    public static DefaultHttpClient getHttpClient(int connectionTimeOut) {
        try {
            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
            HttpConnectionParams.setConnectionTimeout(params, connectionTimeOut);
            HttpConnectionParams.setSoTimeout(params, connectionTimeOut);
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);
            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            // Log.w(TAG, "Can't create CustomSSLSocketFactory");
            return new DefaultHttpClient();
        }
    }
}
