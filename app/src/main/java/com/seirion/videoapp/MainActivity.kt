package com.seirion.videoapp

import android.os.Bundle
import android.util.Log
import com.seirion.videoapp.base.ActivityLifecycle
import com.seirion.videoapp.base.BaseAppCompatActivity
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

class MainActivity : BaseAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    // 테스트 코드임 (삭제 예정)
    fun test() {
        Observable.interval(1, TimeUnit.SECONDS)
                .takeUntil(getLifecycleSignal(ActivityLifecycle.DESTROY))
                .subscribe { Log.e("", it.toString()) }
    }
}
