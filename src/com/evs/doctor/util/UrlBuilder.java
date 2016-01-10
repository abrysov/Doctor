package com.evs.doctor.util;

import java.net.URISyntaxException;
import java.util.Date;
import java.util.Map;

import android.net.Uri;

public class UrlBuilder {
    private static final String HOST = "http://testapi.doktornarabote.ru";
    private static final String AUTHORIZATION = "/auth/login";

    private static final String GET_PUBLICATION_BY_ID = "/publications/getById?publicationId=%d";
    private static final String GET_PUBLICATIONS = "/publications/list";
    private static final String CREATE_PUBLICATION = "/publications/create";
    private static final String CREATE_COMMENT = "/comments/create";
    private static final String GET_OWN_PUBLICATIONS = "/publications/own";
    private static final String GET_COMMENTS_BY_PUBLICATION_ID = "/comments/list?publicationId=%d";
    private static final String GET_CONTENT_PAGE = "/ContentPage/Get?id=%d";

    public static String getHost() {
        return (AppConfig.isDevMode() ? HOST : HOST);
    }

    public static String getAuthorizationUrl() {
        return getHost() + AUTHORIZATION;
    }

    public static String getPublicationByIdUrl(Long publicationId) {
        return getHost() + String.format(GET_PUBLICATION_BY_ID, publicationId);
    }

    public static String getPublicationListUrl(String typeFilter) {
        String url = getHost() + GET_PUBLICATIONS;
        if (typeFilter != null) {
            url = url + "?type=" + typeFilter;
        }
        return url;
    }

    public static String getPublicationListUrl(String typeFilter, String lastDate) {
        boolean withTypeFilter = false;

        String url = getHost() + GET_PUBLICATIONS;
        if (typeFilter != null) {
            url = url + "?type=" + typeFilter;
            withTypeFilter = true;
        }

        if (lastDate != null) {
            if (withTypeFilter) {
                url = url + "&" + "lastDate=" + lastDate;
            } else {
                url = url + "?lastDate=" + lastDate;
            }
        }

        return url;
    }

    public static String buildParamString(Map<String, String> params) throws URISyntaxException {

        Uri.Builder builder = new Uri.Builder().scheme("http").authority("authority").path("path");
        for (String key : params.keySet()) {
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().getEncodedQuery();
    }

    public static String getPublicationList(Date fate, int count, int type) {
        return getHost() + GET_PUBLICATIONS;
    }

    public static String getCreatePublicationUrl() {
        return getHost() + CREATE_PUBLICATION;
    }

    public static String getOwnPublicationsUrl() {
        return getHost() + GET_OWN_PUBLICATIONS;
    }

    public static String getOwnPublicationsUrl(String lastDate) {
        String url = getHost() + GET_OWN_PUBLICATIONS;

        if (lastDate != null) {
            return url = url + "?lastDate=" + lastDate;
        }

        return url;
    }

    public static String getCommentsByPublicationIdUrl(Long publicationId) {
        return getHost() + String.format(GET_COMMENTS_BY_PUBLICATION_ID, publicationId);
    }

    public static String getCreateCommentUrl() {
        return getHost() + CREATE_COMMENT;
    }

    /**
     * @param id
     * @return
     */
    public static String getContentPage(Long id) {
        return getHost() + String.format(GET_CONTENT_PAGE, id);
    }

}
