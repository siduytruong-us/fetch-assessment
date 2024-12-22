package com.duyts.fetch.network.di

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.duyts.android.network.BuildConfig
import com.duyts.fetch.network.AppNetworkService

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


private const val APP_BASE_URL = BuildConfig.BACKEND_URL

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
	@Provides
	@Singleton
	@Named("AuthInterceptor")
	fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
		val request = chain.request().newBuilder()
			.build()
		chain.proceed(request)
	}

	@Provides
	@Singleton
	@Named("ConnectivityInterceptor")
	fun provideConnectivityInterceptor(@ApplicationContext context: Context): Interceptor =
		Interceptor { chain ->
			if (!isNetworkAvailable(context)) {
				throw NoConnectivityException("No internet connection")
			}
			chain.proceed(chain.request())
		}

	@Provides
	@Singleton
	fun provideOkHttpClient(
		@Named("AuthInterceptor") authInterceptor: Interceptor,
		@Named("ConnectivityInterceptor") connectivityInterceptor: Interceptor,
	): OkHttpClient {
		return OkHttpClient.Builder()
			.addInterceptor(authInterceptor)
			.addInterceptor(connectivityInterceptor)
			.addInterceptor(HttpLoggingInterceptor().apply {
				if (BuildConfig.DEBUG) {
					setLevel(HttpLoggingInterceptor.Level.BODY)
				}
			})
			.retryOnConnectionFailure(true) // Automatically retry on network failures
			.connectTimeout(30, TimeUnit.SECONDS) // Customize timeouts as needed
			.readTimeout(30, TimeUnit.SECONDS)
			.writeTimeout(30, TimeUnit.SECONDS)
			.build()
	}

	@Provides
	@Singleton
	fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
		return Retrofit.Builder()
			.baseUrl(APP_BASE_URL)
			.client(okHttpClient)
			.addConverterFactory(GsonConverterFactory.create())
			.build()
	}

	@Provides
	@Singleton
	fun provideApiService(retrofit: Retrofit): AppNetworkService {
		return retrofit.create(AppNetworkService::class.java)
	}
}

private fun isNetworkAvailable(context: Context): Boolean {
	val connectivityManager =
		context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
	val network = connectivityManager.activeNetwork ?: return false
	val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
	return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
}

class NoConnectivityException(message: String) : IOException(message)