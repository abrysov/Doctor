package com.evs.doctor.util;

import java.lang.Thread.State;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

import com.evs.doctor.R;
import com.evs.doctor.service.ApplicationException;

public class ATask<Param, Result> extends android.os.AsyncTask<Param, Object, Result> {

    private ProgressDialog mProgressDialog;
    private ApplicationException mErrors;

    private boolean mIsCancelale;
    protected boolean mFinished;
    private boolean isProgressBarDialog;
    private CharSequence mDialogMessage;
    private AsyncHandler<Param, Result> mHandler;
    private Context mContext;
    private Thread mThred;
    private CharSequence mDialogTitle;

    public ATask() {
        mIsCancelale = false;
    }

    public void setHandler(AsyncHandler<Param, Result> handler) {
        mHandler = handler;
    }

    /**
     * @param genericAsyncActivity
     */
    public void setupUIHandlers(Context context) {
        mContext = context;
        if (mDialogMessage == null) {
            mDialogMessage = context.getText(R.string.text_loading);
        }
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        } else {
            mProgressDialog = new ProgressDialog(context);
        }

        if (mDialogTitle == null) {
            mDialogTitle = context.getText(R.string.text_loading);
        }
        mProgressDialog.setTitle(mDialogTitle);
        mProgressDialog.setMessage(mDialogMessage);
        mProgressDialog.setCancelable(mIsCancelale);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMax(100);
        if (isProgressBarDialog) {
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        }
        mProgressDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                cancel(true);
                try {
                    mProgressDialog.dismiss();
                } catch (Exception e) {
                }
            }
        });
        mProgressDialog.show();
    }

    public void disableUIHandlers() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        mHandler = null;
    }

    public void initProgressDialog(String progressMessage, boolean isCancelable) {
        isProgressBarDialog = true;
        mDialogMessage = progressMessage;
        mIsCancelale = isCancelable;

    }

    @Override
    protected void onPreExecute() {
        if (mContext != null) {
            setupUIHandlers(mContext);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected Result doInBackground(Param... params) {
        Result result = null;
        try {
            Logger.i(mHandler.getClass(), "doInBackground");
            result = mHandler.doInBackgroundThread(params);
        } catch (ApplicationException e) {
            mErrors = e;
            Logger.e(getClass(), "handled exception", e);
        } catch (Exception e) {
            Logger.e(getClass(), "Unknown exception", e);
            mErrors = new ApplicationException(e);
        }
        mFinished = true;
        return result;
    }

    /** {@inheritDoc} */
    @Override
    protected void onPostExecute(Result result) {
        try {
            mProgressDialog.dismiss();
        } catch (Exception e) {
        }

        if (mHandler != null) {
            if (mHandler.handleError(mErrors)) {
                mHandler.onTaskComplete(result);
            }
        } else {
            Logger.w(getClass(), "no  handler");
        }
        mFinished = true;
    }

    public static interface AsyncHandler<Param, Result> {
        Result doInBackgroundThread(Param... params) throws ApplicationException;

        boolean handleError(ApplicationException e);

        void onTaskComplete(Result result);
    }

    public static interface IProgressTracker {

        // Notifies about task completeness
        void onComplete();

        /**
         * Updates progress message
         * @param from start value of progress
         * @param to max value of progress
         * @param delay  period of incrementing
         * @param message  text message
         */
        void onProgress(final int from, final int to, final int delay, final String message);
    }

    /**
     * @return
     */
    public boolean isWorking() {
        return !mFinished;
    }

    public static <Param, Result> ATask<Param, Result> restoreTask(Object retainInstance,
            AsyncHandler<Param, Result> handler, Context context) {
        ATask<Param, Result> aTask = null;
        if (retainInstance != null && retainInstance instanceof ATask) {
            aTask = (ATask) retainInstance;
            aTask.setupUIHandlers(context);
            aTask.setHandler(handler);
        }
        return aTask;
    }

    public static ATask retainTask(ATask aTask) {
        if (aTask != null) {
            if (aTask.isWorking()) {
                aTask.disableUIHandlers();
            } else {
                aTask = null;
            }
        }
        return aTask;
    }

    /** {@inheritDoc} */
    @Override
    protected void onCancelled() {
        super.onCancelled();
        mFinished = true;
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        super.onProgressUpdate(values);
        if (mProgressDialog.isShowing() && values != null && values[0] != null && values[1] != null) {
            mProgressDialog.setProgress((Integer) values[0]);
            mProgressDialog.setMessage((String) values[1]);
        }
    }

    public void progressUpdate(Integer percentage, String message) {
        publishProgress(percentage, message);
    }

    public void progressUpdate(final int from, final int to, final int delay, final String message) {
        try {
            if (mThred != null && mThred.getState() != State.TERMINATED) {
                mThred.interrupt();
            }
            mThred = new Thread(new Runnable() {

                @Override
                public void run() {
                    for (int i = from; i < to; i++) {
                        progressUpdate(i, message);
                        try {
                            Thread.sleep(delay);
                        } catch (InterruptedException e) {
                            break;
                        }
                    }
                }
            });
            mThred.start();
        } catch (Throwable e) {
            Logger.w(getClass(), e.getMessage());
        }
    }

    /**
     * @param mAsyncMessage
     */
    public void setMessage(String message) {
        mDialogMessage = message;
    }

    /**
     * @param mAsyncMessage
     */
    public void setTitle(String title) {
        mDialogTitle = title;
    }

}
