package com.rgo47.daggertest

import android.app.Activity
import android.app.Application
import com.facebook.drawee.backends.pipeline.Fresco
import com.rgo47.daggertest.di.AppComponent
import com.rgo47.daggertest.di.DaggerAppComponent
import com.rgo47.daggertest.di.modules.AppModule
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

class App : Application(), HasAndroidInjector {

//    @Inject
//    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var activityDispatchingAndroidInjector: DispatchingAndroidInjector<Any>

    //    override fun androidInjector(): AndroidInjector<Any> {
//        return activityDispatchingAndroidInjector
//    }
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
            .application(this)
            .applicationModule(AppModule(this))
            .build()
            .inject(this)

        Fresco.initialize(this)
    }

//    override fun activityInjector(): AndroidInjector<Activity> {
//        return activityDispatchingAndroidInjector
//    }

    override fun androidInjector(): AndroidInjector<Any> {
        return activityDispatchingAndroidInjector
    }
}