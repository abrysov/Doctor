package com.evs.doctor.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.evs.doctor.R;
import com.evs.doctor.util.AppConfig;

public class GenericActivity extends Activity implements OnClickListener {
    public static final String EXTRA_PUBLICATION = "EXTRA_PUBLICATION";
    public static final String EXTRA_PUBLICATION_ID = "EXTRA_PUBLICATION_ID";

    public static final int RC_PULICATION_VIEW = 100;

    protected boolean isUserLoggedIn() {
        return getConfig().getAuthorizationToken() != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_general, menu);
        return true;
    }

    /**{@inheritDoc}*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.menu_profile:
            startActivity(ProfileActivity.class);
            break;
        case R.id.menu_logout:
            getConfig().setAuthorizationToken(null);
            startActivity(LoginActivity.class);
            finish();
            break;

        default:
            break;
        }
        return super.onOptionsItemSelected(item);
    }

    /** {@inheritDoc} */
    @Override
    public void onClick(View v) {
        handleClick(v);
    }

    protected void handleClick(View v) {

        switch (v.getId()) {
        case R.id.nav_settings:
            openOptionsMenu();
            break;
        case R.id.nav_fl_push_area:
            openOptionsMenu();
            break;
        }
        //
    }

    /**
    * @return
    */
    protected AppConfig getConfig() {
        return AppConfig.getInstance();
    }

    public boolean hasInternet() {
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return conMgr.getActiveNetworkInfo() != null;
    }

    /**
     * @param class1
     */
    public void startActivity(Class<? extends Activity> clazz) {
        Intent i = new Intent(this, clazz);
        startActivity(i);
    }

    /**
     * @param createPublicationTitle
     * @return
     */
    protected TextView findTextViewById(int viewId) {
        return (TextView) findViewById(viewId);
    }

}
