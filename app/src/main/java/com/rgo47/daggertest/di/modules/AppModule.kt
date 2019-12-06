package com.rgo47.daggertest.di.modules

import android.app.Application
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.readystatesoftware.chuck.ChuckInterceptor
import com.rgo47.daggertest.App
import com.rgo47.daggertest.utils.Constant
import com.rgo47.daggertest.utils.PreferenceHelper
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule(val app: App) {
    @Singleton
    @Provides
    fun provideApplication(): Application {
        return app
    }

    @Singleton
    @Provides
    fun providePreferenceHelper(application: Application): PreferenceHelper {
        return PreferenceHelper(application)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        application: Application, pHelper: PreferenceHelper
    ): OkHttpClient {
        val cacheSize = (5 * 1024 * 1024).toLong()
        val myCache = Cache(application.cacheDir, cacheSize)

        val client = OkHttpClient.Builder()
        try {
            client
                .addInterceptor(ChuckInterceptor(application))
                .addInterceptor {
                    val request = it.request()
                    val newRequest: Request

                    newRequest = request.newBuilder()
//                        .addHeader("x-api-secret-key", BuildConfig.API_KEY)
                        .addHeader("x-device-id", pHelper.getString(Constant.DEVICE, ""))
//                        .addHeader("x-app-version", BuildConfig.VERSION_NAME)
                        .addHeader("x-language", pHelper.getString(Constant.LANGUAGE, ""))
                        .addHeader("x-api-token", pHelper.getString(Constant.API_TOKEN, ""))
                        .addHeader("x-user-id", pHelper.getString(Constant.USER, ""))
                        .addHeader("x-device_info", pHelper.getString(Constant.DEVICE_INFO, ""))
                        .method(request.method(), request.body())
                        .build()

                    it.proceed(newRequest)
                }
                .cache(myCache)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .proxy(Proxy.NO_PROXY)
        } catch (e: Exception) {

        }
        return client.build()
    }

    @Singleton
    @Provides
    fun provideMoshi(): Moshi {
        return Moshi
            .Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(moshi: Moshi, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .baseUrl("https://www.rgo47.com")
            .addConverterFactory(MoshiConverterFactory.create(moshi).asLenient())
            .addCallAdapterFactory(CoroutineCallAdapterFactory.invoke())
            .client(okHttpClient)
            .build()
    }

//    @Singleton
//    @Provides
//    fun provideApiService(retrofit: Retrofit): ApiService {
//        return retrofit.create(ApiService::class.java)
//    }

}