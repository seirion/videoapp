package com.seirion.videoapp.base;

import android.app.Activity;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class LifecycleHelper {
    private Activity activity;
    private ActivityLifecycle currentLifecycle;
    private BehaviorSubject<ActivityLifecycle> lifecycleSubject;

    public LifecycleHelper(Activity activity) {
        this.activity = activity;
    }

    public void onCreate() {
        setCurrentLifecycle(ActivityLifecycle.CREATE);
    }

    public void onStart() {
        setCurrentLifecycle(ActivityLifecycle.START);
    }

    public void onResume() {
        setCurrentLifecycle(ActivityLifecycle.RESUME);
    }

    public void onPause() {
        setCurrentLifecycle(ActivityLifecycle.PAUSE);
    }

    public void onStop() {
        setCurrentLifecycle(ActivityLifecycle.STOP);
    }

    public void onDestroy() {
        setCurrentLifecycle(ActivityLifecycle.DESTROY);
    }

    public void setCurrentLifecycle(ActivityLifecycle lifecycle) {
        currentLifecycle = lifecycle;
        if (lifecycleSubject != null) {
            lifecycleSubject.onNext(lifecycle);
            if (lifecycle == ActivityLifecycle.DESTROY) {
                lifecycleSubject.onComplete();
                lifecycleSubject = null;
            }
        }
    }

    private Observable<ActivityLifecycle> getObservable() {
        // Lazy instantiation
        if (lifecycleSubject == null) {
            if (currentLifecycle == null) {
                lifecycleSubject = BehaviorSubject.create();
            } else if (currentLifecycle == ActivityLifecycle.DESTROY) {
                return Observable.just(ActivityLifecycle.DESTROY);
            } else {
                lifecycleSubject = BehaviorSubject.createDefault(currentLifecycle);
            }
        }
        return lifecycleSubject;
    }

    public Observable<ActivityLifecycle> getLifecycleSignal(ActivityLifecycle lifecycle) {
        return getObservable()
                .filter(l -> l.getIndex() >= lifecycle.getIndex())
                .take(1);
    }
}
