package com.thisisnotyours.registertaxikotlin.module

import com.thisisnotyours.registertaxikotlin.BuildConfig
import com.thisisnotyours.registertaxikotlin.Repository.CarInfoRepository
import com.thisisnotyours.registertaxikotlin.data.CarInfoApiService
import com.thisisnotyours.registertaxikotlin.module.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


// Module 만들어주기
// 여기서 부터 정신을 바짝 차리고 설명을 봐야 한다. 다들 알다시피 Hilt 에는 Module 어노테이션(@Module)이 존재한다. 이 모듈 어노테이션이 존재하는 이유는 다음과 같다.
//
// 우리가 의존성을 주입할 때 외부 라이브러리는 Hilt 가 인스턴스를 생성하지 못하는 경우가 있다.
// 해당 인스턴스는 어떻게 생성해야 하는지 개발자가 알려줘야 한다.
//
// 위와 같은 이유 때문에 우리는 @Module 을 사용해서 인스턴스를 만들고 Hilt 가 의존성을 주입할 때 생성자를 생성하는 방법을 모를때 어떻게 생성하는지 가이드라인을 제공하는 것이다.
//
// 이렇게 가이드 라인을 생성하고 나면 우리는 이 모듈을 어떤 컴포넌트에 제공할 것인지 정해야 한다. 이번에는 Retrofit 에 관한 모듈이니까 SingletonComponent 를 사용하겠다.

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL
    //또는
//    fun provideBaseUrl() = Constants.BASE_URL


    @Provides
    @Singleton
    fun provideOKHttpClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        OkHttpClient.Builder()
            .connectTimeout(120, TimeUnit.SECONDS)
            .readTimeout(120, TimeUnit.SECONDS)
            .writeTimeout(120, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    } else OkHttpClient.Builder()
        .connectTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()
///
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, BASE_URL: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): CarInfoApiService =
        retrofit.create(CarInfoApiService::class.java)


}