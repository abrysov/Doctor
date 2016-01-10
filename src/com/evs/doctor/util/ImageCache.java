package com.evs.doctor.util;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

import android.app.ActivityManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

public class ImageCache {
    private static ImageCache instance = null;
    private String mHomeDir;
    private LruCache<String, Bitmap> mMemoryCache = null;
    private int maxWidth;
    private int maxHeight;

    public static synchronized ImageCache getInstance() {
        if (instance == null) {
            instance = new ImageCache();
        }
        return instance;
    }

    public void init(Context context) {
        mHomeDir = AppConfig.getHomeDir();
        maxHeight = context.getWallpaper().getMinimumHeight();
        maxWidth = context.getWallpaper().getMinimumWidth();
        int memClass = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass();
        mMemoryCache = new LruCache<String, Bitmap>(1024 * 1024 * memClass / 8);
    }

    /**
     * @return the mHomeDir
     */
    public final String getHomeDir() {
        return mHomeDir;
    }

    /**
     * Return image  from memory cache
     * @param url
     * @return
     */
    private Bitmap getFromCache(String url) {
        Bitmap b = mMemoryCache.get(url);
        if (b != null) {
            Logger.d(ImageCache.class, "Got from MemoryCache");
        }
        return b;
    }

    /**
     * Looking for  image  in the memory cache  and  on  storage  
     * @param url
     * @return
     */
    public Bitmap getFromSD(String url) {
        Bitmap bitmap = null;
        try {
            String imageUrl = (new File(url).exists()) ? url : getStorePath(url);
            if (imageUrl != null && new File(imageUrl).exists()) {
                // (BitmapDrawable) Drawable.createFromPath(imageUrl);
                bitmap = BitmapUtil.getBitmap(imageUrl, maxWidth, maxHeight);
                mMemoryCache.put(url, bitmap);
            } else {
                String msg = "File is not exists on SD card. Initial Url: %s \n Rewrited: %s";
                Logger.d(getClass(), String.format(msg, url, imageUrl));
            }
        } catch (Throwable e) {
            Logger.e(getClass(), e);
        }
        return bitmap;
    }

    /**
     * Worked  with web images  that  needs to be downloaded.
     * !!! Must  been called   not from UI Thread
     * Use  URL rewriting  for web url's
     * Looking for  image  in the memory cache  and  on  storage if no exists download from web
     *   
     *   
     * @param url
     * @return
     */
    public Bitmap downloadImageIfNotExists(String url) {
        if (url == null) {
            return null;
        }
        Bitmap bitmap = getFromSD(getStorePath(url));
        if (bitmap == null) {
            InputStream is;
            try {
                is = new URL(url).openStream();
                File f = FileUtil.download(is, getStorePath(url));
                f.exists();
                bitmap = getFromSD(url);
                if (bitmap == null) {
                    Logger.i(getClass(), "Load from Web " + url);
                    bitmap = BitmapUtil.getBitmapFromWebUrl(url);
                    if (bitmap != null) {
                        mMemoryCache.put(url, bitmap);
                    }
                }
            } catch (Exception e) {
                Logger.w(getClass(), "Cann't download image from url: " + url);
            }
        }
        return bitmap;
    }

    protected Bitmap get(String url) {
        Bitmap image = getFromCache(url);
        if (image == null) {
            Logger.d(getClass(), String.format("Image with url %s is not cached url: ", url));
            image = getFromSD(url);
        }
        return image;
    }

    /**
     * @param url
     * @return
     */
    private String getStorePath(String url) {
        return url == null ? null : getHomeDir() + Math.abs(url.hashCode()) + url.length() + ".jpg";
    }
}