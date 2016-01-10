package com.evs.doctor.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class ImageLink implements Serializable {
    private String thumbnail;
    private String original;

    public ImageLink() {
        super();
    }

    public ImageLink(String path) {
        super();
        thumbnail = path;
        original = path;
    }

    /**
     * @return the url
     */
    @JsonProperty(value = "original")
    public final String getOriginal() {
        return original;
    }

    /**
     * @param original the original to set
     */
    public final void setOriginal(String original) {
        this.original = original;
    }

    /**
     * @return the thumbnail
     */
    @JsonProperty(value = "thumbnail")
    public final String getThumbnail() {
        return thumbnail;
    }

    /**
     * @param thumbnail the thumbnail to set
     */
    public final void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
