package com.seirion.videoapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jakewharton.rxbinding3.view.RxView;
import com.seirion.videoapp.base.ActivityLifecycle;
import com.seirion.videoapp.base.BaseAppCompatActivity;
import com.seirion.videoapp.databinding.ActivityMainBinding;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class MainActivity extends BaseAppCompatActivity {

    private static String TAG = MainActivity.class.getSimpleName();
    private ActivityMainBinding binding;

    private Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDataBinding();
        initUi();
        initAdapter();
    }

    private void setDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
    }

    @SuppressLint("CheckResult")
    private void initUi() {
        RxView.clicks(binding.recordButton)
                .takeUntil(getLifecycleSignal(ActivityLifecycle.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(v -> RecordActivity.start(this), err -> {}, () -> {});
    }

    private void initAdapter() {
        adapter = new Adapter(this);
        binding.viewPager.setAdapter(adapter);
    }

    private static class Adapter extends PagerAdapter {

        @NonNull
        private LayoutInflater inflater;

        Adapter(Context context) {
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View v = inflater.inflate(R.layout.layout_video_page, container, false);
            ((TextView) v.findViewById(R.id.test)).setText("" + position);
            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView((View) object);
        }
    }

    public static void start(Activity activity) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        activity.startActivity(intent);
    }
}
