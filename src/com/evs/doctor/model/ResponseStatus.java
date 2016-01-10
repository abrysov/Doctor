package com.evs.doctor.model;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonProperty;

public class ResponseStatus implements Serializable {

    private Integer errorCode;
    private String errorMessage;

    /**
     * @return the errorCode
     */
    @JsonProperty(value = "errorCode")
    public final Integer getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    @JsonProperty(value = "errorCode")
    public final void setErrorCode(Integer errorCode) {
        this.errorCode = errorCode;
    }

    @JsonProperty(value = "ErrorCode")
    public final Integer getErrorCodeUC() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    @JsonProperty(value = "ErrorCode")
    public final void setErrorCodeUC(Integer errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the errorMessage
     */
    @JsonProperty(value = "errorMessage")
    public final String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    @JsonProperty(value = "errorMessage")
    public final void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return the errorMessage
     */
    @JsonProperty(value = "ErrorMessage")
    public final String getErrorMessageUC() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    @JsonProperty(value = "ErrorMessage")
    public final void setErrorMessageUC(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
