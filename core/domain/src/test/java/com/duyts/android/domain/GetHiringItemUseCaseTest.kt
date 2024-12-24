package com.duyts.android.domain

import com.duyts.fetch.common.Resource.Resource
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.model.GroupHiringItem
import com.duyts.fetch.core.data.repository.AppRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class GetHiringItemUseCaseTest {

	// Mock dependencies
	@Mock
	private lateinit var appRepository: AppRepository

	// System under test
	private lateinit var getHiringItemUseCase: GetHiringItemUseCase

	@Before
	fun setup() {
		getHiringItemUseCase = GetHiringItemUseCase(appRepository)
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

		val result = getHiringItemUseCase().toList()

		assert(result[0] is Resource.Loading)
		assertEquals((result[1] as Resource.Success).data, expectedResult)
		verify(appRepository).observeHiringItems()
	}
}


private fun List<HiringItem>.testFilteringAndSorting() = this.asSequence()
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