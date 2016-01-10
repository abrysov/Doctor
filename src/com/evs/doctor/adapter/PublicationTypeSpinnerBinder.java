package com.evs.doctor.adapter;

import android.view.View;

import com.evs.doctor.R;
import com.evs.doctor.model.PublicationType;
import com.evs.doctor.util.AndroidUtil;

public class PublicationTypeSpinnerBinder implements IViewBinder<PublicationType> {

    @Override
    public int getViewLayoutId() {
        return R.layout.spinner_item;
    }

    @Override
    public void bindView(View view, PublicationType item) {
        AndroidUtil.setText(view, R.id.item_name, item.getTitle());
    }
}
