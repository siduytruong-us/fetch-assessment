package com.duyts.fetch.core.data.transformer

import com.duyts.android.database.entity.HiringItemEntity
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.model.toEntity
import com.duyts.fetch.core.data.model.toModel
import com.duyts.fetch.network.model.HiringItemsResponseItem
import javax.inject.Inject

class DataTransformerImpl @Inject constructor() : DataTransformer {
	override fun transform(responseItem: HiringItemsResponseItem) = responseItem.toEntity()

	override fun transform(entity: HiringItemEntity): HiringItem = entity.toModel()

}