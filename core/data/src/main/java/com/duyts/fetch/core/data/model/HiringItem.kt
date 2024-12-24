package com.duyts.fetch.core.data.model

import com.duyts.android.database.entity.HiringItemEntity
import com.duyts.fetch.network.model.HiringItemsResponseItem

data class HiringItem(
	val name: String? = null,
	val id: Int,
	val listID: Int,
)

fun HiringItemEntity.toModel() = HiringItem(
	name = name,
	id = id,
	listID = listId
)


fun HiringItemsResponseItem.toEntity() = HiringItemEntity(
	name = name,
	id = id,
	listId = listId,
)