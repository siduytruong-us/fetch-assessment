package com.duyts.fetch.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duyts.android.domain.FetchHiringItemUseCase
import com.duyts.android.domain.ObserveHiringItemsUseCase
import com.duyts.fetch.common.result.Resource
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.home.model.DisplayHiringItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
	private val observeHiringItemsUseCase: ObserveHiringItemsUseCase,
	private val fetchHiringItemUseCase: FetchHiringItemUseCase,
) : ViewModel() {
	private val _state: StateFlow<HomeScreenState> = observeHiringItems().stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5_000),
		initialValue = HomeScreenState.Loading
	)

	val state: StateFlow<HomeScreenState> = _state

	fun fetchNewHiringItems() = viewModelScope.launch {
		fetchHiringItemUseCase()
	}

	private fun observeHiringItems(): Flow<HomeScreenState> =
		observeHiringItemsUseCase().map { resource ->
				when (resource) {
					is Resource.Success -> {
						HomeScreenState.Success(hiringItems = resource.data.toDisplayHiringItems())
					}
					is Resource.Loading -> HomeScreenState.Loading
					is Resource.Error -> HomeScreenState.Error(error = resource.message)
				}
			}
}

internal fun List<HiringItem>.toDisplayHiringItems() =
	this.groupBy { it.listID }.flatMap { (listId, uiHiringItem) ->
			val headers = listOf(DisplayHiringItem.Header(listId))
			val items =
				uiHiringItem.asSequence().sortedBy { it.name }.map { DisplayHiringItem.Item(it) }
			headers + items
		}

sealed class HomeScreenState {
	data class Success(val hiringItems: List<DisplayHiringItem>) : HomeScreenState()
	data object Loading : HomeScreenState()
	data class Error(val error: String) : HomeScreenState()
}
