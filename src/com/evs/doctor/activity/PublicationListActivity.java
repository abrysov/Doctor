package com.evs.doctor.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;

import com.evs.doctor.R;
import com.evs.doctor.adapter.GenericAdapter;
import com.evs.doctor.adapter.IViewBinder;
import com.evs.doctor.adapter.PublicationBinder;
import com.evs.doctor.adapter.PublicationTypeSpinnerBinder;
import com.evs.doctor.model.Publication;
import com.evs.doctor.model.PublicationType;
import com.evs.doctor.service.ApiService;
import com.evs.doctor.service.ApplicationException;
import com.evs.doctor.util.AppConfig;
import com.evs.doctor.util.Logger;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class PublicationListActivity extends AsyncActivity<String, List<Publication>> implements OnItemClickListener,
        PullToRefreshBase.OnRefreshListener2 {

    private static final String EXTRA_PUBLICATIONS = "EXTRA_PUBLICATIONS";
    private static final String EXTRA_TYPE_POSITION = "EXTRA_TYPE_POSITION";

    private PullToRefreshListView mListView;
    private Spinner mTypeFilter;
    private Integer mCurrentTypeFilter;
    private ApiService mServ;
    private List<Publication> mPublicationList;
    private List<Publication> mPublicationListOlderItems;
    private int RC_PULICATION_CREATE;
    private GenericAdapter<Publication> mAdapter;
    private ImageButton mSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publication_list);
        mServ = ApiService.getInstance();
        mListView = (PullToRefreshListView) findViewById(R.id.pull_to_refresh_listview);
        mListView.setOnItemClickListener(this);
        mListView.setOnRefreshListener(this);
        mListView.setMode(PullToRefreshBase.Mode.BOTH);
        mSetting = (ImageButton) findViewById(R.id.publication_ib_settings);
        mSetting.setOnClickListener(this);

        initAsyncTask(getString(R.string.wait), getString(R.string.publications_are_being_loaded));

        findViewById(R.id.publication_create).setOnClickListener(this);
        Integer pos = null;
        if (savedInstanceState != null) {
            mPublicationList = (ArrayList<Publication>) savedInstanceState.getSerializable(EXTRA_PUBLICATIONS);
            pos = savedInstanceState.getInt(EXTRA_TYPE_POSITION);
        }
        initTypeFilterDropdown(pos);
        if (mPublicationList == null || mPublicationList.isEmpty()) {
            doAsync();
        } else {
            onTaskComplete(mPublicationList);
        }
    }

    private void initTypeFilterDropdown(Integer currentPosition) {
        mTypeFilter = (Spinner) findViewById(R.id.publication_filter);

        final List<PublicationType> publicationTypesList = ApiService.getInstance().getPublicationTypeCanSelect();

        if (publicationTypesList != null) {
            IViewBinder<PublicationType> binder = new PublicationTypeSpinnerBinder();
            GenericAdapter<PublicationType> spinAdapter = new GenericAdapter<PublicationType>(this, binder,
                    publicationTypesList);
            mTypeFilter.setAdapter(spinAdapter);
            if (currentPosition == null) {
                currentPosition = 0;
            }
            mCurrentTypeFilter = ((PublicationType) mTypeFilter.getItemAtPosition(currentPosition)).getId();
            mTypeFilter.setSelection(currentPosition);
            mTypeFilter.setOnItemSelectedListener(new OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> adapter, View v, int pos, long id) {
                    PublicationType type = publicationTypesList.get(pos);
                    if (mCurrentTypeFilter != type.getId()) {
                        mCurrentTypeFilter = type.getId();
                        doAsync();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> arg0) {
                }
            });

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    /**{@inheritDoc}*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_profile:
            startActivity(ProfileActivity.class);
            break;
        case R.id.menu_pub_all:
            doAsync();
            mTypeFilter.setVisibility(View.VISIBLE);
            break;
        case R.id.menu_pub_my:
            doAsync("OWN");
            mTypeFilter.setVisibility(View.INVISIBLE);
            break;
        case R.id.menu_money:
            new GetPage().execute(mServ.getRatingPageId());
            // Toast.makeText(this, "Money", Toast.LENGTH_LONG).show();
            break;

        case R.id.menu_logout:
            AppConfig.getInstance().setAuthorizationToken(null);
            startActivity(LoginActivity.class);
            finish();
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**{@inheritDoc}*/
    @Override
    protected void handleClick(View v) {
        switch (v.getId()) {
        case R.id.li_publication_readmore:
            Object obj = v.getTag();
            if (obj != null) {
                openPublicationActivity((Integer) obj);
            }
            break;

        case R.id.publication_create:
            Intent i = new Intent(this, PublicationCreateActivity.class);
            startActivityForResult(i, RC_PULICATION_CREATE);
            break;

        case R.id.publication_ib_settings:
            openOptionsMenu();
            break;

        }
        super.handleClick(v);
    }

    /**{@inheritDoc}*/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (RC_PULICATION_CREATE == requestCode && RESULT_OK == resultCode) {
            doAsync();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**{@inheritDoc}*/
    @Override
    public List<Publication> doInBackgroundThread(String... params) throws ApplicationException {
        String typeFilter = null;
        if (mCurrentTypeFilter != null && mCurrentTypeFilter != AppConfig.DEFAULT_SPINNER_NULL_ID) {
            typeFilter = mCurrentTypeFilter.toString();
        }
        return mServ.getPublicationsByFilter(typeFilter);
    }

    /**{@inheritDoc}*/
    @Override
    public void onTaskComplete(List<Publication> result) {
        if (result != null && result.size() > 0) {
            mPublicationList = result;
            mAdapter = new GenericAdapter<Publication>(this, new PublicationBinder(), mPublicationList);
            mListView.setAdapter(mAdapter);
        } else {
            // TODO: Needs to be clarified what to show here
            Toast.makeText(this, "No results for this type of publication", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * @param groupPosition
     */
    private void openPublicationActivity(int pos) {
        Intent i = new Intent(this, PublicationViewActivity.class);
        if (pos >= 0) {
            Publication pub = mPublicationList.get(pos - 1);
            Long publicationId = pub.getId();

            i.putExtra(EXTRA_PUBLICATION_ID, publicationId);
            if (AppConfig.isDevMode()) {
                // i.putExtra(EXTRA_PUBLICATION_ID, 54113L);
            }

        }
        startActivityForResult(i, RC_PULICATION_VIEW);
    }

    /**{@inheritDoc}*/
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        openPublicationActivity(position);
    }

    private Publication getLastestPuliblication() {
        Publication publication = null;
        publication = mPublicationList.get(mPublicationList.size() - 1);
        return publication;
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        new PaginatorLoadNewerData().execute();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        new PaginatorLoadOlderData().execute();
    }

    class PaginatorLoadOlderData extends android.os.AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {

            Publication publication = getLastestPuliblication();
            try {
                mPublicationListOlderItems = mServ.getPublicationsByFilter(mCurrentTypeFilter.toString(),
                        publication.getCreatedAt());
            } catch (ApplicationException e) {
                Logger.e(getClass(), e);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            for (Publication pub : mPublicationListOlderItems) {
                mPublicationList.add(pub);
            }
            mAdapter.notifyDataSetChanged();
            mListView.onRefreshComplete();
        }
    }

    class PaginatorLoadNewerData extends android.os.AsyncTask<Void, Void, List<Publication>> {

        @Override
        protected List<Publication> doInBackground(Void... voids) {
            List<Publication> result = null;
            try {
                String typeFilter = null;
                if (mCurrentTypeFilter != null && mCurrentTypeFilter != AppConfig.DEFAULT_SPINNER_NULL_ID) {
                    typeFilter = mCurrentTypeFilter.toString();
                }
                result = mServ.getPublicationsByFilter(typeFilter);
            } catch (ApplicationException e) {
                Logger.e(getClass(), e);
                handleErrorInUIThread(e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(List<Publication> result) {
            super.onPostExecute(result);
            if (result != null) {
                mPublicationList = result;
                mAdapter = new GenericAdapter<Publication>(AppConfig.getAppContext(), new PublicationBinder(),
                        mPublicationList);
                mListView.setAdapter(mAdapter);
                mListView.onRefreshComplete();
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_publications_isempty, Toast.LENGTH_LONG).show();
            }
        }
    }

    class GetPage extends android.os.AsyncTask<Long, Void, String> {

        @Override
        protected String doInBackground(Long... param) {
            String content = null;
            try {
                content = mServ.getContentPage(param[0]);
            } catch (ApplicationException e) {
                Logger.e(getClass(), e);
            }
            return content;
        }

        @Override
        protected void onPostExecute(String content) {
            super.onPostExecute(content);
            if (content != null) {
                Intent i = new Intent(PublicationListActivity.this, WebViewActivity.class);
                i.putExtra(WebViewActivity.EXTRA_TITLE, getString(R.string.money));
                i.putExtra(WebViewActivity.EXTRA_DATA, content);
                startActivity(i);
            } else {
                Toast.makeText(getApplicationContext(), R.string.error_page_not_found, Toast.LENGTH_LONG).show();
            }
        }
    }

    /**{@inheritDoc}*/
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(EXTRA_PUBLICATIONS, (ArrayList<Publication>) mPublicationList);
        outState.putInt(EXTRA_TYPE_POSITION, mTypeFilter.getSelectedItemPosition());
    }

}
