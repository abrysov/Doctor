package com.evs.doctor.service.responsebeans;

import com.evs.doctor.model.ResponseStatus;

public class ContentPageResponse extends ResponseStatus {

    private String title; // "Компании",
    private String content;
    private Long id;// 1000079
    private String link; // "http://doctoratwork.ru/Page/1000079"

    /**
     * @return the title
     */
    public final String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public final void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return the content
     */
    public final String getContent() {
        return content;
    }

    /**
     * @param content the content to set
     */
    public final void setContent(String content) {
        this.content = content;
    }

    /**
     * @return the id
     */
    public final Long getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public final void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the link
     */
    public final String getLink() {
        return link;
    }

    /**
     * @param link the link to set
     */
    public final void setLink(String link) {
        this.link = link;
    }

}
