package com.evs.doctor.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.evs.doctor.R;
import com.evs.doctor.model.Profile;
import com.evs.doctor.service.ApiService;
import com.evs.doctor.view.CachedImageView;
import com.evs.doctor.view.NavigationBarView;

public class ProfileActivity extends GenericActivity {

    private TextView mLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Profile profile = ApiService.getInstance().getProfile();

        ((NavigationBarView) findViewById(R.id.view_navbar)).updateBar(getString(R.string.profile), null, this, this);
        ((CachedImageView) findViewById(R.id.profile_ava)).loadImage(profile.getNormalAvatarLink());

        findTextViewById(R.id.profile_name).setText(profile.getFullName());

        String rating = getString(R.string.profile_raiting, profile.getRating());
        findTextViewById(R.id.profile_rating).setText(rating);

        String money = getString(R.string.profile_wallet, profile.getMoney());
        findTextViewById(R.id.profile_money).setText(money);

        String link = getString(R.string.profile_link, "\n" + profile.getProfileLink());
        mLink = findTextViewById(R.id.profile_link);
        mLink.setText(link);
        mLink.setTag(profile.getProfileLink());

        findViewById(R.id.profile_link).setOnClickListener(this);
    }

    /**{@inheritDoc}*/
    @Override
    protected void handleClick(View v) {
        super.handleClick(v);
        switch (v.getId()) {
        case R.id.profile_link:
            Intent i = new Intent(this, WebViewActivity.class);
            i.putExtra(WebViewActivity.EXTRA_URL, (String) v.getTag());
            startActivity(i);
            break;
        }
    }
}
