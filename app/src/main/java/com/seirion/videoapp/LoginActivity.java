package com.seirion.videoapp;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import com.seirion.videoapp.databinding.ActivityLoginBinding;

import com.seirion.videoapp.base.BaseAppCompatActivity;

public class LoginActivity extends BaseAppCompatActivity {
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setDataBinding();
    }

    private void setDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setTest(true);
    }
}
