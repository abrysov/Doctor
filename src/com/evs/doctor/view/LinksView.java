package com.evs.doctor.view;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout.LayoutParams;

import com.evs.doctor.R;
import com.evs.doctor.activity.WebViewActivity;
import com.evs.doctor.model.Link;
import com.evs.doctor.model.PDFLink;
import com.evs.doctor.model.VideoLink;

public class LinksView {

    protected static final String GOOGLE_PDF_VIEW = "https://docs.google.com/gview?embedded=true&url=";
    private ViewGroup mView;
    private OnClickListener mClickListener = new OnClickListener() {

        @Override
        public void onClick(View v) {

            Link link = (Link) v.getTag();
            String url = link.getUrl();
            if (link instanceof PDFLink) {
                url = GOOGLE_PDF_VIEW + Uri.encode(link.getUrl());
            }
            if (link instanceof VideoLink) {
                url = link.getUrl();
            }
            Intent i = new Intent(mView.getContext(), WebViewActivity.class);
            i.putExtra(WebViewActivity.EXTRA_URL, url);
            mView.getContext().startActivity(i);
        }
    };

    public LinksView(ViewGroup viewGroup) {
        mView = viewGroup;
    }

    /**
     * 
     */
    private void addLink(Link link) {
        Button linkView = new Button(mView.getContext());
        linkView.setText(link.getTitle());
        linkView.setTag(link);
        linkView.setTextColor(mView.getResources().getColor(R.color.BLUE));
        linkView.setOnClickListener(mClickListener);
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 2, 0, 0);
        mView.addView(linkView, params);
        // mView.addView(linkView, LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
    }

    /**
     * @param images
     */
    public void addLinks(List<? extends Link> links, boolean isCleanRequred) {
        if (isCleanRequred) {
            mView.removeAllViews();
        }
        for (Link link : links) {
            addLink(link);
        }
    }
}
