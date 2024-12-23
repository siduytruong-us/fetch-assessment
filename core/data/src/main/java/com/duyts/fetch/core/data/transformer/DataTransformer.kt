package com.duyts.fetch.core.data.transformer

import com.duyts.android.database.entity.HiringItemEntity
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.network.model.HiringItemsResponseItem

interface DataTransformer {
	fun transform(entity: HiringItemsResponseItem): HiringItemEntity
	fun transform(entities: List<HiringItemEntity>): Map<Int, List<HiringItem>>
}