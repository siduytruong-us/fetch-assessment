package com.duyts.fetch.common.network.exception

import java.lang.RuntimeException

data class NetworkException(val msg: String? = null, val code: Int) : RuntimeException(msg)