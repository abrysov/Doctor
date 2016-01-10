package com.evs.doctor.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.evs.doctor.R;
import com.evs.doctor.adapter.LoginLayoutBinder;
import com.evs.doctor.model.Authorization;
import com.evs.doctor.service.ApiService;
import com.evs.doctor.service.ApplicationException;
import com.evs.doctor.util.AppConfig;
import com.evs.doctor.util.DialogUtils;
import com.evs.doctor.util.Logger;

public class LoginActivity extends AsyncActivity<Authorization, Authorization> {
    private LoginLayoutBinder mLoginViewBinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mLoginViewBinder = new LoginLayoutBinder();
        ((TextView) findViewById(R.id.login_userName)).setText(getConfig().getUserName());
        findViewById(R.id.login_enter).setOnClickListener(this);
        findViewById(R.id.login_goto_site).setOnClickListener(this);

        initAsyncTask("Авторизация.", "Пожалуйста подождите проверки прав доступа.");

        Authorization ab = new Authorization();
        ab.setUserName(getConfig().getUserName());
        ab.setPassword(getConfig().getPassword());

        if (ab.getUserName() != null && ab.getPassword() != null) {
            doAsync(ab);
        }
    }


    @Override
    public void handleClick(View v) {
        switch (v.getId()) {
        case R.id.login_enter:
            if (hasInternet()) {
                Logger.i(getClass(), "Have internet  connection");
                Authorization ab = new Authorization();
                mLoginViewBinder.buildModel(ab, findViewById(R.id.view_layout_login));
                doAsync(ab);
            } else {
                DialogUtils.buildNoInternetDialog(this).show();
            }
            break;
        case R.id.login_goto_site:
            Uri url = Uri.parse(AppConfig.getSiteUrl());
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, url);
            startActivity(browserIntent);
            break;
        }
    }

    /**{@inheritDoc}*/
    @Override
    public Authorization doInBackgroundThread(Authorization... params) throws ApplicationException {
        return ApiService.getInstance().doLogin(params[0]);
    }

    /**{@inheritDoc}*/
    @Override
    public void onTaskComplete(Authorization authorization) {
        if (authorization != null && authorization.getToken() != null) {
            Logger.i(getClass(), "authorization result is success");
            getConfig().setAuthorizationToken(authorization.getToken());
            getConfig().setUserName(authorization.getUserName());
            getConfig().setPassword(authorization.getPassword());

            startActivity(PublicationListActivity.class);
        } else {
            getConfig().setPassword(null);
            getConfig().setAuthorizationToken(null);
            String eMsg = getString(R.string.error_authorization);
            DialogUtils.buildErrorDialog(this, eMsg).show();
        }
    }

}
