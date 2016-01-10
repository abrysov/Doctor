package com.evs.doctor.adapter;

import android.view.View;
import android.widget.EditText;

import com.evs.doctor.R;
import com.evs.doctor.model.Authorization;

public class LoginLayoutBinder implements ILayoutBinder<Authorization> {
    /**{@inheritDoc}*/
    @Override
    public void bindView(View view, Authorization item) {
        ((EditText) view.findViewById(R.id.login_userName)).setText(item.getUserName());
    }

    /**{@inheritDoc}*/
    @Override
    public void buildModel(Authorization bean, View view) {
        if (bean != null && view != null) {
            bean.setUserName(((EditText) view.findViewById(R.id.login_userName)).getText().toString());
            String pwd = ((EditText) view.findViewById(R.id.loginPassword)).getText().toString();
            bean.setPassword(pwd);// SecurityUtil.stringToMD5(pwd)
        }
    }

    /**{@inheritDoc}*/
    @Override
    public int getViewLayoutId() {
        return R.layout.activity_login;
    }
}
