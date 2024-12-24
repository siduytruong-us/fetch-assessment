package com.duyts.fetch.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duyts.android.domain.DisplayHiringItem
import com.duyts.android.domain.FetchHiringItemUseCase
import com.duyts.android.domain.GetHiringItemUseCase
import com.duyts.fetch.common.Resource.Resource
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
	private val getHiringItemUseCase: GetHiringItemUseCase,
	private val fetchHiringItemUseCase: FetchHiringItemUseCase,
) : ViewModel() {
	private val _state: StateFlow<HomeScreenState> = observeHiringItems().stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5_000),
		HomeScreenState.Loading
	)

	val state: StateFlow<HomeScreenState> = _state

	fun fetchNewHiringItems() = viewModelScope.launch {
		fetchHiringItemUseCase.invoke()
	}
	private fun observeHiringItems(): Flow<HomeScreenState> =
		getHiringItemUseCase()
			.map { resource ->
				when (resource) {
					is Resource.Success -> {
						HomeScreenState.Success(hiringItems = resource.data)
					}
					is Resource.Loading -> HomeScreenState.Loading
					is Resource.Error -> HomeScreenState.Error(error = resource.message)
				}
			}

}

sealed class HomeScreenState {
	data class Success(val hiringItems: List<DisplayHiringItem>) : HomeScreenState()
	data object Loading : HomeScreenState()
	data class Error(val error: String) : HomeScreenState()
}
