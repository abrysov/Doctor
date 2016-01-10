package com.evs.doctor.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemSelectedListener;

import com.evs.doctor.R;
import com.evs.doctor.adapter.GenericAdapter;
import com.evs.doctor.adapter.IViewBinder;
import com.evs.doctor.adapter.PublicationSpecialitySpinnerBinder;
import com.evs.doctor.adapter.PublicationTypeSpinnerBinder;
import com.evs.doctor.model.Publication;
import com.evs.doctor.model.PublicationType;
import com.evs.doctor.model.Specialties;
import com.evs.doctor.service.ApiService;
import com.evs.doctor.service.ApplicationException;
import com.evs.doctor.util.*;
import com.evs.doctor.view.NavigationBarView;

public class PublicationCreateActivity extends AsyncActivity<Publication, String> {

    private static final int RC_ATTACHEMNT = 0111;

    private Spinner mType;
    private Spinner mSpecialities;
    private EditText mPublicationTitleEt;
    private EditText mPublicationContentEt;
    private TextView mPublicationAttachImageTv;
    private Button mSubmitBtn;

    private List<PublicationType> mTypesList;
    private List<Specialties> mSpecialitiesList;
    private Publication mPublication = new Publication();

    private ArrayList<String> mAttachedImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication_create);
        findViewById(R.id.create_publication_submit).setOnClickListener(this);
        findTextViewById(R.id.create_publication_attach_image).setOnClickListener(this);
        mTypesList = ApiService.getInstance().getPublicationTypeCanCreate();
        mSpecialitiesList = ApiService.getInstance().getSortedSpecialities();

        initUI();
    }

    private void initUI() {
        ((NavigationBarView) findViewById(R.id.view_navbar)).updateBar(
                getResources().getString(R.string.publication_list),
                getResources().getDrawable(R.drawable.ic_icon_arrow_left_white_back), this, this);
        mPublicationTitleEt = (EditText) findViewById(R.id.create_publication_title);
        mPublicationContentEt = (EditText) findViewById(R.id.create_publication_content);
        mPublicationAttachImageTv = (TextView) findViewById(R.id.create_publication_attach_image);
        mSubmitBtn = (Button) findViewById(R.id.create_publication_submit);

        initPublicationTypeSpinner();
        initPublicationSpecialitiesSpinner();
    }

    private void initPublicationTypeSpinner() {
        mType = (Spinner) findViewById(R.id.create_publication_type);

        IViewBinder<PublicationType> binder = new PublicationTypeSpinnerBinder();

        GenericAdapter<PublicationType> spinAdapter = new GenericAdapter<PublicationType>(this, binder, mTypesList);
        mType.setAdapter(spinAdapter);
        mType.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapter, View v, int pos, long id) {
                PublicationType type = mTypesList.get(pos);

                if (type.getId() != AppConfig.DEFAULT_SPINNER_NULL_ID) {
                    mPublication.setType(type.getId());
                    if (type.isSpecialtyRequired()) {
                        showAllRequiredUiElements();
                    } else {
                        showAllRequiredUiElements();
                        hideSpecialitiesSpinner();
                        mPublication.setAuthorSpecialty(null);
                    }
                } else {
                    hideAllRequiredUiElements();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
    }

    private void initPublicationSpecialitiesSpinner() {
        mSpecialities = (Spinner) findViewById(R.id.create_publication_speciality_spinner);
        IViewBinder<Specialties> specialtiesIViewBinder = new PublicationSpecialitySpinnerBinder();

        GenericAdapter<Specialties> spinAdapterSpecialities = new GenericAdapter<Specialties>(this,
                specialtiesIViewBinder, mSpecialitiesList);
        mSpecialities.setAdapter(spinAdapterSpecialities);
        mSpecialities.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Specialties specialties = mSpecialitiesList.get(position);

                if (specialties.getId() != AppConfig.DEFAULT_SPINNER_NULL_ID) {
                    mPublication.setAuthorSpecialty(specialties.getName());
                } else {
                    mPublication.setAuthorSpecialty(null);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    /**{@inheritDoc}*/
    @Override
    protected void handleClick(View v) {
        super.handleClick(v);
        switch (v.getId()) {
        case R.id.create_publication_attach_image:
            Intent i = new Intent(this, AttachmentActivity.class);
            i.putStringArrayListExtra(AttachmentActivity.EXTRA_IMAGES, mAttachedImages);
            i.putExtra(AttachmentActivity.EXTRA_EDITABLE, true);
            startActivityForResult(i, RC_ATTACHEMNT);
            break;
        case R.id.create_publication_submit:
            submitPublication();
            break;
        case R.id.nav_title:
            finish();
            break;
        // case R.id.nav_fl_push_area:
        // openOptionsMenu();
        // break;
        }
    }

    private void submitPublication() {
        if (isFieldsFilled()) {
            mPublication.setTitle(findTextViewById(R.id.create_publication_title).getText().toString());
            mPublication.setContent(findTextViewById(R.id.create_publication_content).getText().toString());
            new PublicationWithAttachmentSender().execute();
        }
    }

    private boolean isFieldsFilled() {
        PublicationType publicationType = (PublicationType) mType.getSelectedItem();
        Specialties specialties = (Specialties) mSpecialities.getSelectedItem();

        if (publicationType.getId() == AppConfig.DEFAULT_SPINNER_NULL_ID) {
            showToasMessage(getString(R.string.choose_publication_type));
            return false;
        }

        if (mSpecialities.getVisibility() == View.VISIBLE) {
            if (specialties.getId() == AppConfig.DEFAULT_SPINNER_NULL_ID) {
                showToasMessage(getString(R.string.choose_speciality));
                return false;
            }
        }

        if (mPublicationTitleEt.getText().toString().equals("")) {
            showToasMessage(getString(R.string.input_publication_title));
            return false;
        }

        if (mPublicationContentEt.getText().toString().equals("")) {
            showToasMessage(getString(R.string.input_publication_text));
            return false;
        }

        return true;
    }

    private void showToasMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_ATTACHEMNT && resultCode == RESULT_OK) {
            mAttachedImages = data.getStringArrayListExtra("IMAGES");
            if (mAttachedImages.size() > 10) {
                Toast.makeText(this, "Только 10-ть изображений будет загружено", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void showAllRequiredUiElements() {
        mPublicationTitleEt.setVisibility(View.VISIBLE);
        mSpecialities.setVisibility(View.VISIBLE);
        mPublicationContentEt.setVisibility(View.VISIBLE);
        mPublicationAttachImageTv.setVisibility(View.VISIBLE);
        mSubmitBtn.setVisibility(View.VISIBLE);
    }

    private void hideAllRequiredUiElements() {
        mPublicationTitleEt.setVisibility(View.INVISIBLE);
        mSpecialities.setVisibility(View.INVISIBLE);
        mPublicationContentEt.setVisibility(View.INVISIBLE);
        mPublicationAttachImageTv.setVisibility(View.INVISIBLE);
        mSubmitBtn.setVisibility(View.INVISIBLE);
    }

    private void showSpecialitiesSpinner() {
        mSpecialities.setVisibility(View.VISIBLE);
    }

    private void hideSpecialitiesSpinner() {
        mSpecialities.setVisibility(View.GONE);
    }

    /**{@inheritDoc}*/
    @Override
    public void onTaskComplete(String result) {
        // setResult(RESULT_OK);
        // finish();
    }

    /**{@inheritDoc}*/
    @Override
    public String doInBackgroundThread(Publication... params) throws ApplicationException {
        // ApiService.getInstance().createPublication(mPublication, null);
        return null;
    }

    private void showUploadPublicationDialog() {
        final ProgressDialog mProgressDialog = new ProgressDialog(this);
        CharSequence mDialogMessage = getString(R.string.sending_publication);
        mProgressDialog.setMessage(mDialogMessage);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.setMax(100);
        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                try {
                    mProgressDialog.dismiss();
                } catch (Exception e) {
                    Logger.e(getClass(), e);
                }
            }
        });
        mProgressDialog.show();
    }

    private void showPublicationWasNotSentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.publication_was_not_sent));
        builder.setCancelable(false);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        builder.create().show();
    }

    public String getImageAsBase64String(String path) {
        if (!FileUtil.isFileExists(path)) {
            Logger.e(getClass(), "getImageAsBase64String:  image is not exists: " + path);
            return null;
        }
        int h = getWallpaper().getMinimumHeight();
        int w = getWallpaper().getMinimumWidth();
        byte[] b = BitmapUtil.getBitmapAsJpegBytes(path, w, h);
        if (b != null) {
            return Base64.encodeToString(b, Base64.DEFAULT);
        } else {
            Logger.e(FileUtil.class, "can't convert to bytes image " + path);
            return null;
        }
    }

    private class PublicationWithAttachmentSender extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... paths) {
            publishProgress();
            try {
                ArrayList<String> imagesToSend = new ArrayList<String>();

                if (mAttachedImages != null) {
                    for (String path : mAttachedImages) {
                        String string = getImageAsBase64String(path);
                        if (string != null) {
                            imagesToSend.add(string);
                        }
                    }
                }
                ApiService.getInstance().createPublication(mPublication, imagesToSend);
            } catch (ApplicationException e) {
                Logger.e(getClass(), e);
                return false;
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                DialogUtils.showOkDialog(PublicationCreateActivity.this, getString(R.string.publication_sent),
                        new Callback() {

                            @Override
                            public int doIt() {
                                finish();
                                return 0;
                            }
                        });
            } else {
                showPublicationWasNotSentDialog();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
            showUploadPublicationDialog();
        }
    }

    /**{@inheritDoc}*/
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        Logger.w(getClass(), "onLowMemory");
    }
}
