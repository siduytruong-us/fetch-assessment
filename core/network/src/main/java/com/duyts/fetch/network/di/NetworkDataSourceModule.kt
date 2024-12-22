package com.duyts.fetch.network.di

import com.duyts.fetch.network.AppNetworkDataSource
import com.duyts.fetch.network.datasource.AppNetworkDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class NetworkDataSourceModule {
	@Binds
	abstract fun binds(impl: AppNetworkDataSourceImpl): AppNetworkDataSource
}