package com.duyts.fetch.core.data.repository

import com.duyts.fetch.core.data.model.HiringItem
import kotlinx.coroutines.flow.Flow

interface AppRepository {
	fun fetchHiringItems(): Flow<Unit>
	fun observeHiringItems(): Flow<List<HiringItem>>
}