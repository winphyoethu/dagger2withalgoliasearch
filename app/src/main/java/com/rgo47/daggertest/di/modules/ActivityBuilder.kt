package com.rgo47.daggertest.di.modules

import com.rgo47.daggertest.MainActivityModule
import com.rgo47.daggertest.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun bindMainActivity(): MainActivity

}