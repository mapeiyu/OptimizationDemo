package com.mapeiyu.optimizationdemo.tools;

import android.content.AsyncTaskLoader;
import android.content.Context;

public abstract class AsyncDataLoader<T> extends AsyncTaskLoader<T> {

	
	private T mData;
	
	public AsyncDataLoader(Context context) {
		super(context);
	}
	
	@Override
    public void deliverResult(T data) {
        if (isReset()) {
            if (data != null) {
                onReleaseResources(data);
            }
        }
        
        T oldData = mData;
        mData = data;

        if (isStarted()) {
            super.deliverResult(data);
        }
        
        if (oldData != null && oldData != mData) {
            onReleaseResources(oldData);
        }
    }
	
    @Override
    protected void onStartLoading() {
        if (mData != null) {
            deliverResult(mData);
        }
        
        registerObserver();
        
        if (takeContentChanged() || mData == null || isConfigChanged()) {
            forceLoad();
        }
    }
    
    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
    
    @Override
    public void onCanceled(T data) {
        super.onCanceled(data);
        onReleaseResources(data);
    }
    
    @Override
    protected void onReset() {
        super.onReset();
        
        onStopLoading();

        if (mData != null) {
            onReleaseResources(mData);
            mData = null;
        }
        
        unregisterObserver();
    }
    
    protected void registerObserver() {
    }
    
    protected void unregisterObserver() {
    }
    
    protected boolean isConfigChanged() {
    	return false;
    }
    
    protected void onReleaseResources(T data) {
    }
}