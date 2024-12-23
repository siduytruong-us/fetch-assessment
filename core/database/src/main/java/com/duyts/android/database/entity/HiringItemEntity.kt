package com.duyts.android.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("hiring_item")
data class HiringItemEntity(
	@PrimaryKey
	val id: Int,
	val name: String?,
	val listId: Int,
)