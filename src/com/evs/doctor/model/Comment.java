package com.evs.doctor.model;

public class Comment {
    private Long id;
    private String authorName;
    private String authorFullName;
    private String authorSpecialty;
    private String authorDegree;
    private String authorAvatarUrl;
    private int authorSexId;
    private String authorId;
    private String content;
    private String createdAt;

    /*
     * @return Comment's id
     */
    public final Long getId() {
        return id;
    }

    /**
    * @param Comment's id to set
    */
    public final void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Comment's author name
     */
    public final String getAuthorName() {
        return authorName;
    }

    /**
    * @param Comment's author name to set
    */
    public final void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
    * @return Comment's author's speciality
    */
    public final String getAuthorSpecialty() {
        return authorSpecialty;
    }

    /**
    * @param Comment's author's speciality to set
    */
    public final void setAuthorSpecialty(String authorSpecialty) {
        this.authorSpecialty = authorSpecialty;
    }

    /**
    * @return Comment's author's degree
    */
    public final String getAuthorDegree() {
        return authorDegree;
    }

    /**
    * @param Comment's author's degree to set
    */
    public final void setAuthorDegree(String authorDegree) {
        this.authorDegree = authorDegree;
    }

    /**
    * @return Comment's author's avatar url
    */
    public final String getAuthorAvatarUrl() {
        return authorAvatarUrl;
    }

    /**
    * @param Comment's author's avatar url to set
    */
    public final void setAuthorAvatarUrl(String authorAvatarUrl) {
        this.authorAvatarUrl = authorAvatarUrl;
    }

    /**
    * @return Comment's author's sex id
    */
    public final int getAuthorSexId() {
        return authorSexId;
    }

    /**
    * @param Comment's author's sex id to set
    */
    public final void setAuthorSexId(int authorSexId) {
        this.authorSexId = authorSexId;
    }

    /**
    * @return Comment's author's id
    */
    public final String getAuthorId() {
        return authorId;
    }

    /**
    * @param Comment's author's id to set
    */
    public final void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    /**
    * @return Comment's content
    */
    public final String getContent() {
        return content;
    }

    /**
    * @param Comment's content to set
    */
    public final void setContent(String content) {
        this.content = content;
    }

    /**
    * @return Comment's createdAt
    */
    public final String getCreatedAt() {
        return createdAt;
    }

    /**
    * @param Comment's createdAt to set
    */
    public final void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return Comment's author's full name
     */
    public final String getAuthorFullName() {
        return authorFullName;
    }

    /**
     * @param Comment's author's full name to set
     */
    public final void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }
}
