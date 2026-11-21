package com.example.kotlintutorial.di

import android.content.Context
import androidx.room.Room
import com.example.kotlintutorial.data.AppDatabase
import com.example.kotlintutorial.data.MemoDao
import com.example.kotlintutorial.data.QuoteApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import javax.inject.Singleton

/**
 * Hilt 의존성 정의 모듈.
 * 데이터베이스, DAO, Retrofit API를 앱 전체에서 하나씩만 만들어 공유한다.
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /** Room 데이터베이스를 앱 전체에서 하나만 만든다. */
    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "memo.db").build()

    /** 데이터베이스에서 메모 DAO를 꺼내 제공한다. */
    @Provides
    fun provideMemoDao(db: AppDatabase): MemoDao = db.memoDao()

    /**
     * 명언 API용 Retrofit 클라이언트를 만든다.
     * Kotlin 포인트: `Json { ... }`는 람다로 설정하는 빌더(DSL) 형태다.
     */
    @Provides
    @Singleton
    fun provideQuoteApi(): QuoteApi {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://dummyjson.com/")
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(QuoteApi::class.java)
    }
}
