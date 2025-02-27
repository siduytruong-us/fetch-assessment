package com.duyts.fetch.core.data.di

import com.duyts.fetch.core.data.repository.AppRepository
import com.duyts.fetch.core.data.repository.AppRepositoryImpl
import com.duyts.fetch.core.data.transformer.DataTransformer
import com.duyts.fetch.core.data.transformer.DataTransformerImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {


	@Binds
	abstract fun provideRepository(
		appRepository: AppRepositoryImpl,
	): AppRepository
}