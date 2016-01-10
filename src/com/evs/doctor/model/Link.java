package com.evs.doctor.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class Link implements Serializable {

    private String url;
    private String title;

    /**
     * @return the url
     */
    @JsonProperty(value = "url")
    public String getUrl() {
        return url;
    }

    /**
     * @param url the url to set
     */
    public final void setUrl(String url) {
        this.url = url;
    }

    /**
     * @return the title
     */
    @JsonProperty(value = "title")
    public final String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public final void setTitle(String title) {
        this.title = title;
    }

}
