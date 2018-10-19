package com.seirion.videoapp

import android.os.Bundle
import com.seirion.videoapp.base.BaseAppCompatActivity

class MainActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
