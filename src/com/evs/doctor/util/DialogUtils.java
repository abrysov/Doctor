package com.evs.doctor.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;

import com.evs.doctor.R;

public class DialogUtils {
    /**
     * Create dialog with custom title and message
     * @param context current activity context. Set parent activity context if it's child activity
     * @param title - Dialog title
     * @param message - Dialog message
     */
    private static AlertDialog.Builder buildDialog(Context context, String title, String message,
            final Callback callback) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        if (title != null) {
            builder.setTitle(title);
        }
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage(message).setCancelable(true);
        builder.setNeutralButton(context.getText(android.R.string.ok), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
                if (callback != null) {
                    callback.doIt();
                }
            }
        });
        return builder;
    }

    /**
     * Create dialog with custom title and message
     * @param context current activity context. Set parent activity context if it's child activity
     * @param title - Dialog title
     * @param message - Dialog message
     */
    public static Dialog buildMessageDialog(Context context, String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setIcon(android.R.drawable.ic_dialog_info);
        builder.setMessage(message).setCancelable(true);
        return builder.create();
    }

    public static Dialog buildErrorDialog(Context context, String message) {
        // TODO: add icon
        return buildDialog(context, context.getString(R.string.dialog_error_title), message, null).create();
    }

    public static AlertDialog createOkDialog(Context context, CharSequence title, CharSequence message,
            CharSequence buttonText) {
        Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title).setMessage(message).setIcon(android.R.drawable.ic_dialog_info);
        builder.setPositiveButton(buttonText, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return builder.create();
    }

    /**
     * Create dialog with custom title and message and closed and close after time period.
     * @param context current activity context. Set parent activity context if it's child activity
     * @param title - Dialog title
     * @param message - Dialog message
     * @param delay - after this time in ms  dialog will closed automatically 
     * @param callback - implement this interface if you  want execute any action before close
     */
    public static void buildAutoCloseDialog(Context context, String title, String message, int delay,
            final Callback callback) {
        final Dialog dialog = buildMessageDialog(context, title, message);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null && callback.doIt() == 1 && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, delay);
    }

    public static void closeDialogAndActivityWithDelay(final Activity activity, final AlertDialog dialog, int delay) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!activity.isFinishing()) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        }, delay);
    }

    /**
     * @param context
     * @return
     */
    public static Dialog buildNoInternetDialog(Context context) {
        return buildDialog(context, context.getString(R.string.dialog_no_internet_title),
                context.getString(R.string.error_no_internet_message), null).create();
    }

    /**
     * @param context
     * @return
     */
    public static Dialog buildNoAuthorizedDialog(Context context) {
        return buildDialog(context, context.getString(R.string.dialog_error_title),
                context.getString(R.string.error_authorization), null).create();
    }

    public static void showErrorDialog(Context context, String message, Callback callback) {
        buildDialog(context, context.getString(R.string.dialog_error_title), message, callback).create().show();
    }

    /**
     * @param string
     * @param callback
     */
    public static void showOkDialog(Context context, String message, Callback callback) {
        buildDialog(context, null, message, callback).create().show();
    }

}
