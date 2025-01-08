package com.duyts.fetch.core.data.repository

import com.duyts.fetch.common.result.Resource
import com.duyts.fetch.core.data.model.HiringItem
import kotlinx.coroutines.flow.Flow

interface AppRepository {
	suspend fun fetchHiringItems(): Resource<Boolean>
	fun observeHiringItems(): Flow<List<HiringItem>>
}