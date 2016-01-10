package com.evs.doctor.adapter;

import android.view.View;

public interface IViewBinder<T> {
    int getViewLayoutId();

    void bindView(View view, T item);

}