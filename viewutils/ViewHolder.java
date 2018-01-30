package com.appsandlabs.router.viewutils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.appsandlabs.router.R;

import butterknife.ButterKnife;

/**
 * Created by abhinav on 28/2/17.
 * A view holder takes an object and returns viewholder using the render object
 *
 * For every view write your viewholder, no findbyviews in any code ,we use butter knife to set the views
 *
 * 1. create the wrappedView for your view
 * 2. implement getOrCreateView , getViewType , then renderData , you can call render multiple times to display new object if you may like
 *
 * Render calls getOrCreateView (which calls getViewType and inflates appropriate view), set the view from butterknife and display data from object
 * add handlers etc.
 *
 */
public abstract class ViewHolder<V> {

    protected Context ctx = null;
    public View view = null;
    public RecyclerView.Adapter _adapter = null;

    public ViewHolder(Context ctx){
        this(ctx, null);
    }
    public ViewHolder(Context ctx, ViewGroup parent){
        this.ctx = ctx;
        view = createView(parent);
        view.setTag(this);
        ButterKnife.bind(this, view);//sets the fields in the view
        view.setTag(R.id.view_holder_tag , this);
    }

    public abstract ViewHolder<V> render(V obj); //into this viewholder

    //just inflate a view based on the obj type
    protected View createView(ViewGroup parent){
        LayoutFile layoutRes = getClass().getAnnotation(LayoutFile.class);
        return view = LayoutInflater.from(ctx).inflate(layoutRes.layoutId(), parent, false);
    }

    public View getView() {
        return view;
    }

    public void setAdapter(RecyclerAdapter adapter) {
        this._adapter = adapter;
    }
}
