package com.evs.doctor.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.evs.doctor.R;

/**
 * Navigation bar component. <code>
 * Ensure that navigation_bar.xml is present  into the folder layout.
 * Add follow text into your layout.xml file.
  	<code><.view.NavigationBarView
			android:id="@+id/navigation_bar"
			android:layout_height="wrap_content"
			android:layout_width="fill_parent" /></code>
 * 
 * 
 * use follow for update nav bar <code>((NavigationBarView) findViewById(R.id.navigation_bar)).update(R.string.profile,
 * R.string.menu, defaultFinishCallback, null, null);</code>
 * 
 * if you button will be show only if have valid label id and callback function.
 * 
 * */
public class NavigationBarView extends RelativeLayout {
    private View view;
    private TextView vTitle;
    private ImageView vLeftImgBtn;
    private ImageView vSettingsImgBtn;
    private FrameLayout vSettingsArea;

    public NavigationBarView(Context context) {
        super(context);
        init(context);
    }

    public NavigationBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NavigationBarView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.view_navbar, this);
        vLeftImgBtn = (ImageView) view.findViewById(R.id.nav_back_to_publication_list);
        vSettingsImgBtn = (ImageView) view.findViewById(R.id.nav_settings);
        vTitle = (TextView) view.findViewById(R.id.nav_title);
        vSettingsArea = (FrameLayout) findViewById(R.id.nav_fl_push_area);

    }

    public void updateBar(String title, Drawable leftImage, OnClickListener titleOnClickListener,
            OnClickListener settingsImgButtonOnClickListener) {
        vTitle.setText(title);
        vTitle.setOnClickListener(titleOnClickListener);

        if (leftImage != null) {
            vLeftImgBtn.setImageDrawable(leftImage);
        }
        if (settingsImgButtonOnClickListener != null) {
            vSettingsImgBtn.setOnClickListener(settingsImgButtonOnClickListener);
            vSettingsArea.setOnClickListener(settingsImgButtonOnClickListener);
        } else {
            vSettingsArea.setVisibility(INVISIBLE);
            vSettingsImgBtn.setVisibility(INVISIBLE);
        }
    }

    public void updateBar(String title, String rightButtonText, OnClickListener click) {
        updateBar(title, rightButtonText, click, null, null);
    }

    public void updateBar(String title, String rightButtonText, OnClickListener rightClick, String leftButtonText,
            OnClickListener leftClick) {
        vTitle.setText(title);

        vSettingsImgBtn.setVisibility(View.VISIBLE);
        // vSettingsImgBtn.setText(rightButtonText);
        vSettingsImgBtn.setOnClickListener(rightClick);

        vLeftImgBtn.setVisibility(View.VISIBLE);
        // vLeftImgBtn.setText(rightButtonText);
        vLeftImgBtn.setOnClickListener(leftClick);
        vSettingsArea.setOnClickListener(leftClick);
    }

}
