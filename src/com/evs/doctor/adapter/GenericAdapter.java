package com.evs.doctor.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class GenericAdapter<T> extends BaseAdapter {

    protected LayoutInflater inflater;
    protected IViewBinder<T> viewBinder;
    protected List<? extends T> items;

    public GenericAdapter(Context context, IViewBinder<T> viewBinder, List<? extends T> items) {
        inflater = LayoutInflater.from(context);
        this.viewBinder = viewBinder;
        this.items = items;
    }

    /**
     * @return the items
     */
    public final List<? extends T> getItems() {
        return items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public T getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = convertView;
        if (itemView == null) {
            itemView = inflater.inflate(viewBinder.getViewLayoutId(), null);
//            itemView = inflater.inflate(resource, null);
        }
        viewBinder.bindView(itemView, getItem(position));
        return itemView;
    }
}
