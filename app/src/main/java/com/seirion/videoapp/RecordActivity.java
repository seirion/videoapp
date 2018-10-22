package com.seirion.videoapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;

import com.jakewharton.rxbinding3.view.RxView;
import com.seirion.videoapp.base.ActivityLifecycle;
import com.seirion.videoapp.base.BaseAppCompatActivity;
import com.seirion.videoapp.databinding.ActivityRecordBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class RecordActivity extends BaseAppCompatActivity {
    private static String TAG = RecordActivity.class.getSimpleName();
    private ActivityRecordBinding binding;
    private boolean recording = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDataBinding();
        initUi();
    }

    private void setDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_record);
    }

    @SuppressLint("CheckResult")
    private void initUi() {
        RxView.clicks(binding.recordButton)
                .takeUntil(getLifecycleSignal(ActivityLifecycle.DESTROY))
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> record(), err -> {});

        RxView.clicks(binding.uploadButton)
                .takeUntil(getLifecycleSignal(ActivityLifecycle.DESTROY))
                .throttleFirst(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> upload(), err -> {});
    }

    private void record() {

    }

    private void upload() {

    }

    public static void start(Activity activity) {
        Log.d(TAG, "start()");
        Intent intent = new Intent(activity, RecordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }
}
