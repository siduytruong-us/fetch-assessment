package com.duyts.fetch.network.model

import com.google.gson.annotations.SerializedName

data class HiringItemsResponseItem(
	@field:SerializedName("listId")
	val listId: Int,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int,
)
