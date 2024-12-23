package com.duyts.fetch.core.data.transformer

import com.duyts.android.database.entity.HiringItemEntity
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.model.toEntity
import com.duyts.fetch.core.data.model.toModel
import com.duyts.fetch.network.model.HiringItemsResponseItem
import javax.inject.Inject

class DataTransformerImpl @Inject constructor() : DataTransformer {
	override fun transform(entity: HiringItemsResponseItem) = entity.toEntity()

	override fun transform(entities: List<HiringItemEntity>) =
		entities.filter { it.name?.isNotBlank() == true }
			.sortedWith(compareBy<HiringItemEntity> { it.listId }.thenBy { it.name })
			.groupBy { it.listId }
			.mapValues { entry ->
				entry.value.map(HiringItemEntity::toModel)
			}
}