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
		val expected = Resource.Success(true)
		whenever(appRepository.fetchHiringItems()).thenReturn(expected)

		val result = fetchHiringItemUseCase()

		verify(appRepository).fetchHiringItems()
		assertEquals(result, expected)
	}

	@Test
	fun `invoke returns error from appRepository`() = runTest {
		val expectedError = "Failed to fetch items"
		val expectedException = Exception(expectedError)
		whenever(appRepository.fetchHiringItems())
			.thenReturn(Resource.Error(expectedError, -1, expectedException))

		val result = fetchHiringItemUseCase()
		verify(appRepository).fetchHiringItems()
		assertEquals(result, Resource.Error(expectedError, -1, expectedException))
	}
}
