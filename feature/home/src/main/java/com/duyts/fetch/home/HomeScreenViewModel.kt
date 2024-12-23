package com.duyts.fetch.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.duyts.fetch.common.Resource.Resource
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.repository.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
	private val appRepository: AppRepository,
) : ViewModel() {
	private val _state: StateFlow<HomeScreenState> = observeHiringItems().stateIn(
		scope = viewModelScope,
		started = SharingStarted.WhileSubscribed(5_000),
		HomeScreenState.Loading
	)

	val state: StateFlow<HomeScreenState> = _state

	private fun observeHiringItems(): Flow<HomeScreenState> =
		appRepository.observeHiringItems()
			.map { resource ->
				when (resource) {
					is Resource.Success -> {
						if (resource.data.isEmpty()) {
							appRepository.fetchHiringItems()
						}
						HomeScreenState.Success(hiringItems = resource.data)
					}
					is Resource.Loading -> HomeScreenState.Loading
					is Resource.Error -> HomeScreenState.Error(error = resource.message)
				}
			}

}

sealed class HomeScreenState {
	data class Success(val hiringItems: Map<Int, List<HiringItem>>) : HomeScreenState()
	data object Loading : HomeScreenState()
	data class Error(val error: String) : HomeScreenState()
}
