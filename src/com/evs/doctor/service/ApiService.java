package com.evs.doctor.service;

import java.net.UnknownHostException;
import java.util.*;

import org.apache.http.conn.HttpHostConnectException;
import org.codehaus.jackson.JsonProcessingException;

import com.evs.doctor.model.*;
import com.evs.doctor.parser.EntityParser;
import com.evs.doctor.service.responsebeans.AuthorizationResponse;
import com.evs.doctor.service.responsebeans.CommentListResponse;
import com.evs.doctor.service.responsebeans.ContentPageResponse;
import com.evs.doctor.service.responsebeans.PublicationListRespose;
import com.evs.doctor.util.AppConfig;
import com.evs.doctor.util.HttpUtil;
import com.evs.doctor.util.Logger;
import com.evs.doctor.util.UrlBuilder;

public class ApiService {
    private static ApiService mSyncService;
    private AppConfig mConf;
    private List<PublicationType> mPublicationTypes;
    private Profile mProfile;
    private List<Specialties> mSpecialities;
    private Long mRatingPageId;

    private ApiService() {
        super();
    }

    /**
     * @return
     */
    public synchronized static ApiService getInstance() {
        if (mSyncService == null) {
            mSyncService = new ApiService();
            mSyncService.mConf = AppConfig.getInstance();
        }
        return mSyncService;
    }

    /**
     * @param e
     * @return
     */
    private ApplicationException handleException(Exception e) {
        ApplicationException appException = null;
        try {
            throw e;
        } catch (IllegalStateException ex) {
            Logger.e(getClass(), ex);
            appException = new ApplicationException(ApplicationException.SERVER_RESPONSE_ERROR);
        } catch (UnknownHostException uhe) {
            Logger.w(getClass(), uhe.getMessage());
            appException = new ApplicationException(ApplicationException.NO_INTERNET);
        } catch (HttpHostConnectException ex) {
            Logger.w(getClass(), ex.getMessage());
            appException = new ApplicationException(ApplicationException.NO_INTERNET);
        } catch (JsonProcessingException je) {
            String errorMSG = "JsonProcessingException: " + je.getMessage();
            Logger.w(getClass(), errorMSG);

            if (AppConfig.isDevMode()) {
                appException = new ApplicationException(errorMSG);
            } else {
                appException = new ApplicationException(ApplicationException.SERVER_RESPONSE_ERROR);
            }

        } catch (ApplicationException ex) {
            Logger.w(getClass(), ex.getLocalizedMessage());
            appException = ex;
        } catch (Exception ex) {
            Logger.e(getClass(), ex);
            appException = new ApplicationException(ex);
        }

        return appException;
    }

    /**
     * Return true if response ok and contain json model without errors
     * 
     * @param response
     * @return
     */
    private boolean isResponseOK(String response) {
        return response != null && response.indexOf("error") == -1;
    }

    public void createPublication(Publication publication, ArrayList<String> images) throws ApplicationException {
        isAuthorized();
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("title", publication.getTitle());
            params.put("content", publication.getContent());
            params.put("type", publication.getType() + "");
            if (publication.getAuthorSpecialty() != null) {
                params.put("specialtyId", publication.getAuthorSpecialty());
            } else {
                params.put("specialtyId", getProfile().getPrimarySpecialtyId() + "");
            }

            if (images != null) {
                if (images.size() > 0) {
                    int count = 0;
                    for (String image : images) {
                        if (image != null) {
                            params.put("Images[" + count + "].FileName", "fileName" + count + ".png");
                            params.put("Images[" + count + "].MimeType", "image/png");
                            params.put("Images[" + count + "].Content", image);
                        }
                        count++;
                    }
                }
            }

            String url = UrlBuilder.getCreatePublicationUrl();
            // String query = UrlBuilder.buildParamString(params);
            // String response = HttpUtil.makeGetRequest(url + "?" + query, mConf.getAuthorizationToken());

            String response = HttpUtil.makePostRequest(url, params, mConf.getAuthorizationToken());

            if (isResponseOK(response)) {
                if (AppConfig.isDevMode()) {
                    Logger.i(getClass(), "Posting a publication, response OK");
                }
                // inspections = new PostParser().parseEntityList(response);
            } else {
                if (AppConfig.isDevMode()) {
                    Logger.i(getClass(), "Posting a publication, response NOT OK");
                }

                // ResponseStatus rs = ResponseJsonParser.parseResponseStatus(response);
                // throw new ApplicationException(rs.getMessage());
            }
        } catch (Exception e) {
            throw handleException(e);
        }

    }

    /**
    * @param publicationID is mandatory
    * @param content is mandatory
    * @param isRecommendation is optional, set false if no need in it
    * @param addressedToDoctorId is optional, set 0 if no need to pass it, if it is - means answer to that doctor
    * @return void
    * @throws ApplicationException
    */
    public void createComment(Long publicationID, String content, boolean isRecommendation, int addressedToDoctorId)
            throws ApplicationException {
        isAuthorized();
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("publicationID", Long.toString(publicationID));
            params.put("content", content);

            // TODO: to be tested
            if (isRecommendation) {
                params.put("isRecommendation", Boolean.toString(true));
            }
            if (addressedToDoctorId != 0) {
                params.put("addressedToDoctorId", Integer.toString(addressedToDoctorId));
            }

            params.put("specialtyId", getProfile().getPrimarySpecialtyId() + "");

            String url = UrlBuilder.getCreateCommentUrl();
            String response = HttpUtil.makePostRequest(url, params, mConf.getAuthorizationToken());
            Logger.i(getClass(), "Server response after posting comment is " + response);

        } catch (Exception e) {
            Logger.e(getClass(), "PostComment failed");
        }
    }

    /**
     * @param string
     * @throws ApplicationException
     */
    public List<Publication> getPublicationsByFilter(String typeFilter) throws ApplicationException {
        isAuthorized();
        String url = UrlBuilder.getPublicationListUrl(typeFilter);
        if ("OWN".equals(typeFilter)) {
            url = UrlBuilder.getOwnPublicationsUrl();
        }
        return getPublicationsByUrl(url, mConf.getAuthorizationToken());
    }

    public List<Publication> getPublicationsByFilter(String typeFilter, String lastDate) throws ApplicationException {
        isAuthorized();

        String url = UrlBuilder.getPublicationListUrl(typeFilter, lastDate);
        if ("OWN".equals(typeFilter)) {
            url = UrlBuilder.getOwnPublicationsUrl(lastDate);
        }

        return getPublicationsByUrl(url, mConf.getAuthorizationToken());
    }

    public List<Publication> getPublicationsByUrl(String url, String token) throws ApplicationException {
        List<Publication> list = null;
        try {
            Logger.i(getClass(), "getPublicationsByUrl :" + url);
            String response = HttpUtil.makeGetRequest(url, token);
            PublicationListRespose pResponse = EntityParser.parseEntity(response, PublicationListRespose.class);
            handleErrors(pResponse);
            list = pResponse.getItems();
        } catch (Exception e) {
            throw handleException(e);
        }
        return list;
    }

    /**
     * @param publicationID is a mandatory parameter, the id of publication comments for which you want to get
     * @param lastDate is optional
     * @throws ApplicationException
     */
    public List<Comment> getCommentsByPublicationID(Long publicationID) throws ApplicationException {
        isAuthorized();
        List<Comment> listOfComments = null;
        try {
            String url = UrlBuilder.getCommentsByPublicationIdUrl(publicationID);
            if (AppConfig.isDevMode()) {
                Logger.i(getClass(), "getCommentsByPublicationID URL is " + url);
            }
            String response = HttpUtil.makeGetRequest(url, mConf.getAuthorizationToken());
            if (isResponseOK(response)) {
                CommentListResponse commentListResponse = EntityParser.parseEntity(response, CommentListResponse.class);
                listOfComments = commentListResponse.getItems();
            } else {
                Logger.e(getClass(), "Comments response failed ");
            }
        } catch (Exception e) {
            throw handleException(e);
        }

        return listOfComments;
    }

    /**
     * Get publication by Id
     * @param publicationID  - id of publication
     * @throws ApplicationException
     * @return Publication
     */
    public Publication getPublicationByID(Long publicationID) throws ApplicationException {
        String url = UrlBuilder.getPublicationByIdUrl(publicationID);
        isAuthorized();
        Publication publication = null;
        String response = null;
        try {
            Logger.i(getClass(), "getPublicationByUrl" + url);
            response = HttpUtil.makeGetRequest(url, mConf.getAuthorizationToken());
            Logger.i(getClass(), "Received publication " + response.toString()); // for testing purposes
            publication = EntityParser.parseEntity(response, Publication.class);
            handleErrors(publication);

        } catch (Exception e) {
            throw handleException(e);
        }

        if (AppConfig.isDevMode()) {
            Logger.i(getClass(), "Publication's image quantity is " + publication.getImagesCount());
        }
        return publication;
    }

    /**
     * Check than response  is not contain error code.
     * @param status  - {@link ResponseStatus}  bean
     * @throws ApplicationException  in case of error
     */
    private void handleErrors(ResponseStatus status) throws ApplicationException {
        if (status.getErrorCode() != null) {
            switch (status.getErrorCode()) {
            case 1:
                throw new AuthorizationException();

            default:
                break;
            }
        }
    }

    /**
     * @throws ApplicationException
     * 
     */
    private void isAuthorized() throws ApplicationException {
        if (mConf == null || mConf.getAuthorizationToken() == null) {
            Logger.e(getClass(), "Token is null");
            throw new AuthorizationException("Not authorized access! Could you login before.");
        }
    }

    public PublicationType getPublicationTypeById(int id) {
        if (mPublicationTypes != null) {
            for (PublicationType type : mPublicationTypes) {
                if (type.getId() == id) {
                    return type;
                }
            }
        }
        return null;
    }

    public List<PublicationType> getPublicationTypes() {
        if (mPublicationTypes == null) {
            Logger.e(getClass(), "mPublicationTypes is Null");
        }
        return mPublicationTypes;
    }

    public List<Specialties> getSpecialities() {
        if (mSpecialities == null) {
            Logger.e(getClass(), "mSpecialities is Null");
            // throw new AuthorizationException();
        }
        return mSpecialities;
    }

    public List<Specialties> getSortedSpecialities() {
        List<Specialties> sortedSpecialities = new ArrayList<Specialties>();
        sortedSpecialities.addAll(getSpecialities());
        Collections.sort(sortedSpecialities);
        sortedSpecialities.add(0, AppConfig.getDefaultSpeciality());
        return sortedSpecialities;
    }

    public List<PublicationType> getPublicationTypeCanSelect() {
        List<PublicationType> types = new ArrayList<PublicationType>();
        for (PublicationType type : getPublicationTypes()) {
            if (type.isDoctorCanSelect()) {
                types.add(type);
            }
        }
        Collections.sort(types);
        types.add(0, AppConfig.getDefaultType());
        return types;
    }

    public List<PublicationType> getPublicationTypeCanCreate() {
        List<PublicationType> types = new ArrayList<PublicationType>();
        for (PublicationType type : getPublicationTypes()) {
            if (type.isDoctorCanCreate()) {
                types.add(type);
            }
        }
        Collections.sort(types);
        types.add(0, AppConfig.getDefaultType());
        return types;
    }

    /**
     * @param ab
     * POST data: login=validLogin&password=validPassword
     * {"errorCode":2,"errorMessage":"Authorization failed"}
     * 
     * User: asd@asd.ru
     * Password: asd
     */
    public synchronized Authorization doLogin(Authorization authorization) throws ApplicationException {

        Map<String, String> parametres = new HashMap<String, String>();
        parametres.put("login", authorization.getUserName());
        parametres.put("password", authorization.getPassword());
        try {
            String response = HttpUtil.makePostRequest(UrlBuilder.getAuthorizationUrl(), parametres);
            AuthorizationResponse responseBean = EntityParser.parseEntity(response, AuthorizationResponse.class);
            handleErrors(responseBean);
            authorization.setToken(responseBean.getToken());
            mProfile = responseBean.getDoctor();
            mSpecialities = responseBean.getSpecialties();
            mPublicationTypes = responseBean.getPublicationTypes();
            mRatingPageId = responseBean.getRatingPageId();
        } catch (Exception e) {
            throw handleException(e);
        }
        return authorization;
    }

    public String getContentPage(Long id) throws ApplicationException {
        isAuthorized();
        String url = UrlBuilder.getContentPage(id);
        String response = null;
        try {
            Logger.i(getClass(), "getContentPage" + url);
            response = HttpUtil.makeGetRequest(url, mConf.getAuthorizationToken());

            ContentPageResponse page = EntityParser.parseEntity(response, ContentPageResponse.class);
            handleErrors(page);
            return page.getContent();

        } catch (Exception e) {
            throw handleException(e);
        }
    }

    /**
     * @return the mRatingPageId
     */
    public final Long getRatingPageId() {
        return mRatingPageId;
    }

    /**
     * @return the mProfile
     */
    public final Profile getProfile() {
        if (mProfile == null) {
            Logger.e(getClass(), "mProfile is Null");
        }
        return mProfile;
    }

}
