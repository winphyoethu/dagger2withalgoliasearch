package com.rgo47.daggertest

import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun providesMainViewModel(): MainViewModel = MainViewModel()

}