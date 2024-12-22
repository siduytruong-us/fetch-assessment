package com.duyts.android.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.duyts.android.database.dao.HiringDao
import com.duyts.android.database.entity.HiringItemEntity

@Database(entities = [HiringItemEntity::class], version = 1)
internal abstract class AppDatabase : RoomDatabase() {
	abstract fun hiringDao(): HiringDao
}