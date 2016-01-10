package com.evs.doctor.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;

import com.evs.doctor.util.ImageCache;
import com.evs.doctor.util.Logger;

public class CachedImageView extends ImageView {
    public CachedImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public CachedImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CachedImageView(Context context) {
        super(context);
    }

    public void loadImage(String url) {
        if (url != null) {
            Bitmap bitmap = ImageCache.getInstance().getFromSD(url);
            if (bitmap == null) {
                Logger.i(getClass(), "No image in cache Load async !!!. " + url);
                new AsyncImageLoader().execute(url);
            } else {
                setImageBitmap(bitmap);
            }
        } else {
            Logger.i(getClass(), "Invalid url.");
        }
    }

    private class AsyncImageLoader extends AsyncTask<String, String, Bitmap> {
        /**{@inheritDoc}*/
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap dr = null;
            try {
                dr = ImageCache.getInstance().downloadImageIfNotExists(params[0]);
            } catch (Exception e) {
                Log.e("AsyncImageLoader", "AsyncImageLoader", e);
            }
            return dr;
        }

        /**{@inheritDoc}*/
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            setImageBitmap(bitmap);
        }
    }
}
