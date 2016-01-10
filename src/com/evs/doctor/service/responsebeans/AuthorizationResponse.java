package com.evs.doctor.service.responsebeans;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

import com.evs.doctor.model.Profile;
import com.evs.doctor.model.PublicationType;
import com.evs.doctor.model.ResponseStatus;
import com.evs.doctor.model.Specialties;

public class AuthorizationResponse extends ResponseStatus {
    private String token;
    private Profile doctor;
    private List<PublicationType> publicationTypes;
    private List<Specialties> specialties;
    private Long ratingPageId;

    /**
     * @return the token
     */
    @JsonProperty(value = "token")
    public final String getToken() {
        return token;
    }

    /**
     * @param token the token to set
     */
    public final void setToken(String token) {
        this.token = token;
    }

    /**
     * @return the doctor
     */
    @JsonProperty(value = "doctor")
    public final Profile getDoctor() {
        return doctor;
    }

    /**
     * @param doctor the doctor to set
     */
    public final void setDoctor(Profile doctor) {
        this.doctor = doctor;
    }

    @JsonProperty(value = "publicationTypes")
    public final List<PublicationType> getPublicationTypes() {
        return publicationTypes;
    }

    public final void setPublicationTypes(List<PublicationType> publicationTypes) {
        this.publicationTypes = publicationTypes;
    }

    /**
     * @return the specialties
     */
    @JsonProperty(value = "specialties")
    public final List<Specialties> getSpecialties() {
        return specialties;
    }

    /**
     * @param specialties the specialties to set
     */
    public final void setSpecialties(List<Specialties> specialties) {
        this.specialties = specialties;
    }

    /**
     * @return the ratingPageId
     */
    @JsonProperty(value = "ratingPageId")
    public final Long getRatingPageId() {
        return ratingPageId;
    }

    /**
     * @param ratingPageId the ratingPageId to set
     */
    public final void setRatingPageId(Long ratingPageId) {
        this.ratingPageId = ratingPageId;
    }

}
