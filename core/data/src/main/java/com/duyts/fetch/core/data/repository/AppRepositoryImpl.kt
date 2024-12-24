package com.duyts.fetch.core.data.repository


import com.duyts.android.database.dao.HiringDao
import com.duyts.fetch.common.network.Dispatcher
import com.duyts.fetch.common.network.NiaDispatchers
import com.duyts.fetch.core.data.model.HiringItem
import com.duyts.fetch.core.data.transformer.DataTransformer
import com.duyts.fetch.network.AppNetworkDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AppRepositoryImpl @Inject constructor(
	@Dispatcher(NiaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
	private val networkDataSource: AppNetworkDataSource,
	private val hiringDao: HiringDao,
	private val dataTransformer: DataTransformer,
) : AppRepository {

	override fun fetchHiringItems(): Flow<Unit> = flow<Unit> {
		val items = networkDataSource.getHiringItems().map(dataTransformer::transform)
		hiringDao.insertOrIgnoreHiringItems(items)
	}.flowOn(ioDispatcher)

	override fun observeHiringItems(): Flow<List<HiringItem>> =
		hiringDao.observeHiringItems().map { entities ->
			entities.map(dataTransformer::transform)
		}
			.flowOn(ioDispatcher)
}