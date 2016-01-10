package com.evs.doctor.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class VideoLink extends Link {

    private String thumbnailUrl;

    /**
     * @return the thumbnailUrl
     */
    @JsonProperty(value = "thumbnailUrl")
    public final String getThumbnailUrl() {
        return thumbnailUrl;
    }

    /**
     * @param thumbnailUrl the thumbnailUrl to set
     */
    public final void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

}
