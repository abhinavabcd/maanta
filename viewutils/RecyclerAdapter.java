package com.appsandlabs.router.viewutils;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by abhinav on 28/2/17.
 */

public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<RecyclableViewHolderWrapper>{

    public List<T> data = new ArrayList<>();
    private final SparseArray<Class<? extends ViewHolder>> viewTypesMap = new SparseArray<>();
    private HashMap<String, Object> attrs = null;
    public View emptyView = null;

    public RecyclerAdapter(){
        SupportedViewHolders viewHolders = this.getClass().getAnnotation(SupportedViewHolders.class);
        for(Class<? extends ViewHolder> viewHolderClazz : viewHolders.viewHolders()){
            ViewTypeId viewType = viewHolderClazz.getAnnotation(ViewTypeId.class);
            viewTypesMap.put(viewType.id(), viewHolderClazz);
        }
    }

    @Override
    public RecyclableViewHolderWrapper onCreateViewHolder(ViewGroup parent, int viewType) {
        Class<? extends ViewHolder> clazz = viewTypesMap.get(viewType);
        try {
            Constructor<? extends ViewHolder> constructor = clazz.getConstructor(Context.class, ViewGroup.class);
            ViewHolder viewHolderInstance = constructor.newInstance(parent.getContext(), parent);
            viewHolderInstance.setAdapter(this);
            return new RecyclableViewHolderWrapper(viewHolderInstance);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclableViewHolderWrapper holder, int position) {
        try {
            holder.wrappedView.getView().setVisibility(View.VISIBLE);
            holder.wrappedView.render(data.get(position));
        }
        catch (Exception ex){
            holder.wrappedView.getView().setVisibility(View.GONE);
        }
    }


    @Override
    public abstract int getItemViewType(int position);
    @Override
    public int getItemCount() {
        return data.size();
    }

    public Object getAttr(String attr) {
        if(attrs==null){
            return null;
        }
        return attrs.get(attr);
    }

    public void setAttr(String attr, Object value){
       if(attrs==null){
           attrs = new HashMap<String, Object>();
       }
       attrs.put(attr, value);

    }
    /*empty view*/
    public RecyclerAdapter setEmptyView(View emptyView) {
        return setEmptyView(emptyView, false);
    }
    public RecyclerAdapter setEmptyView(View emptyView, boolean hidden) {
        this.emptyView = emptyView;
        if(!hidden) {
            ifEmptyShowView();
        }
        else if(emptyView!=null){
            emptyView.setVisibility(View.GONE);
        }
        return this;
    }
    public void ifEmptyShowView() {
        if(data.size()==0 && emptyView!=null){
            emptyView.setVisibility(View.VISIBLE);
        }
        else if(emptyView!=null){
            emptyView.setVisibility(View.GONE);
        }
    }
    /*will crash if view is not textview*/
    public void ifEmptyShowView(String text) {
        if(data.size()==0 && emptyView!=null && emptyView instanceof TextView){
            emptyView.setVisibility(View.VISIBLE);
            ((TextView)emptyView).setText(text);
        }
        else if(emptyView!=null){
            if(emptyView instanceof ViewGroup){
                ViewGroup emptyContainer = ((ViewGroup) emptyView);
                for (int i=0;i<emptyContainer.getChildCount();i++){
                    View temp = emptyContainer.getChildAt(i);
                    if(temp instanceof TextView){
                        ((TextView) temp).setText(text);
                        break;
                    }
                }
            }
            else {
                emptyView.setVisibility(View.GONE);
            }
        }
    }
}
