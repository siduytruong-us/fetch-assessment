package com.duyts.fetch.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duyts.android.domain.FetchHiringItemUseCase
import com.duyts.android.domain.ObserveHiringItemsUseCase
import com.duyts.fetch.common.result.Resource
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.home.model.DisplayHiringItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
	observeHiringItemsUseCase: ObserveHiringItemsUseCase,
	private val fetchHiringItemUseCase: FetchHiringItemUseCase,
) : ViewModel() {

	private val _forceRefresh: MutableSharedFlow<Boolean> = MutableSharedFlow()
	private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(false)
	private val _errorMsg: MutableSharedFlow<String> = MutableSharedFlow()
	val errorMsg = _errorMsg.asSharedFlow()

	private val _hiringItems = observeHiringItemsUseCase()
		.onEach { list -> if (list.isEmpty()) refreshHiringItems() } //TODO: Logic when fetch empty list
		.map { it.toDisplayHiringItems() }
		.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), emptyList())

	init {
		_forceRefresh
			.onEach { isRefresh ->
				if (!isRefresh) return@onEach
				_isLoading.update { true }
				fetchHiringItemUseCase().let { resource ->
					if (resource is Resource.Error) {
						_errorMsg.emit(resource.msg.orEmpty())
					}
				}
				_isLoading.update { false }
			}.launchIn(viewModelScope)
	}

	val state: StateFlow<UiState> =
		combine(_isLoading, _hiringItems) { isLoading, hiringItems ->
			UiState(isLoading = isLoading, hiringItems = hiringItems)
		}.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), UiState())

	fun refreshHiringItems() = viewModelScope.launch {
		_forceRefresh.emit(true)
	}
}

internal fun List<HiringItem>.toDisplayHiringItems() =
	this.groupBy { it.listID }.flatMap { (listId, uiHiringItem) ->
		val headers = listOf(DisplayHiringItem.Header(listId))
		val items = uiHiringItem.asSequence().sortedBy {
			it.name
		}.map { DisplayHiringItem.Item(it) }
		headers + items
	}

data class UiState(
	val hiringItems: List<DisplayHiringItem> = listOf(),
	val isLoading: Boolean = false,
)