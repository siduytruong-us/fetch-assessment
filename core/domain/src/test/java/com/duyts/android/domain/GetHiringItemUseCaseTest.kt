package com.duyts.android.domain

import com.duyts.fetch.common.Resource.Resource
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.repository.AppRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
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
		val expectedFlow = flowOf(
			Resource.Success(
				mapOf(1 to listOf(HiringItem("Alice", 1)))
			)
		)
		whenever(appRepository.observeHiringItems()).thenReturn(expectedFlow)

		val result = getHiringItemUseCase()

		assertEquals(expectedFlow, result)
		verify(appRepository).observeHiringItems()
	}
}
