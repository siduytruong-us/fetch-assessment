package com.duyts.fetch.common.network.ext

import com.duyts.fetch.common.network.exception.NetworkException
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.io.Serializable
import java.util.concurrent.TimeoutException

suspend fun <T> safeApiCall(apiCall: suspend () -> Response<T>): T {
	return try {
		val response = apiCall()
		when {
			response.isSuccessful -> {
				response.body()
					?: throw Exception("HTTP 200: Empty response body")
			}

			else -> throw HttpException(response)
		}
	} catch (exception: Throwable) {
		throw handleApiError(exception)
	}
}


private fun handleApiError(exception: Throwable): NetworkException {
	return when (exception) {
		is TimeoutException -> NetworkException("Request timed out. Please try again.", -1)
		is IOException -> NetworkException("Network error. Please check your connection.", -1)
		is HttpException -> {
			when (val statusCode = exception.code()) {
				400 -> NetworkException("Bad Request", 400)
				401 -> NetworkException("Unauthorized. Please check your credentials.", 401)
				403 -> NetworkException("Forbidden. Access is denied.", 403)
				404 -> NetworkException("Resource not found.", 404)
				500 -> NetworkException("Internal Server Error. Please try again later.", 500)
				503 -> NetworkException("Service Unavailable. Please try again later.", 503)
				else -> NetworkException("Unexpected HTTP Error: $statusCode", -1)
			}
		}

		else -> NetworkException("Unexpected error occurred: ${exception.message}", -1)
	}
}