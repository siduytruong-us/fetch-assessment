package com.duyts.android.domain

import com.duyts.fetch.common.result.Resource
import com.duyts.fetch.core.data.repository.AppRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class FetchHiringItemUseCaseTest {
	@Mock
	private lateinit var appRepository: AppRepository

	private lateinit var fetchHiringItemUseCase: FetchHiringItemUseCase

	@Before
	fun setup() {
		fetchHiringItemUseCase = FetchHiringItemUseCase(appRepository)
	}

	@Test
	fun `invoke delegates to appRepository fetchHiringItems`() = runTest {
		whenever(appRepository.fetchHiringItems()).thenReturn(flowOf(Unit))

		val result = fetchHiringItemUseCase().toList()

		verify(appRepository).fetchHiringItems()
		assert(result[0] is Resource.Loading)
		assertTrue((result[1] as Resource.Success).data == Unit)
	}

	@Test
	fun `invoke returns error from appRepository`() = runTest {
		val expectedError = "Failed to fetch items"
		whenever(appRepository.fetchHiringItems())
			.thenReturn(flow { throw RuntimeException(expectedError) })

		val result: List<Resource<Unit>> = fetchHiringItemUseCase().toList()
		assert(result[0] is Resource.Loading)
		assertTrue((result[1] as Resource.Error).message == expectedError)
	}
}
