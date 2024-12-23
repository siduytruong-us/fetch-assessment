package com.duyts.fetch.core.data

import com.duyts.android.database.dao.HiringDao
import com.duyts.android.database.entity.HiringItemEntity
import com.duyts.android.test.MainDispatcherRule
import com.duyts.fetch.common.Resource.Resource
import com.duyts.fetch.core.data.ext.testingObserveHiringItems
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.repository.AppRepositoryImpl
import com.duyts.fetch.core.data.transformer.DataTransformer
import com.duyts.fetch.network.AppNetworkDataSource
import com.duyts.fetch.network.model.HiringItemsResponseItem
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class AppRepositoryTest {

	@get:Rule
	val mainDispatcherRule = MainDispatcherRule()
	private val ioDispatcher = UnconfinedTestDispatcher()

	private lateinit var appRepository: AppRepositoryImpl

	@Mock
	private lateinit var networkDataSource: AppNetworkDataSource

	@Mock
	private lateinit var hiringDao: HiringDao

	@Mock
	private lateinit var transformer: DataTransformer

	@Before
	fun setup() {
		MockitoAnnotations.openMocks(this)
		appRepository = AppRepositoryImpl(
			ioDispatcher = ioDispatcher,
			networkDataSource = networkDataSource,
			hiringDao = hiringDao,
			dataTransformer = transformer,
		)
	}

	@Test
	fun `fetchHiringItems inserts items into database on success`() = runTest {
		val networkItems = listOf(
			HiringItemsResponseItem(1, "Alice", 1),
			HiringItemsResponseItem(1, "Bob", 2)
		)
		val transformedItems =
			networkItems.map { HiringItemEntity(it.id, it.name, listId = it.listId) }
		whenever(networkDataSource.getHiringItems()).thenReturn(networkItems)


		networkItems.forEachIndexed { index, item ->
			whenever(transformer.transform(item)).thenReturn(transformedItems[index])
		}

		val result = appRepository.fetchHiringItems()

		verify(transformer, times(networkItems.size)).transform(any<HiringItemsResponseItem>())
		verify(hiringDao).insertOrIgnoreHiringItems(transformedItems)
		assert(result is Resource.Success)
	}

	@Test
	fun `fetchHiringItems handles empty network response`() = runTest {
		val networkItems = emptyList<HiringItemsResponseItem>()
		whenever(networkDataSource.getHiringItems()).thenReturn(networkItems)

		val result = appRepository.fetchHiringItems()

		verify(hiringDao).insertOrIgnoreHiringItems(emptyList())
		verify(transformer, never()).transform(any<HiringItemsResponseItem>())
		assert(result is Resource.Success)
	}

	@Test
	fun `fetchHiringItems ensures transformer is used`() = runTest {
		val networkItems = listOf(
			HiringItemsResponseItem(1, "Alice", 1),
			HiringItemsResponseItem(2, "Bob", 2)
		)
		val transformedItems =
			networkItems.map { HiringItemEntity(it.id, it.name, listId = it.listId) }
		whenever(networkDataSource.getHiringItems()).thenReturn(networkItems)

		networkItems.forEachIndexed { index, item ->
			whenever(transformer.transform(item)).thenReturn(transformedItems[index])
		}

		appRepository.fetchHiringItems()

		verify(transformer, times(networkItems.size)).transform(any<HiringItemsResponseItem>())
	}

	@Test
	fun `observeHiringItems ensures transformer is used`() = runTest {
		val entities = listOf(
			HiringItemEntity(1, "Alice", 1),
			HiringItemEntity(1, "Bob", 2)
		)
		val transformedItems = entities.testingObserveHiringItems()
		whenever(hiringDao.observeHiringItems()).thenReturn(flowOf(entities))
		whenever(transformer.transform(entities)).thenReturn(transformedItems)

		val result: List<Resource<Map<Int, List<HiringItem>>>> =
			appRepository.observeHiringItems().toList()
		//
		verify(transformer).transform(any<List<HiringItemEntity>>())

		assert(result[0] is Resource.Loading)
		assertEquals(
			(result[1] as Resource.Success).data,
			transformedItems
		)
	}

	@Test
	fun `observeHiringItems emits empty data when DAO returns empty list`() = runTest {
		whenever(hiringDao.observeHiringItems()).thenReturn(flowOf(emptyList()))
		whenever(transformer.transform(emptyList())).thenReturn(emptyMap())

		val result = appRepository.observeHiringItems().toList()

		assert(result[0] is Resource.Loading)
		assertTrue((result[1] as Resource.Success).data.isEmpty())
		verify(hiringDao).observeHiringItems()
		verify(transformer).transform(emptyList())
	}
}