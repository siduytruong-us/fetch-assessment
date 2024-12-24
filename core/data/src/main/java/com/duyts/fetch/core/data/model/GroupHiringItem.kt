package com.duyts.fetch.core.data.model

import androidx.compose.runtime.Immutable

@Immutable
data class GroupHiringItem(
	val listID: Int,
	val items: List<HiringItem>,
)