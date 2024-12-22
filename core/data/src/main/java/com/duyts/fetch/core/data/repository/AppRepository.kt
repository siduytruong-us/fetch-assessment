package com.duyts.fetch.core.data.repository

import com.duyts.fetch.common.Resource.Resource
import com.duyts.fetch.core.data.model.HiringItem
import kotlinx.coroutines.flow.Flow

interface AppRepository {
	suspend fun fetchHiringItems(): Resource<Unit>
	fun observeHiringItems(): Flow<Resource<Map<Int, List<HiringItem>>>>
}