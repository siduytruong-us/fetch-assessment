package com.duyts.fetch.core.data.di

import com.duyts.fetch.core.data.transformer.DataTransformer
import com.duyts.fetch.core.data.transformer.DataTransformerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TransformerProvider {
	@Singleton
	@Provides
	fun providesDataTransformer(): DataTransformer = DataTransformerImpl()
}