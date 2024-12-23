package com.duyts.android.domain

import com.duyts.fetch.core.data.repository.AppRepository
import javax.inject.Inject


class FetchHiringItemUseCase @Inject constructor(
	private val appRepository: AppRepository,
) {
	suspend operator fun invoke() = appRepository.fetchHiringItems()
}