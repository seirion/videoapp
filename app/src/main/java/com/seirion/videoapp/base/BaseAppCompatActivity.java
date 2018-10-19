package com.seirion.videoapp.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import io.reactivex.Observable;

public class BaseAppCompatActivity extends AppCompatActivity {
    private final LifecycleHelper activityHelper = new LifecycleHelper(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHelper.onCreate();
    }

    @Override
    protected void onStart() {
        super.onStart();
        activityHelper.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityHelper.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        activityHelper.onStop();
    }

    @Override
    protected void onDestroy() {
        activityHelper.onDestroy();
        super.onDestroy();
    }

    public Observable<ActivityLifecycle> getLifecycleSignal(ActivityLifecycle lifecycle) {
        return activityHelper.getLifecycleSignal(lifecycle);
    }
}
