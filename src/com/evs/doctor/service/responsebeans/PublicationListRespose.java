package com.evs.doctor.service.responsebeans;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.evs.doctor.model.Publication;
import com.evs.doctor.model.ResponseStatus;

public class PublicationListRespose extends ResponseStatus {

    private List<Publication> items;

    /**
     * @return the items
     */
    @JsonProperty(value = "items")
    public final List<Publication> getItems() {
        return items;
    }

    /**
     * @param items the items to set
     */
    public final void setItems(List<Publication> items) {
        this.items = items;
    }

}
