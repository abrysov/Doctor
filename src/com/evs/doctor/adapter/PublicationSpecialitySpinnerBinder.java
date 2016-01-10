package com.evs.doctor.adapter;

import android.view.View;

import com.evs.doctor.R;
import com.evs.doctor.model.Specialties;
import com.evs.doctor.util.AndroidUtil;

public class PublicationSpecialitySpinnerBinder implements IViewBinder<Specialties> {
    @Override
    public int getViewLayoutId() {
        return R.layout.spinner_item;
    }

    @Override
    public void bindView(View view, Specialties item) {
        AndroidUtil.setText(view, R.id.item_name, item.getName());
    }
}
