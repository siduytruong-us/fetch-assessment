package com.duyts.fetch.home

import app.cash.turbine.test
import com.duyts.android.domain.FetchHiringItemUseCase
import com.duyts.android.domain.ObserveHiringItemsUseCase
import com.duyts.android.test.MainDispatcherRule
import com.duyts.fetch.common.result.Resource
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.repository.AppRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
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
		viewModel = HomeScreenViewModel(observeHiringItemsUseCase, fetchHiringItemUseCase)
	}

	@Test
	fun `when refreshHiringItems called, it should trigger loading state and call fetchHiringItemUseCase`() = runTest {
		whenever(fetchHiringItemUseCase()).thenReturn(Resource.Success(true))

		viewModel.refreshHiringItems()

		viewModel.state.test {
			assertTrue(awaitItem().isLoading)
		}

		verify(fetchHiringItemUseCase).invoke()
	}

	@Test
	fun `when hiring items observed with data, state should update correctly`() = runTest {
		whenever(observeHiringItemsUseCase()).thenReturn(flowOf(hiringItems))

		viewModel = HomeScreenViewModel(observeHiringItemsUseCase, fetchHiringItemUseCase)

		viewModel.state.test {
			val updatedState = awaitItem()
			assertEquals( 5, updatedState.hiringItems.size)
			assertEquals(hiringItems.toDisplayHiringItems(), updatedState.hiringItems)
		}
	}

//	@Test
//	fun `initial state is loading false and no hiring items`() = runTest {
//		whenever(appRepository.observeHiringItems()).thenReturn(flowOf(listOf()))
//		viewModel = HomeScreenViewModel(observeHiringItemsUseCase, fetchHiringItemUseCase)
//		viewModel.state.test {
//			val initialState = awaitItem()
//			assertEquals(0, initialState.hiringItems.size)
//			assertFalse(initialState.isLoading)
//		}
//	}
//
//	@Test
//	fun `forceRefresh should trigger fetch and loading`() = runTest {
//		whenever(appRepository.observeHiringItems()).thenReturn(flowOf(listOf()))
//		whenever(fetchHiringItemUseCase()).thenReturn(Resource.Success(true))
//
//		viewModel.state.test {
//			assertTrue(!awaitItem().isLoading)
//			assertTrue(awaitItem().isLoading)
//		}
//
//		verify(fetchHiringItemUseCase).invoke()
//	}
//
//	@Test
//	fun `observeHiringItems calls fetchHiringItems when data is empty`() = runTest {
//		whenever(appRepository.observeHiringItems()).thenReturn(flowOf(listOf()))
//		whenever(appRepository.fetchHiringItems()).thenReturn(Resource.Success(true))
//		viewModel.state.test {
//			assertEquals(UiState(), awaitItem())
//			verify(appRepository).fetchHiringItems()
//			verify(appRepository).observeHiringItems()
//		}
//	}
//
//	//
//	@Test
//	fun `observeHiringItems emits state with non-empty data`() = runTest {
//		whenever(appRepository.observeHiringItems()).thenReturn(flowOf(hiringItems))
//		viewModel.state.test {
//			assertEquals(hiringItems.toDisplayHiringItems(), awaitItem().hiringItems)
//		}
//	}

//	@Test
//	fun `observeHiringItems emits Error state`() = runTest {
//		val errorMessage = "Network error"
//		whenever(appRepository.observeHiringItems()).thenReturn(flow {
//				throw RuntimeException(
//					errorMessage
//				)
//			})
//
//		viewModel = HomeScreenViewModel(observeHiringItemsUseCase, fetchHiringItemUseCase)
//		val state = viewModel.state.first()
//
//		assert(state is HomeScreenState.Error)
//		assertEquals(errorMessage, (state as HomeScreenState.Error).error)
//	}
//
//	//
//	@Test
//	fun `observeHiringItems emits Success state with non-empty data`() = runTest {
//		val hiringItems = listOf(
//			HiringItem(name = "Alice", id = 1, listID = 1),
//			HiringItem(name = "Bob", id = 2, listID = 2),
//			HiringItem(name = "Cat", id = 3, listID = 1)
//		)
//		val expected = hiringItems.toDisplayHiringItems()
//		whenever(appRepository.observeHiringItems()).thenReturn(flowOf(hiringItems))
//		viewModel = HomeScreenViewModel(observeHiringItemsUseCase, fetchHiringItemUseCase)
//		val state = viewModel.state.first()
//
//		assert(state is HomeScreenState.Success)
//		assertEquals(expected, (state as HomeScreenState.Success).hiringItems)
//	}
}

val hiringItems = listOf(
	HiringItem(name = "Alice", id = 1, listID = 1),
	HiringItem(name = "Bob", id = 2, listID = 2),
	HiringItem(name = "Cat", id = 3, listID = 1)
)