package com.seirion.videoapp;

import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.seirion.videoapp.base.ActivityLifecycle;
import com.seirion.videoapp.databinding.ActivityLoginBinding;

import com.seirion.videoapp.base.BaseAppCompatActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseAppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDataBinding();
        tryLogin();
    }

    private void setDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setTest(true);
    }

    @SuppressLint("CheckResult")
    private void tryLogin() {
        binding.progress.setVisibility(View.VISIBLE);
        binding.text.setText("begin");
        login().takeUntil(getLifecycleSignal(ActivityLifecycle.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .doAfterTerminate(() -> binding.progress.setVisibility(View.GONE))
                .subscribe(this::handleLoginResult, e -> {}, () -> {});
    }

    // TODO:
    private Observable<Boolean> login() {
        return Observable.<Boolean>create(
                emitter -> {
                    emitter.onNext(Boolean.FALSE);
                    emitter.onComplete();
                })
                .subscribeOn(Schedulers.io())
                .delay(3, TimeUnit.SECONDS);
    }

    private void handleLoginResult(boolean success) {
        if (success) {
            //startMainActivity();
            finish();
        } else {
            Log.e("progress", "hide progress ");
            binding.progress.setVisibility(View.GONE);
            binding.text.setText("finished");
            // stay login activity
        }
    }
}
