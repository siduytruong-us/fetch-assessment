package com.duyts.fetch.core.data.repository


import com.duyts.android.database.dao.HiringDao
import com.duyts.android.database.entity.HiringItemEntity
import com.duyts.fetch.common.Resource.Resource
import com.duyts.fetch.common.Resource.asResource
import com.duyts.fetch.common.network.Dispatcher
import com.duyts.fetch.common.network.NiaDispatchers
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.model.toEntity
import com.duyts.fetch.network.AppNetworkDataSource
import com.duyts.fetch.network.model.HiringItemsResponseItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
	@Dispatcher(NiaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
	private val networkDataSource: AppNetworkDataSource,
	private val hiringDao: HiringDao,
) : AppRepository {

	override suspend fun fetchHiringItems(): Resource<Unit> = withContext(ioDispatcher) {
		return@withContext try {
			val items = networkDataSource.getHiringItems().map { it.toEntity() }
			hiringDao.insertOrIgnoreHiringItems(items)
			Resource.Success(Unit)
		} catch (ex: Exception) {
			Resource.Error(ex.message.orEmpty(), ex)
		}
	}

	override fun observeHiringItems(): Flow<Resource<Map<Int, List<HiringItem>>>> =
		hiringDao.observeHiringItems().map { entities ->
			entities.filter { it.name?.isNotBlank() == true }
				.sortedWith(compareBy<HiringItemEntity> { it.listId }.thenBy { it.name })
				.groupBy { it.listId }
				.mapValues { entry ->
					entry.value.map { HiringItem(name = it.name, id = it.id) }
				}
		}.asResource().flowOn(ioDispatcher)
}