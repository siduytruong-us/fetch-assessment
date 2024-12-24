package com.duyts.fetch.home

import com.duyts.android.domain.DisplayHiringItem
import com.duyts.android.domain.FetchHiringItemUseCase
import com.duyts.android.domain.GetHiringItemUseCase
import com.duyts.android.test.MainDispatcherRule
import com.duyts.fetch.core.data.model.GroupHiringItem
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

	private lateinit var getHiringItemUseCase: GetHiringItemUseCase

	private lateinit var fetchHiringItemUseCase: FetchHiringItemUseCase

	@Before
	fun setup() {
		MockitoAnnotations.openMocks(this)
		getHiringItemUseCase = GetHiringItemUseCase(appRepository)
		fetchHiringItemUseCase = FetchHiringItemUseCase(appRepository)
	}


	@Test
	fun `observeHiringItems calls fetchHiringItems when data is empty`() = runTest {
		whenever(appRepository.observeHiringItems()).thenReturn(flowOf(listOf()))

		viewModel = HomeScreenViewModel(getHiringItemUseCase, fetchHiringItemUseCase)
		val state = viewModel.state.first()

		assert(state is HomeScreenState.Success)
		assertTrue((state as HomeScreenState.Success).hiringItems.isEmpty())
	}

	@Test
	fun `observeHiringItems emits Loading state`() = runTest {
		whenever(appRepository.observeHiringItems()).thenReturn(flowOf())

		viewModel = HomeScreenViewModel(getHiringItemUseCase, fetchHiringItemUseCase)
		val state = viewModel.state.first()

		assert(state is HomeScreenState.Loading)
	}

	@Test
	fun `observeHiringItems emits Error state`() = runTest {
		val errorMessage = "Network error"
		whenever(appRepository.observeHiringItems())
			.thenReturn(flow { throw RuntimeException(errorMessage) })

		viewModel = HomeScreenViewModel(getHiringItemUseCase, fetchHiringItemUseCase)
		val state = viewModel.state.first()

		assert(state is HomeScreenState.Error)
		assertEquals(errorMessage, (state as HomeScreenState.Error).error)
	}

	//
	@Test
	fun `observeHiringItems emits Success state with non-empty data`() = runTest {
		val hiringItems =
			listOf(
				GroupHiringItem(
					1, listOf(
						HiringItem("Alice", 1),
						HiringItem("Bob", 2)
					)
				),
				GroupHiringItem(
					2, listOf(
						HiringItem("Cat", 3)
					)
				)
			)
		val expected = hiringItems.testing()
		whenever(appRepository.observeHiringItems()).thenReturn(flowOf(hiringItems))
		viewModel = HomeScreenViewModel(getHiringItemUseCase, fetchHiringItemUseCase)
		val state = viewModel.state.first()

		assert(state is HomeScreenState.Success)
		assertEquals(expected, (state as HomeScreenState.Success).hiringItems)
	}
}

private fun List<GroupHiringItem>.testing() = sortedBy { it.listID }
	.flatMap { uiHiringItem ->
		val headers = listOf(DisplayHiringItem.Header(uiHiringItem.listID))
		val items = uiHiringItem.items.asSequence()
			.filter { it.name?.isNotEmpty() == true }
			.sortedBy { it.name }
			.map { DisplayHiringItem.Item(it) }
		headers + items
	}

