package com.duyts.android.domain

import com.duyts.fetch.common.Resource.Resource
import com.duyts.fetch.core.data.repository.AppRepository
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class GetHiringItemUseCase @Inject constructor(
	private val appRepository: AppRepository,
) {
	operator fun invoke() = appRepository.observeHiringItems()
}