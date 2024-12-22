package com.duyts.fetch.common.network.ext

import retrofit2.Response

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): T {
	return try {
		val response = apiCall()
		when {
			response.isSuccessful -> {
				response.body()
					?: throw Exception("HTTP 200: Empty response body")
			}
			else -> throw Exception("HTTP Error: ${response.code()} - ${response.message()}")
		}
	} catch (exception: Throwable) {
//		handleApiError(exception)
		throw exception
	}
}

// Exception handling with detailed Resource.Error
//private fun <T> handleApiError(exception: Throwable): String {
//	return when (exception) {
//		is TimeoutException -> "Request timed out. Please try again."
//		is IOException -> "Network error. Please check your connection."
//		is HttpException -> {
//			when (val statusCode = exception.code()) {
//				400 -> "Bad Request"
//				401 -> "Unauthorized. Please check your credentials."
//				403 -> "Forbidden. Access is denied."
//				404 -> "Resource not found."
//				500 -> "Internal Server Error. Please try again later."
//				503 -> "Service Unavailable. Please try again later."
//				else -> "Unexpected HTTP Error: $statusCode"
//			}
//		}
//		is IllegalArgumentException -> "Invalid argument provided. ${exception.message}"
//		is IllegalStateException -> "Illegal application state. ${exception.message}"
//		else -> "Unexpected error occurred: ${exception.message}"
//	}
//}