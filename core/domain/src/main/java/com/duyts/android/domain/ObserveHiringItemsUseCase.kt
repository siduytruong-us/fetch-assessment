package com.duyts.android.domain

import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class ObserveHiringItemsUseCase @Inject constructor(
	private val appRepository: AppRepository,
) {
	operator fun invoke(): Flow<List<HiringItem>> = appRepository
		.observeHiringItems()
		.map { hiringItems ->
			hiringItems.filter { it.name?.isNotBlank() == true }
		}
}
