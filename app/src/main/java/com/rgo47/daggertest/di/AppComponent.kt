package com.rgo47.daggertest.di

import android.app.Application
import com.rgo47.daggertest.App
import com.rgo47.daggertest.di.modules.ActivityBuilder
import com.rgo47.daggertest.di.modules.AppModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(modules = [AndroidInjectionModule::class, ActivityBuilder::class, AppModule::class])
interface AppComponent: AndroidInjector<App> {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: App): Builder

        fun applicationModule(app: AppModule): Builder

        fun build(): AppComponent
    }

    override fun inject(app: App)

}