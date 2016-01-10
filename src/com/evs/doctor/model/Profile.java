package com.evs.doctor.model;

import org.codehaus.jackson.annotate.JsonProperty;

public class Profile implements Identify<String> {

    private String id;
    private String fullName;
    private String shortName;
    private Integer primarySpecialtyId;
    private String profileLink;
    private String money;
    private String rating;
    private String smallAvatarLink;
    private String normalAvatarLink;

    /**{@inheritDoc}*/
    @Override
    @JsonProperty(value = "id")
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public final void setId(String id) {
        this.id = id;
    }

    /**
     * @return the fullName
     */
    @JsonProperty(value = "fullName")
    public final String getFullName() {
        return fullName;
    }

    /**
     * @param fullName the fullName to set
     */
    public final void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * @return the shortName
     */
    @JsonProperty(value = "shortName")
    public final String getShortName() {
        return shortName;
    }

    /**
     * @param shortName the shortName to set
     */
    public final void setShortName(String shortName) {
        this.shortName = shortName;
    }

    /**
     * @return the primarySpecialtyId
     */
    @JsonProperty(value = "primarySpecialtyId")
    public final Integer getPrimarySpecialtyId() {
        return primarySpecialtyId;
    }

    /**
     * @param primarySpecialtyId the primarySpecialtyId to set
     */
    public final void setPrimarySpecialtyId(Integer primarySpecialtyId) {
        this.primarySpecialtyId = primarySpecialtyId;
    }

    /**
     * @return the profileLink
     */
    @JsonProperty(value = "profileLink")
    public final String getProfileLink() {
        return profileLink;
    }

    /**
     * @param profileLink the profileLink to set
     */
    public final void setProfileLink(String profileLink) {
        this.profileLink = profileLink;
    }

    /**
     * @return the money
     */
    @JsonProperty(value = "money")
    public final String getMoney() {
        return money;
    }

    /**
     * @param money the money to set
     */
    public final void setMoney(String money) {
        this.money = money;
    }

    /**
     * @return the rating
     */
    @JsonProperty(value = "rating")
    public final String getRating() {
        return rating;
    }

    /**
     * @param rating the rating to set
     */
    public final void setRating(String rating) {
        this.rating = rating;
    }

    /**
     * @return the smallAvatarLink
     */
    @JsonProperty(value = "smallAvatarLink")
    public final String getSmallAvatarLink() {
        return smallAvatarLink;
    }

    /**
     * @param smallAvatarLink the smallAvatarLink to set
     */
    public final void setSmallAvatarLink(String smallAvatarLink) {
        this.smallAvatarLink = smallAvatarLink;
    }

    /**
     * @return the normalAvatarLink
     */
    @JsonProperty(value = "normalAvatarLink")
    public final String getNormalAvatarLink() {
        return normalAvatarLink;
    }

    /**
     * @param normalAvatarLink the normalAvatarLink to set
     */
    public final void setNormalAvatarLink(String normalAvatarLink) {
        this.normalAvatarLink = normalAvatarLink;
    }

}
