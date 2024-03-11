package team.godsaeng.cooktionary_android.di.network

import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import team.godsaeng.cooktionary_android.BuildConfig
import team.godsaeng.data.remote.CooktionaryApi
import team.godsaeng.domain.model.model.CTError
import team.godsaeng.domain.model.model.CTException
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val BASE_URL = "http://3.34.159.0/api/v1/"
    var accessToken = ""

    @Singleton
    @Provides
    fun provideHttpClient(
        @ErrorInterceptor errorInterceptor: Interceptor,
        @AccessTokenInterceptor accessTokenInterceptor: Interceptor
    ): OkHttpClient {
        val client = OkHttpClient
            .Builder()
            .addInterceptor(errorInterceptor)
            .addInterceptor(accessTokenInterceptor)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .connectTimeout(30, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) {
            client.addNetworkInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
        }

        return client.build()
    }

    @ErrorInterceptor
    @Singleton
    @Provides
    fun provideErrorInterceptor(
        gson: Gson
    ): Interceptor = Interceptor { chain ->
        try {
            val response = chain.proceed(chain.request())
            if (response.isSuccessful && response.body != null) {
                return@Interceptor response
            } else {
                val errorString = response.body?.string()
                val errorResponse = gson.fromJson(errorString, CTError::class.java)
                throw CTException(errorResponse)
            }
        } catch (exception: Exception) {
            throw CTException(CTError(-1, exception.message ?: "UNKNOWN_ERROR"))
        }
    }

    @AccessTokenInterceptor
    @Singleton
    @Provides
    fun provideAccessTokenInterceptor(): Interceptor = Interceptor { chain ->
        if (accessToken.isNotEmpty()) {
            val newRequest = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer $accessToken")
                .build()

            return@Interceptor chain.proceed(newRequest)
        }

        return@Interceptor chain.proceed(chain.request())
    }

    @Singleton
    @Provides
    fun provideGson(): Gson = Gson()

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideCooktionaryApi(retrofit: Retrofit): CooktionaryApi {
        return retrofit.create(CooktionaryApi::class.java)
    }
}