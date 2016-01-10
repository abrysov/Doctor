package com.evs.doctor.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class PDFLink extends Link {

    private String size;

    /**
     * @return the size
     */
    @JsonProperty(value = "size")
    public final String getSize() {
        return size;
    }

    /**
     * @param size the size to set
     */
    public final void setSize(String size) {
        this.size = size;
    }

}
