package com.evs.doctor.adapter;

import android.text.Html;
import android.view.View;

import com.evs.doctor.R;
import com.evs.doctor.model.Publication;
import com.evs.doctor.model.PublicationType;
import com.evs.doctor.service.ApiService;
import com.evs.doctor.util.AndroidUtil;
import com.evs.doctor.util.DateParser;
import com.evs.doctor.view.CachedImageView;

public class PublicationBinder implements IViewBinder<Publication> {

    private ApiService mServ = ApiService.getInstance();

    /**{@inheritDoc}*/
    @Override
    public int getViewLayoutId() {
        return R.layout.list_item_publication;
    }

    /**{@inheritDoc}*/
    @Override
    public void bindView(View view, Publication item) {
        // specialty
        AndroidUtil.setText(view, R.id.li_publication_specialty, item.getAuthorSpecialty());
        // name
        AndroidUtil.setText(view, R.id.li_publication_name, item.getAuthorFullName());
        // the question (title)
        AndroidUtil.setText(view, R.id.li_publication_theme, item.getTitle());
        // set time
        AndroidUtil.setText(view, R.id.li_publication_time, DateParser.parseDate(item.getCreatedAt()));

        PublicationType type = mServ.getPublicationTypeById(item.getType());
        if (type != null) {
            AndroidUtil.setText(view, R.id.li_publication_part, type.getTitle());
        }

        AndroidUtil.setText(view, R.id.li_publication_tv_clouds_count, Integer.toString(item.getCommentsCount()));
        // String img = "http://www.doktornarabote.ru/image/avatar/081218014224044115163187007117051061233001142159";
        String img = item.getAuthorAvatarUrl();
        ((CachedImageView) view.findViewById(R.id.li_publication_iv_ava)).loadImage(img);

        AndroidUtil.setText(view, R.id.li_publication_annotation, Html.fromHtml(item.getAnnotation()));
    }

}
