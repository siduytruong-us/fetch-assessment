package com.duyts.android.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.duyts.android.database.entity.HiringItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HiringDao {
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	suspend fun insertOrIgnoreHiringItems(items: List<HiringItemEntity>)

	@Query("SELECT * From hiring_item")
	fun observeHiringItems(): Flow<List<HiringItemEntity>>
}