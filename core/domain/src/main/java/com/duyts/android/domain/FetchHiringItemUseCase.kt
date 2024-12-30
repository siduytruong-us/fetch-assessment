package com.duyts.android.domain

import com.duyts.fetch.common.result.Resource
import com.duyts.fetch.common.result.asResource
import com.duyts.fetch.core.data.repository.AppRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class FetchHiringItemUseCase @Inject constructor(
	private val appRepository: AppRepository,
) {
	operator fun invoke(): Flow<Resource<Unit>> = appRepository.fetchHiringItems().asResource()
}