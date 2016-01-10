package com.evs.doctor;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.crittercism.app.Crittercism;
import com.evs.doctor.util.AppConfig;
import com.evs.doctor.util.DateParser;
import com.evs.doctor.util.ImageCache;

public class DoctorApp extends Application {
    private static DoctorApp mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        AppConfig.init(this);
        ImageCache.getInstance().init(this);
        DateParser.init(getResources());
        if (AppConfig.isDevMode()) {
            Toast.makeText(this, "App running in DEV_MODE", Toast.LENGTH_LONG).show();
        } else {
            Crittercism.init(getApplicationContext(), "50b3455341ae500497000004");
        }
    }

    public static Context getContext() {
        return mInstance.getApplicationContext();
    }

    public static String getApplicationPackage() {
        return DoctorApp.class.getPackage().getName();
    }
}
