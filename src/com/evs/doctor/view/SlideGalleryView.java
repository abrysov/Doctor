package com.evs.doctor.view;

import java.util.List;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import com.evs.doctor.R;
import com.evs.doctor.model.ImageLink;
import com.evs.doctor.util.BitmapUtil;
import com.evs.doctor.util.FileUtil;

public class SlideGalleryView extends HorizontalScrollView {

    private View view;
    private LayoutInflater inflater;
    private LinearLayout mGalleryPreview;
    private int mBorder;

    private OnImageClickListener mImageClickListener;
    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            if (mImageClickListener != null && v.getTag() != null) {
                String link = null;
                if (v.getTag() instanceof Uri) {
                    link = ((Uri) v.getTag()).getPath();
                } else {
                    link = (String) v.getTag();
                }
                mImageClickListener.clickedItem(v, link);
            }
        }
    };
    private int mPreviewHeight;

    public SlideGalleryView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public SlideGalleryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideGalleryView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.view_slide_galary, this);
        mGalleryPreview = (LinearLayout) view.findViewById(R.id.gallery_preview);
        mBorder = (int) context.getResources().getDimension(R.dimen.gallery_border);
        mPreviewHeight = (int) context.getResources().getDimension(R.dimen.gallery_preview_height);
    }

    private void addImageToGallery(ImageLink imageLink) {
        CachedImageView iv = (CachedImageView) inflater.inflate(R.layout.view_slide_galary_item, null);

        iv.setPadding(mBorder, 0, mBorder, 0);
        iv.setOnClickListener(mClickListener);
        iv.setTag(imageLink.getOriginal());

        int w = mPreviewHeight;
        if (FileUtil.isFileExists(imageLink.getThumbnail())) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.outHeight = mPreviewHeight;
            iv.setImageBitmap(BitmapUtil.getBitmapWithOptions(imageLink.getThumbnail(), options));
            w = options.outWidth;
            if (options.outHeight > mPreviewHeight) {
                w = (int) (w * ((double) mPreviewHeight / options.outHeight));
            }
        } else {
            iv.loadImage(imageLink.getThumbnail());
        }

        LayoutParams layoutParams = new LayoutParams(w, mPreviewHeight);
        mGalleryPreview.addView(iv, layoutParams);
    }

    /**
     * @param images
     */
    public void init(List<ImageLink> images) {
        if (images != null && images.size() > 0) {
            setVisibility(View.VISIBLE);
            mGalleryPreview.removeAllViews();

            for (ImageLink imageLink : images) {
                addImageToGallery(imageLink);
            }
        }
    }

    /**
     * @param images
     */
    public void initByPath(List<String> images) {
        if (images != null && images.size() > 0) {
            setVisibility(View.VISIBLE);
            mGalleryPreview.removeAllViews();

            for (String path : images) {
                addImageToGallery(new ImageLink(path));
            }
        }
    }

    public void refresh() {
    }

    public static interface OnImageClickListener {
        public void clickedItem(View v, String path);
    }

    /**
     * @param onImageClickListener
     */
    public void setOnImageClickListener(OnImageClickListener clickListener) {
        mImageClickListener = clickListener;
    }

}
