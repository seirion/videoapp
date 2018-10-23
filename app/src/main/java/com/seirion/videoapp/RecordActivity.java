package com.seirion.videoapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.jakewharton.rxbinding3.view.RxView;
import com.seirion.videoapp.base.ActivityLifecycle;
import com.seirion.videoapp.base.BaseAppCompatActivity;
import com.seirion.videoapp.databinding.ActivityRecordBinding;
import com.tedpark.tedpermission.rx2.TedRx2Permission;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class RecordActivity extends BaseAppCompatActivity {

    private static final int REQUEST_VIDEO_CAPTURE = 1;
    private static String TAG = RecordActivity.class.getSimpleName();
    private ActivityRecordBinding binding;
    private boolean hasVideo = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDataBinding();
        requestPermission();
        initUi();
    }

    private void setDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_record);
    }

    @SuppressLint("CheckResult")
    private void requestPermission() {
        TedRx2Permission.with(this)
                .setRationaleTitle(R.string.rationale_title)
                .setRationaleMessage(R.string.rationale_message) // "we need permission for read contact and find your location"
                .setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA)
                .request()
                .subscribe(tedPermissionResult -> {
                    if (tedPermissionResult.isGranted()) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this,
                                "Permission Denied\n" + tedPermissionResult.getDeniedPermissions().toString(), Toast.LENGTH_SHORT)
                                .show();

                        binding.recordButton.setEnabled(false);
                        binding.uploadButton.setEnabled(false);
                    }
                }, throwable -> {
                    Log.e(TAG, "e: " +throwable);
                });
    }

    @SuppressLint("CheckResult")
    private void initUi() {
        //boolean hasCamera = hasSystemFeature(PackageManager.FEATURE_CAMERA);
        // if (!hasCamera) return;
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
        dispatchTakeVideoIntent();
    }

    private void upload() {

    }

    private void dispatchTakeVideoIntent() {
        Intent takeVideoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (takeVideoIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takeVideoIntent, REQUEST_VIDEO_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        Log.d(TAG, "requestCode: " + requestCode);
        Log.d(TAG, "resultCode: " + resultCode);
        if (requestCode == REQUEST_VIDEO_CAPTURE && resultCode == RESULT_OK) {
            Uri videoUri = intent.getData();
            Log.d(TAG, "video: " + videoUri.getPath());
            //mVideoView.setVideoURI(videoUri);
        }
    }

    public static void start(Activity activity) {
        Log.d(TAG, "start()");
        Intent intent = new Intent(activity, RecordActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }
}
