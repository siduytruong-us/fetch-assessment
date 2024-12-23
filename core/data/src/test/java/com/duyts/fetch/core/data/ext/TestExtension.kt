package com.duyts.fetch.core.data.ext

import com.duyts.android.database.entity.HiringItemEntity
import com.duyts.fetch.core.data.model.HiringItem


internal fun List<HiringItemEntity>.testingObserveHiringItems() =
	filter { it.name?.isNotBlank() == true }
		.sortedWith(compareBy<HiringItemEntity> { it.listId }.thenBy { it.name })
		.groupBy { it.listId }
		.mapValues { entry ->
			entry.value.map {
				HiringItem(name = it.name, id = it.id)
			}
		}