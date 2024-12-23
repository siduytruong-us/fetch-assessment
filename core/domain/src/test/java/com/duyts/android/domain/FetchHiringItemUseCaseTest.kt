package com.duyts.android.domain

import com.duyts.fetch.common.Resource.Resource
import com.duyts.fetch.core.data.repository.AppRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
		val expectedResource = Resource.Success(Unit)
		whenever(appRepository.fetchHiringItems()).thenReturn(expectedResource)

		val result = fetchHiringItemUseCase()

		assertEquals(expectedResource, result)
		verify(appRepository).fetchHiringItems()
	}

	@Test
	fun `invoke returns error from appRepository`() = runTest {
		val expectedError = Resource.Error("Failed to fetch items")
		whenever(appRepository.fetchHiringItems()).thenReturn(expectedError)

		val result = fetchHiringItemUseCase()

		assertEquals(expectedError, result)
		verify(appRepository).fetchHiringItems()
	}
}
