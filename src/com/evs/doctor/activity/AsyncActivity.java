package com.evs.doctor.activity;

import android.content.res.Configuration;
import android.os.Bundle;

import com.evs.doctor.service.ApplicationException;
import com.evs.doctor.service.AuthorizationException;
import com.evs.doctor.util.ATask;
import com.evs.doctor.util.ATask.AsyncHandler;
import com.evs.doctor.util.DialogUtils;
import com.evs.doctor.util.Logger;

public abstract class AsyncActivity<Param, Result> extends GenericActivity implements AsyncHandler<Param, Result> {

    private ATask<Param, Result> mAsyncTask;
    private String mAsyncMessage;
    private String mAsyncTitle;

    /** {@inheritDoc} */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAsyncTask = ATask.restoreTask(getLastNonConfigurationInstance(), this, this);
    }

    /**{@inheritDoc}*/
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Logger.i(getClass(), "onConfigurationChanged " + newConfig);
    }

    /**{@inheritDoc}*/
    @Override
    public Object onRetainNonConfigurationInstance() {
        return ATask.retainTask(mAsyncTask);
    }

    protected void initAsyncTask(String title, String message) {
        mAsyncMessage = message;
        mAsyncTitle = title;
    }

    /**
     * @param error
     */
    protected void showErrorDialog(String error) {
        DialogUtils.buildErrorDialog(this, error).show();
    }

    public void handleErrorInUIThread(final ApplicationException e) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                handleError(e);
            }
        });
    }

    /**Return false if exception is fatal
     * True   continue UI updating*/
    @Override
    public boolean handleError(ApplicationException e) {
        if (e != null) {
            if (e instanceof AuthorizationException) {
                startActivity(LoginActivity.class);
                return false;
            }
            Logger.log(getClass(), "handleError", e);
            String msg = e.getLocalizedMessage(getResources());
            DialogUtils.buildErrorDialog(this, msg == null ? e.getMessage() : msg).show();
            return false;
        }
        return true;
    }

    public abstract Result doInBackgroundThread(Param... params) throws ApplicationException;

    protected void doAsync(Param... params) {
        if (mAsyncTask == null || !mAsyncTask.isWorking()) {
            mAsyncTask = new ATask<Param, Result>();
            mAsyncTask.setMessage(mAsyncMessage);
            mAsyncTask.setTitle(mAsyncTitle);
            mAsyncTask.setHandler(this);
            mAsyncTask.setupUIHandlers(this);
            mAsyncTask.execute(params);
        } else {
            Logger.w(getClass(), "Async task is already working");
        }
    }

}
