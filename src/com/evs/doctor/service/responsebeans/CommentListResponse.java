package com.evs.doctor.service.responsebeans;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.evs.doctor.model.Comment;
import com.evs.doctor.model.ResponseStatus;

public class CommentListResponse extends ResponseStatus {

    private List<Comment> items;

    /**
     * @return the List of Comment objects
     */
    @JsonProperty(value = "items")
    public final List<Comment> getItems() {
        return items;
    }

    /**
     * @param items (comments List) to set
     */
    public final void setItems(List<Comment> items) {
        this.items = items;
    }
}
