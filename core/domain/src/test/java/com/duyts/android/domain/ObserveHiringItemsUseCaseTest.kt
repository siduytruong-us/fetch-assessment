package com.duyts.android.domain

import com.duyts.fetch.common.result.Resource
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.repository.AppRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class ObserveHiringItemsUseCaseTest {

	// Mock dependencies
	@Mock
	private lateinit var appRepository: AppRepository

	// System under test
	private lateinit var observeHiringItemsUseCase: ObserveHiringItemsUseCase

	@Before
	fun setup() {
		observeHiringItemsUseCase = ObserveHiringItemsUseCase(appRepository)
	}

	@Test
	fun `invoke delegates to appRepository observeHiringItems`() = runTest {
		val hiringItems: List<HiringItem> = listOf(
			HiringItem("Alice", 1, 1),
			HiringItem("Bob", 1, 2),
			HiringItem("Cat", 3, 2)
		)

		val expectedResult = hiringItems.testFilteringAndSorting()
		whenever(appRepository.observeHiringItems()).thenReturn(flowOf(hiringItems))

		val result = observeHiringItemsUseCase().first()
		assertEquals(result, expectedResult)
		verify(appRepository).observeHiringItems()
	}
}


private fun List<HiringItem>.testFilteringAndSorting() =
	this.filter { it.name?.isNotBlank() == true }
