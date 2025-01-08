package com.duyts.fetch.common

import com.duyts.fetch.common.network.exception.NetworkException
import com.duyts.fetch.common.network.ext.safeApiCall
import com.duyts.fetch.common.result.Resource
import kotlinx.coroutines.test.runTest
import okhttp3.ResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Response
import java.io.IOException
import java.util.concurrent.TimeoutException
import kotlin.coroutines.cancellation.CancellationException
import kotlin.test.assertContentEquals

@RunWith(MockitoJUnitRunner::class)
class SafeApiCallTest {

	@Test
	fun `safeApiCall should return success for successful response`() = runTest {
		val mockResponse = Response.success("Success Response")

		val result = safeApiCall { mockResponse }

		assertTrue(result is Resource.Success)
		assertEquals("Success Response", (result as Resource.Success).data)
	}

	@Test
	fun `safeApiCall should return error for HTTP error response`() = runTest {
		val errorResponse =
			Response.error<String>(404, okhttp3.ResponseBody.create(null, "Error Body"))

		val result = safeApiCall { errorResponse }

		assertTrue(result is Resource.Error)
		assertEquals("Resource not found.", (result as Resource.Error).msg)
	}

	@Test
	fun `safeApiCall should return error for timeout exception`() = runTest {
		val result = safeApiCall<String> { throw TimeoutException("Request timed out") }

		assertTrue(result is Resource.Error)
		assertEquals("Request timed out. Please try again.", (result as Resource.Error).msg)
	}

	// ✅ Test Case 4: IOException (Network Error)
	@Test
	fun `safeApiCall should return error for network error`() = runTest {
		val result = safeApiCall<String> { throw IOException("Network Error") }

		assertTrue(result is Resource.Error)
		assertEquals("Network error. Please check your connection.", (result as Resource.Error).msg)
	}

	@Test(expected = CancellationException::class)
	fun `safeApiCall should throw cancellation exception`() = runTest {
		safeApiCall<String> { throw CancellationException("Cancelled!") }
	}

	@Test
	fun `safeApiCall should return error for unknown exception`() = runTest {
		val result = safeApiCall<String> { throw Exception("Unknown Error") }

		assertTrue(result is Resource.Error)
		assertEquals("Unexpected error occurred: Unknown Error", (result as Resource.Error).msg)
	}

}
