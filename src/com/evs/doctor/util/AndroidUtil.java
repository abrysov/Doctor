package com.evs.doctor.util;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.evs.doctor.model.ImageLink;

public class AndroidUtil {
    public static void setText(View view, int viewId, CharSequence text) {
        if (view != null) {
            setText(view.findViewById(viewId), text);
        }
    }

    /**
     * @param findViewById
     * @param userName
     */
    public static void setText(View v, CharSequence text) {
        if (v != null && v instanceof TextView) {
            ((TextView) v).setText(text);
        }
    }

    public static String getText(View view, int viewId) {
        View v = view.findViewById(viewId);
        return getText(v);
    }

    public static boolean isSdCardUsed() {
        return Environment.getExternalStorageState() != null
                && Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * @param findViewById
     * @return
     */
    public static String getText(View v) {
        if (v != null && v instanceof TextView) {
            return ((TextView) v).getText().toString();
        }
        return null;
    }

    public static void hideSoftKeyboard(Activity activity) {

        if (activity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }

    /**
     * @param images
     * @return
     */
    public static ArrayList<String> convertToStringArray(List<ImageLink> images) {
        if (images == null) {
            return null;
        }
        ArrayList<String> list = new ArrayList<String>(images.size());
        for (ImageLink imageLink : images) {
            list.add(imageLink.getOriginal());
        }
        return list;
    }

}
