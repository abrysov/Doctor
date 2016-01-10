package com.evs.doctor.adapter;

import android.view.View;

public interface ILayoutBinder<T> extends IViewBinder<T> {

    void buildModel(T item, View view);

}