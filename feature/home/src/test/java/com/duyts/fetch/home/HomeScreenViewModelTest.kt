package com.duyts.fetch.home

import com.duyts.android.domain.FetchHiringItemUseCase
import com.duyts.android.domain.ObserveHiringItemsUseCase
import com.duyts.android.test.MainDispatcherRule
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.repository.AppRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

@RunWith(MockitoJUnitRunner::class)
class HomeScreenViewModelTest {
	@get:Rule
	val mainDispatcherRule = MainDispatcherRule()


	private lateinit var viewModel: HomeScreenViewModel

	@Mock
	private lateinit var appRepository: AppRepository

	private lateinit var observeHiringItemsUseCase: ObserveHiringItemsUseCase

	private lateinit var fetchHiringItemUseCase: FetchHiringItemUseCase

	@Before
	fun setup() {
		MockitoAnnotations.openMocks(this)
		observeHiringItemsUseCase = ObserveHiringItemsUseCase(appRepository)
		fetchHiringItemUseCase = FetchHiringItemUseCase(appRepository)
	}


	@Test
	fun `observeHiringItems calls fetchHiringItems when data is empty`() = runTest {
		whenever(appRepository.observeHiringItems()).thenReturn(flowOf(listOf()))

		viewModel = HomeScreenViewModel(observeHiringItemsUseCase, fetchHiringItemUseCase)
		val state = viewModel.state.first()

		assert(state is HomeScreenState.Success)
		assertTrue((state as HomeScreenState.Success).hiringItems.isEmpty())
	}

	@Test
	fun `observeHiringItems emits Loading state`() = runTest {
		whenever(appRepository.observeHiringItems()).thenReturn(flowOf())

		viewModel = HomeScreenViewModel(observeHiringItemsUseCase, fetchHiringItemUseCase)
		val state = viewModel.state.first()

		assert(state is HomeScreenState.Loading)
	}

	@Test
	fun `observeHiringItems emits Error state`() = runTest {
		val errorMessage = "Network error"
		whenever(appRepository.observeHiringItems()).thenReturn(flow {
				throw RuntimeException(
					errorMessage
				)
			})

		viewModel = HomeScreenViewModel(observeHiringItemsUseCase, fetchHiringItemUseCase)
		val state = viewModel.state.first()

		assert(state is HomeScreenState.Error)
		assertEquals(errorMessage, (state as HomeScreenState.Error).error)
	}

	//
	@Test
	fun `observeHiringItems emits Success state with non-empty data`() = runTest {
		val hiringItems = listOf(
			HiringItem(name = "Alice", id = 1, listID = 1),
			HiringItem(name = "Bob", id = 2, listID = 2),
			HiringItem(name = "Cat", id = 3, listID = 1)
		)
		val expected = hiringItems.toDisplayHiringItems()
		whenever(appRepository.observeHiringItems()).thenReturn(flowOf(hiringItems))
		viewModel = HomeScreenViewModel(observeHiringItemsUseCase, fetchHiringItemUseCase)
		val state = viewModel.state.first()

		assert(state is HomeScreenState.Success)
		assertEquals(expected, (state as HomeScreenState.Success).hiringItems)
	}
}
