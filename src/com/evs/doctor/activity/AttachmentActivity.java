package com.evs.doctor.activity;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import com.evs.doctor.R;
import com.evs.doctor.util.AppConfig;
import com.evs.doctor.util.BitmapUtil;
import com.evs.doctor.util.FileUtil;
import com.evs.doctor.util.Logger;
import com.evs.doctor.view.CachedImageView;
import com.evs.doctor.view.NavigationBarView;
import com.evs.doctor.view.SlideGalleryView;
import com.evs.doctor.view.SlideGalleryView.OnImageClickListener;

public class AttachmentActivity extends GenericActivity {
    private static final int SELECT_PICTURE = 0001;
    private static final int TAKE_NEW_PHOTO = 0002;

    public static final String EXTRA_IMAGES = "EXTRA_IMAGES";
    public static final String EXTRA_EDITABLE = "EXTRA_IS_EDITABLE";

    private CachedImageView mPhoto;
    private SlideGalleryView mSlideGalaryView;

    private ArrayList<String> mImages = new ArrayList<String>();
    private String homeDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attachment);
        mPhoto = (CachedImageView) findViewById(R.id.photo);
        homeDir = AppConfig.getHomeDir();

        ((NavigationBarView) findViewById(R.id.view_navbar)).updateBar(getString(R.string.attachments), null, null,
                this);

        mSlideGalaryView = (SlideGalleryView) findViewById(R.id.attachment_gallery);
        mSlideGalaryView.setOnImageClickListener(new OnImageClickListener() {

            @Override
            public void clickedItem(View v, String path) {
                setPhotoBitmap(path);
            }
        });

        Bundle extras = savedInstanceState == null ? getIntent().getExtras() : savedInstanceState;

        FileUtil.createFolderIfNoExists(homeDir);
        if (extras.getBoolean(EXTRA_EDITABLE, true)) {
            findViewById(R.id.attachment_buttons_panel).setVisibility(View.VISIBLE);
            findViewById(R.id.attachment_add).setOnClickListener(this);
            findViewById(R.id.attachment_remove).setOnClickListener(this);
        } else {
            findViewById(R.id.attachment_buttons_panel).setVisibility(View.GONE);
            findViewById(R.id.attachement_gallery_container).setVisibility(View.VISIBLE);
        }

        if (extras.getStringArrayList(EXTRA_IMAGES) != null) {
            mImages = extras.getStringArrayList(EXTRA_IMAGES);
            mSlideGalaryView.initByPath(mImages);
            findViewById(R.id.attachement_gallery_container).setVisibility(View.VISIBLE);
            if (mImages.size() > 0) {
                setPhotoBitmap(mImages.get(0));
            }
        }
    }

    private void chooseAttachmentOption() {
        String[] s = { "Camera", "Gallery" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose images options").setItems(s, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                case 0:
                    getImageFromCamera();
                    break;
                case 1:
                    getImageFromGallery();
                    break;
                }
            }
        });
        builder.create().show();
    }

    private void getImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        String pickTitle = "Select or take a new Picture";
        Intent chooserIntent = Intent.createChooser(intent, pickTitle);
        startActivityForResult(chooserIntent, SELECT_PICTURE);
    }

    private void getImageFromCamera() {
        String fileName = FileUtil.getNextFileName(homeDir, "IMG", ".jpg");
        Uri imgUri = FileUtil.getFileUri(homeDir + File.separator, fileName);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        getIntent().putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(cameraIntent, TAKE_NEW_PHOTO);
        Logger.d(getClass(), "getImageFromCamera");
    }

    @Override
    public void onClick(View v) {
        super.handleClick(v);
        switch (v.getId()) {
        case R.id.attachment_add:
            chooseAttachmentOption();
            break;
        case R.id.attachment_remove:
            Uri uri = (Uri) mPhoto.getTag();
            if (uri != null && mImages.remove(uri.toString())) {
                mSlideGalaryView.initByPath(mImages);
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (RESULT_OK == resultCode) {
            Uri uri = null;

            switch (requestCode) {
            case SELECT_PICTURE:

                String[] filePathColumn = { MediaStore.Images.Media.DATA };
                Cursor cursor = getContentResolver().query(data.getData(), filePathColumn, null, null, null);
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();
                mImages.add(filePath);
                setPhotoBitmap(filePath);
                mSlideGalaryView.initByPath(mImages);

                findViewById(R.id.attachement_gallery_container).setVisibility(View.VISIBLE);
                break;
            case TAKE_NEW_PHOTO:
                Logger.d(getClass(), "got ImageFromCamera");
                uri = (Uri) getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
                if (uri != null && FileUtil.isFileExists(uri.getPath())) {
                    String path = uri.getPath();
                    mImages.add(path);
                    int h = getWallpaper().getMinimumHeight();
                    int w = getWallpaper().getMinimumWidth();
                    BitmapUtil.reduceImage(path, w, h);
                    mSlideGalaryView.initByPath(mImages);
                    setPhotoBitmap(path);
                    findViewById(R.id.attachement_gallery_container).setVisibility(View.VISIBLE);
                }
                break;
            }
        } else {
            String msg = "cann't take Photo";
            Logger.log(getClass(), "Cann't take Photo" + msg, null);
        }
    }

    private void setPhotoBitmap(String path) {

        if (FileUtil.isFileExists(path)) {
            mPhoto.setImageBitmap(BitmapUtil.getBitmap(path, 2));
        } else {
            mPhoto.loadImage(path);
        }

        mPhoto.setTag(path);

    }

    @Override
    public void onBackPressed() {
        if (!mImages.isEmpty()) {
            Intent i = getIntent();
            i.putExtra("IMAGES", mImages);
            setResult(Activity.RESULT_OK, i);
        } else {
            setResult(Activity.RESULT_CANCELED);
        }

        super.onBackPressed();
        Logger.i(getClass(), "On Back pressed");
    }

    /**{@inheritDoc}*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        getIntent().putStringArrayListExtra(EXTRA_IMAGES, mImages);
        super.onSaveInstanceState(outState);
        outState.putAll(getIntent().getExtras());
    }
}
