package com.duyts.fetch.common

import com.duyts.fetch.common.network.exception.NetworkException
import com.duyts.fetch.common.network.ext.safeApiCall
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class SafeApiCallTest {

	@Test
	fun `safeApiCall returns data when response is successful with body`() = runTest {
		val data = "body"
		val response = Response.success(data)
		val apiCall: suspend () -> Response<String> = { response }

		val result = safeApiCall(apiCall)

		assertEquals(data, result)
	}

	@Test(expected = Exception::class)
	fun `safeApiCall throws exception when response is successful but body is null`() = runTest {
		// Arrange
		val data: String? = null
		val response = Response.success(data)
		val apiCall: suspend () -> Response<String> = { response }
		val result = safeApiCall(apiCall)
		assertEquals(data, result)
	}

	@Test
	fun `safeApiCall throws exception when response is unsuccessful`() = runTest {
		// Arrange
		val errorMsg = "Internal Server Error. Please try again later."
		val errorCode = 500
		val expected = NetworkException(errorMsg, errorCode)
		val response = Response.error<String>(errorCode, ResponseBody.create(null, ""))
		val apiCall: suspend () -> Response<String> = { response }

		// Act
		try {
			safeApiCall(apiCall)
		} catch (ex: Exception) {
			assertEquals(expected, ex)
		}
	}

	@Test(expected = Exception::class)
	fun `safeApiCall throws exception when apiCall throws exception`() = runTest {
		// Arrange
		val apiCall: suspend () -> Response<String> = { throw Exception("Network error") }

		// Act
		safeApiCall(apiCall)
	}
}
