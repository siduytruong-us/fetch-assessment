package com.duyts.fetch.network

import com.duyts.fetch.common.result.Resource
import com.duyts.fetch.network.model.HiringItemsResponseItem

interface AppNetworkDataSource {
	suspend fun getHiringItems(): Resource<List<HiringItemsResponseItem>>
}