package com.duyts.fetch.network

import com.duyts.fetch.network.model.HiringItemsResponseItem

interface AppNetworkDataSource {
	suspend fun getMessage(): String

	suspend fun getHiringItems(): List<HiringItemsResponseItem>
}