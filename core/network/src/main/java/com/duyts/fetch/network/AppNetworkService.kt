package com.duyts.fetch.network

import com.duyts.fetch.network.model.HiringItemsResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface AppNetworkService {
	@GET("/example/get")
	suspend fun getMessage(): Response<String>

	@GET("/hiring.json")
	suspend fun getHiringItems(): Response<List<HiringItemsResponseItem>>
}