package com.evs.doctor.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class Publication extends ResponseStatus implements Identify<Long> {

    private Long id;
    private String title;
    private String annotation;
    // THIS filed used for list view.
    private String authorName;
    private String authorFullName;
    private String authorSpecialty;
    private String authorId;// "081218014224044115163187007117051061233001142159",
    private String authorDegree;
    private String authorAvatarUrl; // http://img1.http://doktornarabote.ru/image/avatarq/081218014224044115163187007117051061233001142159
    private int type;// 20
    private String createdAt; // 2012-11-22T12:16:11.313Z
    private int commentsCount;// 0
    private int recommendationsCount;// 0
    private int videosCount;// 0
    private int linksCount;// 0
    private int pdfsCount;// 0
    private int imagesCount;// 0
    private int authorSexId;// 1
    private String originalLink;// http://test.doktornarabote.ru/Publication/Single/54264",
    private int approvalStatus;// 1
    private String content;

    private List<ImageLink> images;
    private List<PDFLink> pdfs;
    private List<Link> links;
    private List<VideoLink> videos;

    /**
     * @return the images
     */
    @JsonProperty(value = "images")
    public final List<ImageLink> getImages() {
        return images;
    }

    /**
     * @param images the images to set
     */
    public final void setImages(List<ImageLink> images) {
        this.images = images;
    }

    /**
     * @return the pdfs
     */
    @JsonProperty(value = "pdfs")
    public final List<PDFLink> getPdfs() {
        return pdfs;
    }

    /**
     * @param pdfs the pdfs to set
     */
    public final void setPdfs(List<PDFLink> pdfs) {
        this.pdfs = pdfs;
    }

    /**
     * @return the links
     */
    @JsonProperty(value = "links")
    public final List<Link> getLinks() {
        return links;
    }

    /**
     * @param links the links to set
     */
    public final void setLinks(List<Link> links) {
        this.links = links;
    }

    /**
     * @return the videos
     */
    @JsonProperty(value = "videos")
    public final List<VideoLink> getVideos() {
        return videos;
    }

    /**
     * @param videos the videos to set
     */
    public final void setVideos(List<VideoLink> videos) {
        this.videos = videos;
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
     * @return the annotation
     */
    public final String getAnnotation() {
        return annotation;
    }

    /**
     * @param annotation the annotation to set
     */
    public final void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    /**
     * @return the authorName
     */
    public final String getAuthorName() {
        return authorName;
    }

    /**
     * @param authorName the authorName to set
     */
    public final void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * @return the authorFullName
     */
    public final String getAuthorFullName() {
        return authorFullName;
    }

    /**
     * @param authorFullName the authorFullName to set
     */
    public final void setAuthorFullName(String authorFullName) {
        this.authorFullName = authorFullName;
    }

    /**
     * @return the authorSpecialty
     */
    public final String getAuthorSpecialty() {
        return authorSpecialty;
    }

    /**
     * @param authorSpecialty the authorSpecialty to set
     */
    public final void setAuthorSpecialty(String authorSpecialty) {
        this.authorSpecialty = authorSpecialty;
    }

    /**
     * @return the authorId
     */
    public final String getAuthorId() {
        return authorId;
    }

    /**
     * @param authorId the authorId to set
     */
    public final void setAuthorId(String authorId) {
        this.authorId = authorId;
    }

    /**
     * @return the authorDegree
     */
    public final String getAuthorDegree() {
        return authorDegree;
    }

    /**
     * @param authorDegree the authorDegree to set
     */
    public final void setAuthorDegree(String authorDegree) {
        this.authorDegree = authorDegree;
    }

    /**
     * @return the authorAvatarUrl
     */
    public final String getAuthorAvatarUrl() {
        return authorAvatarUrl;
    }

    /**
     * @param authorAvatarUrl the authorAvatarUrl to set
     */
    public final void setAuthorAvatarUrl(String authorAvatarUrl) {
        this.authorAvatarUrl = authorAvatarUrl;
    }

    /**
     * @return the type
     */
    public final int getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public final void setType(int type) {
        this.type = type;
    }

    /**
     * @return the createdAt
     */
    public final String getCreatedAt() {
        return createdAt;
    }

    /**
     * @param createdAt the createdAt to set
     */
    public final void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    /**
     * @return the commentsCount
     */
    public final int getCommentsCount() {
        return commentsCount;
    }

    /**
     * @param commentsCount the commentsCount to set
     */
    public final void setCommentsCount(int commentsCount) {
        this.commentsCount = commentsCount;
    }

    /**
     * @return the recommendationsCount
     */
    public final int getRecommendationsCount() {
        return recommendationsCount;
    }

    /**
     * @param recommendationsCount the recommendationsCount to set
     */
    public final void setRecommendationsCount(int recommendationsCount) {
        this.recommendationsCount = recommendationsCount;
    }

    /**
     * @return the videosCount
     */
    public final int getVideosCount() {
        return videosCount;
    }

    /**
     * @param videosCount the videosCount to set
     */
    public final void setVideosCount(int videosCount) {
        this.videosCount = videosCount;
    }

    /**
     * @return the linksCount
     */
    public final int getLinksCount() {
        return linksCount;
    }

    /**
     * @param linksCount the linksCount to set
     */
    public final void setLinksCount(int linksCount) {
        this.linksCount = linksCount;
    }

    /**
     * @return the pdfsCount
     */
    public final int getPdfsCount() {
        return pdfsCount;
    }

    /**
     * @param pdfsCount the pdfsCount to set
     */
    public final void setPdfsCount(int pdfsCount) {
        this.pdfsCount = pdfsCount;
    }

    /**
     * @return the imagesCount
     */
    public final int getImagesCount() {
        return imagesCount;
    }

    /**
     * @param imagesCount the imagesCount to set
     */
    public final void setImagesCount(int imagesCount) {
        this.imagesCount = imagesCount;
    }

    /**
     * @return the authorSexId
     */
    public final int getAuthorSexId() {
        return authorSexId;
    }

    /**
     * @param authorSexId the authorSexId to set
     */
    public final void setAuthorSexId(int authorSexId) {
        this.authorSexId = authorSexId;
    }

    /**
     * @return the originalLink
     */
    public final String getOriginalLink() {
        return originalLink;
    }

    /**
     * @param originalLink the originalLink to set
     */
    public final void setOriginalLink(String originalLink) {
        this.originalLink = originalLink;
    }

    /**
     * @return the approvalStatus
     */
    public final int getApprovalStatus() {
        return approvalStatus;
    }

    /**
     * @param approvalStatus the approvalStatus to set
     */
    public final void setApprovalStatus(int approvalStatus) {
        this.approvalStatus = approvalStatus;
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

}
