package com.evs.doctor.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

public class BitmapUtil {

    public static Drawable getDrawableFromPath(String pathName) {
        return BitmapDrawable.createFromPath(pathName);
    }

    public static Drawable getDrawableFromUri(Uri uri) {
        return BitmapDrawable.createFromPath(uri.getPath());
    }

    public static Bitmap getBitmap(String path, int inSampleSize) {
        File f = null;
        if (path != null && (f = new File(path)).exists()) {
            return getBitmap(f, inSampleSize);
        }
        return null;
    }

    public static Bitmap getBitmap(String path, int reqWidth, int reqHeight) {

        if (path != null && new File(path).exists()) {
            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.outHeight = reqHeight;
            bitmapOptions.outWidth = reqWidth;
            initBitmapOptions(path, bitmapOptions);
            return getBitmapWithOptions(path, bitmapOptions);
        }
        return null;
    }

    public static Bitmap getBitmapWithOptions(String path, BitmapFactory.Options bitmapOptions) {
        if (path != null && new File(path).exists()) {
            initBitmapOptions(path, bitmapOptions);
            return getBitmap(path, bitmapOptions);
        }
        return null;
    }

    private static Bitmap getBitmap(String path, BitmapFactory.Options bitmapOptions) {
        Logger.d(BitmapUtil.class, "getBitmap for " + path);
        try {
            // Decode bitmap with inSampleSize set
            bitmapOptions.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(path, bitmapOptions);
        } catch (OutOfMemoryError e) {
            Logger.e(BitmapUtil.class, e);
        } catch (Throwable e) {
            Logger.e(BitmapUtil.class, e);
        }
        return null;
    }

    private static void initBitmapOptions(String path, BitmapFactory.Options options) {
        final int reqWidth = options.outWidth;
        final int reqHeight = options.outHeight;

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Raw height and width of image
        final int realHeight = options.outHeight;
        final int realWidth = options.outWidth;
        int inSampleSize = 1;

        if (realHeight > reqHeight || realWidth > reqWidth) {
            if (reqHeight > 0 && reqWidth > 0) {
                if (realWidth > realHeight) {
                    inSampleSize = Math.round((float) realHeight / (float) reqHeight);
                } else {
                    inSampleSize = Math.round((float) realWidth / (float) reqWidth);
                }
            } else {
                inSampleSize = reqWidth == 0 ? Math.round((float) realHeight / (float) reqHeight) : Math
                        .round((float) realWidth / (float) reqWidth);
            }

        }
        options.inSampleSize = inSampleSize;
    }

    public static Bitmap getBitmap(File file, int inSampleSize) {
        Logger.d(BitmapUtil.class, "getBitmap for " + file.getPath());
        try {
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getPath(), options);

            // Calculate inSampleSize
            options.inSampleSize = inSampleSize;

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;
            return BitmapFactory.decodeFile(file.getPath(), options);
        } catch (OutOfMemoryError e) {
            Logger.e(BitmapUtil.class, e);
        } catch (Throwable e) {
            Logger.e(BitmapUtil.class, e);
        }
        return null;
    }

    public static Bitmap convertDrawableToBitmap(Drawable drawable) {
        return drawable == null ? null : ((BitmapDrawable) drawable).getBitmap();
    }

    public static Bitmap getBitmapFromResources(Context context, int resource) {
        return convertDrawableToBitmap(context.getResources().getDrawable(resource));
    }

    public static byte[] getBitmapAsJpegBytes(String path, int maxWidth, int maxHeight) {
        Logger.d(BitmapUtil.class, "getBitmapAsJpegBytes for " + path);
        byte[] b = null;
        Bitmap bm = getBitmap(path.replace("file://", ""), maxWidth, maxHeight);
        if (bm != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 10, baos);
            b = baos.toByteArray();
            FileUtil.closeStream(baos);
            bm.recycle();
        }
        return b;
    }

    /**
     * Reduce file  by the {@link Uri}
     * @param i
     */
    public static boolean reduceImage(String path, int reqWidth, int reqHeight) {
        File file = null;
        if (path == null || !(file = new File(path)).exists()) {
            return false;
        }
        Logger.d(BitmapUtil.class, "reduceImage for " + path);
        Bitmap bitmap = getBitmap(path, reqWidth, reqHeight);
        if (bitmap != null) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
                bitmap.recycle();
                Logger.d(BitmapUtil.class, "reduceImage for " + file.getPath() + " ok!");
                return true;
            } catch (Exception e) {
                Logger.e(BitmapUtil.class, e);
            } finally {
                FileUtil.closeStream(fos);
            }
        }
        return false;
    }

    public static Bitmap getBitmapFromWebUrl(String url) {
        Bitmap bitmap = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.connect();
            InputStream input = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(input);
        } catch (Exception e) {
            Logger.e(BitmapUtil.class, e);
        }
        return bitmap;
    }
}
