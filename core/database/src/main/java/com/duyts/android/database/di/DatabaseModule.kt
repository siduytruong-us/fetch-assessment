package com.duyts.android.database.di

import android.content.Context
import androidx.room.Room
import com.duyts.android.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
	@Provides
	@Singleton
	fun providesNiaDatabase(
		@ApplicationContext context: Context,
	): AppDatabase = Room.databaseBuilder(
		context,
		AppDatabase::class.java,
		"nia-database",
	).build()
}
