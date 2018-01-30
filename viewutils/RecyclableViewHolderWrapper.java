package com.appsandlabs.router.viewutils;

import android.support.v7.widget.RecyclerView;

// takes a general view holder and returns a recyclable version
public class RecyclableViewHolderWrapper extends RecyclerView.ViewHolder{

    public final ViewHolder wrappedView;

    public RecyclableViewHolderWrapper(ViewHolder viewHolder) {
            super(viewHolder.view);
            wrappedView = viewHolder;
        }
}