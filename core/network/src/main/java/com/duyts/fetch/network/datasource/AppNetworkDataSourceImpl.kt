package com.duyts.fetch.network.datasource

import com.duyts.fetch.common.network.Dispatcher
import com.duyts.fetch.common.network.NiaDispatchers
import com.duyts.fetch.common.network.ext.safeApiCall
import com.duyts.fetch.network.AppNetworkDataSource
import com.duyts.fetch.network.AppNetworkService
import com.duyts.fetch.network.model.HiringItemsResponseItem
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class AppNetworkDataSourceImpl @Inject constructor(
	private val networkApi: AppNetworkService,
	@Dispatcher(NiaDispatchers.IO) private val ioDispatcher: CoroutineDispatcher,
) : AppNetworkDataSource {

	override suspend fun getMessage(): String = safeApiCall {
		delay(2000)
		return@safeApiCall networkApi.getMessage()
	}

	override suspend fun getHiringItems(): List<HiringItemsResponseItem> =
		safeApiCall {
			networkApi.getHiringItems()
		}
}