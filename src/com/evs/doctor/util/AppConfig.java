package com.evs.doctor.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.evs.doctor.R;
import com.evs.doctor.model.PublicationType;
import com.evs.doctor.model.Specialties;

/**
 * Wrapper for {@link SharedPreferences} that retrieve and store application specific properties
 * 
 * */
public class AppConfig {

    private static AppConfig mConf;
    private static String homeDirPath;
    private final SharedPreferences mPref;
    private final Context mCtx;
    private static final String KEY_AUTHORIZATION_TOKEN = "KEY_AUTHORIZATION_TOKEN";
    private static final String KEY_USER_NAME = "KEY_USER_NAME";
    public static final String LAST_UPDATE_TIMESTAMP = "lastUpdateTimestamp";
    private static final String KEY_PWD = "KEY_PWD";
    public static final int DEFAULT_SPINNER_NULL_ID = -1;

    private AppConfig(Context context) {
        Logger.d(getClass(), "initialized");
        mCtx = context;
        mPref = mCtx.getSharedPreferences("config", Context.MODE_PRIVATE);
        homeDirPath = FileUtil.getExternalDir(context);
        FileUtil.createFolderIfNoExists(homeDirPath);

    }

    /**
     * @return the homeDirPath
     */
    public static final String getHomeDir() {
        return homeDirPath;
    }

    public static boolean isDevMode() {
        // getInstance().mPref.edit().putBoolean("IS_DEV", false).commit();
        return getInstance().mPref.getBoolean("IS_DEV", false);
    }

    public String getAuthorizationToken() {
        return mPref.getString(KEY_AUTHORIZATION_TOKEN, null);
    }

    public void setAuthorizationToken(String token) {
        mPref.edit().putString(KEY_AUTHORIZATION_TOKEN, token).commit();
    }

    public String getUserName() {
        return mPref.getString(KEY_USER_NAME, null);
    }

    public void setUserName(String userName) {
        mPref.edit().putString(KEY_USER_NAME, userName).commit();
    }

    public String getPassword() {
        return mPref.getString(KEY_PWD, null);
    }

    public void setPassword(String pwd) {
        mPref.edit().putString(KEY_PWD, pwd).commit();
    }

    /**
     * @return
     */
    public static AppConfig getInstance() {
        if (mConf == null) {
            throw new RuntimeException("AppConfig  is not initialized");
        }
        return mConf;
    }

    /**
     * @return
     */
    public static void init(Context applicationContext) {
        mConf = new AppConfig(applicationContext);
    }

    public static Context getAppContext() {
        return mConf.mCtx;
    }

    /**
     * @return
     */
    public static String getSiteUrl() {
        return "http://doctoratwork.ru";
    }

    public static PublicationType getDefaultType() {
        PublicationType nullType = new PublicationType();
        nullType.setTitle(getAppContext().getString(R.string.choose_publication_type));
        nullType.setId(AppConfig.DEFAULT_SPINNER_NULL_ID);
        return nullType;
    }

    public static Specialties getDefaultSpeciality() {
        Specialties fakeSpeciality = new Specialties();
        fakeSpeciality.setName(getAppContext().getString(R.string.choose_speciality));
        fakeSpeciality.setId(DEFAULT_SPINNER_NULL_ID);
        return fakeSpeciality;
    }

}
