package com.duyts.fetch.network

import com.duyts.android.test.MainDispatcherRule
import com.duyts.fetch.network.datasource.AppNetworkDataSourceImpl
import com.duyts.fetch.network.model.HiringItemsResponseItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AppNetworkDataSourceImplTest {

	@get:Rule
	val mainDispatcherRule = MainDispatcherRule()
	private val ioDispatcher = UnconfinedTestDispatcher()

	private lateinit var appNetworkDataSource: AppNetworkDataSourceImpl

	@Mock
	private lateinit var networkApi: AppNetworkService

	@Before
	fun setUp() {
		appNetworkDataSource = AppNetworkDataSourceImpl(
			networkApi = networkApi,
			ioDispatcher = ioDispatcher
		)
	}

	@After
	fun tearDown() {
		reset(networkApi)
	}

	@Test
	fun `getHiringItems returns list of HiringItemsResponseItem`() = runTest {
		val mockResponse = listOf(
			HiringItemsResponseItem(1, "Item 1", 101),
			HiringItemsResponseItem(2, "Item 2", 102)
		)
		`when`(networkApi.getHiringItems()).thenReturn(Response.success(mockResponse))

		val result = appNetworkDataSource.getHiringItems()

		assertEquals(mockResponse, result)
		verify(networkApi).getHiringItems()
	}

	@Test(expected = Exception::class)
	fun `getHiringItems throws exception when API call fails`() = runTest {
		`when`(networkApi.getHiringItems()).thenThrow(RuntimeException("Network Error"))

		appNetworkDataSource.getHiringItems()
	}
}
