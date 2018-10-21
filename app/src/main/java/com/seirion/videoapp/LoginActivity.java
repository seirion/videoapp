package com.seirion.videoapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.seirion.videoapp.base.ActivityLifecycle;
import com.seirion.videoapp.databinding.ActivityLoginBinding;

import com.seirion.videoapp.base.BaseAppCompatActivity;

import java.util.Optional;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends BaseAppCompatActivity {

    private static String TAG = LoginActivity.class.getSimpleName();
    private ActivityLoginBinding binding;
    private CallbackManager callbackManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDataBinding();
        //FacebookSdk.sdkInitialize(this.getApplicationContext());
        initFacebookLogin();
        loginWithoutUi();
    }

    private void initFacebookLogin() {
        callbackManager = CallbackManager.Factory.create();
        com.facebook.login.LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "token: " + loginResult.getAccessToken());
                String userId = loginResult.getAccessToken().getUserId();
                String token = loginResult.getAccessToken().getToken();
                handleLoginResult(true);
                //loginToServer(userId, token);
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "cancel");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d(TAG, "error");
            }
        });
    }

    private void setDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setTest(true);
    }

    @SuppressLint("CheckResult")
    private void loginWithoutUi() {
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
                    boolean loggedIn = Optional.ofNullable(AccessToken.getCurrentAccessToken())
                            .map(accessToken -> !accessToken.isExpired())
                            .orElse(false);
                    Log.d(TAG, "loginWithoutUi: " + loggedIn);
                    emitter.onNext(loggedIn);
                    emitter.onComplete();
                })
                .subscribeOn(Schedulers.io());
    }

    private void handleLoginResult(boolean success) {
        if (success) {
            MainActivity.start(this);
            finish();
        } else {
            Log.e("progress", "hide progress ");
            binding.progress.setVisibility(View.GONE);
            binding.text.setText("finished");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
