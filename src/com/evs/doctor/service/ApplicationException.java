package com.evs.doctor.service;

import android.content.res.Resources;

import com.evs.doctor.R;

public class ApplicationException extends Exception {
    public static int NO_INTERNET = R.string.error_no_internet_message;
    public static int AUTHORIZATION_ERROR = R.string.error_authorization;
    public static int SERVER_RESPONSE_ERROR = R.string.error_incorrect_server_response;
    private String mMessage;
    private int errorCode = -1;

    /**
     * @param errorServerIsDown
     * @param string
     */
    public ApplicationException(int errorID) {
        errorCode = errorID;
    }

    public ApplicationException(String detailMessage, Throwable throwable) {
        super(detailMessage, throwable);
        mMessage = detailMessage;
    }

    public ApplicationException(String detailMessage) {
        super(detailMessage);
        mMessage = detailMessage;
    }

    /**
     * @param e
     */
    public ApplicationException(Throwable th) {
        super(th);
    }

    /**{@inheritDoc}*/
    public String getLocalizedMessage(Resources res) {
        if (errorCode > 0 && res != null) {
            return res.getString(errorCode);
        }
        return getMessage();
    }

}
