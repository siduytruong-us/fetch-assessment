package com.duyts.fetch.common.result

sealed interface Resource<out T> {
	data class Success<T>(val data: T) : Resource<T>
	data class Error(
		val msg: String? = null,
		val code: Int,
		val exception: Throwable? = null,
	) : Resource<Nothing>
}

