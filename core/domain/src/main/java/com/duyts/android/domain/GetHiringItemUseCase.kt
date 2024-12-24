package com.duyts.android.domain

import androidx.compose.runtime.Immutable
import com.duyts.fetch.common.Resource.Resource
import com.duyts.fetch.common.Resource.asResource
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetHiringItemUseCase @Inject constructor(
	private val appRepository: AppRepository,
) {
	operator fun invoke(): Flow<Resource<List<DisplayHiringItem>>> = appRepository
		.observeHiringItems()
		.map { hiringItems ->
			hiringItems.asSequence()
				.filter { it.name?.isNotBlank() == true }
				.sortedBy { it.name }
				.groupBy { it.listID }
				.flatMap { (listId, uiHiringItem) ->
					val headers = listOf(DisplayHiringItem.Header(listId))
					val items = uiHiringItem.asSequence()
						.sortedBy { it.name }
						.map { DisplayHiringItem.Item(it) }
					headers + items
				}
		}.asResource()
}

@Immutable
sealed interface DisplayHiringItem {
	data class Header(val listID: Int) : DisplayHiringItem
	data class Item(val item: HiringItem) : DisplayHiringItem
}