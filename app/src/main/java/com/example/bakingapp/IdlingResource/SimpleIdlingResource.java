package com.example.bakingapp.IdlingResource;

import android.support.annotation.Nullable;
import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by Taha on 6/21/2018.
 */

public class SimpleIdlingResource implements IdlingResource {

    @Nullable
    private ResourceCallback mCallback;

    private AtomicBoolean mIsIdleNow = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return mIsIdleNow.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIdleState(boolean state) {
        mIsIdleNow.set(state);
        if (state && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }
}
